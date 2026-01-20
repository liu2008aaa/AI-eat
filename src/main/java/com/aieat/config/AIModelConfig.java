package com.aieat.config;

import com.aieat.service.QwenAssistant;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
     * 1. 配置 Qwen 流式模型
     * 显式构建模型，而不是依赖 starter 自动注入
     */
    @Bean
    public StreamingChatLanguageModel qwenStreamingChatModel() {
        return QwenStreamingChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .listeners(List.of(new MyChatModelListener()))
                .temperature(0.5f)
                .build();
    }

    /**
     * 2. 配置记忆提供者
     * 决定了如何根据 ChatId 创建记忆，以及记忆存放在哪里(store)
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider(ChatMemoryStore chatMemoryStore) {
        return chatId -> MessageWindowChatMemory.builder()
                .id(chatId)
                .maxMessages(20)
                .chatMemoryStore(chatMemoryStore) // 绑定您的持久化存储实现
                .build();
    }

    /**
     * 3. 【关键点】手动构建 AI 服务实例
     * 在这里，将 模型(Model) 和 记忆(Memory) 组装到 接口(Assistant) 中
     */
    @Bean
    public QwenAssistant qwenAssistant(StreamingChatLanguageModel chatModel,
                                       ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(QwenAssistant.class)
                .streamingChatLanguageModel(chatModel) // 注入流式模型
                .chatMemoryProvider(chatMemoryProvider)// 显式应用记忆提供者
                .build();
    }
}