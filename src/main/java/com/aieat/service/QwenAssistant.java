package com.aieat.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
public interface QwenAssistant {
    @SystemMessage("你是一个乐于助人的AI助手，请用简洁的中文回答。")
    TokenStream chat(@MemoryId String chatId, @UserMessage String userMessage);
}