package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.WaybillTaskRelevanceMapper;
import com.jayud.mall.model.bo.WaybillTaskRelevanceForm;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.po.WaybillTaskRelevance;
import com.jayud.mall.model.vo.WaybillTaskRelevanceVO;
import com.jayud.mall.model.vo.WaybillTaskVO;
import com.jayud.mall.service.IWaybillTaskRelevanceService;
import com.jayud.mall.service.IWaybillTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        //根据`报价id(服务id)`,查询运单任务列表
        //List<WaybillTaskVO> list = waybillTaskService.findWaybillTaskByOfferInfoId(offerInfoId);
        //根据`订单id(order_info id)`,查询运单任务列表 orderInfoId
        List<WaybillTaskVO> list = waybillTaskService.findWaybillTaskByOrderInfoId(orderInfoId);

        List<WaybillTaskRelevanceForm> waybillTaskRelevanceForms = new ArrayList<>();
        list.forEach(waybillTaskVO -> {
            WaybillTaskRelevanceForm form = new WaybillTaskRelevanceForm();
            form.setOrderInfoId(orderInfoId);
            form.setTaskCode(waybillTaskVO.getTaskCode());
            form.setTaskName(waybillTaskVO.getTaskName());
            form.setSort(waybillTaskVO.getSort());
            form.setDays(waybillTaskVO.getDays());
            form.setDayFlag(waybillTaskVO.getDayFlag());
            form.setOperators(waybillTaskVO.getMemberUserName());
            form.setMinutes(waybillTaskVO.getMinutes());
            form.setScore(waybillTaskVO.getScore());
            form.setRemarks(waybillTaskVO.getRemarks());
            form.setStatus("1");
            form.setReason(null);
            form.setUpTime(null);
            form.setUserId(waybillTaskVO.getMemberUserId().intValue());
            form.setUserName(waybillTaskVO.getMemberUserName());
            form.setCreateTime(LocalDateTime.now());
            waybillTaskRelevanceForms.add(form);
        });

        //先删除
        QueryWrapper<WaybillTaskRelevance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_info_id", orderInfoId);
        this.remove(queryWrapper);

        //再保存
        List<WaybillTaskRelevance> waybillTaskRelevances =
                ConvertUtil.convertList(waybillTaskRelevanceForms, WaybillTaskRelevance.class);
        this.saveOrUpdateBatch(waybillTaskRelevances);

        List<WaybillTaskRelevanceVO> waybillTaskRelevanceVOS =
                ConvertUtil.convertList(waybillTaskRelevances, WaybillTaskRelevanceVO.class);

        return waybillTaskRelevanceVOS;
    }

}
