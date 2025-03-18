package org.cm.service;

import org.cm.vo.TtsCreateCommand;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeTtsService implements TtsService{

    @Override
    public void create(TtsCreateCommand command) {
        System.out.println("call fake tts service");
    }
}
