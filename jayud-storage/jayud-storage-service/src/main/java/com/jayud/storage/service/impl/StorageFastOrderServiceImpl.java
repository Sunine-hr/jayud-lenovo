package com.jayud.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.FileClient;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.*;
import com.jayud.storage.mapper.StorageFastOrderMapper;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.IStorageFastOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.storage.service.IWarehouseGoodsService;
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
 * 快进快出订单表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-06-10
 */
@Service
public class StorageFastOrderServiceImpl extends ServiceImpl<StorageFastOrderMapper, StorageFastOrder> implements IStorageFastOrderService {

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private OmsClient omsClient;

    @Override
    public String createOrder(StorageFastOrderForm storageFastOrderForm) {
        //无论驳回还是草稿在提交，都先删除原来的商品信息
        if (storageFastOrderForm.getId() != null) {
            warehouseGoodsService.deleteWarehouseGoodsFormsByOrderId(storageFastOrderForm.getId());
        }
        //创建
        StorageFastOrder storageFastOrder = ConvertUtil.convert(storageFastOrderForm, StorageFastOrder.class);
        if (storageFastOrderForm.getIsKJKC() != null && storageFastOrderForm.getIsKJKC()) {
            storageFastOrder.setId(storageFastOrderForm.getId());
            storageFastOrder.setUpdateTime(LocalDateTime.now());
            storageFastOrder.setUpdateUser(UserOperator.getToken());
            boolean update = this.updateById(storageFastOrder);
            if (update) {
                log.warn(storageFastOrder.getMainOrderNo() + "仓储快进快出修改成功");
            } else {
                log.error(storageFastOrder.getMainOrderNo() + "仓储快进快出修改失败");
            }
        } else if (storageFastOrder.getId() == null) {
            storageFastOrder.setCreateTime(LocalDateTime.now());
            storageFastOrder.setCreateUser(UserOperator.getToken());
            storageFastOrder.setStatus("CCF_0");
            boolean save = this.save(storageFastOrder);
            if (save) {
                log.warn(storageFastOrder.getMainOrderNo() + "仓储快进快出单添加成功");
            } else {
                log.error(storageFastOrder.getMainOrderNo() + "仓储快进快出单添加失败");
            }
        } else {
            storageFastOrder.setId(storageFastOrderForm.getId());
            storageFastOrder.setUpdateTime(LocalDateTime.now());
            storageFastOrder.setUpdateUser(UserOperator.getToken());
            storageFastOrder.setStatus("CCF_0");
            boolean update = this.updateById(storageFastOrder);
            if (update) {
                log.warn(storageFastOrder.getMainOrderNo() + "仓储快进快出单修改成功");
            } else {
                log.error(storageFastOrder.getMainOrderNo() + "仓储快进快出单修改失败");
            }
        }
        String orderNo = storageFastOrder.getOrderNo();
        if (storageFastOrder.getIsWarehouse().equals(0)) {
            if (CollectionUtils.isNotEmpty(storageFastOrderForm.getFastGoodsFormList())) {
                List<WarehouseGoodsForm> goodsFormList = storageFastOrderForm.getFastGoodsFormList();
                List<WarehouseGoods> warehouseGoods = new ArrayList<>();
                for (WarehouseGoodsForm warehouseGood : goodsFormList) {

                    WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                    convert.setOrderId(storageFastOrder.getId());
                    convert.setOrderNo(storageFastOrder.getOrderNo());
                    convert.setType(3);
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                    convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                    warehouseGoods.add(convert);
                }
                warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
            }
        } else {
            if (CollectionUtils.isNotEmpty(storageFastOrderForm.getInGoodsFormList())) {
                List<WarehouseGoodsForm> goodsFormList = storageFastOrderForm.getInGoodsFormList();
                List<WarehouseGoods> warehouseGoods = new ArrayList<>();
                for (WarehouseGoodsForm warehouseGood : goodsFormList) {

                    WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                    convert.setOrderId(storageFastOrder.getId());
                    convert.setOrderNo(storageFastOrder.getOrderNo());
                    convert.setType(4);
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                    convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                    warehouseGoods.add(convert);
                }
                warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
            }
            if (CollectionUtils.isNotEmpty(storageFastOrderForm.getOutGoodsFormList())) {
                List<WarehouseGoodsForm> goodsFormList = storageFastOrderForm.getOutGoodsFormList();
                List<WarehouseGoods> warehouseGoods = new ArrayList<>();
                for (WarehouseGoodsForm warehouseGood : goodsFormList) {

                    WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                    convert.setOrderId(storageFastOrder.getId());
                    convert.setOrderNo(storageFastOrder.getOrderNo());
                    convert.setType(5);
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                    convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                    warehouseGoods.add(convert);
                }
                warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
            }
        }

        return orderNo;
    }

