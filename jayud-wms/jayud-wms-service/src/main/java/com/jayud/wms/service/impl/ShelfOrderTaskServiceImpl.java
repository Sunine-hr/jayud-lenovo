package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.exception.ServiceException;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.ShelfOrderTaskMapper;
import com.jayud.wms.model.bo.ContainerForm;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.bo.QueryInventoryForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.enums.*;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.MaterialVO;
import com.jayud.wms.model.vo.ReceiptVO;
import com.jayud.wms.model.vo.ShelfOrderTaskVO;
import com.jayud.wms.model.vo.WarehouseLocationVO;
import com.jayud.wms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 上架任务单 服务实现类
 *
 * @author jyd
 * @since 2021-12-23
 */
@Service
public class ShelfOrderTaskServiceImpl extends ServiceImpl<ShelfOrderTaskMapper, ShelfOrderTask> implements IShelfOrderTaskService {


    @Autowired
    private ShelfOrderTaskMapper shelfOrderTaskMapper;
    @Autowired
    private ISowingResultsService sowingResultsService;
    @Autowired
    private IShelfOrderService shelfOrderService;
    @Autowired
    private IReceiptService receiptService;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private IInventoryDetailService inventoryDetailService;
    @Autowired
    public IWmsOwerInfoService wmsOwerInfoService;
    @Autowired
    public IWarehouseService warehouseService;
    @Autowired
    public IWarehouseAreaService warehouseAreaService;
    @Autowired
    public IWarehouseLocationService warehouseLocationService;
    @Autowired
    public IContainerService containerService;
    @Autowired
    public IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private MaterialTypeInfoService materialTypeInfoService;
    @Autowired
    private IWmsOutboundOrderInfoToMaterialService wmsOutboundOrderInfoToMaterialService;
    @Autowired
    private IShelfStrategyService shelfStrategyService;
    @Autowired
    private IStrategyConfService strategyConfService;
    @Autowired
    private IWmsMaterialToLoactionRelationService wmsMaterialToLoactionRelationService;
    @Autowired
    private ILockReLocationService lockReLocationService;

    @Override
    public IPage<ShelfOrderTaskVO> selectPage(QueryShelfOrderTaskForm queryShelfOrderTaskForm,
                                              Integer pageNo,
                                              Integer pageSize,
                                              HttpServletRequest req) {

        Page<QueryShelfOrderTaskForm> page = new Page<QueryShelfOrderTaskForm>(pageNo, pageSize);
        IPage<ShelfOrderTaskVO> pageList = shelfOrderTaskMapper.pageList(page, queryShelfOrderTaskForm);
        return pageList;
    }

    @Override
    public List<ShelfOrderTaskVO> selectList(QueryShelfOrderTaskForm queryShelfOrderTaskForm) {
        return shelfOrderTaskMapper.list(queryShelfOrderTaskForm);
    }

