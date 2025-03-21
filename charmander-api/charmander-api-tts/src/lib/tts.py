from logging import getLogger
from tempfile import SpooledTemporaryFile

from scipy.io import wavfile
import torch

from .vits import commons, utils
from .vits.models import SynthesizerTrn
from .vits.text import symbols
from .vits.text.symbols import symbols
from .vits.text.utils import text_to_sequence

logger = getLogger(__name__)

class TTS_Vits:
    __initing = False
    __inited = False

    def __init__(self, state_path: str, config_path : str):
        self._state_path = state_path
        self._config_path = config_path
    
    def _split_text(self, text: str):
        text_list = text.split('\n')
        text_list = map(lambda x: x.strip(), text_list)
        text_list = filter(lambda x: x != '', text_list)
        text_list = list(text_list)
        return text_list

    def _text_to_tensor(self, text: str):
        text = text_to_sequence(text, self._hps.data.text_cleaners)
        text = commons.intersperse(text, 0) if self._hps.data.add_blank else text
        text = torch.LongTensor(text)
        return text
    
    def init(self):
        if self.__inited:
            return
        
        self.__initing = True
        hps = utils.get_hparams(self._config_path)
        net_g = SynthesizerTrn(
            len(symbols),
            hps.data.filter_length // 2 + 1,
            hps.train.segment_size // hps.data.hop_length,
            n_speakers=hps.data.n_speakers,
            **hps.model
        )

        if torch.cuda.is_available():
            net_g = net_g.cuda().eval()
        else:
            net_g = net_g.cpu().eval()

        utils.load_checkpoint(self._state_path, net_g, None)

        self._hps = hps
        self._net_g = net_g

        self.__initing = False
        self.__inited = True

    def synthesize(
        self, 
        text: str,
        *,
        speaker_id: int = 89,
        noise_scale: float = 0.5,
        noise_scale_w: float = 0.5,
        length_scale: float = 1.0
    ):
        """
            Synthesize audio for the given text.

            Args:
                text (str): The text to synthesize.
                noise_scale (float): The noise scale.
                noise_scale_w (float): The noise scale w.
                length_scale (float): The length scale.

            Returns:
                bytes: The synthesized audio bytes. format WAV.
        """

        if not self.__inited:
            if self.__initing:
                raise RuntimeError("TTS is initializing")
            raise RuntimeError("TTS not initialized")
        
        logger.info(f"Synthesizing audio for text: {text}")

        with torch.no_grad():
            stn_tst = self._text_to_tensor(text)
            if torch.cuda.is_available():
                x_tst = stn_tst.cuda().unsqueeze(0)
                x_tst_lengths = torch.LongTensor([stn_tst.size(0)]).cuda()
            else:
                x_tst = stn_tst.cpu().unsqueeze(0)
                x_tst_lengths = torch.LongTensor([stn_tst.size(0)]).cpu()
            
            if torch.cuda.is_available():
                sid = torch.LongTensor([speaker_id]).cuda()
            else:
                sid = torch.LongTensor([speaker_id]).cpu()

            inference_audio = self._net_g.infer(
                x_tst, 
                x_tst_lengths, 
                sid=sid, 
                noise_scale=noise_scale,
                noise_scale_w=noise_scale_w,
                length_scale=length_scale
            )[0][0, 0].data
            if torch.cuda.is_available():
                inference_audio = inference_audio.cuda().float().numpy()
            else:
                inference_audio = inference_audio.cpu().float().numpy()
        
        with SpooledTemporaryFile(max_size=20480, mode='w+b') as fp:
            logger.debug(f"Writing synthesized audio to temporary file: {fp.name}")

            wavfile.write(fp, self._hps.data.sampling_rate, inference_audio)
            fp.seek(0)
            audio_bytes = fp.read() 

        logger.info(f"Synthesized audio for text: {text}")
        return audio_bytes