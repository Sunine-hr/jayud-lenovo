package com.jayud.oms.config;

public class TypeUtils {

    public static Integer getTypeCode(String desc){
        if(desc.equals("同行")){
            return 1;
        }
        if(desc.equals("电商")){
            return 2;
        }
        if(desc.equals("货代")){
            return 3;
        }
        return 0;
    }

    public static String getIfContract(String str){
        if(str.equals("是")){
            return "1";
        }
        return "0";
    }

    public static Integer getSettlementType(String str){
        if(str.equals("票结")){
            return 1;
        }
        if(str.equals("月结")){
            return 2;
        }
        if(str.equals("周结")){
            return 3;
        }
        return 0;
    }

}
