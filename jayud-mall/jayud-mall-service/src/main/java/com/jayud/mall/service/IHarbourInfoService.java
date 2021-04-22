package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.HarbourInfoForm;
import com.jayud.mall.model.po.HarbourInfo;
import com.jayud.mall.model.vo.HarbourInfoVO;

import java.util.List;

/**
 * <p>
 * 机场、港口信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface IHarbourInfoService extends IService<HarbourInfo> {

    /**
     * 查询机场港口信息
     * @param form
     * @return
     */
    List<HarbourInfoVO> findHarbourInfo(HarbourInfoForm form);
}