    @Override
    public StorageFastOrder getStorageFastOrderByMainOrderNO(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("main_order_no", orderNo);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public StorageFastOrderVO getStorageFastOrderVOById(Long id) {
        //获取附件地址
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        StorageFastOrder storageFastOrder = this.baseMapper.selectById(id);
        StorageFastOrderVO storageFastOrderVO = ConvertUtil.convert(storageFastOrder, StorageFastOrderVO.class);
        //获取商品信息
        if (storageFastOrder.getIsWarehouse().equals(0)) {
            List<WarehouseGoodsVO> warehouseGoods = warehouseGoodsService.getList(storageFastOrderVO.getId(), storageFastOrderVO.getOrderNo(), 3);
            if (CollectionUtils.isEmpty(warehouseGoods)) {
                warehouseGoods.add(new WarehouseGoodsVO());
                storageFastOrderVO.setTotalNumberStr("0板0件0pcs");
                storageFastOrderVO.setTotalWeightStr("0KG");

            } else {
                double totalWeight = 0.0;
                Integer borderNumber = 0;
                Integer number = 0;
                Integer pcs = 0;
                for (WarehouseGoodsVO warehouseGood : warehouseGoods) {
                    if (warehouseGood.getWeight() != null) {
                        totalWeight = totalWeight + warehouseGood.getWeight();
                    }
                    if (warehouseGood.getBoardNumber() != null) {
                        borderNumber = borderNumber + warehouseGood.getBoardNumber();
                    }
                    if (warehouseGood.getNumber() != null) {
                        number = number + warehouseGood.getNumber();
                    }
                    if (warehouseGood.getPcs() != null) {
                        pcs = pcs + warehouseGood.getPcs();
                    }
                    warehouseGood.setTakeFiles(StringUtils.getFileViews(warehouseGood.getFilePath(), warehouseGood.getFileName(), prePath));
                }
                storageFastOrderVO.setTotalNumberStr(borderNumber + "板" + number + "件" + pcs + "pcs");
                storageFastOrderVO.setTotalWeightStr(totalWeight + "KG");
            }
            storageFastOrderVO.setFastGoodsFormList(warehouseGoods);

        } else {
            List<WarehouseGoodsVO> warehouseGoods = warehouseGoodsService.getList(storageFastOrderVO.getId(), storageFastOrderVO.getOrderNo(), 3);
            if (CollectionUtils.isEmpty(warehouseGoods)) {
                warehouseGoods.add(new WarehouseGoodsVO());
                storageFastOrderVO.setTotalNumberStr("0板0件0pcs");
                storageFastOrderVO.setTotalWeightStr("0KG");

            } else {
                double totalWeight = 0.0;
                Integer borderNumber = 0;
                Integer number = 0;
                Integer pcs = 0;
                for (WarehouseGoodsVO warehouseGood : warehouseGoods) {
                    if (warehouseGood.getWeight() != null) {
                        totalWeight = totalWeight + warehouseGood.getWeight();
                    }
                    if (warehouseGood.getBoardNumber() != null) {
                        borderNumber = borderNumber + warehouseGood.getBoardNumber();
                    }
                    if (warehouseGood.getNumber() != null) {
                        number = number + warehouseGood.getNumber();
                    }
                    if (warehouseGood.getPcs() != null) {
                        pcs = pcs + warehouseGood.getPcs();
                    }
                    warehouseGood.setTakeFiles(StringUtils.getFileViews(warehouseGood.getFilePath(), warehouseGood.getFileName(), prePath));
                }
                storageFastOrderVO.setTotalNumberStr(borderNumber + "板" + number + "件" + pcs + "pcs");
                storageFastOrderVO.setTotalWeightStr(totalWeight + "KG");
            }
            storageFastOrderVO.setInGoodsFormList(warehouseGoods);

            List<WarehouseGoodsVO> warehouseGoodsVOS = warehouseGoodsService.getList(storageFastOrderVO.getId(), storageFastOrderVO.getOrderNo(), 4);
            if (CollectionUtils.isEmpty(warehouseGoodsVOS)) {
                warehouseGoodsVOS.add(new WarehouseGoodsVO());
            }
            //获取附件信息
            for (WarehouseGoodsVO warehouseGood : warehouseGoodsVOS) {
                warehouseGood.setTakeFiles(StringUtils.getFileViews(warehouseGood.getFilePath(), warehouseGood.getFileName(), prePath));
            }
            storageFastOrderVO.setOutGoodsFormList(warehouseGoodsVOS);
        }
        return storageFastOrderVO;
    }

    @Override
    public IPage<StorageFastOrderFormVO> findByPage(QueryStorageFastOrderForm form) {
        if (form.getProcessStatus() != null) {
            form.setProcessStatusList(Collections.singletonList(form.getProcessStatus()));
        } else {
            if (StringUtils.isEmpty(form.getStatus())) { //订单列表
                form.setProcessStatusList(Arrays.asList(ProcessStatusEnum.PROCESSING.getCode()
                        , ProcessStatusEnum.COMPLETE.getCode(), ProcessStatusEnum.CLOSE.getCode()));
            } else {
                form.setProcessStatusList(Collections.singletonList(ProcessStatusEnum.PROCESSING.getCode()));
            }

        }

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        Page<StorageOutOrderFormVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form, legalIds);
    }

