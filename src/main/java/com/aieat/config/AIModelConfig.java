package com.aieat.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.dashscope.QwenChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 模型配置类
 *
 * @author liuyu001
 */
@Configuration
public class AIModelConfig {
    /**
     * 模型apiKey
     */
    @Value("${ai-model.api-key}")
    private String apiKey;
    /**
     * 模型name
     */
    @Value("${ai-model.model-name}")
    private String modelName;

    /**
     * 基于模型配置构造一个实现了lang-chain4j的Chat模型对象到spring容器
     *
     * @return
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return QwenChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .build();
    }
}