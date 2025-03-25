import os
from .lib.tts import TTS_Vits

tts = TTS_Vits(
    state_path=os.getenv('MODEL_STATE_PATH', 'model_state.pth'),
    config_path=os.getenv('MODEL_CONFIG_PATH', 'model_config.json'),
)