    @Override
    public void orderReceiving(StorageFastOrder storageFastOrder, AuditInfoForm auditInfoForm, StorageOutCargoRejected storageOutCargoRejected) {
        StorageFastOrder tmp = new StorageFastOrder();
        tmp.setId(storageFastOrder.getId());
        tmp.setUpdateTime(LocalDateTime.now());
        tmp.setUpdateUser(UserOperator.getToken());
        tmp.setStatus(auditInfoForm.getAuditStatus());

        omsClient.saveAuditInfo(auditInfoForm);
        omsClient.doMainOrderRejectionSignOpt(storageFastOrder.getMainOrderNo(),
                storageFastOrder.getOrderNo() + "-" + auditInfoForm.getAuditComment() + ",");

        omsClient.saveAuditInfo(auditInfoForm);
        this.updateById(tmp);
        //订单驳回，释放锁定库存，增加库存信息
//        boolean result = stockService.changeInventory(storageFastOrder.getOrderNo(),storageFastOrder.getId());
//        if(!result){
//            log.warn("库存变更失败");
//        }
    }

    @Override
    public void ordersReceived(StorageFastProcessOptForm form) {
        form.setOperatorUser(UserOperator.getToken());
        form.setStatus("CCF_1");
        StorageFastOrder storageFastOrder = new StorageFastOrder();
        storageFastOrder.setId(form.getOrderId());
        storageFastOrder.setStatus(form.getStatus());
        storageFastOrder.setProcessStatus(1);
        storageFastOrder.setOrderTaker(form.getOperatorUser());
        storageFastOrder.setReceivingOrdersDate(LocalDateTime.now());
        storageFastOrder.setUpdateUser(UserOperator.getToken());
        storageFastOrder.setUpdateTime(LocalDateTime.now());
        this.baseMapper.updateById(storageFastOrder);
        this.storageProcessOptRecord(form);
    }

    /**
     * 快进快出流程操作记录
     */
    @Override
    public void storageProcessOptRecord(StorageFastProcessOptForm form) {
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getOrderId());
        auditInfoForm.setExtDesc(SqlConstant.STORAGE_FAST_ORDER);
        auditInfoForm.setAuditComment(form.getDescription());
        auditInfoForm.setAuditUser(UserOperator.getToken());
        auditInfoForm.setFileViews(form.getFileViewList());
        auditInfoForm.setAuditStatus(form.getStatus());
        auditInfoForm.setAuditTypeDesc(form.getStatusName());

        //文件拼接
        form.setStatusPic(StringUtils.getFileStr(form.getFileViewList()));
        form.setStatusPicName(StringUtils.getFileNameStr(form.getFileViewList()));
        form.setBusinessType(BusinessTypeEnum.IO.getCode());

