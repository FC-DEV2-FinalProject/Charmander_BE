package org.cm.service;

import org.cm.vo.TtsCreateCommand;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@Component
@HttpExchange
public interface TtsService{

    @PostExchange("/tts")
    void create(@RequestBody TtsCreateCommand command);

}