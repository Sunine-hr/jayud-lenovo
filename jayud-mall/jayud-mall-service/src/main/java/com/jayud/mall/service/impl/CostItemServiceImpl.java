package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.model.bo.CostItemForm;
import com.jayud.mall.model.bo.CostItemSupForm;
import com.jayud.mall.model.po.CostItem;
import com.jayud.mall.mapper.CostItemMapper;
import com.jayud.mall.model.vo.CostItemVO;
import com.jayud.mall.service.ICostItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 费用项目 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-28
 */
@Service
public class CostItemServiceImpl extends ServiceImpl<CostItemMapper, CostItem> implements ICostItemService {

    @Autowired
    CostItemMapper costItemMapper;

    @Override
    public List<CostItemVO> findCostItem(CostItemForm form) {
        QueryWrapper<CostItem> queryWrapper = new QueryWrapper<>();
        Long id = form.getId();
        if(id != null){
            queryWrapper.eq("id", id);
        }
        String identifying = form.getIdentifying();
        if(identifying != null && identifying != ""){
            queryWrapper.eq("identifying", identifying);
        }
        String costCode = form.getCostCode();
        if(costCode != null && costCode != ""){
            queryWrapper.like("cost_code", costCode);
        }
        String costName = form.getCostName();
        if(costName != null && costName != ""){
            queryWrapper.like("cost_name", costName);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<String> notCostCodes = form.getNotCostCodes();
        if(notCostCodes != null && notCostCodes.size()>0){
            queryWrapper.notIn("cost_code", notCostCodes);
        }
        List<CostItem> costItems = costItemMapper.selectList(queryWrapper);
        List<CostItemVO> costItemVOS = ConvertUtil.convertList(costItems, CostItemVO.class);
        return costItemVOS;
    }

    @Override
    public List<CostItemVO> findCostItemBySupId(CostItemSupForm form) {
        List<CostItemVO> list = costItemMapper.findCostItemBySupId(form);
        return list;
    }

}
