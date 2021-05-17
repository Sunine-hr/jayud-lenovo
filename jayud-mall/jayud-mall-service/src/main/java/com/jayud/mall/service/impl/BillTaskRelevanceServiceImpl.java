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
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.BillTaskRelevanceIdForm;
import com.jayud.mall.model.bo.BillTaskRelevanceQueryForm;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.BillTaskRelevanceVO;
import com.jayud.mall.model.vo.BillTaskVO;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 提单任务关联 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Service
public class BillTaskRelevanceServiceImpl extends ServiceImpl<BillTaskRelevanceMapper, BillTaskRelevance> implements IBillTaskRelevanceService {

    @Autowired
    BillTaskRelevanceMapper billTaskRelevanceMapper;
    @Autowired
    OceanBillMapper oceanBillMapper;
    @Autowired
    TaskExecutionRuleMapper taskExecutionRuleMapper;
    @Autowired
    BillOrderRelevanceMapper billOrderRelevanceMapper;
    @Autowired
    WaybillTaskRelevanceMapper waybillTaskRelevanceMapper;

    @Autowired
    BaseService baseService;
    @Autowired
    IBillTaskService billTaskService;
    @Autowired
    ILogisticsTrackService logisticsTrackService;
    @Autowired
    IWaybillTaskRelevanceService waybillTaskRelevanceService;

    @Override
    public List<BillTaskRelevanceVO> savebillTaskRelevance(OceanBill oceanBill) {
        Long obId = oceanBill.getId();//提单id

        OceanBillVO oceanBillVO = oceanBillMapper.findOceanBillById(obId);
        LocalDateTime sailTime = oceanBillVO.getSailTime();

        //根据提单id，找运营组和任务
        List<BillTaskVO> billTaskVOS = billTaskRelevanceMapper.findBillTaskByObId(obId);

        List<BillTaskRelevance> billTaskRelevances  = new ArrayList<>();
        billTaskVOS.forEach(billTaskVO -> {
            BillTaskRelevance billTaskRelevance = ConvertUtil.convert(billTaskVO, BillTaskRelevance.class);
            billTaskRelevance.setOceanBillId(obId);
            String activationSwitch = billTaskVO.getActivationSwitch();//激活开关(0未激活 1已激活)
            if (activationSwitch.equals("1")){
                billTaskRelevance.setStatus("1");//状态(0未激活 1已激活,未完成 2已完成)
            }else{
                billTaskRelevance.setStatus("0");//状态(0未激活 1已激活,未完成 2已完成)
            }
            String dayFlag = billTaskRelevance.getDayFlag();
            Integer days = billTaskRelevance.getDays();
            if(dayFlag.equals(DayFlagEnum.ETD.getCode())){
                LocalDateTime taskLastTime = sailTime.plusDays(days);
                billTaskRelevance.setTaskLastTime(taskLastTime);
            }
            billTaskRelevances.add(billTaskRelevance);
        });

        //先删除
        QueryWrapper<BillTaskRelevance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ocean_bill_id", obId);
        this.remove(queryWrapper);
        //再保存
        this.saveOrUpdateBatch(billTaskRelevances);
        List<BillTaskRelevanceVO> billTaskRelevanceVOS = ConvertUtil.convertList(billTaskRelevances, BillTaskRelevanceVO.class);
        return billTaskRelevanceVOS;

    }

    @Override
    public List<BillTaskRelevanceVO> findBillTaskRelevance(BillTaskRelevanceQueryForm form) {

        String isPersonal = form.getIsPersonal();
        if(isPersonal.equals("1")){
            //是否个人任务(1 仅看自己的任务 2查看所有的任务)
            AuthUser user = baseService.getUser();
            Long userId = user.getId();
            form.setUserId(userId.intValue());
        }
        List<BillTaskRelevanceVO> list = billTaskRelevanceMapper.findBillTaskRelevance(form);

        return list;
    }

    @Override
    public BillTaskRelevanceVO findBillTaskRelevanceById(Long id) {
        return billTaskRelevanceMapper.findBillTaskRelevanceById(id);
    }

    /**
     * 完成提单任务
     * 1.保存运单任务
     * 2.判断是否关联了 其他任务，并激活其他任务 (提单任务  激活  提单任务)
     * 3.是否通知运单物流轨迹 判断是否修改 订单 物流轨迹跟踪表 ,保存订单物流轨迹表
     * 4.4.判断是否关联了 其他任务，并激活其他任务 (提单任务  激活  运单任务)
     * @param form
     */
    @Override
    public void finishTask(BillTaskRelevanceIdForm form) {
        Long id = form.getId();
        BillTaskRelevanceVO billTaskRelevanceVO = billTaskRelevanceMapper.findBillTaskRelevanceById(id);
        AuthUser user = baseService.getUser();
        Integer loginUserId = user.getId().intValue();
        Integer userId = billTaskRelevanceVO.getUserId();
        if(!loginUserId.equals(userId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "仅当前任务负责人，能完成任务");
        }
        if(ObjectUtil.isEmpty(billTaskRelevanceVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "任务不存在");
        }
        String status = billTaskRelevanceVO.getStatus();
        if(!status.equals("1")){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "任务状态错误");
        }

