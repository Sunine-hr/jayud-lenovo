package com.jayud.common.utils;


import java.math.BigDecimal;

public class BigDecimalUtil {

    public static BigDecimal add(BigDecimal param1, BigDecimal param2) {
        param1 = param1 == null ? new BigDecimal(0) : param1;
        param2 = param2 == null ? new BigDecimal(0) : param2;
        return param1.add(param2);
    }

    public static BigDecimal subtract(BigDecimal param1, BigDecimal param2) {
        param1 = param1 == null ? new BigDecimal(0) : param1;
        param2 = param2 == null ? new BigDecimal(0) : param2;
        return param1.subtract(param2);
    }

    public static BigDecimal multiply(BigDecimal param1, BigDecimal param2) {
        param1 = param1 == null ? new BigDecimal(0) : param1;
        param2 = param2 == null ? new BigDecimal(0) : param2;
        return param1.multiply(param2);
    }

    public static BigDecimal divide(BigDecimal param1, BigDecimal param2) {
        param1 = param1 == null ? new BigDecimal(0) : param1;
        param2 = param2 == null ? new BigDecimal(0) : param2;
        return param1.divide(param2);
    }

}
