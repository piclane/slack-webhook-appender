package jp.co.dwango.logback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.encoder.EchoEncoder;

public class SlackWebhookAppenderTest {

    static class AppenderForTest extends SlackWebhookAppender {
        public byte[] body = null;

        @Override
        protected void post(byte[] body) throws IOException {
            this.body = body;
        }
    }

    @Test
    public void testBuildsMessage() throws IOException {
        EchoEncoder<ILoggingEvent> encoder = new EchoEncoder<>();
        encoder.start();
        
        AppenderForTest appender = new AppenderForTest();
        appender.setChannel("channel");
        appender.setUsername("username");
        appender.setIconEmoji("icon-emoji");
        appender.setIconUrl("icon-url");
        appender.setLinkNames(true);
        appender.setEncoder(encoder);
        appender.start();

        LoggingEvent event = new LoggingEvent();
        event.setLevel(Level.INFO);
        event.setMessage("text \"quoted\"");
        appender.writeOut(event);

        String actual = new String(appender.body, StandardCharsets.UTF_8);
        String expected = "{ \"text\": \"[INFO] text \\\"quoted\\\"\n\", \"channel\": \"channel\", \"username\": \"username\", \"icon_emoji\": \"icon-emoji\", \"icon_url\": \"icon-url\", \"link_names\": 1 }";
        Assert.assertEquals(expected, actual);
    }

    @Ignore
    public void testPost() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.error("nyaos");
    }
}
