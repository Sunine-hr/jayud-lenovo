package com.jayud.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.AddCustomerInfoForm;
import com.jayud.oms.model.po.CustomerRelaLegal;
import com.jayud.oms.model.vo.LegalEntityVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-12-03
 */
public interface ICustomerRelaLegalService extends IService<CustomerRelaLegal> {


    /**
     * 保存法人主体和客户的关联关系
     * @param form
     * @return
     */
    Boolean saveCusRelLegal(AddCustomerInfoForm form);

    /**
     * 根据客户ID获取相关联的法人主体
     * @param id
     * @return
     */
    List<LegalEntityVO> findLegalByCustomerId(Long id);
}
