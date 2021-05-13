package com.jayud.finance.enums;

import com.jayud.finance.vo.InitComboxStrVO;
import com.jayud.finance.vo.template.order.AirOrderTemplate;
import com.jayud.finance.vo.template.order.TmsOrderTemplate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public enum BillTemplateEnum {

    KY("ky", AirOrderTemplate.class, "空运", true),
    ZGYS("zgys", TmsOrderTemplate.class, "中港", true),
    NL("nl", AirOrderTemplate.class, "内陆", true),
    ZGYS_ONE("zgys-one", TmsOrderTemplate.class, "中港-安克", true);

    private String cmd;
    private Class clazz;
    private String desc;
    private Boolean show;

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

    public static List<InitComboxStrVO> initBillTemplate() {
        List<InitComboxStrVO> list = new ArrayList<>();
        for (BillTemplateEnum billTemplateEnum : BillTemplateEnum.values()) {
            if (!billTemplateEnum.show) continue;
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setName(billTemplateEnum.getDesc());
            initComboxStrVO.setCode(billTemplateEnum.getCmd());
            list.add(initComboxStrVO);
        }
        return list;
    }

}
