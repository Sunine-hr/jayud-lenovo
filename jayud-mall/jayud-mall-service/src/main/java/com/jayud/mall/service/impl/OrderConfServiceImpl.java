package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OrderConfMapper;
import com.jayud.mall.model.bo.OrderConfForm;
import com.jayud.mall.model.bo.QueryOrderConfForm;
import com.jayud.mall.model.po.OceanConfDetail;
import com.jayud.mall.model.po.OrderConf;
import com.jayud.mall.model.vo.OrderConfVO;
import com.jayud.mall.service.IOceanConfDetailService;
import com.jayud.mall.service.IOrderConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 配载单 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class OrderConfServiceImpl extends ServiceImpl<OrderConfMapper, OrderConf> implements IOrderConfService {

    @Autowired
    OrderConfMapper orderConfMapper;

    @Autowired
    IOceanConfDetailService oceanConfDetailService;

    @Override
    public IPage<OrderConfVO> findOrderConfByPage(QueryOrderConfForm form) {
        //定义分页参数
        Page<OrderConfVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OrderConfVO> pageInfo = orderConfMapper.findOrderConfByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderConf(OrderConfForm form) {
        OrderConf orderConf = ConvertUtil.convert(form, OrderConf.class);
        this.saveOrUpdate(orderConf);

        //配载单id
        Long orderId = orderConf.getId();

        //先删除
        QueryWrapper<OceanConfDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        oceanConfDetailService.remove(queryWrapper);

        List<OceanConfDetail> oceanConfDetailList = form.getOceanConfDetailList();
        oceanConfDetailList.forEach(oceanConfDetail -> {
            oceanConfDetail.setOrderId(orderId);
        });
        //再保存
        oceanConfDetailService.saveOrUpdateBatch(oceanConfDetailList);
    }

    @Override
    public CommonResult<OrderConfVO> lookOrderConf(Long id) {
        return null;
    }


}
