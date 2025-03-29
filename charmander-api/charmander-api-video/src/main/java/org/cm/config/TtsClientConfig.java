package org.cm.config;


import org.cm.service.TtsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class TtsClientConfig {

    @Value("${tts.url}")
    private String ttsServerUrl;

    @Bean("ttsClient")
    RestClient ttsClient(){
        return RestClient.builder()
                .baseUrl(ttsServerUrl)
                .build();
    }

    @Bean
    public TtsService ttsService() {
        RestClientAdapter adapter = RestClientAdapter.create(ttsClient());

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(adapter)
                .build();

        return httpServiceProxyFactory.createClient(TtsService.class);
    }
}
