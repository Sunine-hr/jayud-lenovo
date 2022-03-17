package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.ShelfOrderMapper;
import com.jayud.wms.model.bo.QueryInventoryForm;
import com.jayud.wms.model.bo.QueryShelfOrderForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.enums.MaterialStatusEnum;
import com.jayud.wms.model.enums.ShelfOrderStatusEnum;
import com.jayud.wms.model.enums.ShelfOrderTaskStatusEnum;
import com.jayud.wms.model.po.LockReLocation;
import com.jayud.wms.model.po.Receipt;
import com.jayud.wms.model.po.ShelfOrder;
import com.jayud.wms.model.po.ShelfOrderTask;
import com.jayud.wms.model.vo.*;
import com.jayud.wms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 上架单 服务实现类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Service
public class ShelfOrderServiceImpl extends ServiceImpl<ShelfOrderMapper, ShelfOrder> implements IShelfOrderService {


    @Autowired
    private ShelfOrderMapper shelfOrderMapper;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private IShelfOrderTaskService shelfOrderTaskService;
    @Autowired
    private IWarehouseLocationService warehouseLocationService;
    @Autowired
    private IReceiptService receiptService;
    @Autowired
    private IWmsOutboundOrderInfoToMaterialService wmsOutboundOrderInfoToMaterialService;
    @Autowired
    private ILockReLocationService lockReLocationService;

    @Override
    public IPage<ShelfOrderVO> selectPage(QueryShelfOrderForm queryShelfOrderForm,
                                          Integer pageNo,
                                          Integer pageSize,
                                          HttpServletRequest req) {

        Page<QueryShelfOrderForm> page = new Page<QueryShelfOrderForm>(pageNo, pageSize);
        IPage<ShelfOrderVO> pageList = shelfOrderMapper.pageList(page, queryShelfOrderForm);
        return pageList;
    }

