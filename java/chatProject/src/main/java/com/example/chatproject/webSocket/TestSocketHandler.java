package com.example.chatproject.webSocket;

import com.example.chatproject.utils.GlobalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class TestSocketHandler implements WebSocketHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(TestSocketHandler.class);
    private CopyOnWriteArraySet<WebSocketSession> meetings = new CopyOnWriteArraySet<>();
    private int count = 0;
    private List<String> h1 = GlobalData.history;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //LOGGER.info("Test Socket 连接成功，sessionId：{}", session.getId());
        meetings.add(session);

        int onlineCount = meetings.size();
        // 构建固定格式的在线人数消息
        String countMessage = String.valueOf(onlineCount);
        TextMessage textMessage = new TextMessage(countMessage);

        for (WebSocketSession s : meetings) {
            if (s.isOpen()) {
                s.sendMessage(textMessage);
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            String payload = ((TextMessage) message).getPayload();
            if (payload.startsWith("|online_count|")) {
                // 提取在线人数信息（去掉前缀后解析为数字）
                int count = Integer.parseInt(payload.substring("|online_count|".length()));
                LOGGER.info("收到在线人数更新消息：{}", count);
                // 可以在这里进行其他相关处理，比如记录等
            } else {
                // 当作普通聊天消息处理，调用原来的文本消息处理方法
                handlerTextMessage(session, (TextMessage) message);
            }
        } else {
            LOGGER.error("Test Socket 消息处理失败，只接受 文本消息，sessionId：{}", session.getId());
        }
    }


    public void handlerTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final String msg = message.getPayload();
        h1.add(msg);
        count++;
        if(count==51){
            count=50;
            h1.remove(0);
        }
        GlobalData.setHistory(h1);
        //LOGGER.info("Test Socket 收到消息：{}", msg);
        for (WebSocketSession s : meetings) {
            if (s.isOpen()) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOGGER.error("Test Socket 处理异常，sessionId：{}, 异常原因：{}", session.getId(), exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        meetings.remove(session);
        LOGGER.info("Test Socket 关闭，sessionId：{}", session.getId());
        int onlineCount = meetings.size();
        // 构建固定格式的在线人数消息
        String countMessage = String.valueOf(onlineCount);
        TextMessage textMessage = new TextMessage(countMessage);

        for (WebSocketSession s : meetings) {
            if (s.isOpen()) {
                s.sendMessage(textMessage);
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
