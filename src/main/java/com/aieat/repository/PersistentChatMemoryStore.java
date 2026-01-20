package com.aieat.repository;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟持久化存储。
 * 生产环境请将 Map 替换为数据库操作 (Redis/MySQL/Mongo)
 */
@Component
@Slf4j
public class PersistentChatMemoryStore implements ChatMemoryStore {

    // 模拟数据库表：Key是chatId, Value是消息列表 JSON
    private final Map<Object, List<ChatMessage>> database = new ConcurrentHashMap<>();

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // 从数据库读取
        return database.getOrDefault(memoryId, List.of());
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        // 保存到数据库
        database.put(memoryId, messages); 
        log.info("持久化保存记忆 -> ID: {}, 条数: {}",memoryId, messages.size());
    }

    @Override
    public void deleteMessages(Object memoryId) {
        database.remove(memoryId);
    }
}