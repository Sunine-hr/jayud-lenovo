package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.WaybillTaskRelevanceMapper;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.po.WaybillTaskRelevance;
import com.jayud.mall.model.vo.WaybillTaskRelevanceVO;
import com.jayud.mall.model.vo.WaybillTaskVO;
import com.jayud.mall.service.IWaybillTaskRelevanceService;
import com.jayud.mall.service.IWaybillTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 运单(订单）任务关联 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Service
public class WaybillTaskRelevanceServiceImpl extends ServiceImpl<WaybillTaskRelevanceMapper, WaybillTaskRelevance> implements IWaybillTaskRelevanceService {

    @Autowired
    WaybillTaskRelevanceMapper waybillTaskRelevanceMapper;

    @Autowired
    IWaybillTaskService waybillTaskService;

    @Override
    public List<WaybillTaskRelevanceVO> saveWaybillTaskRelevance(OrderInfo orderInfo) {
        Long orderInfoId = orderInfo.getId();//订单Id
        Integer offerInfoId = orderInfo.getOfferInfoId();//报价id(服务id)

        //废弃
        //根据`报价id(服务id)`,查询运单任务列表
        //List<WaybillTaskVO> list = waybillTaskService.findWaybillTaskByOfferInfoId(offerInfoId);
        //根据`订单id(order_info id)`,查询运单任务列表 orderInfoId
        //List<WaybillTaskVO> list = waybillTaskService.findWaybillTaskByOrderInfoId(orderInfoId);

        /*
        根据订单id，找运营组和任务
         梳理逻辑：
         1.找订单，订单找客户，客户找运营小组，运营小组找人员和任务
         2.找订单，订单找报价，报价找报价模板，报价模板找任务组，任务组找任务
         3.任务设置人员
         */
        //# 1.找订单，订单找客户，客户找运营小组，运营小组找人员和任务
        //# 2.找订单，订单找报价，报价找报价模板，报价模板找任务组，任务组找任务
        //# 第三步，两个数据的合并
        //# 同一个运营小组，人员设置任务，一个人可以同时有多个任务，但是一个任务不能有多个人
        List<WaybillTaskVO> list = waybillTaskRelevanceMapper.findWaybillTaskByOrderInfoId(orderInfoId);

        List<WaybillTaskRelevance> waybillTaskRelevances = ConvertUtil.convertList(list, WaybillTaskRelevance.class);
        waybillTaskRelevances.forEach(waybillTaskRelevance -> {
            waybillTaskRelevance.setOrderInfoId(orderInfoId);
            waybillTaskRelevance.setStatus("0");//状态(0未激活 1已激活 2异常 3已完成)
        });
        //先删除
        QueryWrapper<WaybillTaskRelevance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_info_id", orderInfoId);
        this.remove(queryWrapper);
        //再保存
        this.saveOrUpdateBatch(waybillTaskRelevances);
        List<WaybillTaskRelevanceVO> waybillTaskRelevanceVOS = ConvertUtil.convertList(waybillTaskRelevances, WaybillTaskRelevanceVO.class);

        return waybillTaskRelevanceVOS;
    }

}
