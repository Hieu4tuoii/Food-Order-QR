package vn.hieu4tuoi.dto.request.chatbot;

import lombok.Data;

import java.util.List;

@Data
public class DeepSeekRequest {
    private String model;
    private List<Message> messages;
    private double temperature;
    private int max_tokens;
    private Double top_p;

    @Data
    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
