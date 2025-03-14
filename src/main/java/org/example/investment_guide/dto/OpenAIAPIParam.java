package org.example.investment_guide.dto;

import org.example.investment_guide.common.ModelType;

public record OpenAIAPIParam(String prompt, ModelType modelType) {
}