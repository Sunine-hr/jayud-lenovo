package com.jayud.finance.enums;

import com.jayud.finance.service.BillTemplateService;
import com.jayud.finance.service.impl.PayBillTemplateServiceImpl;
import com.jayud.finance.service.impl.PaySupplierBillTemplateServiceImpl;
import com.jayud.finance.vo.InitComboxStrVO;
import com.jayud.finance.vo.template.order.*;
import com.jayud.finance.vo.template.pay.InlandTPPayTemplate;
import com.jayud.finance.vo.template.pay.TmsOrderPayTemplate;
import com.jayud.finance.vo.template.pay.TrailerOrderPayTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public enum BillTemplateEnum {


    KY("ky", AirOrderTemplate.class, "空运", false, 0, null),
    ZGYS("zgys", TmsOrderTemplate.class, "中港", false, 0, null),
    TC("tc", TrailerOrderTemplate.class, "拖车", false, 0, null),
    NL("nl", InlandTPTemplate.class, "内陆", false, 0, null),
    HY("hy", SeaOrderTemplate.class, "海运", false, 0, null),

    //导出应收模板(标准)
    KY_NORM_RE("ky-norm-re", AirOrderTemplate.class, "空运", false, 0, null),
    ZGYS_NORM_RE("zgys-norm-re", TmsOrderTemplate.class, "中港", true, 0, null),
    TC_NORM_RE("tc-norm-re", TrailerOrderTemplate.class, "拖车", true, 0, null),
    NL_NORM_RE("nl-norm-re", InlandTPTemplate.class, "内陆", true, 0, null),
    HY_NORM_RE("hy-norm-re", SeaOrderTemplate.class, "海运", false, 0, null),

    //导出应付模板(标准)
//    KY_NORM_PAY("ky-norm-pay", AirOrderTemplate.class, "空运", true, 1),
    ZGYS_NORM_PAY("zgys-norm-pay", TmsOrderPayTemplate.class, "中港", true, 1, PayBillTemplateServiceImpl.class),
    TC_NORM_PAY("tc-norm-pay", TrailerOrderPayTemplate.class, "拖车", true, 1, PayBillTemplateServiceImpl.class),
    NL_NORM_PAY("nl-norm-pay", InlandTPPayTemplate.class, "内陆", true, 1, PayBillTemplateServiceImpl.class),
    ZGYS_SUPPLIER_PAY("zgys-supplier-pay", TmsOrderPayTemplate.class, "中港供应商", true, 1, PaySupplierBillTemplateServiceImpl.class),
    TC_SUPPLIER_PAY("tc-supplier-pay", TrailerOrderPayTemplate.class, "拖车供应商", true, 1, PaySupplierBillTemplateServiceImpl.class),
    NL_SUPPLIER_PAY("nl-supplier-pay", InlandTPPayTemplate.class, "内陆供应商", true, 1, PaySupplierBillTemplateServiceImpl.class),
//    HY_NORM_PAY("hy-norm-pay", SeaOrderTemplate.class, "海运", false, 1),


//    NL("nl", AirOrderTemplate.class, "内陆", true),
//    ZGYS_ONE("zgys-one", TmsOrderTemplate.class, "中港-安克", true)
    ;

    private String cmd;
    private Class clazz;
    private String desc;
    private Boolean show;
    // 0:应收 1:应付
    private Integer type;
    private Class<? extends BillTemplateService> clz;

    public static Class getTemplate(String cmd) {
        for (BillTemplateEnum values : BillTemplateEnum.values()) {
            if (values.cmd.equals(cmd)) {
                return values.clazz;
            }
        }
        return null;
    }

    public static String getDesc(String cmd) {
        for (BillTemplateEnum values : BillTemplateEnum.values()) {
            if (values.cmd.contains(cmd)) {
                return values.desc;
            }
        }
        return null;
    }


    public static BillTemplateEnum getTemplateEnum(String cmd) {
        for (BillTemplateEnum values : BillTemplateEnum.values()) {
            if (values.cmd.equals(cmd)) {
                return values;
            }
        }
        return null;
    }

    public static List<InitComboxStrVO> initBillTemplate(Integer type) {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (BillTemplateEnum billTemplateEnum : BillTemplateEnum.values()) {
            if (!billTemplateEnum.show || !billTemplateEnum.getType().equals(type)) continue;
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(billTemplateEnum.getDesc());
            initComboxStrVO.setCode(billTemplateEnum.getCmd());
            list.add(initComboxStrVO);
        }
        return list;
    }

}
