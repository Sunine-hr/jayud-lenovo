package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddElectronicFenceForm;
import com.jayud.oms.model.bo.QueryElectronicFenceForm;
import com.jayud.oms.model.po.ElectronicFence;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.ElectronicFenceVO;

/**
 * <p>
 * 电子围栏 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-10-25
 */
public interface IElectronicFenceService extends IService<ElectronicFence> {

    void saveOrUpdate(AddElectronicFenceForm form);

    IPage<ElectronicFenceVO> findByPage(QueryElectronicFenceForm form);
}
