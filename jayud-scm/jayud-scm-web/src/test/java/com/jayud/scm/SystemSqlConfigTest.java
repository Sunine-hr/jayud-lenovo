package com.jayud.scm;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jayud.scm.model.vo.TableColumnVO;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void test1(){
        BigDecimal a = BigDecimal.ONE;
        System.out.println(a);
    }

    @Test
    public void text2(){
        /*java字符串大小写转换
                .toLowerCase();//转成小写
                .toUpperCase();//转成大写
        */
        String aa = "aaabbsds张三..。。..BBHGG";
        System.out.println(aa.toLowerCase());
        System.out.println(aa.toUpperCase());

    }

    @Test
    public void test3(){
        String sqlStr = "select   \n" +
                "t.id,t.sql_code,t.sql_name,t.sql_str,t.sql_params,t.is_hav_param,t.is_condition,t.msg_str,t.column_name,t.sql_data_str,t.sql_sum_column,t.remark,t.crt_by,t.crt_by_name,t.crt_by_dtm,t.mdy_by,t.mdy_by_name,t.mdy_by_dtm,t.voided,t.voided_by,t.voided_by_name,t.voided_by_dtm  \n" +
                "from system_sql_config t \n" +
                "@WHERE \n" +
                "t.sql_code = @sqlCode and t.sql_name = @sqlName";

        String where = "@WHERE";
        String[] split = sqlStr.split(where);
        System.out.println(split.length);


        sqlStr = split[0];
        String whereCond = split[1];
        System.out.println(sqlStr);
        System.out.println(whereCond);

    }

    @Test
    public void test4(){
        String sqlParams = "    ";
        if(StrUtil.isEmpty(sqlParams.trim())){
            System.out.println("xxx");
        }else{
            System.out.println("yyy");
        }
    }

    @Test
    public void underlineToCamelhumpTest(){

        String inputString = "id";
        String s = underlineToCamelhump(inputString);
        System.out.println(s);

    }

    @Test
    public void replaceTest(){
        String score = "where 1=1 | and t.sql_code = '@sqlCode' | and t.sql_name = '@sqlName'";
        String xxx = score.replace("@sqlCode", "xxx");
        System.out.println(xxx);
        System.out.println(score);
    }



    @Test
    public void regexTest(){
        String paraIdentifier = "@";//查询参数标识符

        Map<String, Object> condPara = new HashMap<>();
//        condPara.put("sqlCode", "sql_conf_8848");
//        condPara.put("sqlName", "sql配置基础查询");



        //String score="数学=100,语文=69;英语=70,计算机=90:地理=85,体育=50";
        String score = "where 1=1 | and t.sql_code = '@sqlCode' | and t.sql_name = '@sqlName'";
        String[] scores=score.split("[|]");
        for(String str:scores)
        {
            System.out.println(str);
        }

        StringBuffer stringBuffer = new StringBuffer();
        for (int k=0; k<scores.length; k++){
            String s = scores[k];
            if(k== 0){
                stringBuffer.append(s);//第一个where条件，直接放入，不用匹配
            }else{
                //condPara
                for (Map.Entry<String, Object> m : condPara.entrySet()) {
                    //System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                    String key = m.getKey();//键
                    Object value = m.getValue();//值

                    String matchesParam  = paraIdentifier+key;//例子：@sqlCode

                    //匹配`字符串s`中是否含有`字符串matchesParam`
                    if(s.contains(matchesParam)){
                        //替换
                        String replace = s.replace(matchesParam, value.toString());
                        stringBuffer.append(replace);
                        break;
                    }
                }
            }
        }
        String where = stringBuffer.toString();
        System.out.println(where);


    }


    /**
     * 将下划线风格替换为驼峰风格
     *
     * @param inputString
     * @return
     */
    public String underlineToCamelhump(String inputString) {
        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (c == '_') {
                if (sb.length() > 0) {
                    nextUpperCase = true;
                }
            } else {
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }


}
