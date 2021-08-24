package com.jayud.scm.service;

import com.jayud.scm.model.bo.AddHgTruckForm;
import com.jayud.scm.model.po.HgTruck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.HgTruckVO;

/**
 * <p>
 * 港车运输主表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
public interface IHgTruckService extends IService<HgTruck> {

    HgTruckVO getHgTruckById(Integer id);

    boolean saveOrUpdateHgTruck(AddHgTruckForm form);
}