        if (omsClient.saveOprStatus(form).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用物流轨迹失败");
        }
        if (omsClient.saveAuditInfo(auditInfoForm).getCode() != HttpStatus.SC_OK) {
            log.error("远程调用审核记录失败");
        }

    }

    @Override
    public void confirmReceipt(StorageFastProcessOptForm form) {
        form.setOperatorUser(UserOperator.getToken());
        StorageFastOrder storageFastOrder = new StorageFastOrder();
        storageFastOrder.setId(form.getOrderId());
        storageFastOrder.setStatus(form.getStatus());
        storageFastOrder.setOrderTaker(form.getOperatorUser());
        storageFastOrder.setReceivingOrdersDate(LocalDateTime.now());
        storageFastOrder.setUpdateUser(UserOperator.getToken());
        storageFastOrder.setUpdateTime(LocalDateTime.now());
        this.baseMapper.updateById(storageFastOrder);
        this.storageProcessOptRecord(form);
    }

    @Override
    public StorageFastOrder getStorageFastOrderByOrderNO(String orderNo) {
        QueryWrapper<StorageFastOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(StorageFastOrder::getOrderNo, orderNo);
        return this.getOne(condition);
    }

    @Override
    public String createOrUpdateOrder(StorageFastOrderForm storageFastOrderForm) {
        //无论驳回还是草稿在提交，都先删除原来的商品信息
        if (storageFastOrderForm.getId() != null) {
            warehouseGoodsService.deleteWarehouseGoodsFormsByOrderId(storageFastOrderForm.getId());
        }
        //创建
        StorageFastOrder storageFastOrder = ConvertUtil.convert(storageFastOrderForm, StorageFastOrder.class);

        storageFastOrder.setId(storageFastOrderForm.getId());
        storageFastOrder.setUpdateTime(LocalDateTime.now());
        storageFastOrder.setUpdateUser(UserOperator.getToken());
        boolean update = this.updateById(storageFastOrder);
        if (update) {
            log.warn(storageFastOrder.getMainOrderNo() + "仓储快进快出单修改成功");
        } else {
            log.error(storageFastOrder.getMainOrderNo() + "仓储快进快出单修改失败");
        }

        String orderNo = storageFastOrder.getOrderNo();
        if (storageFastOrder.getIsWarehouse().equals(0)) {
            if (CollectionUtils.isNotEmpty(storageFastOrderForm.getFastGoodsFormList())) {
                List<WarehouseGoodsForm> goodsFormList = storageFastOrderForm.getFastGoodsFormList();
                List<WarehouseGoods> warehouseGoods = new ArrayList<>();
                for (WarehouseGoodsForm warehouseGood : goodsFormList) {

                    WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                    convert.setOrderId(storageFastOrder.getId());
                    convert.setOrderNo(storageFastOrder.getOrderNo());
                    convert.setType(3);
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                    convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                    warehouseGoods.add(convert);
                }
                warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
            }
        } else {
            if (CollectionUtils.isNotEmpty(storageFastOrderForm.getInGoodsFormList())) {
                List<WarehouseGoodsForm> goodsFormList = storageFastOrderForm.getInGoodsFormList();
                List<WarehouseGoods> warehouseGoods = new ArrayList<>();
                for (WarehouseGoodsForm warehouseGood : goodsFormList) {

                    WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                    convert.setOrderId(storageFastOrder.getId());
                    convert.setOrderNo(storageFastOrder.getOrderNo());
                    convert.setType(4);
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                    convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                    warehouseGoods.add(convert);
                }
                warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
            }
            if (CollectionUtils.isNotEmpty(storageFastOrderForm.getOutGoodsFormList())) {
                List<WarehouseGoodsForm> goodsFormList = storageFastOrderForm.getOutGoodsFormList();
                List<WarehouseGoods> warehouseGoods = new ArrayList<>();
                for (WarehouseGoodsForm warehouseGood : goodsFormList) {

                    WarehouseGoods convert = ConvertUtil.convert(warehouseGood, WarehouseGoods.class);
                    convert.setOrderId(storageFastOrder.getId());
                    convert.setOrderNo(storageFastOrder.getOrderNo());
                    convert.setType(5);
                    convert.setCreateTime(LocalDateTime.now());
                    convert.setCreateUser(UserOperator.getToken());
                    convert.setFileName(StringUtils.getFileNameStr(warehouseGood.getTakeFiles()));
                    convert.setFilePath(StringUtils.getFileStr(warehouseGood.getTakeFiles()));
                    warehouseGoods.add(convert);
                }
                warehouseGoodsService.saveOrUpdateBatch(warehouseGoods);
            }
        }

        return orderNo;
    }

    @Override
    public List<StorageFastOrder> getByCondition(StorageFastOrder storageFastOrder) {
        QueryWrapper<StorageFastOrder> condition = new QueryWrapper<>(storageFastOrder);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据子订单号集合查询子订单
     *
     * @param orderNos
     * @return
     */
    @Override
    public List<StorageFastOrder> getOrdersByOrderNos(List<String> orderNos) {
        QueryWrapper<StorageFastOrder> condition = new QueryWrapper<>();
        condition.lambda().in(StorageFastOrder::getOrderNo, orderNos);
        return this.baseMapper.selectList(condition);
    }
}
