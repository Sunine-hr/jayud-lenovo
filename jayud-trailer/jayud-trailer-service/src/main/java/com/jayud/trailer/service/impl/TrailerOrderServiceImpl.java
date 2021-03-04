package com.jayud.trailer.service.impl;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.OrderTypeEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.trailer.bo.AddGoodsForm;
import com.jayud.trailer.bo.AddOrderAddressForm;
import com.jayud.trailer.bo.AddTrailerOrderFrom;
import com.jayud.trailer.bo.QueryTrailerOrderForm;
import com.jayud.trailer.feign.FileClient;
import com.jayud.trailer.feign.OauthClient;
import com.jayud.trailer.feign.OmsClient;
import com.jayud.trailer.po.OrderFlowSheet;
import com.jayud.trailer.po.OrderStatus;
import com.jayud.trailer.po.TrailerDispatch;
import com.jayud.trailer.po.TrailerOrder;
import com.jayud.trailer.mapper.TrailerOrderMapper;
import com.jayud.trailer.service.IOrderFlowSheetService;
import com.jayud.trailer.service.IOrderStatusService;
import com.jayud.trailer.service.ITrailerDispatchService;
import com.jayud.trailer.service.ITrailerOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.trailer.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 拖车订单表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Service
@Slf4j
public class TrailerOrderServiceImpl extends ServiceImpl<TrailerOrderMapper, TrailerOrder> implements ITrailerOrderService {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private IOrderStatusService orderStatusService;

//    @Autowired
//    private IOrderFlowSheetService orderFlowSheetService;

    @Autowired
    private ITrailerDispatchService trailerDispatchService;

