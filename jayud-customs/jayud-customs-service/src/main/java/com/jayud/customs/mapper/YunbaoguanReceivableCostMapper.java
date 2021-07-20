package com.jayud.customs.mapper;

import com.jayud.customs.model.po.YunbaoguanReceivableCost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.customs.model.vo.YunbaoguanReceivableCostVO;

import java.util.List;

/**
 * <p>
 * 云报关应收费用信息 Mapper 接口
 * </p>
 *
 * @author Daniel
 * @since 2021-05-19
 */
public interface YunbaoguanReceivableCostMapper extends BaseMapper<YunbaoguanReceivableCost> {


    List<YunbaoguanReceivableCostVO> getIncompleteData();
}
