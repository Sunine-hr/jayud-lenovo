package com.jayud.oceanship.service.impl;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.DelOprStatusForm;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oceanship.bo.*;
import com.jayud.oceanship.enums.SeaBookShipStatusEnum;
import com.jayud.oceanship.feign.FileClient;
import com.jayud.oceanship.feign.OauthClient;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.po.*;
import com.jayud.oceanship.mapper.SeaOrderMapper;
import com.jayud.oceanship.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oceanship.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 海运订单表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Slf4j
@Service
public class SeaOrderServiceImpl extends ServiceImpl<SeaOrderMapper, SeaOrder> implements ISeaOrderService {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private IOrderFlowSheetService orderFlowSheetService;

    @Autowired
    private IOrderStatusService orderStatusService;

    @Autowired
    private ISeaBookshipService seaBookshipService;

    @Autowired
    private ITermsService termsService;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private FileClient fileClient;

    @Override
    @Transactional
    public void createOrder(AddSeaOrderForm addSeaOrderForm) {
        LocalDateTime now = LocalDateTime.now();
        SeaOrder seaOrder = ConvertUtil.convert(addSeaOrderForm, SeaOrder.class);
        //创建海运单
        if (addSeaOrderForm.getOrderId() == null) {
            //生成订单号
            String orderNo = generationOrderNo();
            seaOrder.setOrderNo(orderNo);
            seaOrder.setCreateTime(now);
            seaOrder.setCreateUser(UserOperator.getToken());
            seaOrder.setStatus(OrderStatusEnum.SEA_S_0.getCode());
            this.save(seaOrder);
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("class_code","HY");
            queryWrapper.isNotNull("sub_sorts");
            queryWrapper.orderByAsc("sub_sorts");
            List<OrderStatus> statuses = orderStatusService.list(queryWrapper);
            for (int i = 0; i < statuses.size(); i++) {
                OrderFlowSheet orderFlowSheet = new OrderFlowSheet();
                orderFlowSheet.setMainOrderNo(seaOrder.getMainOrderNo());
                orderFlowSheet.setOrderNo(seaOrder.getOrderNo());
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
                orderFlowSheet.setCreateUser(seaOrder.getCreateUser());
                orderFlowSheetService.saveOrUpdate(orderFlowSheet);
            }
        } else {
            //修改海运单
            seaOrder.setId(addSeaOrderForm.getOrderId());
            seaOrder.setStatus(OrderStatusEnum.SEA_S_0.getCode());
            seaOrder.setUpdateTime(now);
            seaOrder.setUpdateUser(UserOperator.getToken());
            this.updateById(seaOrder);
        }
        //获取用户地址
        List<AddOrderAddressForm> orderAddressForms = addSeaOrderForm.getOrderAddressForms();
        System.out.println("orderAddressForms=================================="+orderAddressForms);
        for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
            orderAddressForm.setOrderNo(seaOrder.getOrderNo());
            orderAddressForm.setBusinessType(BusinessTypeEnum.HY.getCode());
            orderAddressForm.setBusinessId(seaOrder.getId());
            orderAddressForm.setCreateTime(LocalDateTime.now());
            orderAddressForm.setFileName(StringUtils.getFileNameStr(orderAddressForm.getTakeFiles()));
            orderAddressForm.setFilePath(StringUtils.getFileStr(orderAddressForm.getTakeFiles()));
        }
        //批量保存用户地址
        ApiResult result = this.omsClient.saveOrUpdateOrderAddressBatch(orderAddressForms);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("批量保存/修改订单地址信息失败,订单地址信息={}", new JSONArray(orderAddressForms));
        }

        List<AddGoodsForm> goodsForms = addSeaOrderForm.getGoodsForms();
        for (AddGoodsForm goodsForm : goodsForms) {
            goodsForm.setOrderNo(seaOrder.getOrderNo());
            goodsForm.setBusinessId(seaOrder.getId());
            goodsForm.setBusinessType(BusinessTypeEnum.HY.getCode());
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
    public String generationOrderNo() {
        //生成订单号
        String orderNo = StringUtils.loadNum(CommonConstant.S, 12);
        while (true) {
            if (isExistOrder(orderNo)) {//重复
                orderNo = StringUtils.loadNum(CommonConstant.S, 12);
            } else {
                break;
            }
        }
        return orderNo;
    }

    /**
     * 是否存在订单
     */
    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper<SeaOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(SeaOrder::getOrderNo, orderNo);
        return this.count(condition) > 0;
    }

    /**
     * 根据主订单号获取海运订单详情
     * @param orderNo
     * @return
     */
    @Override
    public SeaOrder getByMainOrderNO(String orderNo) {
        QueryWrapper<SeaOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(SeaOrder::getMainOrderNo, orderNo);
        return this.getOne(condition);
    }

    @Override
    public SeaOrderVO getSeaOrderByOrderNO(Long id) {
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        Integer businessType = BusinessTypeEnum.HY.getCode();
        //海运订单信息
        SeaOrderVO seaOrderVO = this.baseMapper.getSeaOrder(id);
        //查询商品信息
        ApiResult<List<GoodsVO>> result = this.omsClient.getGoodsByBusIds(Collections.singletonList(id), businessType);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("查询商品信息失败 airOrderId={}");
        }
        seaOrderVO.setGoodsForms(result.getData());
        //查询地址信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(Collections.singletonList(id), businessType);
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 airOrderId={}");
        }
        //处理地址信息
        for (OrderAddressVO address : resultOne.getData()) {
            address.getFile(prePath);
            seaOrderVO.processingAddress(address);
        }
        //查询订船信息
        SeaBookship seaBookship = this.seaBookshipService.getEnableBySeaOrderId(id);
        SeaBookshipVO seaBookshipVO = ConvertUtil.convert(seaBookship,SeaBookshipVO.class);
        seaOrderVO.setSeaBookshipVO(seaBookshipVO);
        return seaOrderVO;
    }

    @Override
    public IPage<SeaOrderFormVO> findByPage(QuerySeaOrderForm form) {
        if (StringUtils.isEmpty(form.getStatus())) { //订单列表
            form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                    , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
        } else {
            form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
        }

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<SeaOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form,legalIds);
    }

    /**
     * 更新流程状态
     */
    @Transactional
    @Override
    public void updateProcessStatus(SeaOrder seaOrder, SeaProcessOptForm form) {
        seaOrder.setId(form.getOrderId());
        seaOrder.setUpdateTime(LocalDateTime.now());
        seaOrder.setUpdateUser(UserOperator.getToken());
        seaOrder.setStatus(form.getStatus());

        //更改流程节点完成状态
        this.updateProcessStatusComplte(form);

        //更新状态节点状态
        this.baseMapper.updateById(seaOrder);
        //节点操作记录
        this.seaProcessOptRecord(form);

        //完成订单状态
        finishSeaOrderOpt(seaOrder);
    }

    /**
     * 修改流程节点状态
     * @param from
     */
    public void updateProcessStatusComplte(SeaProcessOptForm from){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",from.getOrderNo());
        queryWrapper.eq("status",from.getStatus());
        OrderFlowSheet one = orderFlowSheetService.getOne(queryWrapper);
        one.setComplete("1");
        boolean b = orderFlowSheetService.saveOrUpdate(one);

    }

    /**
     * 海运流程操作记录
     */
    @Override
    public void seaProcessOptRecord(SeaProcessOptForm form) {
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.SEA_ORDER);
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditUser(form.getOperatorUser());
        auditInfoForm.setFileViews(form.getFileViewList());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditTypeDesc(form.getStatusName());

        //文件拼接
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setBusinessType(BusinessTypeEnum.HY.getCode());

        if (omsClient.saveOprStatus(form).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用物流轨迹失败");
        }
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
        }

    }

    private void finishSeaOrderOpt(SeaOrder seaOrder) {
        if (OrderStatusEnum.SEA_S_8.getCode().equals(seaOrder.getStatus())) {
            //查询海运订单信息
            SeaOrder tmp = this.getById(seaOrder.getId());
            List<Terms> terms = termsService.list();

            for (Terms term : terms) {
                if(term.getName().equals("FOB")||term.getName().equals("CIF")||term.getName().equals("DAP")||term.getName().equals("FCA")){
                    if(term.getId().equals(tmp.getTerms())){
                        SeaOrder seaOrder1 = new SeaOrder();
                        seaOrder1.setId(tmp.getId());
                        seaOrder1.setProcessStatus(1);
                        this.updateById(seaOrder1);
                        return;
                    }
                }
            }
        }

        if (OrderStatusEnum.SEA_S_10.getCode().equals(seaOrder.getStatus())) {
            SeaOrder seaOrder2 = new SeaOrder();
            seaOrder2.setId(seaOrder.getId());
            seaOrder2.setProcessStatus(1);
            this.updateById(seaOrder2);
        }
    }

    /**
     * 订船操作
     */
    @Override
    @Transactional
    public void doSeaBookShipOpt(SeaProcessOptForm form) {
        AddSeaBookShipForm seaBookShipForm = form.getSeaBookShipForm();
        //查询订船是否存在,存在做更新操作
        SeaBookship oldSeaBookship = this.seaBookshipService.getEnableBySeaOrderId(form.getOrderId());
        seaBookShipForm.setId(oldSeaBookship != null ? oldSeaBookship.getId() : null);

        seaBookShipForm.getFile();
        SeaBookship seaBookship = ConvertUtil.convert(seaBookShipForm, SeaBookship.class);
        //处理提单文件
        //this.handleLadingBillFile(seaBookship, form);
        //设置订船状态
        seaBookship.setStatus(0);

        seaBookshipService.saveOrUpdateBookShip(seaBookship);
        //更改流程节点完成状态
        this.updateProcessStatusComplte(form);
        updateProcessStatus(new SeaOrder(), form);
    }

    @Override
    public void updateOrSaveProcessStatus(SeaProcessOptForm form) {
        AddSeaOrderForm convert = ConvertUtil.convert(form, AddSeaOrderForm.class);
        convert.assemblyAddress();
        createOrder(convert);
        SeaOrder seaOrder = new SeaOrder();
        seaOrder.setId(form.getOrderId());
        seaOrder.setUpdateTime(LocalDateTime.now());
        seaOrder.setUpdateUser(UserOperator.getToken());
        seaOrder.setStatus(form.getStatus());
        seaOrder.setCutReplenishTime(form.getCutReplenishTime());
        seaOrder.setCabinetNumber(form.getCabinetNumber());
        seaOrder.setPaperStripSeal(form.getPaperStripSeal());

        //更改流程节点完成状态
        this.updateProcessStatusComplte(form);

        //更新状态节点状态
        this.baseMapper.updateById(seaOrder);
        //节点操作记录
        this.seaProcessOptRecord(form);

        //完成订单状态
        finishSeaOrderOpt(seaOrder);
    }

    /**
     * 获取订单详情
     * @param seaOrderId
     * @return
     */
    @Override
    public SeaOrderVO getSeaOrderDetails(Long seaOrderId) {

        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        Integer businessType = BusinessTypeEnum.HY.getCode();
        //海运订单信息
        SeaOrderVO seaOrder = this.baseMapper.getSeaOrder(seaOrderId);
        //查询商品信息
        ApiResult<List<GoodsVO>> result = this.omsClient.getGoodsByBusIds(Collections.singletonList(seaOrderId), businessType);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("查询商品信息失败 airOrderId={}", seaOrderId);
        }
        seaOrder.setGoodsForms(result.getData());
        //查询地址信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(Collections.singletonList(seaOrderId), businessType);
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 airOrderId={}", seaOrderId);
        }
        //处理地址信息
        for (OrderAddressVO address : resultOne.getData()) {
            address.getFile(prePath);
            seaOrder.processingAddress(address);
        }

        //查询订舱信息
        SeaBookship seaBookship = this.seaBookshipService.getEnableBySeaOrderId(seaOrderId);
        SeaBookshipVO convert = ConvertUtil.convert(seaBookship, SeaBookshipVO.class);
        convert.getFile(prePath);
        seaOrder.setSeaBookshipVO(convert);

        System.out.println("status========================"+seaOrder.getStatus());
        if(seaOrder.getStatus() != OrderStatusEnum.SEA_S_0.getCode() || seaOrder.getStatus() != OrderStatusEnum.SEA_S_1.getCode()){
            CommonResult<List<InitComboxVO>> initSupplierInfo = omsClient.initSupplierInfo();
            List<InitComboxVO> data = initSupplierInfo.getData();
            for (InitComboxVO datum : data) {
                if(datum.getId().equals(seaOrder.getSeaBookshipVO().getAgentSupplierId())){
                    seaOrder.setSupplierName(datum.getName());
                }
            }
        }

        return seaOrder;
    }

    @Override
    public void orderReceiving(SeaOrder seaOrder, AuditInfoForm auditInfoForm, SeaCargoRejected seaCargoRejected) {
        SeaOrder tmp = new SeaOrder();
        tmp.setId(seaOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());

        omsClient.saveAuditInfo(auditInfoForm);
        this.updateById(tmp);
    }

    @Override
    public void rejectedOpt(SeaOrder seaOrder, AuditInfoForm auditInfoForm, SeaCargoRejected seaCargoRejected) {

        SeaOrder tmp = new SeaOrder();
        tmp.setId(seaOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());
        //根据选择是否订船驳回
        ApiResult result = new ApiResult();
        //删除物流轨迹表订舱数据
        switch (seaCargoRejected.getRejectOptions()) {
            case 1://订单驳回
                result = omsClient.deleteLogisticsTrackByType(seaOrder.getId(), BusinessTypeEnum.HY.getCode());
                //删除订船数据
                SeaBookship seaBookship = new SeaBookship();
                seaBookship.setStatus(SeaBookShipStatusEnum.DELETE.getCode());
                this.seaBookshipService.updateBySeaOrderId(seaOrder.getId(), seaBookship);
                break;
            case 2://订船驳回
                DelOprStatusForm form = new DelOprStatusForm();
                form.setOrderId(seaOrder.getId());
                form.setStatus(Collections.singletonList(OrderStatusEnum.SEA_S_2.getCode()));
                result = this.omsClient.delSpecOprStatus(form);
                tmp.setStatus(OrderStatusEnum.SEA_S_3_2.getCode());
        }

        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("远程调用删除订舱轨迹失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        //更改为驳回状态
        this.updateById(tmp);
    }

    @Override
    public List<SeaOrder> getSeaOrderByOrderNOs(List<String> mainOrderNoList) {
        QueryWrapper<SeaOrder> condition = new QueryWrapper<>();
        condition.lambda().in(SeaOrder::getMainOrderNo, mainOrderNoList);
        return this.baseMapper.selectList(condition);
    }


    private void handleLadingBillFile(SeaBookship seaBookship, SeaProcessOptForm form) {
            seaBookship.setFilePath(StringUtils.getFileStr(form.getFileViewList()));
            seaBookship.setFileName(StringUtils.getFileNameStr(form.getFileViewList()));
    }
}
