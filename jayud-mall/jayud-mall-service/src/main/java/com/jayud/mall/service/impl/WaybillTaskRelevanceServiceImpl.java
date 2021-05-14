package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.DayFlagEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.mapper.WaybillTaskRelevanceMapper;
import com.jayud.mall.model.bo.WaybillTaskRelevanceQueryForm;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.po.WaybillTaskRelevance;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.model.vo.WaybillTaskRelevanceVO;
import com.jayud.mall.model.vo.WaybillTaskVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IWaybillTaskRelevanceService;
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
    OrderInfoMapper orderInfoMapper;
    @Autowired
    BaseService baseService;

    @Override
    public List<WaybillTaskRelevanceVO> saveWaybillTaskRelevance(OrderInfo orderInfo) {
        Long orderInfoId = orderInfo.getId();//订单Id
        Integer offerInfoId = orderInfo.getOfferInfoId();//报价id(服务id)

        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfo(orderInfoId);
        LocalDateTime sailTime = orderInfoVO.getSailTime();//开船日期

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

        /**
         * 运单任务：
         * 1.报价，没有运营组，只有一个任务组
         * 2.客户，有多个运营组，不同的运营组负责不同的任务
         * 3.下单，客户的运营组人员，和任务做匹配，确认运单任务和运单任务的负责人。
         */
        List<WaybillTaskVO> list = waybillTaskRelevanceMapper.findWaybillTaskByOrderInfoId(orderInfoId);

        List<WaybillTaskRelevance> waybillTaskRelevances = new ArrayList<>();
        list.forEach(waybillTaskVO -> {
            WaybillTaskRelevance waybillTaskRelevance = ConvertUtil.convert(waybillTaskVO, WaybillTaskRelevance.class);
            waybillTaskRelevance.setOrderInfoId(orderInfoId);
            String activationSwitch = waybillTaskVO.getActivationSwitch();//激活开关(0未激活 1已激活)
            if(activationSwitch.equals("1")){
                waybillTaskRelevance.setStatus("1");//状态(0未激活 1已激活,未完成 2已完成)
            }else{
                waybillTaskRelevance.setStatus("0");//状态(0未激活 1已激活,未完成 2已完成)
            }
            String dayFlag = waybillTaskRelevance.getDayFlag();//天数标识
            Integer days = waybillTaskRelevance.getDays();//天数
            if(dayFlag.equals(DayFlagEnum.ETD.getCode())){
                //ETD("ETD", "开船日期")
                LocalDateTime taskLastTime = sailTime.plusDays(days);
                waybillTaskRelevance.setTaskLastTime(taskLastTime);//任务最后完成时间
            }
            waybillTaskRelevances.add(waybillTaskRelevance);
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

    @Override
    public List<WaybillTaskRelevanceVO> findWaybillTaskRelevance(WaybillTaskRelevanceQueryForm form) {

        String isPersonal = form.getIsPersonal();
        if(isPersonal.equals("1")){
            //是否个人任务(1 仅看自己的任务 2查看所有的任务)
            AuthUser user = baseService.getUser();
            Long userId = user.getId();
            form.setUserId(userId.intValue());
        }
        List<WaybillTaskRelevanceVO> list = waybillTaskRelevanceMapper.findWaybillTaskRelevance(form);
        return list;
    }

}
