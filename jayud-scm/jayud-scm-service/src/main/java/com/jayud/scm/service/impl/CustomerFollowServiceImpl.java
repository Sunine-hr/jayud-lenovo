package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.enums.OperationEnum;
import com.jayud.scm.model.po.CommodityFollow;
import com.jayud.scm.model.po.Customer;
import com.jayud.scm.model.po.CustomerFollow;
import com.jayud.scm.mapper.CustomerFollowMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.CustomerFollowVO;
import com.jayud.scm.service.ICustomerFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ICustomerService;
import com.jayud.scm.service.ISystemUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 客户跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Service
public class CustomerFollowServiceImpl extends ServiceImpl<CustomerFollowMapper, CustomerFollow> implements ICustomerFollowService {

    @Override
    public IPage<CustomerFollowVO> findListByCustomerId(QueryCommonForm form) {
        Page<CustomerFollowVO> page = new Page<>(form.getPageNum(),form.getPageSize() );
        return this.baseMapper.findListByCustomerId(form,page);
    }
}
