package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.CustomerRelaLegalMapper;
import com.jayud.oms.model.bo.AddCustomerInfoForm;
import com.jayud.oms.model.po.CustomerRelaLegal;
import com.jayud.oms.model.vo.LegalEntityVO;
import com.jayud.oms.service.ICustomerRelaLegalService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-12-03
 */
@Service
public class CustomerRelaLegalServiceImpl extends ServiceImpl<CustomerRelaLegalMapper, CustomerRelaLegal> implements ICustomerRelaLegalService {

    @Override
    public Boolean saveCusRelLegal(AddCustomerInfoForm form) {
        //保存关系时先清空现有的
        QueryWrapper removeWrapper = new QueryWrapper();
        removeWrapper.eq("customer_info_id",form.getId());
        remove(removeWrapper);

        List<CustomerRelaLegal> customerRelaLegals = new ArrayList<>();
        for (Long legalEntityId : form.getLegalEntityIds()) {
            CustomerRelaLegal customerRelaLegal = new CustomerRelaLegal();
            customerRelaLegal.setCustomerInfoId(form.getId());
            customerRelaLegal.setLegalEntityId(legalEntityId);
            customerRelaLegal.setCreatedUser(form.getLoginUserName());
            customerRelaLegals.add(customerRelaLegal);
        }
        return saveBatch(customerRelaLegals);
    }

    @Override
    public List<LegalEntityVO> findLegalByCustomerId(Long id) {
        return baseMapper.findLegalByCustomerId(id);
    }
}