        BillTaskRelevance billTaskRelevance = ConvertUtil.convert(billTaskRelevanceVO, BillTaskRelevance.class);
        billTaskRelevance.setStatus("2");//状态(0未激活 1已激活,未完成 2已完成)
        billTaskRelevance.setUpTime(LocalDateTime.now());
        //1.保存运单任务
        this.saveOrUpdate(billTaskRelevance);

        //2.判断是否关联了 其他任务，并激活其他任务 (提单任务  激活  提单任务)
        String fromTaskCode = billTaskRelevance.getTaskCode();

        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("ocean_bill_id", billTaskRelevance.getOceanBillId());
        paraMap.put("from_task_code", fromTaskCode);
        List<BillTaskRelevance> billTaskRelevances = billTaskRelevanceMapper.findBillTaskRelevanceByParaMap(paraMap);
        if(CollUtil.isNotEmpty(billTaskRelevances)){
            billTaskRelevances.forEach(billTaskRelevance1 -> {
                billTaskRelevance1.setStatus("1");//状态(0未激活 1已激活,未完成 2已完成)
            });
            this.saveOrUpdateBatch(billTaskRelevances);
        }

        //根据提单id，查询 提单关联的订单(运单)
        Long billId = billTaskRelevanceVO.getOceanBillId();
        List<BillOrderRelevance> billOrderRelevances = billOrderRelevanceMapper.findBillOrderRelevanceByBillId(billId);
        for(int i=0; i<billOrderRelevances.size(); i++){
            BillOrderRelevance billOrderRelevance = billOrderRelevances.get(i);
            String isInform = billOrderRelevance.getIsInform();//是否通知运单物流轨迹(1通知 2不通知)
            //3.是否通知运单物流轨迹 判断是否修改 订单 物流轨迹跟踪表 ,保存订单物流轨迹表
            if(isInform.equals("1")){
                String logisticsTrackDescription = form.getLogisticsTrackDescription();
                LocalDateTime logisticsTrackCreateTime = ObjectUtil.isEmpty(form.getLogisticsTrackCreateTime()) ? LocalDateTime.now() : form.getLogisticsTrackCreateTime();
                if(StrUtil.isNotEmpty(logisticsTrackDescription)){
                    Long orderId = billOrderRelevance.getOrderId();
                    LogisticsTrack logisticsTrack = new LogisticsTrack();
                    logisticsTrack.setOrderId(orderId.toString());
                    logisticsTrack.setStatus(1);
                    logisticsTrack.setStatusName("1");
                    logisticsTrack.setDescription(logisticsTrackDescription);
                    logisticsTrack.setCreateTime(logisticsTrackCreateTime);
                    logisticsTrack.setOperatorId(billTaskRelevance.getUserId());
                    logisticsTrack.setOperatorName(billTaskRelevance.getUserName());
                    logisticsTrackService.saveOrUpdate(logisticsTrack);
                }
            }

            //4.判断是否关联了 其他任务，并激活其他任务 (提单任务  激活  运单任务)
            Long orderId = billOrderRelevance.getOrderId();
            Map<String, Object> paraMap2 = new HashMap<>();
            paraMap.put("order_info_id", orderId);
            paraMap.put("from_task_code", fromTaskCode);
            List<WaybillTaskRelevance> waybillTaskRelevances = waybillTaskRelevanceMapper.findWaybillTaskRelevanceByParaMap(paraMap2);
            if(CollUtil.isNotEmpty(waybillTaskRelevances)){
                waybillTaskRelevances.forEach(waybillTaskRelevance1 -> {
                    waybillTaskRelevance1.setStatus("1");//状态(0未激活 1已激活,未完成 2已完成)
                });
                waybillTaskRelevanceService.saveOrUpdateBatch(waybillTaskRelevances);
            }
        }

    }

    /**
     * 延期提单任务
     * 1.保存提单任务
     * 2.判断是否关联了 其他任务，并激活其他任务 (提单任务  激活  提单任务)
     * 3.是否通知运单物流轨迹 判断是否修改 订单 物流轨迹跟踪表 ,保存订单物流轨迹表
     * 4.判断是否关联了 其他任务，并激活其他任务 (提单任务  激活  运单任务)
     * @param form
     */
    @Override
    public void postponeTask(BillTaskRelevanceIdForm form) {
        Long id = form.getId();
        BillTaskRelevanceVO billTaskRelevanceVO = billTaskRelevanceMapper.findBillTaskRelevanceById(id);
        AuthUser user = baseService.getUser();
        Integer loginUserId = user.getId().intValue();
        Integer userId = billTaskRelevanceVO.getUserId();
        LocalDateTime taskLastTime = billTaskRelevanceVO.getTaskLastTime();
        LocalDateTime now = LocalDateTime.now();
        if(now.compareTo(taskLastTime) < 0){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "最后完成时间不小于今天，不能延期");
        }
        if(!loginUserId.equals(userId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "仅当前任务负责人，能完成任务");
        }
        if(ObjectUtil.isEmpty(billTaskRelevanceVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "任务不存在");
        }
        String status = billTaskRelevanceVO.getStatus();
        if(!status.equals("1")){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "任务状态错误");
        }
        BillTaskRelevance billTaskRelevance = ConvertUtil.convert(billTaskRelevanceVO, BillTaskRelevance.class);
        billTaskRelevance.setUpTime(LocalDateTime.now());
        Integer postponeDays = form.getPostponeDays();
        postponeDays = ObjectUtil.isEmpty(postponeDays) ? 1 : postponeDays;
        LocalDateTime localDateTime = taskLastTime.plusDays(postponeDays);//最后完成时间，加上延期的时间
        billTaskRelevance.setTaskLastTime(localDateTime);
        //1.保存提单任务
        this.saveOrUpdate(billTaskRelevance);

        //2.判断是否关联了 其他任务，并激活其他任务 (提单任务  激活  提单任务)
        String fromTaskCode = billTaskRelevance.getTaskCode();

        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("ocean_bill_id", billTaskRelevance.getOceanBillId());
        paraMap.put("from_task_code", fromTaskCode);
        List<BillTaskRelevance> billTaskRelevances = billTaskRelevanceMapper.findBillTaskRelevanceByParaMap(paraMap);
        if(CollUtil.isNotEmpty(billTaskRelevances)){
            billTaskRelevances.forEach(billTaskRelevance1 -> {
                billTaskRelevance1.setStatus("1");//状态(0未激活 1已激活,未完成 2已完成)
            });
            this.saveOrUpdateBatch(billTaskRelevances);
        }

        //根据提单id，查询 提单关联的订单(运单)
        Long billId = billTaskRelevanceVO.getOceanBillId();
        List<BillOrderRelevance> billOrderRelevances = billOrderRelevanceMapper.findBillOrderRelevanceByBillId(billId);

        for(int i=0; i<billOrderRelevances.size(); i++){
            BillOrderRelevance billOrderRelevance = billOrderRelevances.get(i);
            String isInform = billOrderRelevance.getIsInform();//是否通知运单物流轨迹(1通知 2不通知)
            //3.是否通知运单物流轨迹 判断是否修改 订单 物流轨迹跟踪表 ,保存订单物流轨迹表
            if(isInform.equals("1")){
                String logisticsTrackDescription = form.getLogisticsTrackDescription();
                LocalDateTime logisticsTrackCreateTime = ObjectUtil.isEmpty(form.getLogisticsTrackCreateTime()) ? LocalDateTime.now() : form.getLogisticsTrackCreateTime();
                if(StrUtil.isNotEmpty(logisticsTrackDescription)){
                    Long orderId = billOrderRelevance.getOrderId();
                    LogisticsTrack logisticsTrack = new LogisticsTrack();
                    logisticsTrack.setOrderId(orderId.toString());
                    logisticsTrack.setStatus(1);
                    logisticsTrack.setStatusName("1");
                    logisticsTrack.setDescription(logisticsTrackDescription);
                    logisticsTrack.setCreateTime(logisticsTrackCreateTime);
                    logisticsTrack.setOperatorId(billTaskRelevance.getUserId());
                    logisticsTrack.setOperatorName(billTaskRelevance.getUserName());
                    logisticsTrackService.saveOrUpdate(logisticsTrack);
                }
            }

            //4.判断是否关联了 其他任务，并激活其他任务 (提单任务  激活  运单任务)
            Long orderId = billOrderRelevance.getOrderId();
            Map<String, Object> paraMap2 = new HashMap<>();
            paraMap.put("order_info_id", orderId);
            paraMap.put("from_task_code", fromTaskCode);
            List<WaybillTaskRelevance> waybillTaskRelevances = waybillTaskRelevanceMapper.findWaybillTaskRelevanceByParaMap(paraMap2);
            if(CollUtil.isNotEmpty(waybillTaskRelevances)){
                waybillTaskRelevances.forEach(waybillTaskRelevance1 -> {
                    waybillTaskRelevance1.setStatus("1");//状态(0未激活 1已激活,未完成 2已完成)
                });
                waybillTaskRelevanceService.saveOrUpdateBatch(waybillTaskRelevances);
            }
        }

    }


}
