package com.jayud.finance.bo;

import com.jayud.finance.po.CustomsPayable;
import lombok.Data;

import java.util.List;

/**
 * @author william
 * @description
 * @Date: 2020-09-19 14:25
 */
@Data
public class CustomsPayableForm {
    private List<CustomsPayable> payable;
}
