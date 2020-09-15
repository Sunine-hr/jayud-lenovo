package com.jayud.model.bo;

import com.jayud.model.vo.FindOrderInfoVO;
import lombok.Data;

/**
 * 委托单查询入参
 *
 * @author william
 * @description
 * @Date: 2020-09-08 11:54
 */
@Data
public class FindOrderInfoWrapperForm extends FindOrderInfoForm {
    private Integer page;
    private Integer rows;
}
