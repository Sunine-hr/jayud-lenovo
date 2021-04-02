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
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.FileView;
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
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private ISeaBookshipService seaBookshipService;

    @Autowired
    private ITermsService termsService;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private ICabinetSizeNumberService cabinetSizeNumberService;

    @Autowired
    private ISeaReplenishmentService seaReplenishmentService;

    @Autowired
    private ISeaContainerInformationService seaContainerInformationService;

    @Override
    @Transactional
    public String createOrder(AddSeaOrderForm addSeaOrderForm) {
        LocalDateTime now = LocalDateTime.now();
        SeaOrder seaOrder = ConvertUtil.convert(addSeaOrderForm, SeaOrder.class);
        //System.out.println("orderId===================================="+addSeaOrderForm.getOrderId());
        //创建海运单
        if (addSeaOrderForm.getOrderId() == null) {
            //生成订单号
//            String orderNo = generationOrderNo(addSeaOrderForm.getLegalEntityId(),addSeaOrderForm.getImpAndExpType());
//            seaOrder.setOrderNo(orderNo);
            seaOrder.setCreateTime(now);
            seaOrder.setCreateUser(UserOperator.getToken());
            seaOrder.setStatus(OrderStatusEnum.SEA_S_0.getCode());
            this.save(seaOrder);
        } else {
            //修改海运单
            seaOrder.setId(addSeaOrderForm.getOrderId());
            seaOrder.setStatus(OrderStatusEnum.SEA_S_0.getCode());
            seaOrder.setUpdateTime(now);
            seaOrder.setUpdateUser(UserOperator.getToken());
            this.updateById(seaOrder);
        }
        //获取柜型数量
        if(addSeaOrderForm.getCabinetType()!=null){
            if (addSeaOrderForm.getCabinetType().equals(2)) {
                //先删除原来的柜型

                this.cabinetSizeNumberService.deleteCabinet(addSeaOrderForm.getOrderId());

            }
            if (addSeaOrderForm.getCabinetType().equals(1)) {
                List<CabinetSizeNumber> cabinetSizeNumbers = addSeaOrderForm.getCabinetSizeNumbers();

                for (CabinetSizeNumber cabinetSizeNumber : cabinetSizeNumbers) {
                    cabinetSizeNumber.setSeaOrderId(seaOrder.getId());
                    cabinetSizeNumber.setSeaOrderNo(seaOrder.getOrderNo());
                    cabinetSizeNumber.setCreateTime(LocalDateTime.now());
                    cabinetSizeNumber.setCreateUser(UserOperator.getToken());
                    boolean save = cabinetSizeNumberService.saveOrUpdate(cabinetSizeNumber);
                    if (!save) {
                        log.error("柜型数量添加失败");
                    }
                }
            }
        }


        //获取用户地址
        List<AddOrderAddressForm> orderAddressForms = addSeaOrderForm.getOrderAddressForms();
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
        return seaOrder.getOrderNo();
    }

    /**
     * 生成订单号
     */
    @Override
    public String generationOrderNo(Long legalId, Integer integer) {
        //生成订单号
//        String orderNo = StringUtils.loadNum(CommonConstant.S, 12);
//        while (true) {
//            if (isExistOrder(orderNo)) {//重复
//                orderNo = StringUtils.loadNum(CommonConstant.S, 12);
//            } else {
//                break;
//            }
//        }
        String legalCode = (String) oauthClient.getLegalEntityCodeByLegalId(legalId).getData();
        String preOrder = null;
        String classCode = null;
        if (integer.equals("1")) {
            preOrder = OrderTypeEnum.SI.getCode() + legalCode;
            classCode = OrderTypeEnum.SI.getCode();
        } else {
            preOrder = OrderTypeEnum.SE.getCode() + legalCode;
            classCode = OrderTypeEnum.SE.getCode();
        }
        String orderNo = (String) omsClient.getOrderNo(preOrder, classCode).getData();
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
     *
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
        ApiResult<List<GoodsVO>> result = this.omsClient.getGoodsByBusOrders(Collections.singletonList(seaOrderVO.getOrderNo()), businessType);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("查询商品信息失败 airOrderId={}");
        }
        seaOrderVO.setGoodsForms(result.getData());
        //查询地址信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusOrders(Collections.singletonList(seaOrderVO.getOrderNo()), businessType);
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 airOrderId={}");
        }
        //处理地址信息
        for (OrderAddressVO address : resultOne.getData()) {
            address.getFile(prePath);
            seaOrderVO.processingAddress(address);
        }
        //获取柜型数量
        if(seaOrderVO.getCabinetType()!=null){
            if (seaOrderVO.getCabinetType().equals(1)) {
                List<CabinetSizeNumberVO> cabinetSizeNumberVOS = cabinetSizeNumberService.getList(seaOrderVO.getOrderId());
                seaOrderVO.setCabinetSizeNumbers(cabinetSizeNumberVOS);
            }
        }


        //查询订船信息
        SeaBookship seaBookship = this.seaBookshipService.getEnableBySeaOrderId(id);
        SeaBookshipVO seaBookshipVO = ConvertUtil.convert(seaBookship, SeaBookshipVO.class);
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
        return this.baseMapper.findByPage(page, form, legalIds);
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
        //this.updateProcessStatusComplte(form);

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
//    public void updateProcessStatusComplte(SeaProcessOptForm from){
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("order_no",from.getOrderNo());
//        queryWrapper.eq("status",from.getStatus());
//        OrderFlowSheet one = orderFlowSheetService.getOne(queryWrapper);
//        one.setComplete("1");
//        boolean b = orderFlowSheetService.saveOrUpdate(one);
//
//    }

    /**
     * 海运流程操作记录
     */
    @Override
    public void seaProcessOptRecord(SeaProcessOptForm form) {
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.SEA_ORDER);
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditUser(UserOperator.getToken());
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
        if (OrderStatusEnum.SEA_S_9.getCode().equals(seaOrder.getStatus())) {
            //查询海运订单信息
            SeaOrder tmp = this.getById(seaOrder.getId());
            List<Terms> terms = termsService.list();

            for (Terms term : terms) {
                if (term.getName().equals("FOB") || term.getName().equals("CIF") || term.getName().equals("DAP") || term.getName().equals("FCA")) {
                    if (term.getId().equals(tmp.getTerms())) {
                        SeaOrder seaOrder1 = new SeaOrder();
                        seaOrder1.setId(tmp.getId());
                        seaOrder1.setProcessStatus(1);
                        this.updateById(seaOrder1);
                        return;
                    }
                }
            }
        }

        if (OrderStatusEnum.SEA_S_11.getCode().equals(seaOrder.getStatus())) {
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
//        this.updateProcessStatusComplte(form);
        updateProcessStatus(new SeaOrder(), form);
    }

    //生成补料单号
    public String getBLOrderNo(String seaOrderNo, Integer type, Integer size, Integer i) {
        String order = null;
        if (type == 1) {
            order = seaOrderNo + "BL" + "101";
        }
        if (type == 2) {
            order = seaOrderNo + "BL" + size + "0" + i;
        }
        return order;
    }

    @Override
    public void updateOrSaveProcessStatus(SeaProcessOptForm form) {

        //删除补料信息
        seaReplenishmentService.deleteSeaReplenishment(form.getOrderId(),form.getOrderNo());
        List<SeaReplenishment> list = seaReplenishmentService.getList(form.getOrderId(),form.getOrderNo());
        List<String> orderNo = new ArrayList<>();
        for (SeaReplenishment replenishment : list) {
            orderNo.add(replenishment.getOrderNo());
        }
        omsClient.deleteOrderAddressByBusOrders(orderNo,BusinessTypeEnum.HY.getCode());
        omsClient.deleteGoodsByBusOrders(orderNo,BusinessTypeEnum.HY.getCode());

        if (form.getType().equals(1)) {//合并，多个订单合并成一个补料
            List<AddSeaOrderForm> seaOrderForms = form.getSeaOrderForms();
            List<AddSeaReplenishment> seaReplenishments = form.getSeaReplenishments();
            StringBuffer stringBuffer = new StringBuffer();
            String blOrderNo = getBLOrderNo(form.getOrderNo(), form.getType(), seaReplenishments.size(), 1);
            for (AddSeaOrderForm seaOrderForm : seaOrderForms) {

                stringBuffer.append(seaOrderForm.getOrderNo()).append(",");

                SeaOrder seaOrder = new SeaOrder();
                seaOrder.setId(seaOrderForm.getId());
                seaOrder.setUpdateTime(LocalDateTime.now());
                seaOrder.setUpdateUser(UserOperator.getToken());
                seaOrder.setStatus(form.getStatus());

                //更新状态节点状态
                this.baseMapper.updateById(seaOrder);
                //节点操作记录
                this.seaProcessOptRecord(form);

                //完成订单状态
                finishSeaOrderOpt(seaOrder);
            }
            SeaReplenishment replenishment = ConvertUtil.convert(seaReplenishments.get(0), SeaReplenishment.class);
            replenishment.setSeaOrderId(form.getOrderId());
            replenishment.setSeaOrderNo(stringBuffer.toString().substring(0,stringBuffer.length()-1));
            replenishment.setOrderNo(blOrderNo);
            replenishment.setIsBillOfLading(0);
            replenishment.setIsReleaseOrder(0);
            boolean save = seaReplenishmentService.save(replenishment);
            if (!save) {
                log.warn("合并补料信息添加失败");
            }

            //增加或修改货柜信息
            if (replenishment.getCabinetType().equals(1)) {
                //修改或保存
                List<SeaContainerInformation> seaContainerInformations = seaReplenishments.get(0).getSeaContainerInformations();
                for (SeaContainerInformation seaContainerInformation : seaContainerInformations) {
                    SeaContainerInformation convert = ConvertUtil.convert(seaContainerInformation, SeaContainerInformation.class);
                    convert.setSeaRepId(replenishment.getId());
                    convert.setSeaRepNo(replenishment.getOrderNo());
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    boolean b = seaContainerInformationService.saveOrUpdate(convert);
                    if (!b) {
                        log.warn("合并货柜信息添加失败");
                    }
                }

            }

            //获取用户地址
            seaReplenishments.get(0).assemblyAddress();
            List<AddOrderAddressForm> orderAddressForms = seaReplenishments.get(0).getOrderAddressForms();
            //System.out.println("orderAddressForms=================================="+orderAddressForms);
            for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
                if(form.getAuditOpinion() == null){
                    orderAddressForm.setId(null);
                }
                orderAddressForm.setOrderNo(replenishment.getOrderNo());
                orderAddressForm.setBusinessType(BusinessTypeEnum.HY.getCode());
                orderAddressForm.setBusinessId(replenishment.getId());
                orderAddressForm.setCreateTime(LocalDateTime.now());
                orderAddressForm.setFileName(StringUtils.getFileNameStr(orderAddressForm.getTakeFiles()));
                orderAddressForm.setFilePath(StringUtils.getFileStr(orderAddressForm.getTakeFiles()));
            }
            //批量保存用户地址
            ApiResult result = this.omsClient.saveOrUpdateOrderAddressBatch(orderAddressForms);
            if (result.getCode() != HttpStatus.SC_OK) {
                log.warn("批量保存/修改订单地址信息失败,订单地址信息={}", new JSONArray(orderAddressForms));
            }

            List<AddGoodsForm> goodsForms = seaReplenishments.get(0).getGoodsForms();
            for (AddGoodsForm goodsForm : goodsForms) {
                if(form.getAuditOpinion() == null){
                    goodsForm.setId(null);
                }
                goodsForm.setOrderNo(replenishment.getOrderNo());
                goodsForm.setBusinessId(replenishment.getId());
                goodsForm.setBusinessType(BusinessTypeEnum.HY.getCode());
            }
            //批量保存货物信息
            result = this.omsClient.saveOrUpdateGoodsBatch(goodsForms);
            if (result.getCode() != HttpStatus.SC_OK) {
                log.warn("批量保存/修改商品信息失败,商品信息={}", new JSONArray(goodsForms));
            }
        }
        if (form.getType().equals(2)) {//分单，一个订单多个补料，订单号只有一个，补料信息有多个
            List<AddSeaOrderForm> seaOrderForms = form.getSeaOrderForms();
            List<AddSeaReplenishment> seaReplenishments = form.getSeaReplenishments();
            int count = 0;
            for (int i = 0; i < seaReplenishments.size(); i++) {
                SeaReplenishment replenishment = ConvertUtil.convert(seaReplenishments.get(i), SeaReplenishment.class);
                replenishment.setSeaOrderId(seaOrderForms.get(0).getId());
                replenishment.setSeaOrderNo(seaOrderForms.get(0).getOrderNo());
                replenishment.setOrderNo(getBLOrderNo(seaOrderForms.get(0).getOrderNo(), form.getType(), seaReplenishments.size(), i + 1));
                replenishment.setIsBillOfLading(0);
                replenishment.setIsReleaseOrder(0);
                boolean save = seaReplenishmentService.save(replenishment);
                if (!save) {
                    log.warn("分单补料信息添加失败");
                }

                //获取柜型数量
//                if (seaOrderForms.get(0).getCabinetType().equals(1)) {
//                    //保存或修改
//                    List<CabinetSizeNumber> cabinetSizeNumbers = seaOrderForms.get(0).getCabinetSizeNumbers();
//                    for (CabinetSizeNumber cabinetSizeNumber : cabinetSizeNumbers) {
//                        if(form.getAuditOpinion() == null){
//                            cabinetSizeNumber.setId(null);
//                        }
//                        cabinetSizeNumber.setSeaOrderId(replenishment.getId());
//                        cabinetSizeNumber.setSeaOrderNo(replenishment.getOrderNo());
//                        cabinetSizeNumber.setCreateTime(LocalDateTime.now());
//                        cabinetSizeNumber.setCreateUser(UserOperator.getToken());
//                        boolean b = cabinetSizeNumberService.saveOrUpdate(cabinetSizeNumber);
//                        if (!b) {
//                            log.warn("分单柜型信息添加失败");
//                        }
//                    }
//                }

                //获取货物信息数据,增加或修改货柜信息
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("sea_rep_id",replenishment.getId());
                queryWrapper.eq("sea_rep_no",replenishment.getOrderNo());
                seaContainerInformationService.remove(queryWrapper);
                if (replenishment.getCabinetType().equals(1)) {
                    //修改或保存
                    List<SeaContainerInformation> seaContainerInformations = seaReplenishments.get(i).getSeaContainerInformations();
                    for (SeaContainerInformation seaContainerInformation : seaContainerInformations) {
                        seaContainerInformation.setSeaRepId(replenishment.getId());
                        seaContainerInformation.setSeaRepNo(replenishment.getOrderNo());
                        seaContainerInformation.setCreateTime(LocalDateTime.now());
                        seaContainerInformation.setCreateUser(UserOperator.getToken());
                        boolean b = seaContainerInformationService.saveOrUpdate(seaContainerInformation);
                        if (!b) {
                            log.warn("分单货柜信息添加失败");
                        }
                    }

                }

                seaReplenishments.get(i).assemblyAddress();
                //获取用户地址
                List<AddOrderAddressForm> orderAddressForms = seaReplenishments.get(i).getOrderAddressForms();
                for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
                    if(form.getAuditOpinion() == null){
                        orderAddressForm.setId(null);
                    }
                    orderAddressForm.setOrderNo(replenishment.getOrderNo());
                    orderAddressForm.setBusinessType(BusinessTypeEnum.HY.getCode());
                    orderAddressForm.setBusinessId(replenishment.getId());
                    orderAddressForm.setCreateTime(LocalDateTime.now());
                    orderAddressForm.setFileName(StringUtils.getFileNameStr(orderAddressForm.getTakeFiles()));
                    orderAddressForm.setFilePath(StringUtils.getFileStr(orderAddressForm.getTakeFiles()));
                }
                //批量保存用户地址
                ApiResult result = this.omsClient.saveOrUpdateOrderAddressBatch(orderAddressForms);
                if (result.getCode() != HttpStatus.SC_OK) {
                    log.warn("批量保存/修改订单地址信息失败,订单地址信息={}", new JSONArray(orderAddressForms));
                }

                List<AddGoodsForm> goodsForms = seaReplenishments.get(i).getGoodsForms();
                for (AddGoodsForm goodsForm : goodsForms) {
                    if(form.getAuditOpinion() == null){
                        goodsForm.setId(null);
                    }
                    goodsForm.setOrderNo(replenishment.getOrderNo());
                    goodsForm.setBusinessId(replenishment.getId());
                    goodsForm.setBusinessType(BusinessTypeEnum.HY.getCode());
                }
                //批量保存货物信息
                result = this.omsClient.saveOrUpdateGoodsBatch(goodsForms);
                if (result.getCode() != HttpStatus.SC_OK) {
                    log.warn("批量保存/修改商品信息失败,商品信息={}", new JSONArray(goodsForms));
                }
                count++;
            }
            if (count == seaReplenishments.size()) {
                AddSeaOrderForm convert = ConvertUtil.convert(seaOrderForms.get(0), AddSeaOrderForm.class);
                convert.assemblyAddress();
                SeaOrder seaOrder = new SeaOrder();
                seaOrder.setId(seaOrderForms.get(0).getId());
                seaOrder.setUpdateTime(LocalDateTime.now());
                seaOrder.setUpdateUser(UserOperator.getToken());
                seaOrder.setStatus(form.getStatus());

                //更新状态节点状态
                this.baseMapper.updateById(seaOrder);
                //节点操作记录
                this.seaProcessOptRecord(form);

                //完成订单状态
                finishSeaOrderOpt(seaOrder);
            } else {
                log.warn("提交补料操作失败", count);
            }
        }


    }

    /**
     * 获取订单详情
     *
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
        ApiResult<List<GoodsVO>> result = this.omsClient.getGoodsByBusOrders(Collections.singletonList(seaOrder.getOrderNo()), businessType);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("查询商品信息失败 seaOrderId={}", seaOrderId);
        }
        seaOrder.setGoodsForms(result.getData());
        //查询地址信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusOrders(Collections.singletonList(seaOrder.getOrderNo()), businessType);
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 seaOrderId={}", seaOrderId);
        }
        //处理地址信息
        for (OrderAddressVO address : resultOne.getData()) {
            address.getFile(prePath);
            seaOrder.processingAddress(address);
        }
        seaOrder.setOrderAddressForms(resultOne.getData());

        List<FileView> attachments = (List<FileView>) this.omsClient.getAttachments(seaOrder.getOrderId()).getData();
        seaOrder.setAllPics(attachments);

        //获取柜型数量
        if (seaOrder.getCabinetType().equals(1)) {
            List<CabinetSizeNumberVO> cabinetSizeNumberVOS = cabinetSizeNumberService.getList(seaOrder.getOrderId());
            seaOrder.setCabinetSizeNumbers(cabinetSizeNumberVOS);
        }

        //查询订舱信息
        SeaBookship seaBookship = this.seaBookshipService.getEnableBySeaOrderId(seaOrderId);
        SeaBookshipVO convert = ConvertUtil.convert(seaBookship, SeaBookshipVO.class);
        convert.getFile(prePath);
        seaOrder.setSeaBookshipVO(convert);

//        System.out.println("status========================"+seaOrder.getStatus());
        if (seaOrder.getStatus() != OrderStatusEnum.SEA_S_0.getCode() || seaOrder.getStatus() != OrderStatusEnum.SEA_S_1.getCode()) {
            CommonResult<List<InitComboxVO>> initSupplierInfo = omsClient.initSupplierInfo();
            List<InitComboxVO> data = initSupplierInfo.getData();
            for (InitComboxVO datum : data) {
                if (datum.getId().equals(seaOrder.getSeaBookshipVO().getAgentSupplierId())) {
                    seaOrder.setSupplierName(datum.getName());
                }
            }
        }
        //获取截补料数据
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("sea_order_no", seaOrder.getOrderNo());
        List<SeaReplenishment> list = seaReplenishmentService.list(queryWrapper);
        List<SeaReplenishmentVO> seaReplenishmentVOS = ConvertUtil.convertList(list, SeaReplenishmentVO.class);
        for (SeaReplenishmentVO seaReplenishmentVO : seaReplenishmentVOS) {
            //获取截补料中的柜型数量以及货柜信息
            List<CabinetSizeNumberVO> list1 = cabinetSizeNumberService.getList(seaReplenishmentVO.getId());
            seaReplenishmentVO.setCabinetSizeNumbers(list1);
            List<SeaContainerInformationVO> seaContainerInformations = seaContainerInformationService.getList(seaReplenishmentVO.getId());
            seaReplenishmentVO.setSeaContainerInformations(seaContainerInformations);

            //查询商品信息
            ApiResult<List<GoodsVO>> result1 = this.omsClient.getGoodsByBusOrders(Collections.singletonList(seaReplenishmentVO.getOrderNo()), businessType);
            if (result1.getCode() != HttpStatus.SC_OK) {
                log.warn("查询商品信息失败 seaOrderId={}", seaReplenishmentVO.getId());
            }
            seaReplenishmentVO.setGoodsForms(result1.getData());
            //查询地址信息
            ApiResult<List<OrderAddressVO>> resultOne1 = this.omsClient.getOrderAddressByBusOrders(Collections.singletonList(seaReplenishmentVO.getOrderNo()), businessType);
            if (resultOne1.getCode() != HttpStatus.SC_OK) {
                log.warn("查询订单地址信息失败 seaOrderId={}", seaReplenishmentVO.getId());
            }
            //处理地址信息
            for (OrderAddressVO address : resultOne1.getData()) {
                address.getFile(prePath);
                seaReplenishmentVO.processingAddress(address);
            }
        }
        seaOrder.setSeaReplenishments(seaReplenishmentVOS);
        return seaOrder;
    }

    //驳回
    @Override
    public void orderReceiving(SeaOrder seaOrder, AuditInfoForm auditInfoForm, SeaCargoRejected seaCargoRejected) {
        SeaOrder tmp = new SeaOrder();
        tmp.setId(seaOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());

        omsClient.saveAuditInfo(auditInfoForm);

        //执行主订单驳回标识
        omsClient.doMainOrderRejectionSignOpt(seaOrder.getMainOrderNo(),
                seaOrder.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");
        this.updateById(tmp);
    }

    //驳回编辑
    @Override
    public void rejectedOpt(SeaOrder seaOrder, AuditInfoForm auditInfoForm, SeaCargoRejected seaCargoRejected) {

        SeaOrder tmp = new SeaOrder();
        tmp.setId(seaOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());
        //根据选择是否订船驳回
        ApiResult result = new ApiResult();
        //删除物流轨迹表订船数据
        switch (seaCargoRejected.getRejectOptions()) {
            case 1://订单驳回
                result = omsClient.deleteLogisticsTrackByType(seaOrder.getId(), BusinessTypeEnum.HY.getCode());
                //删除订船数据
                SeaBookship seaBookship = new SeaBookship();
                seaBookship.setStatus(SeaBookShipStatusEnum.DELETE.getCode());
                this.seaBookshipService.updateBySeaOrderId(seaOrder.getId(), seaBookship);

                //执行主订单驳回标识
                omsClient.doMainOrderRejectionSignOpt(seaOrder.getMainOrderNo(),
                        seaOrder.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");
                break;
            case 2://订船驳回
                DelOprStatusForm form = new DelOprStatusForm();
                form.setOrderId(seaOrder.getId());
                form.setStatus(Collections.singletonList(OrderStatusEnum.SEA_S_2.getCode()));
                result = this.omsClient.delSpecOprStatus(form);
                tmp.setStatus(OrderStatusEnum.SEA_S_3_2.getCode());
        }

        if (result.getCode() != HttpStatus.SC_OK) {
            log.error("远程调用删除订船轨迹失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
            throw new JayudBizException(ResultEnum.OPR_FAIL);
        }

        //更改为驳回状态
        this.updateById(tmp);
    }

    //根据主订单号集合获取海运订单信息
    @Override
    public List<SeaOrder> getSeaOrderByOrderNOs(List<String> mainOrderNoList) {
        QueryWrapper<SeaOrder> condition = new QueryWrapper<>();
        condition.lambda().in(SeaOrder::getMainOrderNo, mainOrderNoList);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 补料审核流程
     *
     * @param form
     */
    @Override
    public void updateOrSaveReplenishmentAudit(SeaProcessOptForm form) {
        SeaOrder seaOrder = new SeaOrder();
        seaOrder.setAuditStatus(form.getAuditStatus());
        seaOrder.setAuditOpinion(form.getAuditOpinion());
        String orderNo = form.getSeaReplenishments().get(0).getSeaOrderNo();
        String[] orderNoes = orderNo.split(",");
        for (String orderNoe : orderNoes) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_no",orderNoe);
            SeaOrder seaOrder1 = this.baseMapper.selectOne(queryWrapper);
            form.setOrderNo(seaOrder1.getOrderNo());
            form.setOrderId(seaOrder1.getId());
            form.setMainOrderNo(seaOrder1.getMainOrderNo());
            //根据订单号获取订单id
            Long mainOrderId = omsClient.getMainOrderByOrderNo(seaOrder1.getMainOrderNo()).getData();
            form.setMainOrderId(mainOrderId);

            if (form.getAuditStatus().equals(1)) {//审核通过
                updateProcessStatus(new SeaOrder(), form);
            }
            if (form.getAuditStatus().equals(2)) {
                DelOprStatusForm delOprStatusForm = new DelOprStatusForm();
                delOprStatusForm.setOrderId(form.getOrderId());
                delOprStatusForm.setStatus(Collections.singletonList(OrderStatusEnum.SEA_S_4.getCode()));
                ApiResult result = this.omsClient.delSpecOprStatus(delOprStatusForm);
                if (result.getCode() != HttpStatus.SC_OK) {
                    log.error("远程调用删除补料轨迹失败");
                    throw new JayudBizException(ResultEnum.OPR_FAIL);
                }

                seaOrder.setId(form.getOrderId());
                seaOrder.setUpdateTime(LocalDateTime.now());
                seaOrder.setUpdateUser(UserOperator.getToken());
                seaOrder.setStatus(OrderStatusEnum.SEA_S_3.getCode());
                form.setStatus(OrderStatusEnum.SEA_S_5_1.getCode());

                //更新状态节点状态
                this.baseMapper.updateById(seaOrder);
                //节点操作记录
                this.seaProcessOptRecord(form);
            }
        }


    }

    @Override
    public void updateOrSaveConfirmationAudit(SeaProcessOptForm form) {

        if(form.getStatus().equals(OrderStatusEnum.SEA_S_6.getCode())){
            AddSeaReplenishment seaReplenishment = form.getSeaReplenishment();
            SeaReplenishment convert = ConvertUtil.convert(seaReplenishment, SeaReplenishment.class);
            convert.setIsBillOfLading(1);
            boolean save = seaReplenishmentService.saveOrUpdate(convert);
            if (!save) {
                log.error("操作失败");
                throw new JayudBizException(ResultEnum.OPR_FAIL);
            }
            String orderNo = seaReplenishment.getSeaOrderNo();
            String[] orderNoes = orderNo.split(",");
            for (String orderNoe : orderNoes) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("order_no", orderNoe);
                SeaOrder seaOrder1 = this.baseMapper.selectOne(queryWrapper);
                form.setOrderNo(seaOrder1.getOrderNo());
                form.setOrderId(seaOrder1.getId());
                form.setMainOrderNo(seaOrder1.getMainOrderNo());
                //根据订单号获取订单id
                Long mainOrderId = omsClient.getMainOrderByOrderNo(seaOrder1.getMainOrderNo()).getData();
                form.setMainOrderId(mainOrderId);
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.like("sea_order_no",seaReplenishment.getSeaOrderNo());
                int count = this.seaReplenishmentService.count(queryWrapper1);
                queryWrapper1.eq("is_bill_of_lading",1);
                int count1 = this.seaReplenishmentService.count(queryWrapper1);
                if(count==count1){ //该订单所有补料单都已提单
                    updateProcessStatus(new SeaOrder(),form);
                }else{
                    //该订单还有补料单未提交，无法到确认装船流程
                    //存储流程节点
                    AuditInfoForm auditInfoForm = new AuditInfoForm();
                    auditInfoForm.setExtId(seaReplenishment.getId());
                    auditInfoForm.setExtDesc(SqlConstant.SEA_REPLENISHMENT);
                    auditInfoForm.setAuditComment(form.getDescription());
                    auditInfoForm.setAuditUser(UserOperator.getToken());
                    auditInfoForm.setFileViews(form.getFileViewList());
                    auditInfoForm.setAuditStatus(form.getStatus());
                    auditInfoForm.setAuditTypeDesc(form.getStatusName());

                    //文件拼接
                    form.setOrderId(seaReplenishment.getId());
                    form.setOrderNo(seaReplenishment.getOrderNo());
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
            }

        }
        if(form.getStatus().equals(OrderStatusEnum.SEA_S_8.getCode())){
            AddSeaReplenishment seaReplenishment = form.getSeaReplenishment();
            SeaReplenishment convert = ConvertUtil.convert(seaReplenishment, SeaReplenishment.class);
            convert.setIsReleaseOrder(1);
            boolean save = seaReplenishmentService.saveOrUpdate(convert);
            if (!save) {
                log.error("操作失败");
                throw new JayudBizException(ResultEnum.OPR_FAIL);
            }
            String orderNo = seaReplenishment.getSeaOrderNo();
            String[] orderNoes = orderNo.split(",");
            for (String orderNoe : orderNoes) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("order_no", orderNoe);
                SeaOrder seaOrder1 = this.baseMapper.selectOne(queryWrapper);
                form.setOrderNo(seaOrder1.getOrderNo());
                form.setOrderId(seaOrder1.getId());
                form.setMainOrderNo(seaOrder1.getMainOrderNo());
                //根据订单号获取订单id
                Long mainOrderId = omsClient.getMainOrderByOrderNo(seaOrder1.getMainOrderNo()).getData();
                form.setMainOrderId(mainOrderId);
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.like("sea_order_no",seaReplenishment.getSeaOrderNo());
                int count = this.seaReplenishmentService.count(queryWrapper1);
                queryWrapper1.eq("is_release_order",1);
                int count1 = this.seaReplenishmentService.count(queryWrapper1);
                if(count==count1){ //该订单所有补料单都已提单
                    updateProcessStatus(new SeaOrder(),form);
                }else{
                    //存储流程节点
                    AuditInfoForm auditInfoForm = new AuditInfoForm();
                    auditInfoForm.setExtId(seaReplenishment.getId());
                    auditInfoForm.setExtDesc(SqlConstant.SEA_REPLENISHMENT);
                    auditInfoForm.setAuditComment(form.getDescription());
                    auditInfoForm.setAuditUser(UserOperator.getToken());
                    auditInfoForm.setFileViews(form.getFileViewList());
                    auditInfoForm.setAuditStatus(form.getStatus());
                    auditInfoForm.setAuditTypeDesc(form.getStatusName());

                    //文件拼接
                    form.setOrderId(seaReplenishment.getId());
                    form.setOrderNo(seaReplenishment.getOrderNo());
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
            }

        }


    }

    @Override
    public Integer getNumByStatus(String status, List<Long> legalIds) {
        //获取当前用户所属法人主体
        Integer num = this.baseMapper.getNumByStatus(status, legalIds);
        return num == null ? 0 : num;
    }


//    private void handleLadingBillFile(SeaBookship seaBookship, SeaProcessOptForm form) {
//        seaBookship.setFilePath(StringUtils.getFileStr(form.getFileViewList()));
//        seaBookship.setFileName(StringUtils.getFileNameStr(form.getFileViewList()));
//    }
}
