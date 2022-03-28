package fr.ocr.p5_safetynetalerts.logbook;

import lombok.extern.log4j.Log4j2;

import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;

import org.zalando.logbook.Precorrelation;


@Log4j2
public class CustomHttpLogWriter implements HttpLogWriter {

    public CustomHttpLogWriter() {}

    @Override
    public boolean isActive() {
        return log.isTraceEnabled();
    }

    @Override
    public void write(Precorrelation precorrelation, String request) {
        log.trace("RECEIVE " + request);
    }

    @Override
    public void write(Correlation correlation, String response) {
        log.trace("SEND " + response);
    }
}