    @Override
    public List<ShelfOrderVO> selectList(QueryShelfOrderForm queryShelfOrderForm) {
        return shelfOrderMapper.list(queryShelfOrderForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShelfOrder saveOrUpdateShelfOrder(ShelfOrder shelfOrder) {
        Long id = shelfOrder.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            shelfOrder.setCreateBy(CurrentUserUtil.getUsername());
            shelfOrder.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<ShelfOrder> shelfOrderQueryWrapper = new QueryWrapper<>();
            //shelfOrderQueryWrapper.lambda().eq(ShelfOrder::getCode, shelfOrder.getCode());
            //shelfOrderQueryWrapper.lambda().eq(ShelfOrder::getIsDeleted, 0);
            //List<ShelfOrder> list = this.list(shelfOrderQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        } else {
            //修改 --> update 更新人、更新时间
            shelfOrder.setUpdateBy(CurrentUserUtil.getUsername());
            shelfOrder.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<ShelfOrder> shelfOrderQueryWrapper = new QueryWrapper<>();
            //shelfOrderQueryWrapper.lambda().ne(ShelfOrder::getId, id);
            //shelfOrderQueryWrapper.lambda().eq(ShelfOrder::getCode, shelfOrder.getCode());
            //shelfOrderQueryWrapper.lambda().eq(ShelfOrder::getIsDeleted, 0);
            //List<ShelfOrder> list = this.list(shelfOrderQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(shelfOrder);
        return shelfOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delShelfOrder(Long id) {
//        ShelfOrder shelfOrderOne = this.baseMapper.selectById(id);
//        if (ObjectUtil.isEmpty(shelfOrderOne)) {
//            throw new IllegalArgumentException("上架单不存在，无法删除");
//        }
        ShelfOrder shelfOrder = new ShelfOrder();
        //逻辑删除 -->update 修改人、修改时间、是否删除
        shelfOrder.setId(id);
        shelfOrder.setUpdateBy(CurrentUserUtil.getUsername());
        shelfOrder.setUpdateTime(new Date());
        shelfOrder.setIsDeleted(true);
        this.updateById(shelfOrder);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryShelfOrderForExcel(QueryShelfOrderForm queryShelfOrderForm,
                                                                       HttpServletRequest req) {
        return this.baseMapper.queryShelfOrderForExcel(queryShelfOrderForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shelfOperation(List<ShelfOrder> shelfOrders) {
        shelfOrders.forEach(e -> {
            if (ShelfOrderStatusEnum.TWO.getCode().equals(e.getStatus())) {
                throw new ServiceException("物料:" + e.getMaterialCode() + " 容器号:" + e.getContainerNum() + " 已经上架无法执行上架操作");
            }
        });
        String shelfNum = authClient.getOrder("shelf_order_task_shelf", new Date()).getResult();
        Set<Long> orderIds = shelfOrders.stream().map(e -> e.getOrderId()).collect(Collectors.toSet());
        Set<Long> warehouseIds = new HashSet<>();
        Map<Long, Receipt> receiptMap = this.receiptService.listByIds(orderIds).stream().map(e -> {
            warehouseIds.add(e.getWarehouseId());
            return e;
        }).collect(Collectors.toMap(e -> e.getId(), e -> e));

        //重组数据
        Map<Long, List<ShelfOrder>> shelfOrderData = this.reorganizeShelfData(shelfOrders, receiptMap);
        List<ShelfOrder> shelfOrderList = new ArrayList<>();
        List<ShelfOrderTask> list = new ArrayList<>();
        Date date = new Date();
        Map<String, LockReLocation> lockReLocationMap = lockReLocationService.selectList(new LockReLocation().setIsDeleted(false)).stream().collect(Collectors.toMap(e -> e.getWarehouseId() + "-" + e.getWarehouseAreaId() + "-" + e.getCode(), e -> e));

        shelfOrderData.forEach((k, v) -> {
            //获取一组空闲库位
            //TODO 远程调用
            List<WarehouseLocationVO> warehouseLocations = this.warehouseLocationService.queryWarehouseLocation(new QueryInventoryForm().setWarehouseId(k)
//                    .setLimit(v.size() * 2 + lockReLocationMap.keySet().size())
                    ).stream()
                    .filter(e -> lockReLocationMap.get(e.getWarehouseId() + "-" + e.getWarehouseAreaId() + "-" + e.getCode()) == null)
                    .collect(Collectors.toList());
            v.forEach(e -> {
                ShelfOrderTask shelfOrderTask = new ShelfOrderTask();
                Receipt receipt = receiptMap.get(e.getOrderId());
                String recommendedLocation = this.shelfOrderTaskService.doRecommendedLocation(e.getMaterialCode(), k, warehouseLocations);
                if (recommendedLocation == null) {
                    throw new ServiceException("暂无推荐库位");
                }
                shelfOrderTask.setRecommendedLocation(recommendedLocation);
//                if (!CollectionUtil.isEmpty(warehouseLocations)) {
//                    WarehouseLocationVO warehouseLocationVO = warehouseLocations.remove(0);
//                    shelfOrderTask.setRecommendedLocation(warehouseLocationVO.getCode());
//                }
//                BigDecimal account = wmsOutboundOrderInfoToMaterialService.getDistributionAccount(e.getMaterialCode(), e.getUnit(), new BigDecimal(e.getNum()));
                shelfOrderTask.setReceiptNoticeNum(receipt.getReceiptNoticeNum()).setShelfOrderId(e.getId()).setShelfNum(shelfNum)
                        .setTaskDetailNum(authClient.getOrder("shelf_order_task_detail", new Date()).getResult())
                        .setOrderId(e.getOrderId()).setOrderNum(e.getOrderNum())
                        .setMaterialId(e.getMaterialId()).setMaterialCode(e.getMaterialCode())
                        .setBatchNum(e.getBatchNum())
                        .setProductionDate(e.getProductionDate())
                        .setColumnOne(e.getColumnOne())
                        .setColumnTwo(e.getColumnTwo())
                        .setColumnThree(e.getColumnThree())
//                        .setUnit("EA").setNum(account.doubleValue())
                        .setUnit(e.getUnit()).setNum(e.getNum()).setActualNum(0.0).setContainerNum(e.getContainerNum())
                        .setStartSeedingPosition(e.getSeedingPositionNum()).setIsDeleted(false).setStatus(ShelfOrderTaskStatusEnum.ONE.getCode())
                        .setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(new Date());
                list.add(shelfOrderTask);

                ShelfOrder shelfOrder = new ShelfOrder();
                shelfOrder.setReceiptNoticeNum(receipt.getReceiptNoticeNum()).setRecommendedLocation(shelfOrderTask.getRecommendedLocation()).setShelfNum(shelfOrderTask.getShelfNum()).setTaskDetailNum(shelfOrderTask.getTaskDetailNum()).setStatus(ShelfOrderStatusEnum.TWO.getCode()).setId(e.getId())
                        .setUpdateTime(date).setUpdateBy(CurrentUserUtil.getUsername());
                shelfOrderList.add(shelfOrder);
            });
        });

//        shelfOrders.forEach(e -> {
//            ShelfOrderTask shelfOrderTask = new ShelfOrderTask();
//            Receipt receipt = receiptMap.get(e.getOrderId());
//
//            if (!CollectionUtil.isEmpty(warehouseLocations)) {
//                WarehouseLocationVO warehouseLocationVO = warehouseLocations.remove(0);
//                shelfOrderTask.setActualShelfSpace(warehouseLocationVO.getCode());
//            }
//
//            shelfOrderTask.setReceiptNoticeNum(receipt.getReceiptNoticeNum()).setShelfOrderId(e.getId()).setShelfNum(shelfNum)
//                    .setTaskDetailNum(bNoRuleClient.getOrder("shelf_order_task_detail", LocalDateTime.now()).getResult())
//                    .setOrderId(e.getOrderId()).setOrderNum(e.getOrderNum()).setReceiptNoticeNum(e.getReceiptNoticeNum())
//                    .setMaterialId(e.getMaterialId()).setMaterialCode(e.getMaterialCode())
//                    .setUnit(e.getUnit()).setNum(e.getNum()).setActualNum(0.0).setContainerNum(e.getContainerNum())
//                    .setStartSeedingPosition(e.getSeedingPositionNum()).setIsDeleted(false).setStatus(ShelfOrderTaskStatusEnum.ONE.getCode())
//                    .setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(new Date());
//            list.add(shelfOrderTask);
//
//            ShelfOrder shelfOrder = new ShelfOrder();
//            shelfOrder.setReceiptNoticeNum(receipt.getReceiptNoticeNum()).setShelfNum(shelfOrderTask.getShelfNum()).setTaskDetailNum(shelfOrderTask.getTaskDetailNum()).setStatus(ShelfOrderStatusEnum.TWO.getCode()).setId(e.getId())
//                    .setUpdateTime(date).setUpdateBy(CurrentUserUtil.getUsername());
//            shelfOrderList.add(shelfOrder);
//        });


        this.updateBatchById(shelfOrderList);
        this.shelfOrderTaskService.saveBatch(list);
    }

    private Map<Long, List<ShelfOrder>> reorganizeShelfData(List<ShelfOrder> shelfOrders, Map<Long, Receipt> receiptMap) {
        Map<Long, List<ShelfOrder>> group = new HashMap<>();
        shelfOrders.forEach(e -> {
            Receipt receipt = receiptMap.get(e.getOrderId());
            List<ShelfOrder> list = group.get(receipt.getWarehouseId());
            if (CollectionUtil.isEmpty(list)) {
                list = new ArrayList<>();
                list.add(e);
                group.put(receipt.getWarehouseId(), list);
            } else {
                list.add(e);
            }
        });
        return group;
    }

    /**
     * 生成上架任务
     *
     * @param details
     */
    @Override
    public void generateShelfOrder(ReceiptVO details) {
        List<MaterialVO> materialList = details.getMaterialForms();
        List<ShelfOrder> list = new ArrayList<>();
        for (MaterialVO materialVO : materialList) {
            if (MaterialStatusEnum.FOUR.getCode().equals(materialVO.getStatus())) {
                continue;
            }
            ShelfOrder shelfOrder = new ShelfOrder();
            shelfOrder.setOrderId(materialVO.getOrderId()).setOrderNum(materialVO.getOrderNum()).setReceiptNoticeNum(details.getReceiptNoticeNum())
                    .setMaterialId(materialVO.getId()).setMaterialCode(materialVO.getMaterialCode())
                    .setNum(materialVO.getActualNum()).setUnit(materialVO.getUnit())
                    .setContainerNum(materialVO.getContainerNum()).setIsDeleted(false)
                    .setBatchNum(materialVO.getBatchNum())
                    .setProductionDate(materialVO.getProductionDate())
                    .setColumnOne(materialVO.getColumnOne())
                    .setColumnTwo(materialVO.getColumnTwo())
                    .setSeedingPositionNum(null)
                    .setColumnThree(materialVO.getColumnThree())
                    .setStatus(ShelfOrderStatusEnum.ONE.getCode())
                    .setIsPutShelf(false).setCreateBy(CurrentUserUtil.getUsername())
                    .setCreateTime(new Date());
            list.add(shelfOrder);
        }
        this.saveBatch(list);
    }

    @Override
    public BaseResult selectWarehousingReport(QueryShelfOrderTaskForm queryShelfOrderTaskForm) {
        List<WarehouseingReportVO> warehouseingReportVOS = this.baseMapper.selectWarehousingReport(queryShelfOrderTaskForm);
        return BaseResult.ok(warehouseingReportVOS);
    }

}
