import com.alibaba.fastjson.JSONObject;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.tools.model.bo.QuerySensitiveCommodityForm;
import com.jayud.tools.model.bo.SensitiveCommodityForm;
import com.jayud.tools.model.po.SensitiveCommodity;
import com.jayud.tools.model.vo.CargoNameVO;
import com.jayud.tools.utils.MathUtils;
import com.jayud.tools.utils.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class Test {

    @org.junit.Test
    public void test1(){

        Double d = 22.924;
        BigDecimal b1 = MathUtils.getBigDecimal(d);
        String outString=b1.toString();
        System.out.println(outString);

        BigDecimal b2 = MathUtils.getBigDecimal(d, 3);
        outString=b2.toString();
        System.out.println(outString);

    }

    @org.junit.Test
    public void test2(){

        Field[] s = CargoNameVO.class.getDeclaredFields();
        int length = s.length;
        System.out.println();
    }

    @org.junit.Test
    public void test3(){
        String name = StringUtils.toUtf8String("XXX国际贸易公司");
        System.out.println(name);//XXX%E5%9B%BD%E9%99%85%E8%B4%B8%E6%98%93%E5%85%AC%E5%8F%B8
        //校验网址：https://www.qqxiuzi.cn/bianma/utf-8.htm
    }

    @org.junit.Test
    public void test4(){
        SensitiveCommodityForm sensitiveCommodityForm = new SensitiveCommodityForm();
        sensitiveCommodityForm.setId(1001L);
        sensitiveCommodityForm.setName("特殊敏感品名");
        System.out.println(JSONObject.toJSONString(sensitiveCommodityForm));
    }

    @org.junit.Test
    public void test5(){
        SensitiveCommodityForm commodityForm = new SensitiveCommodityForm();
        commodityForm.setName("sss");
        commodityForm.setId(1111L);
//        hutool 5.4.6 和 4.1.19 版本冲突
        SensitiveCommodity convert = ConvertUtil.convert(commodityForm, SensitiveCommodity.class);
        System.out.println(convert);
    }

    @org.junit.Test
    public void test6(){
        QuerySensitiveCommodityForm querySensitiveCommodityForm = new QuerySensitiveCommodityForm();
        querySensitiveCommodityForm.setName("特殊敏感品名");
        querySensitiveCommodityForm.setPageNum(1);
        querySensitiveCommodityForm.setPageSize(5);

        System.out.println(JSONObject.toJSONString(querySensitiveCommodityForm));

    }



}
