//package com.jayud.oms.service.impl;
//
//import cn.hutool.json.JSONObject;
//import com.jayud.oms.service.DingDingMsgService;
//
//public class DingDingMsgServiceImpl implements DingDingMsgService {
//
//    @Override
//    public JSONObject send(String mobile, String content) {
//        return null;
//    }
//
//    @Override
//    public JSONObject getAccessToken(String appKey, String appsecret) {
//        return null;
//    }
//
//    @Override
//    public JSONObject senWorkMsg(String accessToken, String userId, String content, String appKey, String appsecret) {
//        return null;
//    }
//
//    @Override
//    public JSONObject initUserList(String accessToken, String appKey, String appsecret) {
//        return null;
//    }
//
//    @Override
//    public JSONObject getMobile(String accessToken, String userId, String appKey, String appsecret) {
//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
//        OapiUserGetRequest request = new OapiUserGetRequest();
//        request.setUserid(userId);
//        request.setHttpMethod("GET");
//        OapiUserGetResponse response = client.execute(request, accessToken);
//        return response.getMobile();
//
//    }
//}
