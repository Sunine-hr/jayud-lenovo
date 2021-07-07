package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.BillClearanceInfoMapper;
import com.jayud.mall.mapper.ClearanceInfoCaseMapper;
import com.jayud.mall.mapper.OrderClearanceFileMapper;
import com.jayud.mall.model.bo.BillClearanceInfoQueryForm;
import com.jayud.mall.model.po.BillClearanceInfo;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IBillClearanceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    OrderClearanceFileMapper orderClearanceFileMapper;
    @Autowired
    ClearanceInfoCaseMapper clearanceInfoCaseMapper;

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

    @Override
    public List<OrderInfoVO> findSelectOrderInfoByClearance(BillClearanceInfoQueryForm form) {
        Long billClearanceInfoId = form.getId();//(提单)报关信息表id(bill_customs_info id)
        BillClearanceInfoVO billClearanceInfoVO = billClearanceInfoMapper.findBillClearanceInfoById(billClearanceInfoId);
        if(ObjectUtil.isEmpty(billClearanceInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "(提单)清关信息不存在");
        }
        Integer billId = billClearanceInfoVO.getBillId();//提单id(ocean_bill id)
        Integer type = billClearanceInfoVO.getType();//类型(0买单 1独立)
        form.setBillId(billId);
        form.setType(type);
        List<OrderInfoVO> orderInfoVOList = new ArrayList<>();
        if(type.equals(0)){
            //买单清关
            //根据 清关清单 关联的 订单箱子，查询订单list    need_clearance = 0    0代表买单
            orderInfoVOList = clearanceInfoCaseMapper.findOrderInfoByBillBillClearanceInfoId(billClearanceInfoId);
        }else if(type.equals(1)){
            //独立清关
            //根据 清关清单 所属的 提单，查询订单list    need_clearance = 1    1代表独立
            orderInfoVOList = clearanceInfoCaseMapper.findOrderInfoByBillId(billId);
        }

        //订单清关文件
        if(CollUtil.isNotEmpty(orderInfoVOList)){
            orderInfoVOList.forEach(orderInfoVO -> {
                Long orderId = orderInfoVO.getId();
                //运单中所有 清关文件列表
                List<OrderClearanceFileVO> orderClearanceFileList = orderClearanceFileMapper.findOrderClearanceFileByOrderId(orderId);
                if(CollUtil.isNotEmpty(orderClearanceFileList)){
                    orderClearanceFileList.forEach(orderCustomsFileVO -> {
                        String templateUrl = orderCustomsFileVO.getTemplateUrl();
                        if(templateUrl != null && templateUrl.length() > 0){
                            String json = templateUrl;
                            try {
                                List<TemplateUrlVO> templateUrlVOS = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                                });
                                orderCustomsFileVO.setTemplateUrlVOS(templateUrlVOS);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println("json格式错误");
                                orderCustomsFileVO.setTemplateUrlVOS(new ArrayList<>());
                            }
                        }else{
                            orderCustomsFileVO.setTemplateUrlVOS(new ArrayList<>());
                        }
                    });
                }
                orderInfoVO.setOrderClearanceFileVOList(orderClearanceFileList);
            });
        }
        return orderInfoVOList;
    }
}
