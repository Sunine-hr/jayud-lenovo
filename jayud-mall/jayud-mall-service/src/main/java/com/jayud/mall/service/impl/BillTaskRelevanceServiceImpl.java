package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.BillTaskRelevanceMapper;
import com.jayud.mall.model.po.BillTaskRelevance;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.vo.BillTaskRelevanceVO;
import com.jayud.mall.model.vo.BillTaskVO;
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
    IBillTaskService billTaskService;

    @Override
    public List<BillTaskRelevanceVO> savebillTaskRelevance(OceanBill oceanBill) {
        Long obId = oceanBill.getId();
        //根据提单id，查询提单任务
        List<BillTaskVO> list = billTaskService.findbillTaskByObId(obId);
        List<BillTaskRelevance> billTaskRelevances = new ArrayList<>();
        list.forEach(billTaskVO -> {
            BillTaskRelevance form = new BillTaskRelevance();
            form.setOceanBillId(obId);
            form.setTaskCode(billTaskVO.getTaskCode());
            form.setTaskName(billTaskVO.getTaskName());
            form.setSort(billTaskVO.getSort());
            form.setDays(billTaskVO.getDays());
            form.setDayFlag(billTaskVO.getDayFlag());
            form.setOperators(billTaskVO.getMemberUserName());
            form.setMinutes(billTaskVO.getMinutes().toString());
            form.setScore(billTaskVO.getScore());
            form.setRemarks(billTaskVO.getRemarks());
            form.setStatus("1");
            form.setReason(null);
            form.setUpTime(null);
            form.setUserId(billTaskVO.getMemberUserId().intValue());
            form.setUserName(billTaskVO.getMemberUserName());
            form.setCreateTime(LocalDateTime.now());

            billTaskRelevances.add(form);
        });

        //先删除
        QueryWrapper<BillTaskRelevance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ocean_bill_id", obId);
        this.remove(queryWrapper);

        //再保存
        this.saveOrUpdateBatch(billTaskRelevances);

        List<BillTaskRelevanceVO> billTaskRelevanceVOS =
                ConvertUtil.convertList(billTaskRelevances, BillTaskRelevanceVO.class);
        return billTaskRelevanceVOS;
    }
}
