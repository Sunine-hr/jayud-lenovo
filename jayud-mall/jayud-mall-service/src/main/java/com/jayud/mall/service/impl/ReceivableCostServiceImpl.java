package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.ReceivableCostMapper;
import com.jayud.mall.model.bo.ReceivableCostForm;
import com.jayud.mall.model.po.ReceivableCost;
import com.jayud.mall.model.vo.ReceivableCostReturnVO;
import com.jayud.mall.service.IReceivableCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 应收/应付费用名称 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class ReceivableCostServiceImpl extends ServiceImpl<ReceivableCostMapper, ReceivableCost> implements IReceivableCostService {

    @Autowired
    ReceivableCostMapper receivableCostMapper;

    @Override
    public List<ReceivableCost> findReceivableCost(ReceivableCostForm form) {
        QueryWrapper<ReceivableCost> queryWrapper = new QueryWrapper<>();
        String idCode = form.getIdCode();
        if(idCode != null && idCode != ""){
            queryWrapper.eq("id_code", idCode);
        }
        String costName = form.getCostName();
        if(costName != null && costName != ""){
            queryWrapper.like("cost_name", costName);
        }
        Integer identifying = form.getIdentifying();
        if(identifying != null){
            queryWrapper.eq("identifying", identifying);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<ReceivableCost> list = receivableCostMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<ReceivableCostReturnVO> findReceivableCostBy(ReceivableCostForm form) {
        List<ReceivableCostReturnVO> receivableCostReturnVOS = receivableCostMapper.findReceivableCostBy(form);
        return receivableCostReturnVOS;
    }
}
