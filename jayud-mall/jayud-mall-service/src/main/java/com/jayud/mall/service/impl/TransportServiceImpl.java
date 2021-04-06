package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.TransportMapper;
import com.jayud.mall.model.bo.QueryTransportForm;
import com.jayud.mall.model.po.Transport;
import com.jayud.mall.model.vo.TransportVO;
import com.jayud.mall.service.ITransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运输管理表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-06
 */
@Service
public class TransportServiceImpl extends ServiceImpl<TransportMapper, Transport> implements ITransportService {

    @Autowired
    TransportMapper transportMapper;

    @Override
    public IPage<TransportVO> findTransportByPage(QueryTransportForm form) {
        //定义分页参数
        Page<TransportVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<TransportVO> pageInfo = transportMapper.findTransportByPage(page, form);
        return pageInfo;
    }
}
