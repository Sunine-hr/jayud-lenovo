package com.jayud.common.enums;

import com.jayud.common.entity.InitComboxStrVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 订单标识
 */
@Getter
@AllArgsConstructor
public enum SubOrderSignEnum {

    MAIN("main", "order_info", "主订单"),
    KY("ky", "air_order", "空运"),
    BG("bg", "order_customs", "报关"),
    ZGYS("zgys", "order_transport", "中港运输"),
    HY("hy", "sea_order", "海运"),
    NL("nl", "order_inland_transport", "内陆运输"),
    TC("tc", "trailer_order", "拖车"),
    CCI("cci", "storage_input_order", "仓储入库"),
    CCE("cce", "storage_out_order", "仓储出库"),
    CCF("ccf", "storage_fast_order", "仓储快进快出");
    //    private Integer code;
    private String signOne;
    private String signTwo;
    private String desc;

    public static String getSignOne2SignTwo(String signOne) {
        for (SubOrderSignEnum value : values()) {
            if (Objects.equals(signOne, value.getSignOne())) {
                return value.getSignTwo();
            }
        }
        return "";
    }

    public static SubOrderSignEnum getEnum(String signOne) {
        for (SubOrderSignEnum value : values()) {
            if (Objects.equals(signOne, value.getSignOne())) {
                return value;
            }
        }
        return null;
    }

    public static List<InitComboxStrVO> initBusinessType() {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (SubOrderSignEnum value : values()) {
//            if (value != MAIN) {
                InitComboxStrVO tmp = new InitComboxStrVO();
                tmp.setName(value.getDesc());
                tmp.setCode(value.getSignOne());
                list.add(tmp);
//            }
        }
        return list;
    }

//    public static Integer getCode(String desc) {
//        for (SubOrderSignEnum value : values()) {
//            if (Objects.equals(desc, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return -1;
//    }
}