    @Override
    public void createOrder(AddTrailerOrderFrom addTrailerOrderFrom) {
        LocalDateTime now = LocalDateTime.now();
        TrailerOrder trailerOrder = ConvertUtil.convert(addTrailerOrderFrom, TrailerOrder.class);
        //System.out.println("orderId===================================="+addSeaOrderForm.getOrderId());
        //创建拖车单
        if (addTrailerOrderFrom.getId() == null) {
            //生成订单号
//            String orderNo = generationOrderNo(addTrailerOrderFrom.getLegalEntityId(),addTrailerOrderFrom.getImpAndExpType());
//            trailerOrder.setOrderNo(orderNo);
            trailerOrder.setCreateTime(now);
            trailerOrder.setCreateUser(UserOperator.getToken());
            trailerOrder.setStatus(OrderStatusEnum.TT_0.getCode());
            this.save(trailerOrder);
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("class_code","TC");
            queryWrapper.isNotNull("sub_sorts");
            queryWrapper.orderByAsc("sub_sorts");
            if(addTrailerOrderFrom.getImpAndExpType().equals("1")){
                queryWrapper.ne("contain_state","TT7");
            }else{
                queryWrapper.ne("contain_state","TT4");
                if(!addTrailerOrderFrom.getIsWeighed()){
                    queryWrapper.ne("contain_state","TT7");
                }
            }
            List<OrderStatus> statuses = orderStatusService.list(queryWrapper);
            List<OrderFlowSheet> list = new ArrayList<>();
            for (int i = 0; i < statuses.size(); i++) {
                OrderFlowSheet orderFlowSheet = new OrderFlowSheet();
                orderFlowSheet.setMainOrderNo(trailerOrder.getMainOrderNo());
                orderFlowSheet.setOrderNo(trailerOrder.getOrderNo());
                orderFlowSheet.setProductClassifyId(statuses.get(i).getClassCode());
                orderFlowSheet.setProductClassifyName(statuses.get(i).getClassName());
                orderFlowSheet.setStatus(statuses.get(i).getContainState());
                orderFlowSheet.setStatusName(statuses.get(i).getName());
                if(i==0){
                    orderFlowSheet.setComplete("1");
                    orderFlowSheet.setFStatus(null);
                }else{
                    orderFlowSheet.setComplete("0");
                    orderFlowSheet.setFStatus(statuses.get(i-1).getContainState());
                }
                orderFlowSheet.setIsPass("1");
                orderFlowSheet.setCreateTime(now);
                orderFlowSheet.setCreateUser(trailerOrder.getCreateUser());
                list.add(orderFlowSheet);
            }
            ApiResult apiResult = omsClient.batchAddOrUpdateProcess(list);
            if (apiResult.getCode() != HttpStatus.SC_OK) {
                log.warn("批量保存/修改订单地址信息失败,订单地址信息={}", new JSONArray(list));
            }
        } else {
            //修改拖车单
            trailerOrder.setId(addTrailerOrderFrom.getId());
            trailerOrder.setStatus(OrderStatusEnum.TT_0.getCode());
            trailerOrder.setUpdateTime(now);
            trailerOrder.setUpdateUser(UserOperator.getToken());
            this.updateById(trailerOrder);
        }
        //获取用户地址
        List<AddOrderAddressForm> orderAddressForms = addTrailerOrderFrom.getOrderAddressForms();
        //System.out.println("orderAddressForms=================================="+orderAddressForms);
        for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
            orderAddressForm.setOrderNo(trailerOrder.getOrderNo());
            orderAddressForm.setBusinessType(BusinessTypeEnum.TC.getCode());
            orderAddressForm.setBusinessId(trailerOrder.getId());
            orderAddressForm.setCreateTime(LocalDateTime.now());
            orderAddressForm.setFileName(StringUtils.getFileNameStr(orderAddressForm.getTakeFiles()));
            orderAddressForm.setFilePath(StringUtils.getFileStr(orderAddressForm.getTakeFiles()));
        }
        //批量保存用户地址
        ApiResult result = this.omsClient.saveOrUpdateOrderAddressBatch(orderAddressForms);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("批量保存/修改订单地址信息失败,订单地址信息={}", new JSONArray(orderAddressForms));
        }

        List<AddGoodsForm> goodsForms = addTrailerOrderFrom.getGoodsForms();
        for (AddGoodsForm goodsForm : goodsForms) {
            goodsForm.setOrderNo(trailerOrder.getOrderNo());
            goodsForm.setBusinessId(trailerOrder.getId());
            goodsForm.setBusinessType(BusinessTypeEnum.TC.getCode());
        }
        //批量保存货物信息
        result = this.omsClient.saveOrUpdateGoodsBatch(goodsForms);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("批量保存/修改商品信息失败,商品信息={}", new JSONArray(goodsForms));
        }

    }

    /**
     * 生成订单号
     */
    @Override
    public String generationOrderNo(Long legalId, Integer integer) {
        //生成订单号
        String legalCode = (String)oauthClient.getLegalEntityCodeByLegalId(legalId).getData();
        String preOrder = null;
        String classCode = null;
        if(integer.equals("1")){
            preOrder = OrderTypeEnum.TTI.getCode() + legalCode;
            classCode = OrderTypeEnum.TTI.getCode();
        }else {
            preOrder = OrderTypeEnum.TTE.getCode() + legalCode;
            classCode = OrderTypeEnum.TTE.getCode();
        }
        String orderNo = (String)omsClient.getOrderNo(preOrder,classCode).getData();
        return orderNo;
    }

    /**
     * 是否存在订单
     */
    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(TrailerOrder::getOrderNo, orderNo);
        return this.count(condition) > 0;
    }

    /**
     * 根据主订单号获取海运订单详情
     * @param orderNo
     * @return
     */
    @Override
    public TrailerOrder getByMainOrderNO(String orderNo) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(TrailerOrder::getMainOrderNo, orderNo);
        return this.getOne(condition);
    }

    @Override
    public TrailerOrderVO getTrailerOrderByOrderNO(Long id) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        Integer businessType = BusinessTypeEnum.TC.getCode();
        //海运订单信息
        TrailerOrderVO trailerOrderVO = this.baseMapper.getTrailerOrder(id);
        //查询商品信息
        ApiResult<List<GoodsVO>> result = this.omsClient.getGoodsByBusIds(Collections.singletonList(id), businessType);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("查询商品信息失败 airOrderId={}");
        }
        trailerOrderVO.setGoodsForms(result.getData());
        //查询地址信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(Collections.singletonList(id), businessType);
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 airOrderId={}");
        }
        //处理地址信息
        for (OrderAddressVO address : resultOne.getData()) {
            address.getFile(prePath);
        }
        //查询订船信息
        TrailerDispatch trailerDispatch = this.trailerDispatchService.getEnableByTrailerOrderId(id);
        TrailerDispatchVO trailerDispatchVO = ConvertUtil.convert(trailerDispatch,TrailerDispatchVO.class);
        trailerOrderVO.setTrailerDispatchVO(trailerDispatchVO);
        return trailerOrderVO;
    }

    @Override
    public List<TrailerOrder> getTrailerOrderByOrderNOs(List<String> mainOrderNoList) {
        QueryWrapper<TrailerOrder> condition = new QueryWrapper<>();
        condition.lambda().in(TrailerOrder::getMainOrderNo, mainOrderNoList);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public IPage<TrailerOrderFormVO> findByPage(QueryTrailerOrderForm form) {
        if (StringUtils.isEmpty(form.getStatus())) { //订单列表
            form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                    , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
        } else {
            form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
        }

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<TrailerOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form,legalIds);
    }

}
