package com.jayud.oauth.websocket;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @ServerEndpoint 这个注解有什么作用？
 * <p>
 * 这个注解用于标识作用在类上，它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 */

@Slf4j
@Component
@ServerEndpoint("/websocket/{name}")
@Data
public class WebSocket {

    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    /**
     * 标识当前连接客户端的用户名
     */
    private String name;

    /**
     * 用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<String, WebSocket> webSocketSet = new ConcurrentHashMap<>();

    /**
     * 记录下次发送时间的时间戳
     */
    private long timeStamp;

    /**
     * 是否收到了心跳
     */
    private boolean isHeart=false;
    /**
     * 心跳丢失次数
     */
    private int loseHeartCounter;


    @OnOpen
    public void OnOpen(Session session, @PathParam(value = "name") String name) {
        this.session = session;
        this.name = name;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        webSocketSet.put(name, this);
        addOnlineCount();           //在线数加1
        log.info("[WebSocket] 连接成功，当前连接人数为：={}", webSocketSet.size());
    }


    @OnClose
    public void OnClose() {
        webSocketSet.remove(this.name);
        subOnlineCount();           //在线数减1
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", webSocketSet.size());
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("非正常关闭,发生错误!====>" + error.toString() + "当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void OnMessage(String message) {
        log.info("[WebSocket] 收到消息：{}", message);
        //判断是否需要指定发送，具体规则自定义
        if (message.indexOf("TOUSER") == 0) {
            String name = message.substring(message.indexOf("TOUSER") + 6, message.indexOf(";"));
            AppointSending(name, message.substring(message.indexOf(";") + 1, message.length()));
        } else {
            GroupSending(message);
        }

    }

    /**
     * 群发
     *
     * @param message
     */
    public void GroupSending(String message) {
        for (String name : webSocketSet.keySet()) {
            try {
                webSocketSet.get(name).session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 指定发送
     *
     * @param name
     * @param message
     */
    public Boolean AppointSending(String name, String message) {
        try {
            if (webSocketSet.get(name) != null) {
                webSocketSet.get(name).session.getBasicRemote().sendText(message);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized int getOnlineCount() {
        return onlineCount;
    }

    public synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public ConcurrentHashMap<String, WebSocket> getWebSocketSet() {
        return webSocketSet;
    }
}
