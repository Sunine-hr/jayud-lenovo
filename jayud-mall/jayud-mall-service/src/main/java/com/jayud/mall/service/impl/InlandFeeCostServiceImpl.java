package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.InlandFeeCostMapper;
import com.jayud.mall.model.bo.InlandFeeCostForm;
import com.jayud.mall.model.bo.QueryInlandFeeCostForm;
import com.jayud.mall.model.po.InlandFeeCost;
import com.jayud.mall.model.vo.InlandFeeCostVO;
import com.jayud.mall.service.IInlandFeeCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 内陆费费用表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-08
 */
@Service
public class InlandFeeCostServiceImpl extends ServiceImpl<InlandFeeCostMapper, InlandFeeCost> implements IInlandFeeCostService {

    @Autowired
    InlandFeeCostMapper inlandFeeCostMapper;

    @Override
    public IPage<InlandFeeCostVO> findInlandFeeCostByPage(QueryInlandFeeCostForm form) {
        //定义分页参数
        Page<InlandFeeCostVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.id"));
        IPage<InlandFeeCostVO> pageInfo = inlandFeeCostMapper.findInlandFeeCostByPage(page, form);
        return pageInfo;
    }

    @Override
    public CommonResult saveInlandFeeCost(InlandFeeCostForm form) {
        InlandFeeCost inlandFeeCost = ConvertUtil.convert(form, InlandFeeCost.class);
        this.saveOrUpdate(inlandFeeCost);
        return CommonResult.success("保存成功");
    }

    @Override
    public InlandFeeCostVO findInlandFeeCostById(Long id) {
        InlandFeeCostVO inlandFeeCostVO = inlandFeeCostMapper.findInlandFeeCostById(id);
        if(ObjectUtil.isEmpty(inlandFeeCostVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有找到内陆费费用");
        }
        return inlandFeeCostVO;
    }

}
