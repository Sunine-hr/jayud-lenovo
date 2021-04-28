package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.po.BillClearanceInfo;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;

import java.util.List;

/**
 * <p>
 * (提单)清关信息表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
public interface IBillClearanceInfoService extends IService<BillClearanceInfo> {

    /**
     * 根据清关信息id，查询提单对应清关箱号信息
     * @param b_id
     * @return
     */
    List<ClearanceInfoCaseVO> findClearanceInfoCase(Long b_id);
}
