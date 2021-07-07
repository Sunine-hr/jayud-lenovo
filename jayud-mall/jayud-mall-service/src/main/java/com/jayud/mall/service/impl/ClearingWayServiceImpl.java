package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.ClearingWay;
import com.jayud.mall.mapper.ClearingWayMapper;
import com.jayud.mall.model.vo.ClearingWayVO;
import com.jayud.mall.service.IClearingWayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 基础表-结算方式 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-26
 */
@Service
public class ClearingWayServiceImpl extends ServiceImpl<ClearingWayMapper, ClearingWay> implements IClearingWayService {


    @Autowired
    ClearingWayMapper clearingWayMapper;

    @Override
    public List<ClearingWayVO> findClearingWay() {
        return clearingWayMapper.findClearingWay();
    }
}
