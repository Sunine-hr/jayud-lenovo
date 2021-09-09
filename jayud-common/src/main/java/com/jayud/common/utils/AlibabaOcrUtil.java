package com.jayud.common.utils;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class AlibabaOcrUtil {

    public static String characterRecognition(String base64,String appCode) {
        String host = "https://ocrapi-advanced.taobao.com";
        String path = "/ocrservice/advanced";
        String method = "POST";
//        String appcode = "f3b8f4766eda484b93bf1fbb6a11d462";
        String appcode = appCode;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        String bodys = "{\"img\":\"" + base64 + "\",\"url\":\"\",\"prob\":false,\"charInfo\":false,\"rotate\":false,\"table\":false}";

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = AlibabaHttpUtil.doPost(host, path, method, headers, querys, bodys);
            //获取response的body
            String responseStr = EntityUtils.toString(response.getEntity());
            System.out.println(responseStr);
            return responseStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
