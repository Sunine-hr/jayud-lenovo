package com.jayud.customs.service.impl;

import com.jayud.customs.model.po.YunbaoguanReceivableCost;
import com.jayud.customs.mapper.YunbaoguanReceivableCostMapper;
import com.jayud.customs.model.vo.YunbaoguanReceivableCostVO;
import com.jayud.customs.service.IYunbaoguanReceivableCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 云报关应收费用信息 服务实现类
 * </p>
 *
 * @author Daniel
 * @since 2021-05-19
 */
@Service
public class YunbaoguanReceivableCostServiceImpl extends ServiceImpl<YunbaoguanReceivableCostMapper, YunbaoguanReceivableCost> implements IYunbaoguanReceivableCostService {

    @Override
    public List<YunbaoguanReceivableCostVO> getIncompleteData() {
        return this.baseMapper.getIncompleteData();
    }
}
