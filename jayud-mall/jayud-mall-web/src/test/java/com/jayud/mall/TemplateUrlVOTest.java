package com.jayud.mall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jayud.mall.model.vo.TemplateUrlVO;
import org.junit.Test;

import java.util.List;

public class TemplateUrlVOTest {

    @Test
    public void test1(){
        String json = "[{\n" +
                "\t\"fileName\": \"2021 春节信息部假期安排1.xlsx\",\n" +
                "\t\"relativePath\": \"/group1/M00/00/16/wKgA52A4Yc-AarNUAAAsIDCZxT430.xlsx\",\n" +
                "\t\"absolutePath\": \"http://test.oms.jayud.com:9448/group1/M00/00/16/wKgA52A4Yc-AarNUAAAsIDCZxT430.xlsx\"\n" +
                "}, {\n" +
                "\t\"fileName\": \"2021 春节信息部假期安排2.xlsx\",\n" +
                "\t\"relativePath\": \"/group1/M00/00/16/wKgA52A4Yc-AarNUAAAsIDCZxT430.xlsx\",\n" +
                "\t\"absolutePath\": \"http://test.oms.jayud.com:9448/group1/M00/00/16/wKgA52A4Yc-AarNUAAAsIDCZxT430.xlsx\"\n" +
                "}, {\n" +
                "\t\"fileName\": \"2021 春节信息部假期安排3.xlsx\",\n" +
                "\t\"relativePath\": \"/group1/M00/00/16/wKgA52A4Yc-AarNUAAAsIDCZxT430.xlsx\",\n" +
                "\t\"absolutePath\": \"http://test.oms.jayud.com:9448/group1/M00/00/16/wKgA52A4Yc-AarNUAAAsIDCZxT430.xlsx\"\n" +
                "}]";

        List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json,new TypeReference<List<TemplateUrlVO>>(){});
        System.out.println(templateUrlVOS);


        String s = JSONObject.toJSONString(templateUrlVOS);
        System.out.println(s);

    }


    @Test
    public void test2(){
        String json = "";
        try {
            List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>(){});
            System.out.println(templateUrlVOS);
        }catch (JSONException e){
            e.printStackTrace();
            System.out.println("json格式错误");
        }

    }

    @Test
    public void test3(){
        String s = JSONObject.toJSONString(null);
        System.out.println(s);

    }



}
