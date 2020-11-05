import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.HanLP;
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

    /**
     * 汉语言文化圈
     */
    @org.junit.Test
    public void test7(){
        // 简体转台湾繁体
        System.out.println(HanLP.s2tw("hankcs在台湾写代码,我爱中国"));//输出：hankcs在臺灣寫程式碼,我愛中國
        // 台湾繁体转简体
        System.out.println(HanLP.tw2s("hankcs在臺灣寫程式碼,我愛中國"));//输出：hankcs在台湾写代码,我爱中国

        //香港的业务，用香港的繁体
        // 简体转香港繁体
        System.out.println(HanLP.s2hk("hankcs在香港写代码,我爱中国"));//输出：hankcs在香港寫代碼,我愛中國
        // 香港繁体转简体
        System.out.println(HanLP.hk2s("hankcs在香港寫代碼,我愛中國"));//输出：hankcs在香港写代码,我爱中国

    }

    @org.junit.Test
    public void test8(){
        String name1 = HanLP.hk2s("扬声器");
        System.out.println(name1);
        String name2 = HanLP.s2hk("扬声器");
        System.out.println(name2);

    }

    @org.junit.Test
    public void test9(){
        String name1 = HanLP.hk2s("揚聲器");
        System.out.println(name1);
        String name2 = HanLP.s2hk("揚聲器");
        System.out.println(name2);
    }



}
