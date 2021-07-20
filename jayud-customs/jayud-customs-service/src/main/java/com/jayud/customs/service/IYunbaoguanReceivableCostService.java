package com.jayud.customs.service;

import com.jayud.customs.model.po.YunbaoguanReceivableCost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.customs.model.vo.YunbaoguanReceivableCostVO;

import java.util.List;

/**
 * <p>
 * 云报关应收费用信息 服务类
 * </p>
 *
 * @author Daniel
 * @since 2021-05-19
 */
public interface IYunbaoguanReceivableCostService extends IService<YunbaoguanReceivableCost> {


    List<YunbaoguanReceivableCost> getByCondition(YunbaoguanReceivableCost receivableCost);

    List<YunbaoguanReceivableCostVO> getIncompleteData();
}
