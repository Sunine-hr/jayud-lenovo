package com.jayud.finance.enums;

import jdk.nashorn.internal.ir.ReturnNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报关费用项-金蝶费用项对应枚举
 *
 * @author william
 * @description
 * @Date: 2020-09-19 19:43
 */
@Getter
@AllArgsConstructor
public enum CustomsFeeEnum {
    //    GAI_DAN_FEI("改单费","","报关服务","报关服务费"),
    CANG_DAN_FEI("舱单费", "舱单费", "报关服务", "报关服务费"),
    SHUI_FEI("税费", "税费", "报关服务", "报关服务费"),
    GANG_BAO_FEI("港保费", "港保费", "报关服务", "报关服务费"),
    HUAN_DAN_DAI_LI_FEI("换单代理费", "换单代理费", "报关服务", "报关服务费"),
    E_BAO_GUAN_FEI("报关费", "CH出口报关费", "报关服务", "报关服务费"),
    I_BAO_GUAN_FEI("报关费", "CH进口报关费", "报关服务", "报关服务费"),
    GUO_JIAN_FEI("国检费", "国检费", "报关服务", "报关服务费"),
    E_DAI_LI_FEI("代理费", "HK出口报关代理费", "报关服务", "报关服务费"),
    I_DAI_LI_FEI("代理费", "HK进口报关代理费", "报关服务", "报关服务费"),
    CHA_GUI_FEI("查柜费", "查柜费", "报关服务", "报关服务费"),
    WANG_FU_DAI_LI_FEI("网付代理费", "网付代理费", "报关服务", "报关服务费"),
    DAN_ZHENG_FEI("单证费", "代理报关单证费", "报关服务", "报关服务费"),
    DA_DAN_FEI("打单费", "打单费", "报关服务", "报关服务费"),
    BAO_SHUI_HE_ZHU_FEI("保税核注清单", "保税核注清单", "报关服务", "报关服务费"),
    //todo 是否有误
    GUAN_SHUI("关税", "进口关税", "报关服务", "报关服务费"),
    //todo 是否有误
    ZENG_ZHI_SHUI("增值税", "进口增值税", "报关服务", "报关服务费"),
    //todo 是否有误
    YUN_SHU_FEI("运输费", "中港运输费", "报关服务", "报关服务费"),
    SHAN_DAN_FEI("删单费", "删单重报费", "报关服务", "报关服务费"),
    //todo 是否有误
    GUO_BANG_FEI("过磅费", "CH过磅费", "报关服务", "报关服务费"),
    SAN_C_REN_ZHENG_FEI("3C认证费用", "3C认证费用", "报关服务", "报关服务费"),
    XU_GUI_FEI("续柜费", "续柜费", "报关服务", "报关服务费"),
    //    HAI_GUAN_TUI_YUN("海关退运", "", "报关服务", "报关服务费"),
    XU_YE_FEI("续柜费", "续柜费", "报关服务", "报关服务费"),
    GONG_LU_CANG_DAN_FEI("公路舱单费", "公路舱单费", "报关服务", "报关服务费"),
    DAI_LI_BAO_JIAN_FEI("代理报检费", "代理报检费", "报关服务", "报关服务费"),
    GANG_JIAN_FEI("港建费", "港建费", "报关服务", "报关服务费"),
    //    BAO_AN_FEI("保安费", "", "报关服务", "报关服务费"),
    QI_TA_SHOU_RU("其他收入", "其他收入", "报关服务", "报关服务费"),
//    CAO_ZUO_FEI("操作费", "", "报关服务", "报关服务费"),
//    TAI_TOU_FEI("抬头费", "", "报关服务", "报关服务费"),
//    YAN_GU_FEI("验估费", "", "报关服务", "报关服务费"),
//    QI_TA_DAI_DIAN_FEI("其他代垫费", "", "报关服务", "报关服务费"),

    ;

    private String yunbaoguan;
    private String kingdee;
    private String type;
    private String category;

    public static CustomsFeeEnum getEnum(String yunbaoguanFeeName) {
        for (CustomsFeeEnum value : CustomsFeeEnum.values()) {
            if (value.getYunbaoguan().equals(yunbaoguanFeeName)) {
                return value;
            }
        }
        return null;
    }
}