    @Override
    public List<ShelfOrderTaskVO> selectListFeign(QueryShelfOrderTaskForm queryShelfOrderTaskForm) {
        return shelfOrderTaskMapper.listFeign(queryShelfOrderTaskForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShelfOrderTask saveOrUpdateShelfOrderTask(ShelfOrderTask shelfOrderTask) {
        Long id = shelfOrderTask.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            shelfOrderTask.setCreateBy(CurrentUserUtil.getUsername());
            shelfOrderTask.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<ShelfOrderTask> shelfOrderTaskQueryWrapper = new QueryWrapper<>();
            //shelfOrderTaskQueryWrapper.lambda().eq(ShelfOrderTask::getCode, shelfOrderTask.getCode());
            //shelfOrderTaskQueryWrapper.lambda().eq(ShelfOrderTask::getIsDeleted, 0);
            //List<ShelfOrderTask> list = this.list(shelfOrderTaskQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        } else {
            //修改 --> update 更新人、更新时间
            shelfOrderTask.setUpdateBy(CurrentUserUtil.getUsername());
            shelfOrderTask.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<ShelfOrderTask> shelfOrderTaskQueryWrapper = new QueryWrapper<>();
            //shelfOrderTaskQueryWrapper.lambda().ne(ShelfOrderTask::getId, id);
            //shelfOrderTaskQueryWrapper.lambda().eq(ShelfOrderTask::getCode, shelfOrderTask.getCode());
            //shelfOrderTaskQueryWrapper.lambda().eq(ShelfOrderTask::getIsDeleted, 0);
            //List<ShelfOrderTask> list = this.list(shelfOrderTaskQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(shelfOrderTask);
        return shelfOrderTask;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delShelfOrderTask(Long id) {
//        ShelfOrderTask shelfOrderTask = this.baseMapper.selectById(id);
//        if (ObjectUtil.isEmpty(shelfOrderTask)) {
//            throw new IllegalArgumentException("上架任务单不存在，无法删除");
//        }
        ShelfOrderTask shelfOrderTask = new ShelfOrderTask();
        //逻辑删除 -->update 修改人、修改时间、是否删除
        shelfOrderTask.setId(id);
        shelfOrderTask.setUpdateBy(CurrentUserUtil.getUsername());
        shelfOrderTask.setUpdateTime(new Date());
        shelfOrderTask.setIsDeleted(true);
        this.updateById(shelfOrderTask);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryShelfOrderTaskForExcel(QueryShelfOrderTaskForm queryShelfOrderTaskForm,
                                                                           HttpServletRequest req) {
        return this.baseMapper.queryShelfOrderTaskForExcel(queryShelfOrderTaskForm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forcedShelf(List<ShelfOrderTask> shelfOrderTasks) {
        //修改状态
        List<ShelfOrderTask> list = new ArrayList<>();
        Date date = new Date();
        List<String> seedingPositionNums = new ArrayList<>();
        for (ShelfOrderTask shelfOrderTask : shelfOrderTasks) {
            seedingPositionNums.add(shelfOrderTask.getSeedingPositionNum());
            ShelfOrderTask tmp = new ShelfOrderTask();
            tmp.setId(shelfOrderTask.getId()).setUpdateTime(date).setUpdateBy(CurrentUserUtil.getUsername());
            tmp.setStatus(ShelfOrderTaskStatusEnum.FOUR.getCode()).setActualNum(shelfOrderTask.getNum());
            if (shelfOrderTask.getActualShelfSpace() == null) {
                tmp.setActualShelfSpace(shelfOrderTask.getRecommendedLocation());
            } else {
                tmp.setActualShelfSpace(shelfOrderTask.getActualShelfSpace());
            }
            list.add(tmp);
        }

        this.updateBatchById(list);
        //并且找到播种位更改状态
        for (String seedingPositionNum : seedingPositionNums) {
            SowingResults sowingResults = new SowingResults().setStatus(SowingResultsStatusEnum.FOUR.getCode());
            sowingResults.setUpdateBy(CurrentUserUtil.getUsername()).setUpdateTime(date);
            this.sowingResultsService.update(sowingResults, new QueryWrapper<>(new SowingResults().setStatus(SowingResultsStatusEnum.THREE.getCode()).setSeedingPositionNum(seedingPositionNum).setIsDeleted(false)));
        }
        List<InventoryDetailForm> inventoryDetailForms = new ArrayList<>();

        list.forEach(e -> {
            inventoryDetailForms.add(this.assembleInventoryDetail(e));
        });

        this.inventoryDetailService.input(inventoryDetailForms);
        //释放锁定库位
        this.releaseLockedLocation(shelfOrderTasks);
    }

    private InventoryDetailForm assembleInventoryDetail(ShelfOrderTask shelfOrderTask) {
        QueryShelfOrderTaskForm queryShelfOrderTaskForm = new QueryShelfOrderTaskForm();
        queryShelfOrderTaskForm.setId(shelfOrderTask.getId());
        //拿到当前上架任务单信息
        ShelfOrderTaskVO shelfOrderTaskVO = shelfOrderTaskMapper.list(queryShelfOrderTaskForm).get(0);
        //拿到货主信息
        WmsOwerInfo wmsOwerInfOne = wmsOwerInfoService.getById(shelfOrderTaskVO.getOwerId());
        //拿到仓库信息
        Warehouse warehouseOne = warehouseService.getById(shelfOrderTaskVO.getWarehouseId());

        //根据库位编码 查询库区信息actualShelfSpace
        WarehouseLocation warehouseLocation = new WarehouseLocation();
        warehouseLocation.setCode(shelfOrderTaskVO.getActualShelfSpace());//实际上架库位
        warehouseLocation.setWarehouseId(shelfOrderTaskVO.getWarehouseId()); // 仓库id
        WarehouseLocationVO warehouseLocationVO = warehouseLocationService.selectList(warehouseLocation).get(0);
        //根据库区编码和仓库id查询库区信息
        WarehouseArea warehouseAreaByCode = warehouseAreaService.getById(warehouseLocationVO.getWarehouseAreaId());

        //容器信息
        ContainerForm containerForm1 = containerService.getContainerOne(shelfOrderTaskVO.getContainerNum(), null, null, shelfOrderTaskVO.getWarehouseId());
        //物料信息   根据物料编号去查询物料信息
        QueryWrapper<WmsMaterialBasicInfo> condition = new QueryWrapper<>();
        condition.lambda().eq(WmsMaterialBasicInfo::getMaterialCode, shelfOrderTaskVO.getMaterialCode());
        WmsMaterialBasicInfo wmsMaterialBasicInfoOne = wmsMaterialBasicInfoService.getOne(condition);
        String materialTypeName = null;
        if (wmsMaterialBasicInfoOne.getMaterialTypeId() != null) {

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("id",wmsMaterialBasicInfoOne.getMaterialTypeId());
            List<LinkedHashMap<String, Object>> materialTypeInfoMaterialTypeIdOne = materialTypeInfoService.findMaterialTypeInfoMaterialTypeIdOne(paramMap);
            //物料类型编号
//        String materialTypeCode = materialTypeInfoMaterialTypeId.getResult().get("materialTypeCode").toString();
            materialTypeName = materialTypeInfoMaterialTypeIdOne.get(0).get("materialTypeName").toString();
        }

        //创建仓库信息
        InventoryDetailForm inventoryDetailForm = new InventoryDetailForm();
        inventoryDetailForm.setOwerId(wmsOwerInfOne.getId());//货主id
        inventoryDetailForm.setOwerCode(wmsOwerInfOne.getOwerCode());//货主编码
        inventoryDetailForm.setOwerName(wmsOwerInfOne.getOwerName());//货主名称
        inventoryDetailForm.setWarehouseId(warehouseOne.getId());//仓库id
        inventoryDetailForm.setWarehouseCode(warehouseOne.getCode());//仓库编号
        inventoryDetailForm.setWarehouseName(warehouseOne.getName());//仓库名称
        inventoryDetailForm.setWarehouseAreaId(warehouseAreaByCode.getId());//库区id
        inventoryDetailForm.setWarehouseAreaCode(warehouseAreaByCode.getCode());//库区编号
        inventoryDetailForm.setWarehouseAreaName(warehouseAreaByCode.getName());//库区名称
        inventoryDetailForm.setWarehouseLocationId(warehouseLocationVO.getId());//库位id
        inventoryDetailForm.setWarehouseLocationCode(warehouseLocationVO.getCode());//库位编号
        //inventoryDetailForm.setWarehouseLocationStatus(warehouseLocationVO.getStatus());//库位状态(0未冻结 1已冻结)
        //判断容器是否存在
        if(ObjectUtil.isNotEmpty(containerForm1)){
            //容器存在
            inventoryDetailForm.setContainerId(containerForm1.getId());//容器id
            inventoryDetailForm.setContainerCode(containerForm1.getCode());//容器编号
        }else{
            //容器不存在
            inventoryDetailForm.setContainerId(ContainerNumEnum.DEFAULT.getContainerId());//容器id
            inventoryDetailForm.setContainerCode(ContainerNumEnum.DEFAULT.getContainerCode());//容器编号
        }
        inventoryDetailForm.setMaterialId(wmsMaterialBasicInfoOne.getId());//物料id
        inventoryDetailForm.setMaterialCode(wmsMaterialBasicInfoOne.getMaterialCode());//物料编号
        inventoryDetailForm.setMaterialName(wmsMaterialBasicInfoOne.getMaterialName());//物料名称
        inventoryDetailForm.setMaterialTypeId(wmsMaterialBasicInfoOne.getMaterialTypeId());//物料类型id
        inventoryDetailForm.setMaterialType(materialTypeName);//物料类型
        //inventoryDetailForm.setMaterialSpecification();//物料规格
        inventoryDetailForm.setBatchCode(shelfOrderTaskVO.getBatchNum());//批次号
        inventoryDetailForm.setMaterialProductionDate(shelfOrderTaskVO.getProductionDate());//生产日期
        inventoryDetailForm.setCustomField1(shelfOrderTaskVO.getColumnOne());//自定义字段1
        inventoryDetailForm.setCustomField2(shelfOrderTaskVO.getColumnTwo());//自定义字段2
        inventoryDetailForm.setCustomField3(shelfOrderTaskVO.getColumnThree());//自定义字段3
        inventoryDetailForm.setOperationCount(new BigDecimal(shelfOrderTaskVO.getActualNum()));//操作数量入库，出库

        return inventoryDetailForm;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(List<ShelfOrderTask> shelfOrderTasks) {
        QueryWrapper<ShelfOrderTask> condition = new QueryWrapper<>();
        List<Integer> checkStatus = Arrays.asList(ShelfOrderTaskStatusEnum.TWO.getCode(), ShelfOrderTaskStatusEnum.FOUR.getCode(), ShelfOrderTaskStatusEnum.THREE.getCode());
        condition.lambda().in(ShelfOrderTask::getStatus, checkStatus)
                .eq(ShelfOrderTask::getIsDeleted, false);

        Map<Long, List<ShelfOrderTask>> oldMap = this.baseMapper.selectList(condition).stream().collect(Collectors.groupingBy(e -> e.getOrderId()));

        List<ShelfOrderTask> singleOpts = new ArrayList<>();
//        List<ShelfOrderTask> isPutShelfs = new ArrayList<>();
        List<ShelfOrder> shelfOrders = new ArrayList<>();
        Set<String> orderNos = new HashSet<>();
        Date date = new Date();

        shelfOrderTasks.forEach(e -> {
            if (e.getIsPutShelf()) {
                if (oldMap.get(e.getOrderId()) != null || e.getStatus().equals(ShelfOrderTaskStatusEnum.FIVE.getCode())) {
                    throw new ServiceException("该上架单号" + e.getShelfNum() + " 该状态无法进行操作");
                }
//                ShelfOrderTask shelfOrderTask = new ShelfOrderTask();
//                shelfOrderTask.setStatus(ShelfOrderTaskStatusEnum.FIVE.getCode()).setUpdateBy(CurrentUserUtil.getUsername())
//                        .setUpdateTime(date).setId(e.getId());
                orderNos.add(e.getOrderNum());
//                isPutShelfs.add(shelfOrderTask);
            } else {
                if (checkStatus.contains(e.getStatus()) || e.getStatus().equals(ShelfOrderTaskStatusEnum.FIVE.getCode())) {
                    throw new ServiceException("该上架单号" + e.getShelfNum() + " 该状态无法进行操作");
                }
                ShelfOrderTask shelfOrderTask = new ShelfOrderTask();
                shelfOrderTask.setOrderNum(e.getOrderNum()).setRecommendedLocation(e.getRecommendedLocation()).setStatus(ShelfOrderTaskStatusEnum.FIVE.getCode()).setUpdateBy(CurrentUserUtil.getUsername())
                        .setUpdateTime(date).setId(e.getId());
                singleOpts.add(shelfOrderTask);
                ShelfOrder shelfOrder = new ShelfOrder().setStatus(ShelfOrderStatusEnum.ONE.getCode());
                shelfOrder.setId(e.getShelfOrderId()).setUpdateTime(date).setUpdateBy(CurrentUserUtil.getUsername());
                shelfOrders.add(shelfOrder);
            }


        });

        //单个明细操作
        if (!CollectionUtil.isEmpty(shelfOrders)) {
            this.updateBatchById(singleOpts);
            this.shelfOrderService.updateBatchById(shelfOrders);
            //释放锁定库位
            this.releaseLockedLocation(singleOpts);
        }

        //整单撤销
        if (!CollectionUtil.isEmpty(orderNos)) {
            condition = new QueryWrapper<>();
            condition.lambda().in(ShelfOrderTask::getOrderNum, orderNos).eq(ShelfOrderTask::getStatus, ShelfOrderTaskStatusEnum.ONE.getCode()).eq(ShelfOrderTask::getIsDeleted, false);
            List<ShelfOrderTask> list = this.baseMapper.selectList(condition);
            //释放锁定库位
            this.releaseLockedLocation(list);

            ShelfOrderTask shelfOrderTask = new ShelfOrderTask();
            shelfOrderTask.setStatus(ShelfOrderTaskStatusEnum.FIVE.getCode()).setUpdateBy(CurrentUserUtil.getUsername())
                    .setUpdateTime(date);
            this.baseMapper.update(shelfOrderTask, condition);
            //收货单撤销
            Receipt receipt = new Receipt();
            receipt.setStatus(ReceiptStatusEnum.ONE.getCode()).setReceiver("").setUpdateBy(CurrentUserUtil.getUsername())
                    .setUpdateTime(date);
            QueryWrapper<Receipt> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().in(Receipt::getReceiptNum, orderNos).eq(Receipt::getIsDeleted, false);
            receiptService.update(receipt, queryWrapper);
        }


    }

    private void releaseLockedLocation(List<ShelfOrderTask> shelfOrderTasks) {
        List<String> orderNums = shelfOrderTasks.stream().map(ShelfOrderTask::getOrderNum).collect(Collectors.toList());
        Map<String, Receipt> receiptMap = this.receiptService.getByReceiptNums(orderNums).stream().collect(Collectors.toMap(e -> e.getReceiptNum(), e -> e));
        Map<String, LockReLocation> locationMap = this.lockReLocationService.list(new QueryWrapper<>(new LockReLocation().setIsDeleted(false))).stream().collect(Collectors.toMap(e -> e.getWarehouseId() + "~" + e.getCode(), e -> e));
        List<LockReLocation> reLocks = new ArrayList<>();
        shelfOrderTasks.forEach(e -> {
            Receipt receipt = receiptMap.get(e.getOrderNum());
            if (receipt != null) {
                LockReLocation lockReLocation = locationMap.get(receipt.getWarehouseId() + "~" + e.getRecommendedLocation());
                if (lockReLocation != null) {
                    reLocks.add(lockReLocation.setIsDeleted(true));
                }

            }

        });
        if (!CollectionUtil.isEmpty(reLocks)) {
            this.lockReLocationService.updateBatchById(reLocks);
        }

    }

    @Override
    public void directGenerationShelfTask(ReceiptVO details) {
        List<ShelfOrderTask> list = new ArrayList<>();
        String shelfNum = authClient.getOrder("shelf_order_task_shelf", new Date()).getResult();
        List<MaterialVO> materialList = details.getMaterialForms();
        Receipt receipt = this.receiptService.getById(details.getId());
        //获取一组空闲库位
        //TODO 远程调用
        Map<String, LockReLocation> lockReLocationMap = lockReLocationService.selectList(new LockReLocation().setIsDeleted(false)).stream().collect(Collectors.toMap(e -> e.getWarehouseId() + "-" + e.getWarehouseAreaId() + "-" + e.getCode(), e -> e));
        List<WarehouseLocationVO> warehouseLocations = this.warehouseLocationService.queryWarehouseLocation(new QueryInventoryForm().setWarehouseId(receipt.getWarehouseId())
//                .setLimit(materialList.size() * 2 + lockReLocationMap.keySet().size())
                ).stream()
                .filter(e -> lockReLocationMap.get(e.getWarehouseId() + "-" + e.getWarehouseAreaId() + "-" + e.getCode()) == null)
                .collect(Collectors.toList());

        materialList.forEach(e -> {
            if (!MaterialStatusEnum.FOUR.getCode().equals(e.getStatus())) {

                ShelfOrderTask shelfOrderTask = new ShelfOrderTask();

                String recommendedLocation = this.doRecommendedLocation(e.getMaterialCode(), receipt.getWarehouseId(), warehouseLocations);
                if (recommendedLocation == null) {
                    throw new ServiceException("暂无推荐库位");
                }
                shelfOrderTask.setRecommendedLocation(recommendedLocation);
//                if (!CollectionUtil.isEmpty(warehouseLocations)) {
//                    WarehouseLocationVO warehouseLocationVO = warehouseLocations.remove(0);
//                    shelfOrderTask.setRecommendedLocation(warehouseLocationVO.getCode());
//                }
                BigDecimal account = wmsOutboundOrderInfoToMaterialService.getDistributionAccount(e.getMaterialCode(), e.getUnit(), new BigDecimal(e.getActualNum()));
                shelfOrderTask.setShelfNum(shelfNum)
                        .setTaskDetailNum(authClient.getOrder("shelf_order_task_detail", new Date()).getResult())
                        .setOrderId(e.getOrderId()).setOrderNum(e.getOrderNum()).setReceiptNoticeNum(details.getReceiptNoticeNum())
                        .setMaterialId(e.getId()).setMaterialCode(e.getMaterialCode())
                        .setBatchNum(e.getBatchNum())
                        .setProductionDate(e.getProductionDate())
                        .setColumnOne(e.getColumnOne())
                        .setColumnTwo(e.getColumnTwo())
                        .setColumnThree(e.getColumnThree())
                        .setUnit("EA").setNum(account.doubleValue()).setActualNum(0.0).setContainerNum(e.getContainerNum())
                        .setIsPutShelf(details.getIsPutShelf())
                        .setStartSeedingPosition(null).setIsDeleted(false).setStatus(ShelfOrderTaskStatusEnum.ONE.getCode())
                        .setCreateBy(CurrentUserUtil.getUsername()).setCreateTime(new Date());
                list.add(shelfOrderTask);
            }
        });
        this.saveBatch(list);
    }

    @Override
    public String doRecommendedLocation(String materialCode, Long warehouseId, List<WarehouseLocationVO> warehouseLocations) {
        Map<String, String> policyPriority = shelfStrategyService.getPolicyPriority(materialCode, warehouseId);
        String priorityStr = policyPriority.get(materialCode);

        if (StringUtils.isEmpty(priorityStr)) {
            if (!CollectionUtil.isEmpty(warehouseLocations)) {
                WarehouseLocationVO warehouseLocationVO = warehouseLocations.remove(0);
                this.lockReLocationService.save(new LockReLocation().setWarehouseId(warehouseLocationVO.getWarehouseId()).setWarehouseAreaId(warehouseLocationVO.getWarehouseAreaId()).setCode(warehouseLocationVO.getCode()).setWarehouseLocationId(warehouseLocationVO.getId()));
                return warehouseLocationVO.getCode();
            } else {
                return null;
            }
        }
        String[] split = priorityStr.split("~");
        String shelfStrategyId = split[1];

        List<StrategyConf> strategyConfs = this.strategyConfService.getBaseMapper().selectList(new QueryWrapper<>(new StrategyConf().setShelfStrategyId(Long.valueOf(shelfStrategyId)).setIsDeleted(false)));
        if (CollectionUtil.isEmpty(strategyConfs)) {
            throw new ServiceException("没有配置上架策略");
        }
        return this.pairingStrategy(materialCode, strategyConfs, warehouseLocations);
    }

    private String pairingStrategy(String materialCode, List<StrategyConf> strategyConfs, List<WarehouseLocationVO> warehouseLocations) {
        //TODO 远程调用
        WmsMaterialBasicInfo materialBasicInfo = this.wmsMaterialBasicInfoService.getByMaterialCode(materialCode);

        List<WmsMaterialToLoactionRelation> loactionRelations = null;

        Map<String, WarehouseLocationVO> locationVOMap = warehouseLocations.stream().collect(Collectors.toMap(e -> e.getWarehouseId() + "-" + e.getWarehouseAreaId() + "-" + e.getCode(), e -> e));
        for (StrategyConf strategyConf : strategyConfs) {
            switch (strategyConf.getType()) {
                case 1:
                    if (loactionRelations == null) {
                        loactionRelations = this.wmsMaterialToLoactionRelationService.getByCondition(new WmsMaterialToLoactionRelation().setMaterialBasicInfoId(materialBasicInfo.getId()).setIsDeleted(false));
                    }

                    for (WmsMaterialToLoactionRelation loactionRelation : loactionRelations) {
                        WarehouseLocationVO tmp = locationVOMap.get(loactionRelation.getWarehouseId() + "-" + loactionRelation.getWarehouseAreaId() + "-" + loactionRelation.getLocaltionCode());
                        if (this.filterRecommendedLocations(tmp, strategyConf)) {
                            tmp = locationVOMap.remove(loactionRelation.getWarehouseId() + "-" + loactionRelation.getWarehouseAreaId() + "-" + loactionRelation.getLocaltionCode());
                            if (tmp == null) {
                                continue;
                            }
                            warehouseLocations.remove(tmp);
                            this.lockReLocationService.save(new LockReLocation().setWarehouseId(tmp.getWarehouseId()).setWarehouseAreaId(tmp.getWarehouseAreaId()).setCode(tmp.getCode()).setWarehouseLocationId(tmp.getId()));
                            return tmp.getCode();
                        }
                    }

                    break;
                case 2:
                    Iterator<WarehouseLocationVO> iterator = warehouseLocations.iterator();
                    while (iterator.hasNext()) {

                        WarehouseLocationVO warehouseLocation = iterator.next();
                        if (warehouseLocation.getWarehouseId().equals(materialBasicInfo.getRecommendedWarehouseId())
                                && warehouseLocation.getWarehouseAreaId().equals(materialBasicInfo.getRecommendedWarehouseAreaId())) {

                            if (this.filterRecommendedLocations(warehouseLocation, strategyConf)) {
                                warehouseLocation = locationVOMap.remove(materialBasicInfo.getRecommendedWarehouseId() + "-" + materialBasicInfo.getRecommendedWarehouseAreaId() + "-" + warehouseLocation.getCode());
                                iterator.remove();
                                this.lockReLocationService.save(new LockReLocation().setWarehouseId(materialBasicInfo.getRecommendedWarehouseId()).setWarehouseAreaId(materialBasicInfo.getRecommendedWarehouseAreaId()).setCode(warehouseLocation.getCode()).setWarehouseLocationId(warehouseLocation.getId()));
                                return warehouseLocation.getCode();
                            }
                        }
                    }
                    break;
                case 3:
                    //至库区
                    iterator = warehouseLocations.iterator();
                    while (iterator.hasNext()) {

                        WarehouseLocationVO warehouseLocation = iterator.next();
                        if (warehouseLocation.getWarehouseId().equals(strategyConf.getWarehouseId())
                                && warehouseLocation.getWarehouseAreaId().equals(strategyConf.getWarehouseAreaId())) {

                            if (this.filterRecommendedLocations(warehouseLocation, strategyConf)) {
                                warehouseLocation = locationVOMap.remove(warehouseLocation.getWarehouseId() + "-" + warehouseLocation.getWarehouseAreaId() + "-" + warehouseLocation.getCode());
                                iterator.remove();
                                this.lockReLocationService.save(new LockReLocation().setWarehouseId(warehouseLocation.getWarehouseId()).setWarehouseAreaId(warehouseLocation.getWarehouseAreaId()).setCode(warehouseLocation.getCode()).setWarehouseLocationId(warehouseLocation.getId()));
                                return warehouseLocation.getCode();
                            }
                        }
                    }

                    break;
            }
        }

        return null;
    }

    private Boolean filterRecommendedLocations(WarehouseLocationVO warehouseLocationVO, StrategyConf strategyConf) {
        if (warehouseLocationVO==null){
            return false;
        }
        switch (strategyConf.getType()) {
            case 1:
            case 2:
                if (strategyConf.getFrozen() != null) {
                    Boolean isFrozen = strategyConf.getFrozen() == 1;
                    if (!isFrozen.equals(warehouseLocationVO.getIsInFrozen())) {
                        return false;
                    }
                }

                if (!StringUtils.isEmpty(strategyConf.getLocationType())) {
                    List<Long> locationTypes = Arrays.stream(strategyConf.getLocationType().split(",")).map(Long::valueOf).collect(Collectors.toList());
                    if (locationTypes.contains(warehouseLocationVO.getType())) {
                        return false;
                    }
                }
                break;
            case 3:
                break;
        }
        return true;
    }


    public static void main(String[] args) {
        List<LockReLocation> tmps = new ArrayList<>();
        LockReLocation tmp = new LockReLocation();
        tmp.setCode("1");
        tmps.add(tmp);
        LockReLocation tmp1 = new LockReLocation();
        tmp1.setCode("2");
        tmps.add(tmp1);
        LockReLocation tmp2 = new LockReLocation();
        tmp2.setCode("3");
        tmps.add(tmp2);

        Map<String, LockReLocation> map = tmps.stream().collect(Collectors.toMap(e -> e.getCode(), e -> e));
        LockReLocation remove = map.remove("2");
        tmps.remove(remove);
        System.out.println("删除数据" + new JSONObject(remove));
        System.out.println("集合数据" + tmps.toString());
    }
}
