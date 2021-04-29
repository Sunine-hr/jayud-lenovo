package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.BillClearanceInfoMapper;
import com.jayud.mall.model.po.BillClearanceInfo;
import com.jayud.mall.model.vo.BillClearanceInfoVO;
import com.jayud.mall.model.vo.ClearanceInfoCaseExcelVO;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;
import com.jayud.mall.service.IBillClearanceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 应收对账单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Service
public class BillClearanceInfoServiceImpl extends ServiceImpl<BillClearanceInfoMapper, BillClearanceInfo> implements IBillClearanceInfoService {

    @Autowired
    BillClearanceInfoMapper billClearanceInfoMapper;


    @Override
    public List<ClearanceInfoCaseVO> findClearanceInfoCase(Long b_id) {
        return billClearanceInfoMapper.findClearanceInfoCase(b_id);
    }

    @Override
    public BillClearanceInfoVO findBillClearanceInfoById(Long id) {
        return billClearanceInfoMapper.findBillClearanceInfoById(id);
    }

    @Override
    public List<ClearanceInfoCaseExcelVO> findClearanceInfoCaseBybid(Long b_id) {
        List<ClearanceInfoCaseExcelVO> clearanceInfoCaseExcelVOS = billClearanceInfoMapper.findClearanceInfoCaseBybid(b_id);
        return clearanceInfoCaseExcelVOS;
    }
}
