package org.example.investment_guide.dto;

public record APIClientParam(String url, String method, String body, String[] headers) {
}