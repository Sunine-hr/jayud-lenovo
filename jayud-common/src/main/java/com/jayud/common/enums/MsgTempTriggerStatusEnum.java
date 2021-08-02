package com.jayud.common.enums;

import com.jayud.common.entity.InitComboxStrVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 账单类型
 */
@Getter
@AllArgsConstructor
public enum MsgTempTriggerStatusEnum {

    TMS_T_0("T_0", "T_0", "待接单", SubOrderSignEnum.ZGYS.getSignOne()), //仅中港子订单状态用
    TMS_T_1("T_1", "T_1", "运输接单", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_1_1("T_1_1", "T_1_1", "运输接单驳回", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_2("T_2", "T_2", "运输派车", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_2_1("T_2_1", "T_2_1", "运输派车驳回", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_3("T_3", "T_3", "运输审核", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_3_1("T_3_1", "T_3_1", "运输审核不通过", SubOrderSignEnum.ZGYS.getSignOne()),//仅中港子订单状态用,这个是审核派车信息
    TMS_T_3_2("T_3_2", "T_3_2", "运输审核驳回", SubOrderSignEnum.ZGYS.getSignOne()),//仅中港子订单状态用,这个是驳回可编辑订单
    TMS_T_4("T_4", "T_4", "确认派车", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_4_1("T_4_1", "T_4_1", "确认派车驳回", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_5("T_5", "T_5", "车辆提货", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_5_1("T_5_1", "T_5_1", "车辆提货驳回", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_6("T_6", "T_6", "车辆过磅", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_7("T_7", "T_7", "通关前审核", SubOrderSignEnum.ZGYS.getSignOne()),//通关前审核必须先操作外部报关放行
    TMS_T_7_1("T_7_1", "T_7_1", "通关前审核不通过", SubOrderSignEnum.ZGYS.getSignOne()),//仅中港子订单状态用
    TMS_T_8("T_8", "T_8", "通关前复核", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_8_1("T_8_1", "T_8_1", "通关前复核不通过", SubOrderSignEnum.ZGYS.getSignOne()),//仅中港子订单状态用
    TMS_T_9("T_9", "T_9", "车辆通关", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_9_1("T_9_1", "T_9_1", "车辆通关查验", SubOrderSignEnum.ZGYS.getSignOne()),//仅中港子订单状态用
    TMS_T_9_2("T_9_2", "T_9_2", "车辆通关其他异常", SubOrderSignEnum.ZGYS.getSignOne()),//仅中港子订单状态用
    TMS_T_10("T_10", "T_10", "车辆入仓", SubOrderSignEnum.ZGYS.getSignOne()),
    //    TMS_T_11("T_11", "中转仓卸货",SubOrderSignEnum.ZGYS.getSignOne()),
//    TMS_T_12("T_12", "中转仓装货",SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_13("T_13", "T_13", "车辆出仓", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_14("T_14", "T_14", "车辆派送", SubOrderSignEnum.ZGYS.getSignOne()),
    TMS_T_15("T_15", "T_15", "确认签收", SubOrderSignEnum.ZGYS.getSignOne()),
    ;
    private String code;
    private String newCode;
    private String desc;
    private String mark;

    public static List<InitComboxStrVO> initComboxStrVO(String mark) {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (MsgTempTriggerStatusEnum value : values()) {
            if (Objects.equals(mark, value.getMark())) {
                InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
                initComboxStrVO.setName(value.getDesc());
                initComboxStrVO.setCode(value.getCode());
                list.add(initComboxStrVO);
            }
        }
        return list;
    }


    public static String getDesc(String code) {
        for (MsgTempTriggerStatusEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return "";
    }

    public static List<InitComboxStrVO> getModules() {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (MsgTempTriggerStatusEnum value : values()) {
            SubOrderSignEnum subOrderSignEnum = SubOrderSignEnum.getEnum(value.getMark());
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(subOrderSignEnum.getDesc());
            initComboxStrVO.setCode(subOrderSignEnum.getSignOne());
            list.add(initComboxStrVO);
        }
        return list;
    }

//    public static Integer getEnum(Integer code) {
//        for (MsgTempTriggerStatusEnum value : values()) {
//            if (Objects.equals(code, value.getCode())) {
//                return value;
//            }
//        }
//        return null;
//    }


//    public static Integer getCode(String desc) {
//        for (MsgTempTriggerStatusEnum value : values()) {
//            if (Objects.equals(desc, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return -1;
//    }


    /**
     * main|zgys|bg|ky
     * @param cmd
     * @return
     */
//    public static Integer getCode(String cmd) {
//        for (BusinessTypeEnum value : values()) {
//            if (Objects.equals(cmd, value.getDesc())) {
//                return value.getCode();
//            }
//        }
//        return -1;
//    }
}
