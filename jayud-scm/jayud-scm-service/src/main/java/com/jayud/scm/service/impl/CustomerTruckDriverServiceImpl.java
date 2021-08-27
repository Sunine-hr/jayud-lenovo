package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.model.po.CustomerTruckDriver;
import com.jayud.scm.mapper.CustomerTruckDriverMapper;
import com.jayud.scm.model.vo.CustomerTruckDriverVO;
import com.jayud.scm.service.ICustomerTruckDriverService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
