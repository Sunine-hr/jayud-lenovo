package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.CustomerTruckPlace;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerTruckPlaceVO;

/**
 * <p>
 * 运输公司车牌信息 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
public interface ICustomerTruckPlaceService extends IService<CustomerTruckPlace> {

    IPage<CustomerTruckPlaceVO> findByPage(QueryForm form);
}
