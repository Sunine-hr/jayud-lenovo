package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.BillTaskRelevanceMapper;
import com.jayud.mall.mapper.OceanBillMapper;
import com.jayud.mall.model.po.BillTaskRelevance;
import com.jayud.mall.model.po.OceanBill;
import com.jayud.mall.model.vo.BillTaskRelevanceVO;
import com.jayud.mall.model.vo.BillTaskVO;
import com.jayud.mall.service.IBillTaskRelevanceService;
import com.jayud.mall.service.IBillTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    OceanBillMapper oceanBillMapper;


    @Override
    public List<BillTaskRelevanceVO> savebillTaskRelevance(OceanBill oceanBill) {
        Long obId = oceanBill.getId();//提单id

        //根据提单id，找运营组和任务
        List<BillTaskVO> billTaskVOS = billTaskRelevanceMapper.findBillTaskByObId(obId);

        List<BillTaskRelevance> billTaskRelevances = ConvertUtil.convertList(billTaskVOS, BillTaskRelevance.class);
        billTaskRelevances.forEach(billTaskRelevance -> {
            billTaskRelevance.setOceanBillId(obId);
            billTaskRelevance.setStatus("0");//状态(0未激活 1已激活 2异常 3已完成)
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
