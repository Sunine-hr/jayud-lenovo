package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.CustomerTruckPlace;
import com.jayud.scm.mapper.CustomerTruckPlaceMapper;
import com.jayud.scm.model.vo.CustomerGuaranteeVO;
import com.jayud.scm.model.vo.CustomerTruckPlaceVO;
import com.jayud.scm.service.ICustomerTruckPlaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运输公司车牌信息 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@Service
public class CustomerTruckPlaceServiceImpl extends ServiceImpl<CustomerTruckPlaceMapper, CustomerTruckPlace> implements ICustomerTruckPlaceService {

    @Override
    public IPage<CustomerTruckPlaceVO> findByPage(QueryForm form) {
        Page<CustomerTruckPlaceVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }
}
