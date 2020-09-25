//package com.jayud.finance.enums;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
///**
// * 报关费用项-金蝶费用项对应枚举
// *
// * @author william
// * @description
// * @Date: 2020-09-19 19:43
// */
//@Getter
//@AllArgsConstructor
//public enum CustomsFeeEnum {
//    GAI_DAN_FEI("GDF", "改单费", "C0192", "改单费", "代理报关服务", "代理服务费"),
//    CANG_DAN_FEI("CDF", "舱单费", "C0002", "舱单费", "代理报关服务", "单证费"),
//    SHUI_FEI("SF", "税费", "C0003", "税费", "代理报关服务", "报关服务费"),
//    GANG_BAO_FEI("GBF1", "港保费", "C0004", "港保费", "代理报关服务", "报关服务费"),
//    HUAN_DAN_DAI_LI_FEI("HDDLF", "换单代理费", "C0033", "进口换单代垫费", "代理报关服务", "单证费"),
//    BAO_GUAN_FEI("BGF", "报关费", "C0030", "报关费", "代理报关服务", "报关服务费"),
//    GUO_JIAN_FEI("GJF", "国检费", "C0006","国检费",  "代理报关服务", "单证费"),
//    DAI_LI_FEI("DLF", "代理费", "C0005", "代理费", "代理报关服务", "代理服务费"),
//    CHA_GUI_FEI("CGF", "查柜费", "C0007", "查柜费", "代理报关服务", "港口码头费"),
//    WANG_FU_DAI_LI_FEI("WF", "网付代理费", "C0008", "网付代理费", "代理报关服务", "代理服务费"),
//    DAN_ZHENG_FEI("DZF", "单证费", "C0024", "代理报关单证费", "代理报关服务", "单证费"),
//    DA_DAN_FEI("DDF", "打单费", "C0009", "打单费", "代理报关服务", "报关服务费"),
//    BAO_SHUI_HE_ZHU_FEI("HZQD", "保税核注清单", "C0010", "保税核注清单", "代理报关服务", "代理服务费"),
//    //todo 是否有误
//    HAI_GUAN_TUI_YUN("HWTY","海关退运","C0015","其他收入","代理报关服务","报关服务费"),
//    GUAN_SHUI("GS", "关税", "C0037", "进口关税", "代理报关服务", "代垫税金"),
//    //todo 是否有误
//    ZENG_ZHI_SHUI("ZZS", "增值税", "C0039", "进口增值税", "代理报关服务", "代垫税金"),
//    //todo 是否有误
//    YUN_SHU_FEI("YSF", "运输费", "C0005", "代理费", "代理报关服务", "代理服务费"),
//    SHAN_DAN_FEI("SDCB", "删单费", "C0011", "删单重报费", "代理报关服务", "报关服务费"),
//    //todo 是否有误
//    GUO_BANG_FEI("GBF001", "过磅费", "C0048", "国内过磅费", "代理报关服务", "车费用"),
//    BAO_JIAN_FEI("BJF", "报检费", "C0014", "代理报检费", "代理报关服务", "单证费"),
//    SAN_C_REN_ZHENG_FEI("Y3C", "3C认证费用", "C0023", "3C认证费用", "代理报关服务", "单证费"),
//    XU_GUI_FEI("XGF", "续柜费", "C0001", "续柜费", "代理报关服务", "报关服务费"),
//    XU_YE_FEI("XYF", "续页费", "C0012", "报关续页费", "代理报关服务", "报关服务费"),
//    GONG_LU_CANG_DAN_FEI("GLCD", "公路舱单费", "C0013", "公路舱单费", "代理报关服务", "报关服务费"),
//    DAI_LI_BAO_JIAN_FEI("BJ", "代理报检费", "C0014", "代理报检费", "代理报关服务", "单证费"),
//    GANG_JIAN_FEI("MTF", "港建费", "C0032", "港建费", "代理报关服务", "港口码头费"),
//    BAO_AN_FEI("MTBA", "保安费", "C0004", "港保费", "代理报关服务", "港口码头费"),
//    CAO_ZUO_FEI("CZF", "操作费", "C0115", "操作费", "代理报关服务", "仓储服务费"),
//    YAN_GU_FEI("HGYGF", "验估费", "C0116", "查验费", "代理报关服务", "报关服务费"),
//    QI_TA_SHOU_RU("qts_cost", "其他收入", "C0015", "其他收入", "代理报关服务", "报关服务费"),
//    XIU_GUI_FEI("XGFY", "修柜费用", "C0016 ", "修柜费 ", "代理报关服务", "货柜费用"),
//    HUAN_DAN_FEI("HDF", "换单费", "C0033", "进口换单代垫费", "代理报关服务", "单证费"),
//    KSSMF("KSSMF", "快速扫描费", "C0017", "快速扫描费", "代理报关服务", "单证费"),
//    JKHDF("JKHDF", "进口换单费", "C0033", "进口换单代垫费", "代理报关服务", "单证费"),
//    BHF("BHF", "编号费", "C0020", "编号费", "代理报关服务", "代理服务费"),
//    GCF("GCF", "改船费", "C0019", "改船费", "代理报关服务", "单证费"),
//    BCLHF("BCLHF", "驳船理货费", "C0018", "驳船理货费", "代理报关服务", "单证费"),
//    sjhz_cost("sjhz_cost", "商检换证费", "C0021", "商检换证费", "代理报关服务", "单证费"),
//    sjbj_cost("sjbj_cost", "商检报检费", "C0014", "代理报检费", "代理报关服务", "单证费"),
//    xzxd_cost("xzxd_cost", "熏蒸消毒费", "C0015", "其他收入", "代理报关服务", "报关服务费"),
//    sjdd_cost("sjdd_cost", "商检打单费", "C0014", "代理报检费", "代理报关服务", "单证费"),
//    CYDL("CYDL", "查验代理费", "C0116", "查验费", "代理报关服务", "代理服务费"),
//    TTSYF("TTSYF","抬头费","C0015","其他收入","代理报关服务","报关服务费"),
//
//    //    tcf_cost("tcf_cost", "舱单费", "C0002", "舱单费", "代理报关服务", "单证费"),
//    qtz_cost("qtz_cost", "其他支出", "C0015", "其他收入", "代理报关服务", "报关服务费"),
////    DL("DL", "代理费", "C0005", "代理费", "代理报关服务", "代理服务费"),
//    PTFWF("PTFWF", "平台服务费", "C0008", "网付代理费", "代理报关服务", "代理服务费"),
//    XFS("XFS", "消费税", "C0038", "进口消费税", "代理报关服务", "代垫税金"),
//    FDLHF("FDLHF", "分单理货费", "C0084", "理货费", "代理报关服务", "装卸费"),
//    BYJ("BYJ", "备用金", "#N/A", "#N/A", "代理报关服务", "#N/A"),
//    sjdl_cost("sjdl_cost", "商检代理", "C0014", "代理报检费", "代理报关服务", "单证费"),
//    kd_cost("kd_cost", "快递费", "C4127", "快递费", "代理报关服务", "快递费"),
//    qtc_cost("qtc_cost", "其他代垫费", "C0015", "其他收入", "代理报关服务", "报关服务费"),
//
//    ;
//
//    private String yunbaoguanCode;
//    private String yunbaoguan;
//    private String kingdeeCode;
//    private String kingdee;
//    private String type;
//    private String category;
//
//    /**
//     * 通过输入云报关的费用名称，尝试匹配金蝶的费用名称
//     *
//     * @param yunbaoguanFeeName
//     * @return
//     */
//    public static CustomsFeeEnum getEnum(String yunbaoguanFeeName) {
//        for (CustomsFeeEnum value : CustomsFeeEnum.values()) {
//            if (value.getYunbaoguan().equals(yunbaoguanFeeName)) {
//                return value;
//            }
//        }
//        return null;
//    }
//}
