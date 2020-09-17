package com.jayud.customs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.customs.feign.OmsClient;
import com.jayud.customs.mapper.OrderCustomsMapper;
import com.jayud.customs.service.IOrderCustomsService;
import com.jayud.customs.model.bo.InputOrderCustomsForm;
import com.jayud.customs.model.bo.InputOrderForm;
import com.jayud.customs.model.bo.InputSubOrderCustomsForm;
import com.jayud.customs.model.bo.OprOrderLogForm;
import com.jayud.customs.model.po.OrderCustoms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报关业务订单表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderCustomsServiceImpl extends ServiceImpl<OrderCustomsMapper, OrderCustoms> implements IOrderCustomsService {

    @Autowired
    OmsClient omsClient;

    @Autowired
    RedisUtils redisUtils;


    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",orderNo);
        List<OrderCustoms> orderCustomsList = baseMapper.selectList(queryWrapper);
        if(orderCustomsList == null || orderCustomsList.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean oprOrderCustoms(InputOrderCustomsForm form) {
        try {
            InputOrderForm inputOrderForm = ConvertUtil.convert(form, InputOrderForm.class);
            List<OprOrderLogForm> oprOrderLogForms = new ArrayList<>();
            //处理主订单
            //保存主订单数据,返回主订单号,暂存和提交
            ApiResult apiResult = omsClient.oprMainOrder(inputOrderForm);

            //暂存或提交只是主订单的状态不一样，具体更新还是保存还是根据主键区别
            OrderCustoms customs = ConvertUtil.convert(form, OrderCustoms.class);
            if (form.getSubOrderId() != null) {
                customs.setUpdatedTime(LocalDateTime.now());
                customs.setUpdatedUser(getLoginUser());
            } else {
                customs.setCreateUser(getLoginUser());
            }
            //子订单数据初始化处理
            //设置子订单号/报关抬头/结算单位/附件
            List<OrderCustoms> orderCustomsList = new ArrayList<>();
            List<InputSubOrderCustomsForm> subOrderCustomsForms = form.getSubOrders();
            for (InputSubOrderCustomsForm subOrder : subOrderCustomsForms) {
                customs.setOrderNo(subOrder.getOrderNo());
                customs.setTitle(subOrder.getTitle());
                customs.setUnitCode(subOrder.getUnitCode());
                customs.setUnitAccount(subOrder.getUnitAccount());
                customs.setDescription(subOrder.getDescription());
                customs.setMainOrderNo(String.valueOf(apiResult.getData()));
                customs.setStatus(Integer.valueOf(OrderStatusEnum.CUSTOMS_0.getCode()));
                orderCustomsList.add(customs);

                //记录操作日志
                OprOrderLogForm oprOrderLogForm = new OprOrderLogForm();
                oprOrderLogForm.setMainOrderNo(String.valueOf(apiResult.getData()));
                oprOrderLogForm.setOrderNo(subOrder.getOrderNo());
                oprOrderLogForm.setRemarks("纯报关-创建订单");
                oprOrderLogForm.setOptUname(getLoginUser());
                oprOrderLogForms.add(oprOrderLogForm);
            }
            saveOrUpdateBatch(orderCustomsList);
            //记录订单日志
            omsClient.saveOprOrderLog(oprOrderLogForms);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private String getLoginUser(){
        String loginUser = redisUtils.get("loginUser",100);
        return loginUser;
    }
}
