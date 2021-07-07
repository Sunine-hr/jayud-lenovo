package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.DayFlagEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.mapper.TaskExecutionRuleMapper;
import com.jayud.mall.mapper.WaybillTaskRelevanceMapper;
import com.jayud.mall.model.bo.WaybillTaskRelevanceIdForm;
import com.jayud.mall.model.bo.WaybillTaskRelevanceQueryForm;
import com.jayud.mall.model.po.LogisticsTrack;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.po.WaybillTaskRelevance;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.model.vo.WaybillTaskRelevanceVO;
import com.jayud.mall.model.vo.WaybillTaskVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ILogisticsTrackService;
import com.jayud.mall.service.IWaybillTaskRelevanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    TaskExecutionRuleMapper taskExecutionRuleMapper;
    @Autowired
    BaseService baseService;
    @Autowired
    ILogisticsTrackService logisticsTrackService;

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

    @Override
    public WaybillTaskRelevanceVO findWaybillTaskRelevanceById(Long id) {
        return waybillTaskRelevanceMapper.findWaybillTaskRelevanceById(id);
    }

    /**
     * 完成运单任务
     * 1.保存运单任务
     * 2.保存订单物流轨迹表
     * 3.激活其他任务
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishTask(WaybillTaskRelevanceIdForm form) {
        Long id = form.getId();
        WaybillTaskRelevanceVO waybillTaskRelevanceVO = waybillTaskRelevanceMapper.findWaybillTaskRelevanceById(id);
        AuthUser user = baseService.getUser();
        Integer loginUserId = user.getId().intValue();
        Integer userId = waybillTaskRelevanceVO.getUserId();
        if(!loginUserId.equals(userId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "仅当前任务负责人，能完成任务");
        }
        if(ObjectUtil.isEmpty(waybillTaskRelevanceVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "任务不存在");
        }
        String status = waybillTaskRelevanceVO.getStatus();
        if(!status.equals("1")){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "任务状态错误");
        }

        WaybillTaskRelevance waybillTaskRelevance = ConvertUtil.convert(waybillTaskRelevanceVO, WaybillTaskRelevance.class);
        waybillTaskRelevance.setStatus("2");//状态(0未激活 1已激活,未完成 2已完成)
        waybillTaskRelevance.setUpTime(LocalDateTime.now());
        //1.保存运单任务
        this.saveOrUpdate(waybillTaskRelevance);
        //2.判断是否修改 订单 物流轨迹跟踪表 ,保存订单物流轨迹表
        String logisticsTrackDescription = form.getLogisticsTrackDescription();
        LocalDateTime logisticsTrackCreateTime = ObjectUtil.isEmpty(form.getLogisticsTrackCreateTime()) ? LocalDateTime.now() : form.getLogisticsTrackCreateTime();
        if(StrUtil.isNotEmpty(logisticsTrackDescription)){
            Long orderId = waybillTaskRelevance.getOrderInfoId();
            LogisticsTrack logisticsTrack = new LogisticsTrack();
            logisticsTrack.setOrderId(orderId.toString());
            logisticsTrack.setStatus(1);
            logisticsTrack.setStatusName("1");
            logisticsTrack.setDescription(logisticsTrackDescription);
            logisticsTrack.setCreateTime(logisticsTrackCreateTime);
            logisticsTrack.setOperatorId(waybillTaskRelevance.getUserId());
            logisticsTrack.setOperatorName(waybillTaskRelevance.getUserName());
            logisticsTrackService.saveOrUpdate(logisticsTrack);
        }
        //3.判断是否关联了 其他任务，并激活其他任务
        String fromTaskCode = waybillTaskRelevance.getTaskCode();

        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("order_info_id", waybillTaskRelevance.getOrderInfoId());
        paraMap.put("from_task_code", fromTaskCode);
        List<WaybillTaskRelevance> waybillTaskRelevances = waybillTaskRelevanceMapper.findWaybillTaskRelevanceByParaMap(paraMap);
        if(CollUtil.isNotEmpty(waybillTaskRelevances)){
            waybillTaskRelevances.forEach(waybillTaskRelevance1 -> {
                waybillTaskRelevance1.setStatus("1");//状态(0未激活 1已激活,未完成 2已完成)
            });
            this.saveOrUpdateBatch(waybillTaskRelevances);
        }

    }

    /**
     * 延期运单任务
     * 1.不改变任务的状态，但要改变 最后完成时间
     * 2.保存订单物流轨迹表
     * 3.需要激活 关联的其他任务
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postponeTask(WaybillTaskRelevanceIdForm form) {
        Long id = form.getId();
        WaybillTaskRelevanceVO waybillTaskRelevanceVO = waybillTaskRelevanceMapper.findWaybillTaskRelevanceById(id);
        AuthUser user = baseService.getUser();
        Integer loginUserId = user.getId().intValue();
        Integer userId = waybillTaskRelevanceVO.getUserId();
        LocalDateTime taskLastTime = waybillTaskRelevanceVO.getTaskLastTime();
        LocalDateTime now = LocalDateTime.now();
        if(now.compareTo(taskLastTime) < 0){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "最后完成时间不小于今天，不能延期");
        }
        if(!loginUserId.equals(userId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "仅当前任务负责人，能完成任务");
        }
        if(ObjectUtil.isEmpty(waybillTaskRelevanceVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "任务不存在");
        }
        String status = waybillTaskRelevanceVO.getStatus();
        if(!status.equals("1")){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "任务状态错误");
        }
        WaybillTaskRelevance waybillTaskRelevance = ConvertUtil.convert(waybillTaskRelevanceVO, WaybillTaskRelevance.class);
        waybillTaskRelevance.setUpTime(LocalDateTime.now());
        Integer postponeDays = form.getPostponeDays();
        postponeDays = ObjectUtil.isEmpty(postponeDays) ? 1 : postponeDays;
        LocalDateTime localDateTime = taskLastTime.plusDays(postponeDays);//最后完成时间，加上延期的时间
        waybillTaskRelevance.setTaskLastTime(localDateTime);
        //1.保存运单任务
        this.saveOrUpdate(waybillTaskRelevance);

        //2.判断是否修改 订单 物流轨迹跟踪表 ,保存订单物流轨迹表
        String logisticsTrackDescription = form.getLogisticsTrackDescription();
        LocalDateTime logisticsTrackCreateTime = ObjectUtil.isEmpty(form.getLogisticsTrackCreateTime()) ? LocalDateTime.now() : form.getLogisticsTrackCreateTime();
        if(StrUtil.isNotEmpty(logisticsTrackDescription)){
            Long orderId = waybillTaskRelevance.getOrderInfoId();
            LogisticsTrack logisticsTrack = new LogisticsTrack();
            logisticsTrack.setOrderId(orderId.toString());
            logisticsTrack.setStatus(1);
            logisticsTrack.setStatusName("1");
            logisticsTrack.setDescription(logisticsTrackDescription);
            logisticsTrack.setCreateTime(logisticsTrackCreateTime);
            logisticsTrack.setOperatorId(waybillTaskRelevance.getUserId());
            logisticsTrack.setOperatorName(waybillTaskRelevance.getUserName());
            logisticsTrackService.saveOrUpdate(logisticsTrack);
        }


        //3.判断是否关联了 其他任务，并激活其他任务
        String fromTaskCode = waybillTaskRelevance.getTaskCode();

        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("order_info_id", waybillTaskRelevance.getOrderInfoId());
        paraMap.put("from_task_code", fromTaskCode);
        List<WaybillTaskRelevance> waybillTaskRelevances = waybillTaskRelevanceMapper.findWaybillTaskRelevanceByParaMap(paraMap);
        if(CollUtil.isNotEmpty(waybillTaskRelevances)){
            waybillTaskRelevances.forEach(waybillTaskRelevance1 -> {
                waybillTaskRelevance1.setStatus("1");//状态(0未激活 1已激活,未完成 2已完成)
            });
            this.saveOrUpdateBatch(waybillTaskRelevances);
        }

    }

}
