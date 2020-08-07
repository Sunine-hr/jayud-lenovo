package com.jayud.common.utils;

public class MoneyUtil {
    /**
     * 中文中简写的汉字金额 经常使用
     */
    public static String[] rmbNumbers = new String[]{"一", "二", "三", "四", "五", "六", "七" , "八", "九", "两", "廿", "卅", "○"};
    /**
     * 中文中繁写的汉字金额 经常使用
     */
    public static String[] bigNumbers = new String[]{"壹","贰","叁","肆","伍","陆","柒","捌","玖", "俩", "廿", "卅","零"};//大写的汉字
    /**
     * 与汉字相应的转化的数字
     */
    public static Long[] tonumbers = new Long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 2L, 2L, 3L, 0L};//转化为阿拉伯数字

    /**
     * 倍数关键词 简写 注意：一定要由大到小
     */
    public static String[] rmbMult = new String[]{"亿","万","千","百","十"};//大写中间隔的倍数
    /**
     * 倍数关键词 繁写
     */
    public static String[] bigRmbMult = new String[]{"億","萬","仟","佰","拾"};

    /**
     * 与倍数关键词对应的倍数
     */
    public static Long[] toMult = new Long[]{100000000L,10000L,1000L,100L,10L};//转化为阿拉伯的倍数

    /**
     * 大写转化为小写的过程操作，只处理到元，不带有单位
     * @param money 大写的金额，不带有单位 例如：1.二十一万 2.六五四三 3 贰拾
     * @return
     */
    public static String rmbBigToSmall(String money) {
        Long number = 0L;
        //遍历倍数的中文词遍历的时候一定要注意 选取的倍数词为最后一个倍数词,此次遍历为第一次遍历
        for(int i = 0; i < rmbMult.length; i++) {
            int index = money.lastIndexOf(rmbMult[i]) == -1?money.lastIndexOf(bigRmbMult[i]):money.lastIndexOf(rmbMult[i]);
            if(index >= 0) {
                String storeMult = money.substring(0, index);
                money = money.substring(index+1);
                System.out.println(rmbMult[i] + " " + toMult[i]);
                /**对于 十九万 这样的特殊的十的情况进行特殊处理*/
                if((storeMult == null || storeMult.length() <= 0) && toMult[i].intValue() == 10) {
                    number = number + toMult[i];
                } else {
                    number = number + (toMult[i] * getPrexNum(storeMult));
                }
            }
        }
        /**
         * 个位数的处理
         */
        number = number + getNumByBig(money);
        return number.toString();
    }
    /**
     * 辅助类，第二次循环
     * 此循环一般处理的都是倍数前面的数字，例如十九万，在这里就处理十九
     * @param storeMult 倍数前面的前缀词
     * @return
     */
    private static Long getPrexNum(String storeMult) {
        Long result = 0L;
        for(int i = 0; i < rmbMult.length; i++) {
            int index = storeMult.lastIndexOf(rmbMult[i]) == -1?storeMult.lastIndexOf(bigRmbMult[i]):storeMult.lastIndexOf(rmbMult[i]);
            if(index >= 0) {
                String storeMult2 = storeMult.substring(0, index);
                storeMult = storeMult.substring(index + 1);
                if((storeMult2 == null || storeMult2.length() <= 0) && toMult[i].intValue() == 10) {
                    result = result + toMult[i];
                } else {
                    result += getNumByBig(storeMult2) * toMult[i];
                }
            }
        }
        if(storeMult != null && storeMult.length() > 0) {
            result = result + getNumByBig(storeMult);
        }
        return result;
    }

    /**
     * 辅助类，大写的中文数字 转化为小写的阿拉伯数字
     * @param big
     * @return
     */
    private static Long getNumByBig(String big) {
        Long result = 0L;
        for(int j = 0; j < rmbNumbers.length; j++) {
            big = big.replaceAll(rmbNumbers[j], tonumbers[j].toString());
            big = big.replaceAll(bigNumbers[j], tonumbers[j].toString());
        }
        try {
            result = Long.valueOf(big);
        } catch(Exception e) {
            result = 0L;
        }
        return result;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String st = rmbBigToSmall("贰拾壹万肆仟陆佰元");
        System.out.println(rmbBigToSmall("十九"));
    }
}
