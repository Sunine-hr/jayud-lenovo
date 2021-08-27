package com.jayud.scm.service;

import com.jayud.scm.model.po.CustomerTruckDriver;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerTruckDriverVO;

import java.util.List;

/**
 * <p>
 * 运输公司车牌司机信息 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
public interface ICustomerTruckDriverService extends IService<CustomerTruckDriver> {

    List<CustomerTruckDriverVO> getTruckDriverByTruckId(Integer id);
}
