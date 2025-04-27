import os
import platform
import subprocess
from logging import Formatter, StreamHandler, getLogger
from tempfile import TemporaryDirectory, TemporaryFile

import audio
import cv2
import face_detection
import numpy as np
import torch
from models.wav2lip import Wav2Lip

logger = getLogger(__name__)
logger.setLevel("INFO")
logger.addHandler(StreamHandler())
formatter = Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
for handler in logger.handlers:
    handler.setFormatter(formatter)

device = 'cuda' if torch.cuda.is_available() else 'cpu'

face_batch_size = 16


class Wav2LipApp:
    __model: Wav2Lip = None

    def __init__(self, checkpoint_path: str = 'checkpoints/wav2lip_gan.pth'):
        self.checkpoint_path = checkpoint_path

    def generate(self, video_path: str, audio_path: str, output_path: str):
        logger.info(
            f"Generating video with audio: {video_path}, {audio_path} -> {output_path}")

        tmp_dir = TemporaryDirectory()

        # Load datas for inference
        model = self.__load_model(self.checkpoint_path)
        full_frames, fps = self.__load_video(video_path)
        mel_chunks = self.__load_audio(audio_path, fps)
        full_frames = full_frames[:len(mel_chunks)]

        # Start Inference
        logger.info("Starting inference")

        batch_size = 128
        gen = self.__generate(full_frames.copy(), mel_chunks)

        tmp_video = os.path.join(tmp_dir.name, 'tmp_video.avi')
        total = total = int(np.ceil(float(len(mel_chunks))/batch_size))
        for i, (img_batch, mel_batch, frames, coords) in enumerate(gen):
            logger.info(f"Processing batch {i+1}/{total}")

            if i == 0:
                frame_h, frame_w = full_frames[0].shape[:-1]
                print(f"(frame_w, frame_h)= ({frame_w}, {frame_h})")
                video_result = cv2.VideoWriter(
                    tmp_video, cv2.VideoWriter_fourcc(*'DIVX'), fps, (frame_w, frame_h))

            img_batch = torch.FloatTensor(
                np.transpose(img_batch, (0, 3, 1, 2))).to(device)
            mel_batch = torch.FloatTensor(
                np.transpose(mel_batch, (0, 3, 1, 2))).to(device)

            with torch.no_grad():
                pred = model(mel_batch, img_batch)
            pred = pred.cpu().numpy().transpose(0, 2, 3, 1) * 255.

            for p, f, c in zip(pred, frames, coords):
                y1, y2, x1, x2 = c
                p = cv2.resize(p.astype(np.uint8), (x2 - x1, y2 - y1))
                f[y1:y2, x1:x2] = p
                video_result.write(f)
        video_result.release()

        # Save Result to File
        logger.info(f"Saving result to {output_path}")
        command = 'ffmpeg -y -i {} -i {} -strict -2 -q:v 1 {}'.format(
            audio_path, tmp_video, output_path)
        subprocess.call(command, shell=platform.system() != 'Windows')

        tmp_dir.cleanup()
        logger.info("Inference completed")
        logger.info(f"Output saved to {output_path}")
        logger.info("Temporary files cleaned up")
        return output_path

    def __generate(self, frames: list, mels: list):
        img_batch, mel_batch, frame_batch, coords_batch = [], [], [], []

        img_size = 96
        wav2lip_batch_size = 128
        face_det_results = self.__detect_face(frames)

        for i, m in enumerate(mels):
            idx = i % len(frames)
            frame_to_save = frames[idx].copy()
            face, coords = face_det_results[idx].copy()

            face = cv2.resize(face, (img_size, img_size))

            img_batch.append(face)
            mel_batch.append(m)
            frame_batch.append(frame_to_save)
            coords_batch.append(coords)

            if len(img_batch) >= wav2lip_batch_size:
                img_batch, mel_batch = np.asarray(
                    img_batch), np.asarray(mel_batch)

                img_masked = img_batch.copy()
                img_masked[:, img_size//2:] = 0

                img_batch = np.concatenate(
                    (img_masked, img_batch), axis=3) / 255.
                mel_batch = np.reshape(
                    mel_batch, [len(mel_batch), mel_batch.shape[1], mel_batch.shape[2], 1])

                yield img_batch, mel_batch, frame_batch, coords_batch
                img_batch, mel_batch, frame_batch, coords_batch = [], [], [], []

        if len(img_batch) > 0:
            img_batch, mel_batch = np.asarray(img_batch), np.asarray(mel_batch)

            img_masked = img_batch.copy()
            img_masked[:, img_size//2:] = 0

            img_batch = np.concatenate((img_masked, img_batch), axis=3) / 255.
            mel_batch = np.reshape(
                mel_batch, [len(mel_batch), mel_batch.shape[1], mel_batch.shape[2], 1])

            yield img_batch, mel_batch, frame_batch, coords_batch

    def __detect_face(self, images, *, no_smooth=True):
        global face_batch_size

        detector = face_detection.FaceAlignment(
            face_detection.LandmarksType._2D, flip_input=False, device=device)

        predictions = []
        while True:
            try:
                for i in range(0, len(images), face_batch_size):
                    predictions.extend(detector.get_detections_for_batch(
                        np.array(images[i:i + face_batch_size])))
            except RuntimeError:
                if face_batch_size == 1:
                    raise RuntimeError(
                        'Image too big to run face detection on GPU. Please use the --resize_factor argument')
                face_batch_size //= 2
                print('Recovering from OOM error; New batch size: {}'.format(
                    face_batch_size))
                continue
            break

        results = []
        pady1, pady2, padx1, padx2 = [0, 10, 0, 0]
        for rect, image in zip(predictions, images):
            # TODO: API에 사용하기
            if rect is None:
                # check this frame where the face was not detected.
                cv2.imwrite('temp/faulty_frame.jpg', image)
                raise ValueError(
                    'Face not detected! Ensure the video contains a face in all the frames.')

            y1 = max(0, rect[1] - pady1)
            y2 = min(image.shape[0], rect[3] + pady2)
            x1 = max(0, rect[0] - padx1)
            x2 = min(image.shape[1], rect[2] + padx2)

            results.append([x1, y1, x2, y2])

        boxes = np.array(results)
        # if not no_smooth: boxes = get_smoothened_boxes(boxes, T=5)
        results = [[image[y1: y2, x1:x2], (y1, y2, x1, x2)]
                   for image, (x1, y1, x2, y2) in zip(images, boxes)]

        del detector
        return results

    def __load_model(self, path):
        if self.__model:
            return self.__model
        logger.info(f"Loading model from {path}")

        checkpoint = torch.load(path)
        state = checkpoint["state_dict"]
        new_s = {}
        for k, v in state.items():
            new_s[k.replace('module.', '')] = v

        model = Wav2Lip()
        model.load_state_dict(new_s)
        model = model.to(device)
        self.model = model.eval()
        return self.model

    def __load_video(self, video_path: str):
        logger.info(f"Loading video from {video_path}")

        video_stream = cv2.VideoCapture(video_path)
        fps = video_stream.get(cv2.CAP_PROP_FPS)

        frames = []
        while True:
            hasNext, frame = video_stream.read()
            if not hasNext:
                video_stream.release()
                break
            frames.append(frame)
        return frames, int(fps)

    def __load_audio(self, audio_path: str, fps: int):
        logger.info(f"Loading audio from {audio_path}")

        if not audio_path.endswith('.wav'):
            with TemporaryFile() as temp_file:
                command = 'ffmpeg -y -i {} -strict -2 {}'.format(
                    audio_path, audio_path)
                subprocess.call(command, shell=True)
                wav = audio.load_wav(temp_file, 16000)
        else:
            wav = audio.load_wav(audio_path, 16000)

        mel = audio.melspectrogram(wav)
        if np.isnan(mel.reshape(-1)).sum() > 0:
            raise ValueError(
                'Mel contains NaN! Using a TTS voice? Add a small epsilon noise to the wav file and try again')

        mel_chunks = []
        mel_step_size = 16
        mel_idx_multiplier = 80./fps
        i = 0
        while True:
            start_idx = int(i * mel_idx_multiplier)
            if start_idx + mel_step_size > len(mel[0]):
                mel_chunks.append(mel[:, len(mel[0]) - mel_step_size:])
                break
            mel_chunks.append(mel[:, start_idx: start_idx + mel_step_size])
            i += 1
        return mel_chunks


wav2lip = Wav2LipApp()
