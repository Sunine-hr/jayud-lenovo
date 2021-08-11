package com.jayud.scm;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jayud.scm.model.vo.TableColumnVO;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//测试接口时放开，平时注释掉
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class SystemSqlConfigTest {

    @Test
    public void createJson(){
        /*
        //JSON数据
        //list -> json
        String json = JSONObject.toJSONString(tableColumnList);
        //json -> list
        List<TableColumnVO> lsit = JSON.parseObject(json, new TypeReference<List<TableColumnVO>>() {});
        */
        List<TableColumnVO> lsit = new ArrayList<>();
        TableColumnVO tableColumnVO = new TableColumnVO();
        tableColumnVO.setColumnProp("sqlCode");
        tableColumnVO.setColumnLabel("SQL代码");
        tableColumnVO.setColumnWidth("100");
        tableColumnVO.setColumnSort(1);
        tableColumnVO.setColumnStatus("1");
        lsit.add(tableColumnVO);

        String json2 = JSONObject.toJSONString(lsit);
        List<TableColumnVO> lsit2 = JSON.parseObject(json2, new TypeReference<List<TableColumnVO>>() {});
        System.out.println(json2);
        System.out.println(lsit2);

    }

    @Test
    public void urlEncoder() throws UnsupportedEncodingException {
        String json = "[{\"columnLabel\":\"SQL代码\",\"columnProp\":\"sqlCode\",\"columnSort\":1,\"columnStatus\":\"1\",\"columnWidth\":\"100\"}]";
        System.out.println(json);
        //编码
        String json2 = URLEncoder.encode(json, "utf-8");
        System.out.println(json2);
        //解码
        String json3 = URLDecoder.decode(json2,"utf-8");
        System.out.println(json3);

    }

}
