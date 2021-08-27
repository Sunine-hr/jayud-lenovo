package com.jayud.tools.controller;

import com.jayud.tools.utils.WeixinCheckUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 微信小程序-消息推送配置-验证 controller
 */
@RestController
@RequestMapping("/weixinkey")
public class WeixinKeyController {

    /**
     * 获取请求getProcessRequest,检验signature
     * 微信小程序-消息推送配置，URL(服务器地址)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = {"/getProcessRequest"}, method= {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    protected void getProcessRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isGet=request.getMethod().toLowerCase().equals("get");
        System.out.println(isGet);
        System.out.println("方法是-------"+isGet);
        if(isGet) {//首次验证token
            // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");

            PrintWriter out = null;
            try {
                out = response.getWriter();
                // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
                if (WeixinCheckUtil.checkSignature(signature, timestamp, nonce)) {
                    System.out.println("成功");
                    out.print(echostr);
                    out.flush();  //必须刷新
                }
                System.out.println("失败");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
                out = null;

            }
        }else{// 进入POST聊天处理
            System.out.println("进入了聊天界面");
            // 接收消息并返回消息
            try {
                System.out.println("进入这个方法中了:");
                acceptMessage(request,response);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    public void acceptMessage(HttpServletRequest request,HttpServletResponse response) throws Exception {

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }

//        /*BufferedReader readers = new BufferedReader(new InputStreamReader(request.getInputStream()));*/
//        JSONObject jsonObject = JSONObject.fromObject(responseStrBuilder.toString());
//        String param= jsonObject.toString();
//        System.out.println("转出来的数据:"+param);
//        String attribute = jsonObject.get("FromUserName").toString();//发送的openid
////        String ToUserName = jsonObject.get("ToUserName").toString();//小程序原始id
//        String MsgType = jsonObject.get("MsgType").toString(); //用户发送客服信息的类型
//        System.out.println("发送数据的类型:"+MsgType);
//        System.out.println(MsgType);
//        if(MsgType.equals("event")) {
//            Map<String, Object> text = new HashMap<String, Object>();
//            Feedbacktxt feedbacktxt = new Feedbacktxt();
//            feedbacktxt.setTouser(attribute);
//            feedbacktxt.setMsgtype("text");
//            text.put("content", "你好，欢迎来到这里");
//            feedbacktxt.setText(text);
//            String token = feedbackService.access_token(); //这里是获取接口凭证的接口
//            String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
//            JSONObject jsonStrs = JSONObject.fromObject(feedbacktxt);
//            HttpUtils.httpPost(url, jsonStrs.toString());
//        }
    }


}
