//package com.jayud.oauth.websocket.thread;
//
//import cn.hutool.json.JSONObject;
//import com.jayud.common.utils.DateUtils;
//import com.jayud.oauth.websocket.WebSocket;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Calendar;
//
//@Slf4j
//public class KeepHeartThread implements Runnable {
//
//    private static final long ALLOW_ALIVE_TIME = 3000;
//    private static final int MAX_LOSE_HEART_COUNT = 3;
//
//    @Autowired
//    private WebSocket webSocket;
//
//
//    @Override
//    public void run() {
//        JSONObject heartJson = new JSONObject();
//        heartJson.put("服务端心跳", "保持心跳");
//        heartJson.put("commandCode", 1);
//        heartJson.put("timeStamp", getTimeInMillis());
//        try {
//            log.debug("发送心跳包当前人数为:" + webSocket.getOnlineCount() + "当前时间:" + DateUtils.getLocalToStr(LocalDateTime.now()));
//            sendPing(heartJson.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 发送心跳包
//     *
//     * @param message
//     * @throws IOException
//     */
//    public synchronized void sendPing(String message) throws IOException {
//        if (webSocket.getWebSocketSet().size() <= 0) {
//            return;
//        }
//        for (WebSocket webSocket : webSocket.getWebSocketSet().values()) {
//            synchronized (webSocket) {
//                webSocket.setHeart(false);
//                log.debug("发心跳:{}", webSocket.toString() + ",当前时间:" + LocalDateTime.now());
//                (webSocket.getSession()).getBasicRemote().sendText(message);
//            }
//        }
//    }
//
//    /**
//     * 检测是否收到client心跳 每秒一次
//     */
//    private class CodeOrderHeartThread implements Runnable {
//
//        @Override
//        public void run() {
//            try {
//                // 服务器当前时间戳
//                long currentTimeMillis = getTimeInMillis();
//                for (WebSocket webSocket : webSocket.getWebSocketSet().values()) {
//                    long intervalTime = currentTimeMillis - webSocket.getTimeStamp();
////                    logger.debug("intervalTime:{},allow:{}", intervalTime, ALLOW_ALIVE_TIME);
//                    // 客户端心跳未开启，时间戳非0，当前时间戳和上次ping时间戳大于允许的空闲时间
//                    if (!webSocket.isHeart() && webSocket.getTimeStamp() != 0 && intervalTime > ALLOW_ALIVE_TIME) {
//                        int loseHeartCounter = webSocket.getLoseHeartCounter();
//                        loseHeartCounter++;
//                        webSocket.setLoseHeartCounter(loseHeartCounter);
//                        log.debug(webSocket.getSession().getId() + "- 心跳丢失次数: " + webSocket.getLoseHeartCounter());
//                    }
//
//                    if (webSocket.getLoseHeartCounter() > MAX_LOSE_HEART_COUNT) {
//                        log.debug(webSocket.getSession().getId() + "- 挂了");
//                        webSocket.OnClose();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * @param @return
//     * @return long
//     * @Description: 获取时间戳
//     * @author fuzl
//     */
//    private static long getTimeInMillis() {
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 8);
//        return c.getTimeInMillis();
//    }
//}
