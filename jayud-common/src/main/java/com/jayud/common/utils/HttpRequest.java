package com.jayud.common.utils;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bouncycastle.asn1.cms.CMSObjectIdentifiers.encryptedData;

public class HttpRequest {
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
 
    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "application/x-www-form-urlencoded");
//            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //1.获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //2.中文有乱码的需要将PrintWriter改为如下
            //out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8")
            // 发送请求参数
            out.print(JSONUtil.toJsonStr(param));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        System.out.println("post推送结果："+result);
        return result;
    }
    
    public static void main(String[] args) throws IOException {
        //发送 GET 请求
//        String s=HttpRequest.sendGet("http://192.168.3.35:8221/api/checkOrder/getDataByOms", "key=123&v=456");
//        System.out.println(s);
//
String URL="http://192.168.3.31:8880/test";
String URL1="http://192.168.3.35:8221/api/checkOrder/getDataByOms";

        //发送 POST 请求
        String  aa="i9CMsGfO2Ez1hePqQs-7DvkIu1ghZ1gi4jCBfD4WgxzX-6Lg87oGXP-C00eCjzIfvqMxZAaOL01b_rWX_5oqOkUK3vef78jw7r_Yti52ZQ8W9eZZ7kW7WO2f8KioW6wvm2mqP8-gIEW8vdoTG5xdJcn5q1Voz0N-AfyhPvBAc5SBZTDqh3EHqsiiBk7a4SAtmf8pHWe5b4NY_yToypVXR-VjxU7PfXasRNnTf3e-d-OKEzaT-oWYECnc3HvU5T0Z";
//        String data1 = JSONObject.toJSONString(aa);

//        String sr=HttpRequest.sendPost("http://192.168.3.35:8221/api/checkOrder/getDataByOms", "data="+orderIns);
//        System.out.println(sr);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",aa);
        String s = jsonObject.toString();
        System.out.println(s);
        String feedback = cn.hutool.http.HttpRequest
                .post(URL1)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
//                .header("token",s)
//                .body(JSONUtil.toJsonStr(hgTruckApiVO))
                .body(s)
                .execute().body();

        System.out.println(feedback);


//        String s = httpPostWithjson(URL, data1);
//        System.out.println(s);
//        String feedback = HttpRequest
//                .post(createOutOrderTransport)
//                .header(HttpHeaders.CONTENT_TYPE, "application/json")
////                .header("token",s)
////                .body(JSONUtil.toJsonStr(hgTruckApiVO))
//                .body(JSONUtil.toJsonStr(encryptedData))
//                .execute().body();

    }
    public static String httpPostWithjson(String url, String json) throws IOException {
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            BasicResponseHandler handler = new BasicResponseHandler();
            StringEntity entity = new StringEntity(json, "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            result = httpClient.execute(httpPost, handler);
            return result;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}