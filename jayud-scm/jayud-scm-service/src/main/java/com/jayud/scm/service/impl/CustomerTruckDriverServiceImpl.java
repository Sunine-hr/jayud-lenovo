package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerTruckDriverForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.CustomerTruckDriver;
import com.jayud.scm.mapper.CustomerTruckDriverMapper;
import com.jayud.scm.model.po.CustomerTruckPlace;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CustomerTruckDriverVO;
import com.jayud.scm.model.vo.CustomerTruckPlaceVO;
import com.jayud.scm.service.ICustomerTruckDriverService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 运输公司车牌司机信息 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@Service
public class CustomerTruckDriverServiceImpl extends ServiceImpl<CustomerTruckDriverMapper, CustomerTruckDriver> implements ICustomerTruckDriverService {

    @Autowired
    private ISystemUserService systemUserService;

    @Override
    public List<CustomerTruckDriverVO> getTruckDriverByTruckId(Integer id) {
        QueryWrapper<CustomerTruckDriver> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerTruckDriver::getTruckId,id);
        queryWrapper.lambda().eq(CustomerTruckDriver::getVoided,0);
        List<CustomerTruckDriver> list = this.list(queryWrapper);
        List<CustomerTruckDriverVO> customerTruckDriverVOS = ConvertUtil.convertList(list, CustomerTruckDriverVO.class);
        for (CustomerTruckDriverVO customerTruckDriverVO : customerTruckDriverVOS) {
            customerTruckDriverVO.setValue(customerTruckDriverVO.getDriverName());
        }
        return customerTruckDriverVOS;
    }

    @Override
    public CustomerTruckDriver getTruckDriverByTruckIdAndDriver(Integer id, String driverName, String driverTel) {
        QueryWrapper<CustomerTruckDriver> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CustomerTruckDriver::getTruckId,id);
        queryWrapper.lambda().eq(CustomerTruckDriver::getDriverName,driverName);
        queryWrapper.lambda().eq(CustomerTruckDriver::getDriverTel,driverTel);
        return this.getOne(queryWrapper);
    }

    @Override
    public IPage<CustomerTruckDriverVO> findByPage(QueryForm form) {
        Page<CustomerTruckDriverVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerTruckDriver(AddCustomerTruckDriverForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        CustomerTruckDriver customerTruckDriver = ConvertUtil.convert(form, CustomerTruckDriver.class);
        if(form.getId() != null){
            customerTruckDriver.setMdyBy(systemUser.getId().intValue());
            customerTruckDriver.setMdyByDtm(LocalDateTime.now());
            customerTruckDriver.setMdyByName(systemUser.getUserName());
        }else{
            customerTruckDriver.setCrtBy(systemUser.getId().intValue());
            customerTruckDriver.setCrtByDtm(LocalDateTime.now());
            customerTruckDriver.setCrtByName(systemUser.getUserName());
        }
        boolean update = this.saveOrUpdate(customerTruckDriver);
        return update;
    }

    @Override
    public CustomerTruckDriverVO getCustomerTruckDriverById(Integer id) {
        return ConvertUtil.convert(this.getById(id), CustomerTruckDriverVO.class);
    }


}
