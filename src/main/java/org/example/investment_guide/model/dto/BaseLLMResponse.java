package org.example.investment_guide.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BaseLLMResponse(List<Choice> choices) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(Message message) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Message(String content) {}
    }
}