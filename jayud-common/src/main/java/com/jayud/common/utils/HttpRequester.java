package com.jayud.common.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @anthor Satellite
 * HttpRequesterUtil
 * 用HttpClient模拟发送http post&get请求
 * http://www.javalow.com
 * @date 2018-05-01-17:57
 **/
public class HttpRequester {

    /**
     * 编码
     */
    private String defaultContentEncoding;

    /**
     * 链接超时时间
     */
    private int connectTimeout = 10000;

    /**
     * 读取内容超时时间
     */
    private int readTimeout = 10000;

    /**
     * 默认构造函数，使用默认的响应内容编码和超时时间
     */
    public HttpRequester() {
        this.defaultContentEncoding = Charset.defaultCharset().name();
    }

    /**
     * 构造函数，使用自定义的响应内容编码，使用默认的超时时间
     *
     * @param contentEncodeing 编码字符，如：GBK
     */
    public HttpRequester(String contentEncodeing) {
        this.defaultContentEncoding = contentEncodeing;
    }

    /**
     * 构造函数，使用能够自定义的超时时间，使用默认的响应内容编码
     *
     * @param connectTimeout 链接超时时间
     * @param readTimeout    读取内容超时时间
     */
    public HttpRequester(int connectTimeout, int readTimeout) {
        this.defaultContentEncoding = Charset.defaultCharset().name();
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    /**
     * 构造函数，使用自定义的响应内容编码，自定义的超时时间
     *
     * @param contentEncodeing 编码字符，如：GBK
     * @param connectTimeout   链接超时时间
     * @param readTimeout      读取超时时间
     */
    public HttpRequester(String contentEncodeing, int connectTimeout, int readTimeout) {
        this.defaultContentEncoding = contentEncodeing;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }


    /**
     * 模拟http发送get请求 返回数据以JSONObject接收
     *
     * @param urlString url链接
     * @param params    参数map对象
     * @param propertys 请求头参数map对象
     * @return JSONObject
     * @anthor Satellite
     */
    public JSONObject sendGet(String urlString, Map<String, String> params, Map<String, String> propertys) throws Exception {
        JSONObject result = null;
        //拼接参数
        if (params != null) {
            StringBuffer param = new StringBuffer();
            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(params.get(key));
                i++;
            }
            urlString += param;
        }

        //创建httpClient连接
        CloseableHttpClient httpClient = HttpClients.createDefault();

        StringBuilder urlStringBuilder = new StringBuilder(urlString);

        try {
            //利用URL生成一个HttpGet请求
            HttpGet httpGet = new HttpGet(urlStringBuilder.toString());

            //设置请求头参数
            if (propertys != null) {
                for (String key : propertys.keySet()) {
                    httpGet.setHeader(key, propertys.get(key));
                }
            }
            // HttpClient 发送请求
            CloseableHttpResponse httpResponse = null;
            try {
                httpResponse = httpClient.execute(httpGet);
                return this.makeContent(httpResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 模拟http发送post请求 返回数据以JSONObject接收
     *
     * @param urlString url链接
     * @param params    参数map对象
     * @param propertys 请求头参数map对象
     * @return JSONObject
     * @anthor Satellite
     */
    public JSONObject sendPost(String urlString, Map<String, String> params, Map<String, String> propertys) throws Exception {
        JSONObject result = null;
        //创建httpClient连接
        CloseableHttpClient httpClient = HttpClients.createDefault();

        List<NameValuePair> nameValuePairArrayList = new ArrayList<>();
        // 将传过来的参数添加到List<NameValuePair>中
        if (params != null && !params.isEmpty()) {
            //遍历map
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairArrayList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity entity = null;
        // HttpClient 发送请求
        CloseableHttpResponse httpResponse = null;
        try {
            // 利用List<NameValuePair>生成Post请求的实体数据
            // UrlEncodedFormEntity 把输入数据编码成合适的内容
            entity = new UrlEncodedFormEntity(nameValuePairArrayList, defaultContentEncoding);
            entity.setContentType("application/json");
            entity.setContentEncoding("utf-8");
            HttpPost httpPost = new HttpPost(urlString);
            // 为HttpPost设置实体数据
            httpPost.setEntity(entity);
            //设置请求头参数
            if (propertys != null) {
                for (String key : propertys.keySet()) {
                    httpPost.setHeader(key, propertys.get(key));
                }
            }
            // HttpClient 发送Post请求
            httpResponse = httpClient.execute(httpPost);
            result = this.makeContent(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject sendPostByJson(String urlString, Map<String, String> params, Map<String, String> propertys) throws Exception {
        JSONObject result = null;
        //创建httpClient连接
        CloseableHttpClient httpClient = HttpClients.createDefault();

        List<NameValuePair> nameValuePairArrayList = new ArrayList<>();
        // 将传过来的参数添加到List<NameValuePair>中
        if (params != null && !params.isEmpty()) {
            //遍历map
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairArrayList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        StringEntity entity = null;
        // HttpClient 发送请求
        CloseableHttpResponse httpResponse = null;
        try {
            // 利用List<NameValuePair>生成Post请求的实体数据
            // UrlEncodedFormEntity 把输入数据编码成合适的内容
            entity = new StringEntity(JSONUtil.toJsonStr(nameValuePairArrayList), ContentType.APPLICATION_JSON);
            HttpPost httpPost = new HttpPost(urlString);
            // 为HttpPost设置实体数据
            httpPost.setEntity(entity);
            //设置请求头参数
            if (propertys != null) {
                for (String key : propertys.keySet()) {
                    httpPost.setHeader(key, propertys.get(key));
                }
            }
            // HttpClient 发送Post请求
            httpResponse = httpClient.execute(httpPost);
            result = this.makeContent(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param httpResponse
     * @return
     * @throws IOException
     * @author Satellite
     */
    private JSONObject makeContent(CloseableHttpResponse httpResponse) throws IOException {
        JSONObject result = null;
        StringBuilder strBuilder = new StringBuilder();
        //得到httpResponse的状态响应码
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            //得到httpResponse的实体数据
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "UTF-8"), readTimeout);
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        strBuilder.append(line);
                    }
                    // 从HttpEntity中得到的json String数据转为json
                    String json = strBuilder.toString();
                    result = JSONUtil.parseObj(json);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            //关闭流
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return result;
    }


    public String getDefaultContentEncoding() {
        return defaultContentEncoding;
    }

    public void setDefaultContentEncoding(String defaultContentEncoding) {
        this.defaultContentEncoding = defaultContentEncoding;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
}
