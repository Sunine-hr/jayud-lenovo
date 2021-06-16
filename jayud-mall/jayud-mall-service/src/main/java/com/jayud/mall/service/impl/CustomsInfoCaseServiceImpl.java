package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.BillCustomsInfoMapper;
import com.jayud.mall.mapper.CustomsInfoCaseMapper;
import com.jayud.mall.mapper.OrderCaseMapper;
import com.jayud.mall.model.bo.BillCustomsInfoQueryForm;
import com.jayud.mall.model.bo.CreateCustomsInfoCaseForm;
import com.jayud.mall.model.po.CustomsInfoCase;
import com.jayud.mall.model.vo.BillCaseVO;
import com.jayud.mall.model.vo.BillCustomsInfoVO;
import com.jayud.mall.model.vo.CustomsInfoCaseVO;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.service.ICustomsInfoCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报关文件箱号 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Service
public class CustomsInfoCaseServiceImpl extends ServiceImpl<CustomsInfoCaseMapper, CustomsInfoCase> implements ICustomsInfoCaseService {


    @Autowired
    CustomsInfoCaseMapper customsInfoCaseMapper;
    @Autowired
    BillCustomsInfoMapper billCustomsInfoMapper;
    @Autowired
    OrderCaseMapper orderCaseMapper;


    @Override
    public List<BillCaseVO> findUnselectedBillCaseByCustoms(BillCustomsInfoQueryForm form) {
        Long bId = form.getId();//(提单)报关信息表id(bill_customs_info id)
        BillCustomsInfoVO billCustomsInfoVO = billCustomsInfoMapper.findBillCustomsInfoById(bId);
        if(ObjectUtil.isEmpty(billCustomsInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "(提单)报关信息不存在");
        }
        Integer billId = billCustomsInfoVO.getBillId();//提单id(ocean_bill id)
        Integer type = billCustomsInfoVO.getType();//类型(0买单 1独立)
        form.setBillId(billId);
        form.setType(type);
        List<CustomsInfoCaseVO> customsInfoCaseList = customsInfoCaseMapper.findCustomsInfoCaseByBid(bId);
        List<Long> filterCaseIds = new ArrayList<>();
        if(CollUtil.isNotEmpty(customsInfoCaseList)){
            customsInfoCaseList.forEach(customsInfoCaseVO -> {
                Long caseId = customsInfoCaseVO.getCaseId();
                filterCaseIds.add(caseId);
            });
        }
        form.setFilterCaseIds(filterCaseIds);
        //报关箱子-查询提单下未生成的订单箱子(分类型)
        List<BillCaseVO> billCaseList = customsInfoCaseMapper.findUnselectedBillCaseByCustoms(form);
        return billCaseList;
    }

    @Override
    public List<BillCaseVO> findSelectedBillCaseByCustoms(BillCustomsInfoQueryForm form) {
        //报关箱子-查询提单下已生成的订单箱子
        List<BillCaseVO> billCaseList = customsInfoCaseMapper.findSelectedBillCaseByCustoms(form);
        return billCaseList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCustomsInfoCase(CreateCustomsInfoCaseForm form) {
        Long bId = form.getBId();//提单对应报关信息id(bill_customs_info id)
        BillCustomsInfoVO billCustomsInfoVO = billCustomsInfoMapper.findBillCustomsInfoById(bId);
        if(ObjectUtil.isEmpty(billCustomsInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "(提单)报关信息不存在");
        }
        List<Long> caseIds = form.getCaseIds();
        if(CollUtil.isEmpty(caseIds)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "箱子不能为空");
        }
        List<CustomsInfoCase> customsInfoCaseList = new ArrayList<>();
        for (int i=0; i<caseIds.size(); i++){
            Long caseId = caseIds.get(i);
            OrderCaseVO orderCaseVO = orderCaseMapper.findOrderCaseById(caseId);
            if(ObjectUtil.isEmpty(orderCaseVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "箱子不能为空");
            }
            CustomsInfoCase customsInfoCase = new CustomsInfoCase();
            customsInfoCase.setBId(bId);
            customsInfoCase.setBName(billCustomsInfoVO.getFileName());
            customsInfoCase.setBillId(billCustomsInfoVO.getBillId());
            customsInfoCase.setBillNo(billCustomsInfoVO.getBillNo());
            customsInfoCase.setCaseId(orderCaseVO.getId());
            customsInfoCase.setCartonNo(orderCaseVO.getCartonNo());
            customsInfoCase.setCreateTime(LocalDateTime.now());
            customsInfoCase.setOrderNo(orderCaseVO.getOrderNo());
            customsInfoCaseList.add(customsInfoCase);
        }
        if(CollUtil.isNotEmpty(customsInfoCaseList)){
            this.saveOrUpdateBatch(customsInfoCaseList);
        }
    }

}
