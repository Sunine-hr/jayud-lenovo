package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.DayFlagEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.BillTaskRelevanceMapper;
import com.jayud.mall.mapper.OceanBillMapper;
import com.jayud.mall.model.po.BillTaskRelevance;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.vo.BillTaskRelevanceVO;
import com.jayud.mall.model.vo.BillTaskVO;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.service.IBillTaskRelevanceService;
import com.jayud.mall.service.IBillTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    IBillTaskService billTaskService;

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
}
