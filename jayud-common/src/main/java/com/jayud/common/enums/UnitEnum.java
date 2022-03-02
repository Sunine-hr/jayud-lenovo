package com.jayud.common.enums;

import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 业务类型(空运,中港运输,纯报关,...)
 */
@Getter
@AllArgsConstructor
public enum UnitEnum {

    KGS("KGS", "KGS"), CBM("CBM", "CBM"), CTNS("CTNS", "CTNS"), PCS("PCS", "PCS"), BILL("BILL", "BILL"), Pallet("Pallet", "Pallet");
    private String code;
    private String desc;

    public static String getDesc(String code) {
        for (UnitEnum value : values()) {
            if (Objects.equals(code, value.getCode())) {
                return value.getDesc();
            }
        }
        return null;
    }

    public static String getCode(String desc) {
        for (UnitEnum value : values()) {
            if (Objects.equals(desc, value.getDesc())) {
                return value.getCode();
            }
        }
        return null;
    }

    public static List<InitComboxStrVO> initCostUnit() {
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (UnitEnum unitEnum : UnitEnum.values()) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setCode(unitEnum.getCode());
            comboxStrVO.setName(unitEnum.getDesc());
            comboxStrVOS.add(comboxStrVO);
        }
        return comboxStrVOS;
    }

}
