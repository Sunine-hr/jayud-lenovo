package com.jayud.mall.service;

import com.jayud.mall.model.bo.CounterDocumentInfoForm;
import com.jayud.mall.model.po.CounterDocumentInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CounterDocumentInfoVO;

import java.util.List;

/**
 * <p>
 * (提单)柜子对应-文件信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-24
 */
public interface ICounterDocumentInfoService extends IService<CounterDocumentInfo> {

    /**
     * 添加-(提单)柜子对应文件信息
     * @param form
     */
    void addCounterDocumentInfo(CounterDocumentInfoForm form);

    /**
     * 删除-(提单)柜子对应文件信息
     * @param id
     */
    void delCounterDocumentInfo(Long id);

    /**
     * 查询-(提单)柜子对应文件信息
     * @param counterId
     * @return
     */
    List<CounterDocumentInfoVO> findCounterDocumentInfoByCounterId(Long counterId);
}
