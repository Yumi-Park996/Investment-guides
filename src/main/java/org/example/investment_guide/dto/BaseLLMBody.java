package org.example.investment_guide.dto;

import java.util.List;

public record BaseLLMBody(String model, List<Message> messages) {
    public record Message(String role, String content) { }
}