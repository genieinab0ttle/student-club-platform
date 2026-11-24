package kz.sdu.clubs.meetings.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
public class TelegramNotificationSender implements NotificationSender {

    private static final Logger log =
            LoggerFactory.getLogger(TelegramNotificationSender.class);

    private final boolean enabled;
    private final String token;
    private final String chatId;
    private final HttpClient client = HttpClient.newHttpClient();

    public TelegramNotificationSender(
            @Value("${telegram.enabled:false}") boolean enabled,
            @Value("${telegram.bot-token:}") String token,
            @Value("${telegram.chat-id:}") String chatId) {
        this.enabled = enabled;
        this.token = token;
        this.chatId = chatId;
    }

    @Override
    public void send(String text) {
        if (!enabled) {
            return;
        }
        try {
            String body = "chat_id=" + URLEncoder.encode(chatId, StandardCharsets.UTF_8)
                    + "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder(
                            URI.create("https://api.telegram.org/bot" + token + "/sendMessage"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            log.warn("Telegram notification failed: {}", e.getMessage());
        }
    }
}