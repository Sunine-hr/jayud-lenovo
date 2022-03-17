package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.OrderTrack;
import com.jayud.wms.mapper.OrderTrackMapper;
import com.jayud.wms.service.IOrderTrackService;
import com.jayud.common.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单轨迹 服务实现类
 *
 * @author jyd
 * @since 2021-12-18
 */
@Service
public class OrderTrackServiceImpl extends ServiceImpl<OrderTrackMapper, OrderTrack> implements IOrderTrackService {


    @Autowired
    private OrderTrackMapper orderTrackMapper;

    @Override
    public IPage<OrderTrack> selectPage(OrderTrack orderTrack,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<OrderTrack> page=new Page<OrderTrack>(pageNo, pageSize);
        IPage<OrderTrack> pageList= orderTrackMapper.pageList(page, orderTrack);
        return pageList;
    }

    @Override
    public List<OrderTrack> selectList(OrderTrack orderTrack){
        return orderTrackMapper.list(orderTrack);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderTrack saveOrUpdateOrderTrack(OrderTrack orderTrack) {
        Long id = orderTrack.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
            orderTrack.setCreateBy(CurrentUserUtil.getUsername());
            orderTrack.setCreateTime(new Date());

            QueryWrapper<OrderTrack> orderTrackQueryWrapper = new QueryWrapper<>();
//            orderTrackQueryWrapper.lambda().eq(OrderTrack::getCode, orderTrack.getCode());
            orderTrackQueryWrapper.lambda().eq(OrderTrack::getIsDeleted, 0);
            List<OrderTrack> list = this.list(orderTrackQueryWrapper);
            if(CollUtil.isNotEmpty(list)){
                throw new IllegalArgumentException("编号已存在，操作失败");
            }

        }else{
            //修改 --> update 更新人、更新时间
            orderTrack.setUpdateBy(CurrentUserUtil.getUsername());
            orderTrack.setUpdateTime(new Date());

            QueryWrapper<OrderTrack> orderTrackQueryWrapper = new QueryWrapper<>();
            orderTrackQueryWrapper.lambda().ne(OrderTrack::getId, id);
//            orderTrackQueryWrapper.lambda().eq(OrderTrack::getCode, orderTrack.getCode());
            orderTrackQueryWrapper.lambda().eq(OrderTrack::getIsDeleted, 0);
            List<OrderTrack> list = this.list(orderTrackQueryWrapper);
            if(CollUtil.isNotEmpty(list)){
                throw new IllegalArgumentException("编号已存在，操作失败");
            }
        }
        this.saveOrUpdate(orderTrack);
        return orderTrack;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delOrderTrack(int id) {
        OrderTrack orderTrack = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(orderTrack)){
            throw new IllegalArgumentException("订单轨迹不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        orderTrack.setUpdateBy(CurrentUserUtil.getUsername());
        orderTrack.setUpdateTime(new Date());
        orderTrack.setIsDeleted(true);
        this.saveOrUpdate(orderTrack);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryOrderTrackForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryOrderTrackForExcel(paramMap);
    }

}
