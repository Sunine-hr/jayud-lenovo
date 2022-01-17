package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.bo.AddCustomerTruckPlaceForm;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.CustomerTruckDriver;
import com.jayud.scm.model.po.CustomerTruckPlace;
import com.jayud.scm.mapper.CustomerTruckPlaceMapper;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.CustomerGuaranteeVO;
import com.jayud.scm.model.vo.CustomerTruckDriverVO;
import com.jayud.scm.model.vo.CustomerTruckPlaceVO;
import com.jayud.scm.service.ICustomerService;
import com.jayud.scm.service.ICustomerTruckDriverService;
import com.jayud.scm.service.ICustomerTruckPlaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.scm.service.ISystemUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ICustomerTruckDriverService customerTruckDriverService;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ICustomerService customerService;

    @Override
    public IPage<CustomerTruckPlaceVO> findByPage(QueryForm form) {
        Page<CustomerTruckPlaceVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public boolean saveOrUpdateCustomerTruckPlace(AddCustomerTruckPlaceForm form) {
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());

        CustomerTruckPlace customerTruckPlace = ConvertUtil.convert(form, CustomerTruckPlace.class);
        if(form.getId() != null){
            customerTruckPlace.setMdyBy(systemUser.getId().intValue());
            customerTruckPlace.setMdyByDtm(LocalDateTime.now());
            customerTruckPlace.setMdyByName(systemUser.getUserName());
        }else{
            customerTruckPlace.setCrtBy(systemUser.getId().intValue());
            customerTruckPlace.setCrtByDtm(LocalDateTime.now());
            customerTruckPlace.setCrtByName(systemUser.getUserName());
        }
        boolean update = this.saveOrUpdate(customerTruckPlace);
        if(update){
            log.warn("新增或修改车牌成功");
            if(customerTruckPlace.getDriverName() != null){
                CustomerTruckDriver customerTruckDriver = this.customerTruckDriverService.getTruckDriverByTruckIdAndDriver(customerTruckPlace.getId(),customerTruckPlace.getDriverName(),customerTruckPlace.getDriverTel());
                if(customerTruckDriver != null){
                    customerTruckDriver.setMdyBy(systemUser.getId().intValue());
                    customerTruckDriver.setMdyByDtm(LocalDateTime.now());
                    customerTruckDriver.setMdyByName(systemUser.getUserName());
                }else{
                    customerTruckDriver = new CustomerTruckDriver();
                    customerTruckDriver.setCrtBy(systemUser.getId().intValue());
                    customerTruckDriver.setCrtByDtm(LocalDateTime.now());
                    customerTruckDriver.setCrtByName(systemUser.getUserName());
                }
                customerTruckDriver.setTruckId(customerTruckPlace.getId());
                customerTruckDriver.setCustomerId(customerTruckPlace.getCustomerId());
                customerTruckDriver.setDriverName(customerTruckPlace.getDriverName());
                customerTruckDriver.setDriverTel(customerTruckPlace.getDriverTel());
                customerTruckDriver.setIdCode(customerTruckPlace.getIdCode());
                boolean update1 = this.customerTruckDriverService.saveOrUpdate(customerTruckDriver);
                if(update1){
                    log.warn("新增或修改司机成功");
                }
            }

        }
        return update;
    }

    @Override
    public CustomerTruckPlaceVO getCustomerTruckPlaceById(Integer id) {
        CustomerTruckPlaceVO customerTruckPlaceVO = ConvertUtil.convert(this.getById(id), CustomerTruckPlaceVO.class);
        customerTruckPlaceVO.setCustomerName(customerService.getCustomerById(customerTruckPlaceVO.getId()).getCustomerName());
        return customerTruckPlaceVO;
    }

    @Override
    public boolean delete(DeleteForm deleteForm) {
        List<CustomerTruckDriver> customerTruckDrivers1 = new ArrayList<>();

        for (Long id : deleteForm.getIds()) {
            List<CustomerTruckDriverVO> truckDriverByTruckId = customerTruckDriverService.getTruckDriverByTruckId(id.intValue());
            List<CustomerTruckDriver> customerTruckDrivers = ConvertUtil.convertList(truckDriverByTruckId, CustomerTruckDriver.class);
            for (CustomerTruckDriver customerTruckDriver : customerTruckDrivers) {
                customerTruckDriver.setVoided(1);
                customerTruckDriver.setVoidedBy(deleteForm.getId().intValue());
                customerTruckDriver.setVoidedByDtm(deleteForm.getDeleteTime());
                customerTruckDriver.setVoidedByName(deleteForm.getName());
                customerTruckDrivers1.add(customerTruckDriver);
            }
        }
        boolean b = this.customerTruckDriverService.updateBatchById(customerTruckDrivers1);
        log.warn("删除司机成功");
        return b;
    }
}
