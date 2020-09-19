package com.jayud.finance.bo;

import com.jayud.finance.po.CustomsReceivable;
import lombok.Data;

import java.util.List;

/**
 * @author william
 * @description
 * @Date: 2020-09-19 14:27
 */
@Data
public class CustomsReceivableForm {
    private List<CustomsReceivable> receivables;
}
