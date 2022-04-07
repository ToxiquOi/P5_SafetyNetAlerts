package fr.ocr.p5_safetynetalerts.config;

import fr.ocr.p5_safetynetalerts.logbook.CustomHttpLogWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.zalando.logbook.*;


@Component
public class LogbookConfig {

    @Bean
    public HttpLogWriter httpLogWriter() {
        return new CustomHttpLogWriter();
    }
}
