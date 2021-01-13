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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

    private static final BigDecimal b0 = new BigDecimal("0");
    private static final BigDecimal b1 = new BigDecimal("1");
    private static final BigDecimal b2 = new BigDecimal("2");
    private static final BigDecimal b3 = new BigDecimal("3");
    private static final BigDecimal b4 = new BigDecimal("4");
    private static final BigDecimal b5 = new BigDecimal("5");
    private static final BigDecimal b6 = new BigDecimal("6");
    private static final BigDecimal b7 = new BigDecimal("7");
    private static final BigDecimal b8 = new BigDecimal("8");
    private static final BigDecimal b9 = new BigDecimal("9");
    private static final BigDecimal b10 = new BigDecimal("10");

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

    /**
     * BigDecimal
     * 比较大小
     */
    @org.junit.Test
    public void test10(){
        BigDecimal b1 = new BigDecimal("1");
        BigDecimal b2 = new BigDecimal("2");
        BigDecimal b3 = new BigDecimal("3");
        BigDecimal b4 = new BigDecimal("4");
        BigDecimal b5 = new BigDecimal("5");
        BigDecimal b6 = new BigDecimal("6");
        BigDecimal b7 = new BigDecimal("7");
        BigDecimal b8 = new BigDecimal("8");
        BigDecimal b9 = new BigDecimal("9");
        BigDecimal b10 = new BigDecimal("10");

        BigDecimal grossWeight = MathUtils.getBigDecimal("10");

        /**
         * grossWeight > b1
         * 10 > 1
         */
        int a = grossWeight.compareTo(b1); // 1
        System.out.println(a);
        /**
         * grossWeight = b1
         * 10 = 10
         */
        int b = grossWeight.compareTo(b10); // 0
        System.out.println(b);
        /**
         * grossWeight < b1
         * 10 < 20
         */
        BigDecimal b20 = b10.multiply(b2);
        int c = grossWeight.compareTo(b20); // -1
        System.out.println(c);

    }

    /**
     * BigDecimal
     * 加减乘除
     */
    @org.junit.Test
    public void test11(){
        BigDecimal bignum1 = new BigDecimal("10");
        BigDecimal bignum2 = new BigDecimal("2");
        BigDecimal bignum3 = null;

        //加法
        bignum3 =  bignum1.add(bignum2);
        System.out.println("求和：" + bignum3);
        //减法
        bignum3 = bignum1.subtract(bignum2);
        System.out.println("求差：" + bignum3);
        //乘法
        bignum3 = bignum1.multiply(bignum2);
        System.out.println("乘法积：" + bignum3);
        //除法
        bignum3 = bignum1.divide(bignum2);
        System.out.println("除法结果：" + bignum3);

    }

    /**
     * BigDecimal
     * 向上取整
     * 向下取整
     */
    @org.junit.Test
    public void test12(){
        //取整
        BigDecimal bd = new BigDecimal("12");
        BigDecimal a = bd.setScale( 0, BigDecimal.ROUND_UP ); // 向上取整
        System.out.println(a);
        BigDecimal b = bd.setScale( 0, BigDecimal.ROUND_DOWN ); // 向下取整
        System.out.println(b);

    }



    /**
     * 数据表里的重量是**毛重**，要修改为**净重{
     * 毛重不能小于等于0
     * (0 < a < 1重量不变）
     * (1 <= a < 3重量减0.15），
     * (3 <= a < 10重量减1），
     * (10 <= a < 20减2)，
     * (20 <= a < 30减3)，
     * (30 <= a < 40减4)，
     * 以此类推下去)}
     */
    @org.junit.Test
    public void test13(){
        BigDecimal grossWeight = MathUtils.getBigDecimal("1");
        if(grossWeight.compareTo(b0) == -1 || grossWeight.compareTo(b0) == 0){
            throw new RuntimeException("毛重，数值不能小于或等于0");
        }
        //(0 < a < 1重量不变）
        else if(grossWeight.compareTo(b0) == 1 && grossWeight.compareTo(b1) == -1){
            grossWeight = grossWeight;
        }
        //(1 <= a < 3重量减0.15）
        else if((grossWeight.compareTo(b1) == 1 || grossWeight.compareTo(b1) == 0 )
                && (grossWeight.compareTo(b3) == -1)){
            BigDecimal bigDecimal = new BigDecimal("0.15");
            grossWeight = grossWeight.subtract(bigDecimal);
        }
        //(3 <= a < 10重量减1）
        else if ((grossWeight.compareTo(b3) == 1 || grossWeight.compareTo(b3) == 0 )
                && (grossWeight.compareTo(b10) == -1) ){
            grossWeight = grossWeight.subtract(b1);
        }
        //(10 <= a < 20减2)
        else if ((grossWeight.compareTo(b10) == 1 || grossWeight.compareTo(b10) == 0 )
                && (grossWeight.compareTo(b10.multiply(b2)) == -1 ) ){
            grossWeight = grossWeight.subtract(b2);
        }
        //(20 <= a < 30减3)
        else if ((grossWeight.compareTo(b10.multiply(b2)) == 1 || grossWeight.compareTo(b10.multiply(b2)) == 0 )
                && (grossWeight.compareTo(b10.multiply(b3)) == -1 ) ){
            grossWeight = grossWeight.subtract(b3);
        }
        //(30 <= a < 40减4)
        else if ((grossWeight.compareTo(b10.multiply(b3)) == 1 || grossWeight.compareTo(b10.multiply(b3)) == 0 )
                && (grossWeight.compareTo(b10.multiply(b4)) == -1 ) ){
            grossWeight = grossWeight.subtract(b4);
        }
        //(40 <= a < 50减5)
        else if ((grossWeight.compareTo(b10.multiply(b4)) == 1 || grossWeight.compareTo(b10.multiply(b4)) == 0 )
                && (grossWeight.compareTo(b10.multiply(b5)) == -1 ) ){
            grossWeight = grossWeight.subtract(b5);
        }
        //(50 <= a < 60减6)
        else if ((grossWeight.compareTo(b10.multiply(b5)) == 1 || grossWeight.compareTo(b10.multiply(b5)) == 0 )
                && (grossWeight.compareTo(b10.multiply(b6)) == -1 ) ){
            grossWeight = grossWeight.subtract(b6);
        }
        //(60 <= a < 70减7)
        else if ((grossWeight.compareTo(b10.multiply(b6)) == 1 || grossWeight.compareTo(b10.multiply(b6)) == 0 )
                && (grossWeight.compareTo(b10.multiply(b7)) == -1 ) ){
            grossWeight = grossWeight.subtract(b7);
        }
        //(70 <= a < 80减8)
        else if ((grossWeight.compareTo(b10.multiply(b7)) == 1 || grossWeight.compareTo(b10.multiply(b7)) == 0 )
                && (grossWeight.compareTo(b10.multiply(b8)) == -1 ) ){
            grossWeight = grossWeight.subtract(b8);
        }
        //(80 <= a < 90减9)
        else if ((grossWeight.compareTo(b10.multiply(b8)) == 1 || grossWeight.compareTo(b10.multiply(b8)) == 0 )
                && (grossWeight.compareTo(b10.multiply(b9)) == -1 ) ){
            grossWeight = grossWeight.subtract(b9);
        }
        //(90 <= a < 100减10)
        else if ((grossWeight.compareTo(b10.multiply(b9)) == 1 || grossWeight.compareTo(b10.multiply(b9)) == 0 )
                && (grossWeight.compareTo(b10.multiply(b10)) == -1 ) ){
            grossWeight = grossWeight.subtract(b10);
        }
        System.out.println(grossWeight);

    }

    @org.junit.Test
    public void test14(){
        String[] str = new String[]{"记录仪","摄像头","录像机"};
        for (int i=0; i < 10 ; i++){
            int a = i % str.length;
            System.out.println(a + ":" + str[a] + "_" + (i+1));

        }


    }

    @org.junit.Test
    public void test15(){
        String str = " \nBYT01231YYWB-AP";
        System.out.println(str);
        String s = str.replaceAll("\\s", "");//  "\\s"包含了回车换行制表符
        System.out.println(s);
    }

    @org.junit.Test
    public void test16(){
        Date now = new Date(); // 创建一个Date对象，获取当前时间
        SimpleDateFormat f = new SimpleDateFormat("今天是 " + "yyyy 年 MM 月 dd 日 E HH 点 mm 分 ss 秒");
        System.out.println(f.format(now)); // 将当前时间袼式化为指定的格式


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println(sdf.format(now));

    }


}
