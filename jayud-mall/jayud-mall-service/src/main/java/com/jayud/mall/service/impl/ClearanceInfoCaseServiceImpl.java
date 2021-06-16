package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.BillClearanceInfoMapper;
import com.jayud.mall.mapper.ClearanceInfoCaseMapper;
import com.jayud.mall.mapper.OrderCaseMapper;
import com.jayud.mall.model.bo.BillClearanceInfoQueryForm;
import com.jayud.mall.model.bo.CreateClearanceInfoCaseForm;
import com.jayud.mall.model.po.ClearanceInfoCase;
import com.jayud.mall.model.vo.BillCaseVO;
import com.jayud.mall.model.vo.BillClearanceInfoVO;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.service.IClearanceInfoCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 清关文件箱号 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Service
public class ClearanceInfoCaseServiceImpl extends ServiceImpl<ClearanceInfoCaseMapper, ClearanceInfoCase> implements IClearanceInfoCaseService {

    @Autowired
    ClearanceInfoCaseMapper clearanceInfoCaseMapper;
    @Autowired
    BillClearanceInfoMapper billClearanceInfoMapper;
    @Autowired
    OrderCaseMapper orderCaseMapper;


    @Override
    public List<BillCaseVO> findUnselectedBillCaseByClearance(BillClearanceInfoQueryForm form) {
        Long bId = form.getId();//(提单)清关信息表id(bill_clearance_info id)
        BillClearanceInfoVO billClearanceInfoVO = billClearanceInfoMapper.findBillClearanceInfoById(bId);
        if(ObjectUtil.isEmpty(billClearanceInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "(提单)清关信息表");
        }
        Integer billId = billClearanceInfoVO.getBillId();//提单id(ocean_bill id)
        Integer type = billClearanceInfoVO.getType();//类型(0买单 1独立)
        form.setBillId(billId);
        form.setType(type);
        List<ClearanceInfoCaseVO> clearanceInfoCaseList = clearanceInfoCaseMapper.findClearanceInfoCaseByBid(bId);
        List<Long> filterCaseIds = new ArrayList<>();
        if(CollUtil.isNotEmpty(clearanceInfoCaseList)){
            clearanceInfoCaseList.forEach(clearanceInfoCaseVO -> {
                Long caseId = clearanceInfoCaseVO.getCaseId();
                filterCaseIds.add(caseId);
            });
        }
        form.setFilterCaseIds(filterCaseIds);
        //清关箱子-查询提单下未生成的订单箱子(分类型)
        List<BillCaseVO> billCaseList = clearanceInfoCaseMapper.findUnselectedBillCaseByClearance(form);
        return billCaseList;
    }

    @Override
    public List<BillCaseVO> findSelectedBillCaseByClearance(BillClearanceInfoQueryForm form) {
        //清关箱子-查询提单下已生成的订单箱子
        List<BillCaseVO> billCaseList = clearanceInfoCaseMapper.findSelectedBillCaseByClearance(form);
        return billCaseList;
    }

    @Override
    public void createClearanceInfoCase(CreateClearanceInfoCaseForm form) {
        Long bId = form.getBId();//提单对应清关信息id(bill_clearance_info id)
        BillClearanceInfoVO billClearanceInfoVO = billClearanceInfoMapper.findBillClearanceInfoById(bId);
        if(ObjectUtil.isEmpty(billClearanceInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "提单对应清关信息id不存在");
        }
        List<Long> caseIds = form.getCaseIds();
        if(CollUtil.isEmpty(caseIds)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "箱子不能为空");
        }
        List<ClearanceInfoCase> clearanceInfoCaseList = new ArrayList<>();
        for (int i=0; i<caseIds.size(); i++){
            Long caseId = caseIds.get(i);
            OrderCaseVO orderCaseVO = orderCaseMapper.findOrderCaseById(caseId);
            if(ObjectUtil.isEmpty(orderCaseVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "箱子不能为空");
            }
            ClearanceInfoCase clearanceInfoCase = new ClearanceInfoCase();
            clearanceInfoCase.setBId(bId);
            clearanceInfoCase.setBName(billClearanceInfoVO.getFileName());
            clearanceInfoCase.setBillId(billClearanceInfoVO.getBillId());
            clearanceInfoCase.setBillNo(billClearanceInfoVO.getBillNo());
            clearanceInfoCase.setCaseId(orderCaseVO.getId());
            clearanceInfoCase.setCartonNo(orderCaseVO.getCartonNo());
            clearanceInfoCase.setCreateTime(LocalDateTime.now());
            clearanceInfoCase.setOrderNo(orderCaseVO.getOrderNo());
            clearanceInfoCaseList.add(clearanceInfoCase);
        }
        if(CollUtil.isNotEmpty(clearanceInfoCaseList)){
            this.saveOrUpdateBatch(clearanceInfoCaseList);
        }


    }
}
