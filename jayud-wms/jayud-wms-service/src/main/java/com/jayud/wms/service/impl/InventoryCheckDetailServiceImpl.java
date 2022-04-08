package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.InventoryCheckDetailMapper;
import com.jayud.wms.model.bo.InventoryCheckDetailPostForm;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.enums.InventoryCheckDetailEnum;
import com.jayud.wms.model.po.*;
import com.jayud.wms.service.*;
import com.jayud.wms.utils.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存盘点明细表 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Service
public class InventoryCheckDetailServiceImpl extends ServiceImpl<InventoryCheckDetailMapper, InventoryCheckDetail> implements IInventoryCheckDetailService {


    @Autowired
    private InventoryCheckDetailMapper inventoryCheckDetailMapper;
    @Autowired
    private IInventoryDetailService inventoryDetailService;
    @Autowired
    private IInventoryBusinessService iInventoryBusinessService;
    @Autowired
    private IWmsOwerInfoService wmsOwerInfoService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IWarehouseAreaService warehouseAreaService;
    @Autowired
    private IWarehouseLocationService warehouseLocationService;
    @Autowired
    private IContainerService containerService;
    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;

    @Autowired
    private MaterialTypeInfoService materialTypeInfoService;
    @Autowired
    private CodeUtils codeUtils;

    @Override
    public IPage<InventoryCheckDetail> selectPage(InventoryCheckDetail inventoryCheckDetail,
                                                  Integer pageNo,
                                                  Integer pageSize,
                                                  HttpServletRequest req) {

        Page<InventoryCheckDetail> page = new Page<InventoryCheckDetail>(pageNo, pageSize);
        IPage<InventoryCheckDetail> pageList = inventoryCheckDetailMapper.pageList(page, inventoryCheckDetail);
        return pageList;
    }

    @Override
    public List<InventoryCheckDetail> selectList(InventoryCheckDetail inventoryCheckDetail) {
        return inventoryCheckDetailMapper.list(inventoryCheckDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryCheckDetail saveOrUpdateInventoryCheckDetail(InventoryCheckDetail inventoryCheckDetail) {
        Long id = inventoryCheckDetail.getId();
        if (ObjectUtil.isEmpty(id)) {
            //新增 --> add 创建人、创建时间
            inventoryCheckDetail.setCreateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<InventoryCheckDetail> inventoryCheckDetailQueryWrapper = new QueryWrapper<>();
            //inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getCode, inventoryCheckDetail.getCode());
            //inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getIsDeleted, 0);
            //List<InventoryCheckDetail> list = this.list(inventoryCheckDetailQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        } else {
            //修改 --> update 更新人、更新时间
            inventoryCheckDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<InventoryCheckDetail> inventoryCheckDetailQueryWrapper = new QueryWrapper<>();
            //inventoryCheckDetailQueryWrapper.lambda().ne(InventoryCheckDetail::getId, id);
            //inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getCode, inventoryCheckDetail.getCode());
            //inventoryCheckDetailQueryWrapper.lambda().eq(InventoryCheckDetail::getIsDeleted, 0);
            //List<InventoryCheckDetail> list = this.list(inventoryCheckDetailQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(inventoryCheckDetail);
        return inventoryCheckDetail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delInventoryCheckDetail(int id) {
        InventoryCheckDetail inventoryCheckDetail = this.baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(inventoryCheckDetail)) {
            throw new IllegalArgumentException("库存盘点明细表不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        inventoryCheckDetail.setUpdateBy(CurrentUserUtil.getUsername());
        inventoryCheckDetail.setUpdateTime(new Date());
        inventoryCheckDetail.setIsDeleted(true);
        this.saveOrUpdate(inventoryCheckDetail);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryInventoryCheckDetailForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryInventoryCheckDetailForExcel(paramMap);
    }

    /**
     * 盘点明细新增
     *
     * @param inventoryCheckDetail
     * @return
     */
    @Override
    public InventoryCheckDetail add(InventoryCheckDetail inventoryCheckDetail) {

        inventoryCheckDetail.setInventoryCheckId(null);
        inventoryCheckDetail.setInventoryDetailId(null);

        inventoryCheckDetail.setCheckStatus(1);//明细盘点状态(1未盘点、2已盘点、3已过账)

        Long warehouseId = inventoryCheckDetail.getWarehouseId();
        Warehouse warehouse = warehouseService.getById(warehouseId);
        if (ObjectUtil.isEmpty(warehouse)) {
            throw new IllegalArgumentException("仓库不存在");
        }
        String warehouseCode = warehouse.getCode();
        String warehouseName = warehouse.getName();
        inventoryCheckDetail.setWarehouseId(warehouseId);
        inventoryCheckDetail.setWarehouseCode(warehouseCode);
        inventoryCheckDetail.setWarehouseName(warehouseName);

        Long containerId = inventoryCheckDetail.getContainerId();
        String containerCode = inventoryCheckDetail.getContainerCode();
        if (ObjectUtil.isEmpty(containerCode)) {
            throw new IllegalArgumentException("容器号不能为空");
        }
        QueryWrapper<Container> containerQueryWrapper = new QueryWrapper<>();
        containerQueryWrapper.lambda().eq(Container::getWarehouseId, warehouseId);
        containerQueryWrapper.lambda().eq(Container::getCode, containerCode);
        containerQueryWrapper.lambda().eq(Container::getIsDeleted, 0);
        containerQueryWrapper.lambda().groupBy(Container::getCode);
        Container container = containerService.getOne(containerQueryWrapper);
        if (ObjectUtil.isEmpty(container)) {
            throw new IllegalArgumentException("容器不存在");
        }
        containerId = container.getId();
        inventoryCheckDetail.setContainerId(containerId);
        inventoryCheckDetail.setContainerCode(containerCode);

        Long warehouseLocationId = inventoryCheckDetail.getWarehouseLocationId();//库位ID
        String warehouseLocationCode = inventoryCheckDetail.getWarehouseLocationCode();//库位编号
        if (ObjectUtil.isEmpty(warehouseLocationCode)) {
            throw new IllegalArgumentException("库位编号不能为空");
        }
        QueryWrapper<WarehouseLocation> warehouseLocationQueryWrapper = new QueryWrapper<>();
        warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getWarehouseId, warehouseId);
        warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getCode, warehouseLocationCode);
        warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getIsDeleted, 0);
        warehouseLocationQueryWrapper.lambda().groupBy(WarehouseLocation::getCode);
        WarehouseLocation warehouseLocation = warehouseLocationService.getOne(warehouseLocationQueryWrapper);
        if (ObjectUtil.isEmpty(warehouseLocation)) {
            throw new IllegalArgumentException("库位编号不存在");
        }
        warehouseLocationId = warehouseLocation.getId();
        inventoryCheckDetail.setWarehouseLocationId(warehouseLocationId);
        inventoryCheckDetail.setWarehouseLocationCode(warehouseLocationCode);

        Long warehouseAreaId = warehouseLocation.getWarehouseAreaId();
        WarehouseArea warehouseArea = warehouseAreaService.getById(warehouseAreaId);
        if (ObjectUtil.isEmpty(warehouseArea)) {
            throw new IllegalArgumentException("仓库库区不存在");
        }
        String warehouseAreaCode = warehouseArea.getCode();
        String warehouseAreaName = warehouseArea.getName();
        inventoryCheckDetail.setWarehouseAreaId(warehouseAreaId);
        inventoryCheckDetail.setWarehouseAreaCode(warehouseAreaCode);
        inventoryCheckDetail.setWarehouseAreaName(warehouseAreaName);


        Long materialId = inventoryCheckDetail.getMaterialId();
        String materialCode = inventoryCheckDetail.getMaterialCode();
        QueryWrapper<WmsMaterialBasicInfo> wmsMaterialBasicInfoQueryWrapper = new QueryWrapper<>();
        wmsMaterialBasicInfoQueryWrapper.lambda().eq(WmsMaterialBasicInfo::getMaterialCode, materialCode);
        wmsMaterialBasicInfoQueryWrapper.lambda().eq(WmsMaterialBasicInfo::getIsDeleted, 0);
        wmsMaterialBasicInfoQueryWrapper.lambda().groupBy(WmsMaterialBasicInfo::getMaterialCode);
        WmsMaterialBasicInfo wmsMaterialBasicInfo = wmsMaterialBasicInfoService.getOne(wmsMaterialBasicInfoQueryWrapper);
        if (ObjectUtil.isEmpty(wmsMaterialBasicInfo)) {
            throw new IllegalArgumentException("物料编号不存在");
        }
        materialId = wmsMaterialBasicInfo.getId();
        String materialName = wmsMaterialBasicInfo.getMaterialName();
        inventoryCheckDetail.setMaterialId(materialId);
        inventoryCheckDetail.setMaterialCode(materialCode);
        inventoryCheckDetail.setMaterialName(materialName);

        Long materialTypeId = wmsMaterialBasicInfo.getMaterialTypeId();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", materialTypeId);
        List<LinkedHashMap<String, Object>> materialTypeInfoMaterialTypeIdOne = materialTypeInfoService.findMaterialTypeInfoMaterialTypeIdOne(paramMap);

        Map<String, Object> paramMapOne = new HashMap<>();
        paramMapOne.put("id", materialTypeInfoMaterialTypeIdOne.get(0).get("id"));
        paramMapOne.put("materialTypeName", materialTypeInfoMaterialTypeIdOne.get(0).get("materialTypeName"));
        paramMapOne.put("materialTypeCode", materialTypeInfoMaterialTypeIdOne.get(0).get("materialTypeCode"));

        Map<String, Object> materialTypeInfo = paramMapOne;
        String materialTypeName = MapUtil.getStr(materialTypeInfo, "materialTypeName");
        inventoryCheckDetail.setMaterialTypeId(materialTypeId);
        inventoryCheckDetail.setMaterialType(materialTypeName);


        Long owerId = wmsMaterialBasicInfo.getOwerId();
        WmsOwerInfo wmsOwerInfo = wmsOwerInfoService.getById(owerId);
        if (ObjectUtil.isEmpty(wmsOwerInfo)) {
            throw new IllegalArgumentException("货主不存在");
        }
        String owerCode = wmsOwerInfo.getOwerCode();
        String owerName = wmsOwerInfo.getOwerName();
        inventoryCheckDetail.setOwerId(owerId);
        inventoryCheckDetail.setOwerCode(owerCode);
        inventoryCheckDetail.setOwerName(owerName);

        return inventoryCheckDetail;
    }

    /**
     * 盘点明细确认过账(勾选n个)
     *
     * @param form
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmPost(InventoryCheckDetailPostForm form) {
        //处理 正常、盘盈、盘亏
        Long inventoryCheckId = form.getInventoryCheckId();//库存盘点ID
        List<InventoryCheckDetail> details = form.getDetails();//库存盘点明细
        if (ObjectUtil.isEmpty(inventoryCheckId)) {
            throw new IllegalArgumentException("盘点明细，库存盘点ID不能为空");
        }
        if (CollUtil.isEmpty(details)) {
            throw new IllegalArgumentException("盘点明细，不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) -> 确定唯一组合值
        List<String> collect = details.stream()
                .map(k -> (k.getOwerCode() + "_" + k.getWarehouseCode() + "_" + k.getWarehouseAreaCode() + "_" + k.getWarehouseLocationCode() + "_" + k.getContainerCode() + "_" + k.getMaterialCode() + "_" + k.getBatchCode() + "_" + k.getMaterialProductionDate() + "_" + k.getCustomField1() + "_" + k.getCustomField2() + "_" + k.getCustomField3()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3),唯一组合值不能重复");
        }
        //CheckStatus 明细盘点状态(1未盘点、2已盘点、3已过账)
        boolean present = details.stream().filter(m -> m.getCheckStatus().equals(3)).findAny().isPresent();
        if (present) {
            throw new IllegalArgumentException("存在已过账的盘点明细，不能再次过账");
        }

        List<InventoryCheckDetail> inventoryAddProfit = new ArrayList<>();//新增盘盈

        List<InventoryCheckDetail> inventoryNormal = new ArrayList<>();//正常
        List<InventoryCheckDetail> inventoryProfit = new ArrayList<>();//盘盈
        List<InventoryCheckDetail> inventoryLosses = new ArrayList<>();//盘亏

        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) -> 确定唯一组合值
        details.forEach(inventoryCheckDetail -> {
            //货主 + 仓库 + 库区 + 库位 + 容器 + 物料，确认库位的库存信息
            Long inventoryCheckId1 = inventoryCheckDetail.getInventoryCheckId();//库存盘点ID
            Long inventoryDetailId = inventoryCheckDetail.getInventoryDetailId();//库存明细ID
            Long owerId = inventoryCheckDetail.getOwerId();//货主
            String owerCode = inventoryCheckDetail.getOwerCode();
            String owerName = inventoryCheckDetail.getOwerName();
            if (ObjectUtil.isEmpty(owerCode)) {
                throw new IllegalArgumentException("客户不能为空");
            }
            Long warehouseId = inventoryCheckDetail.getWarehouseId();//仓库
            String warehouseCode = inventoryCheckDetail.getWarehouseCode();
            String warehouseName = inventoryCheckDetail.getWarehouseName();
            if (ObjectUtil.isEmpty(warehouseCode)) {
                throw new IllegalArgumentException("仓库不能为空");
            }
            Long warehouseAreaId = inventoryCheckDetail.getWarehouseAreaId();//库区
            String warehouseAreaCode = inventoryCheckDetail.getWarehouseAreaCode();
            String warehouseAreaName = inventoryCheckDetail.getWarehouseAreaName();
            if (ObjectUtil.isEmpty(warehouseAreaCode)) {
                throw new IllegalArgumentException("库区不能为空");
            }
            Long warehouseLocationId = inventoryCheckDetail.getWarehouseLocationId();//库位
            String warehouseLocationCode = inventoryCheckDetail.getWarehouseLocationCode();
            if (ObjectUtil.isEmpty(warehouseLocationCode)) {
                throw new IllegalArgumentException("库位不能为空");
            }
            Long containerId = inventoryCheckDetail.getContainerId();//容器
            String containerCode = inventoryCheckDetail.getContainerCode();
            if (ObjectUtil.isEmpty(containerCode)) {
                throw new IllegalArgumentException("卡板号不能为空");
            }
            Long materialId = inventoryCheckDetail.getMaterialId();//物料
            String materialCode = inventoryCheckDetail.getMaterialCode();
            if (ObjectUtil.isEmpty(materialCode)) {
                throw new IllegalArgumentException("物料不能为空");
            }
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = inventoryCheckDetail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = inventoryCheckDetail.getMaterialProductionDate();//生产日期
            String customField1 = inventoryCheckDetail.getCustomField1();//自定义字段1
            String customField2 = inventoryCheckDetail.getCustomField2();//自定义字段2
            String customField3 = inventoryCheckDetail.getCustomField3();//自定义字段3


            BigDecimal inventoryCount = inventoryCheckDetail.getInventoryCount();//库存数量
            BigDecimal checkCount = inventoryCheckDetail.getCheckCount();//盘点数量

            if (ObjectUtil.isEmpty(inventoryCount) || ObjectUtil.isEmpty(checkCount)) {
                throw new IllegalArgumentException("库存数量 或 盘点数量，不能为空");
            }
            if (inventoryCount.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("库存数量不能小于0");
            }
            if (checkCount.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("盘点数量不能小于0");
            }

            if (ObjectUtil.isEmpty(inventoryCheckId1) && ObjectUtil.isEmpty(inventoryDetailId)) {
                //新增的盘点明细  相当于 盘盈
                inventoryAddProfit.add(inventoryCheckDetail);
            } else {
                if (inventoryCount.compareTo(checkCount) == 0) {
                    //库存数量 = 盘点数量，正常
                    inventoryNormal.add(inventoryCheckDetail);
                } else if (inventoryCount.compareTo(checkCount) < 0) {
                    //库存数量 < 盘点数量，盘盈
                    inventoryProfit.add(inventoryCheckDetail);
                } else if (inventoryCount.compareTo(checkCount) > 0) {
                    //库存数量 > 盘点数量，盘亏
                    inventoryLosses.add(inventoryCheckDetail);
                }
            }
        });

        //新增库存明细
        List<InventoryDetail> addDetailList = new ArrayList<>();
        Map<String, JSONObject> addOriginalMap = new HashMap<>();//记录原始库存
        inventoryAddProfit.forEach(inventoryCheckDetail -> {
            Long owerId = inventoryCheckDetail.getOwerId();//货主
            String owerCode = inventoryCheckDetail.getOwerCode();
            Long warehouseId = inventoryCheckDetail.getWarehouseId();//仓库
            String warehouseCode = inventoryCheckDetail.getWarehouseCode();
            Long warehouseAreaId = inventoryCheckDetail.getWarehouseAreaId();//库区
            String warehouseAreaCode = inventoryCheckDetail.getWarehouseAreaCode();
            Long warehouseLocationId = inventoryCheckDetail.getWarehouseLocationId();//库位
            String warehouseLocationCode = inventoryCheckDetail.getWarehouseLocationCode();
            Long containerId = inventoryCheckDetail.getContainerId();//容器
            String containerCode = inventoryCheckDetail.getContainerCode();
            Long materialId = inventoryCheckDetail.getMaterialId();//物料
            String materialCode = inventoryCheckDetail.getMaterialCode();
//            inventoryCheckDetail
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = inventoryCheckDetail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = inventoryCheckDetail.getMaterialProductionDate();//生产日期
            String customField1 = inventoryCheckDetail.getCustomField1();//自定义字段1
            String customField2 = inventoryCheckDetail.getCustomField2();//自定义字段2
            String customField3 = inventoryCheckDetail.getCustomField3();//自定义字段3

            QueryWrapper<InventoryDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(InventoryDetail::getOwerId, owerId);
            queryWrapper.lambda().eq(InventoryDetail::getOwerCode, owerCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseId, warehouseId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseCode, warehouseCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaId, warehouseAreaId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaCode, warehouseAreaCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationId, warehouseLocationId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationCode, warehouseLocationCode);
//            queryWrapper.lambda().eq(InventoryDetail::getContainerId, containerId);   //容器id 这边没有
            queryWrapper.lambda().eq(InventoryDetail::getContainerCode, containerCode);  // 卡板号
//            queryWrapper.lambda().eq(InventoryDetail::getMaterialId, materialId);
            queryWrapper.lambda().eq(InventoryDetail::getMaterialCode, materialCode);
            if (ObjectUtil.isNotEmpty(batchCode)) {
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if (ObjectUtil.isNotEmpty(materialProductionDate)) {
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if (ObjectUtil.isNotEmpty(customField1)) {
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if (ObjectUtil.isNotEmpty(customField2)) {
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if (ObjectUtil.isNotEmpty(customField3)) {
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3);//group by 分组，保证唯一值
            InventoryDetail inventoryDetail = inventoryDetailService.getOne(queryWrapper);

            BigDecimal checkCount = inventoryCheckDetail.getCheckCount();
            BigDecimal inventoryCount = inventoryCheckDetail.getInventoryCount();
            if (inventoryCount.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("新增的盘点明细，库存数量不能为小于0");
            }
            BigDecimal subtract = checkCount.subtract(inventoryCount);//盘点数量 - 库存数量
            if (subtract.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("新增的盘点明细，盘点数量不能小于库存数量");
            }
            BigDecimal originalExistingCount = new BigDecimal("0");
            BigDecimal toOperationCount = new BigDecimal("0");
            if (ObjectUtil.isEmpty(inventoryDetail)) {
                //inventoryDetail 不存在，代表新增
                inventoryDetail = ConvertUtil.convert(inventoryCheckDetail, InventoryDetail.class);
                inventoryDetail.setId(null);
                inventoryDetail.setExistingCount(subtract);//(盘点数量-库存数量)的值 设置到 现有量
                inventoryDetail.setWarehouseLocationStatus(1);//库位状态(0未冻结 1已冻结)  盘点过账，默认冻结库位库存
                inventoryDetail.setCreateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setCreateTime(new Date());

                originalExistingCount = new BigDecimal("0");
                toOperationCount = subtract;
            } else {
                //inventoryDetail 存在，代表累加
                BigDecimal existingCount = inventoryDetail.getExistingCount();
                BigDecimal add = existingCount.add(subtract);
                inventoryDetail.setExistingCount(add);
                inventoryDetail.setWarehouseLocationStatus(1);//库位状态(0未冻结 1已冻结)  盘点过账，默认冻结库位库存
                inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setUpdateTime(new Date());

                originalExistingCount = existingCount;
                toOperationCount = subtract;
            }

            String key = inventoryDetail.getOwerCode() + "_" + inventoryDetail.getWarehouseCode() + "_" + inventoryDetail.getWarehouseAreaCode() + "_"
                    + inventoryDetail.getWarehouseLocationCode() + "_" + inventoryDetail.getContainerCode() + "_" + inventoryDetail.getMaterialCode() + "_"
                    + inventoryDetail.getBatchCode() + "_" + inventoryDetail.getMaterialProductionDate() + "_" + inventoryDetail.getCustomField1() + "_" + inventoryDetail.getCustomField2() + "_" + inventoryDetail.getCustomField3();
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("originalExistingCount", originalExistingCount);//原数量(原现有量)
            jsonObject.set("toWarehouseLocationId", inventoryDetail.getWarehouseLocationId());//目标库位ID
            jsonObject.set("toWarehouseLocationCode", inventoryDetail.getWarehouseCode());//目标库位编号
            jsonObject.set("toContainerId", inventoryDetail.getContainerId());//目标容器ID
            jsonObject.set("toContainerCode", inventoryDetail.getContainerCode());//目标容器号
            jsonObject.set("toOperationCount", toOperationCount);//操作数量(正负)
            addOriginalMap.put(key, jsonObject);

            addDetailList.add(inventoryDetail);
        });
        //新增盘盈，保存库存明细
        if (CollUtil.isNotEmpty(addDetailList)) {
            boolean b = inventoryDetailService.saveOrUpdateBatch(addDetailList);
        }
        Map<String, Long> inventoryDetailMap = addDetailList.stream().collect(Collectors.toMap(
                k -> (k.getOwerCode() + "_" + k.getWarehouseCode() + "_" + k.getWarehouseAreaCode() + "_" + k.getWarehouseLocationCode() + "_" + k.getContainerCode() + "_" + k.getMaterialCode() + "_" + k.getBatchCode() + "_" + k.getMaterialProductionDate() + "_" + k.getCustomField1() + "_" + k.getCustomField2() + "_" + k.getCustomField3()),
                v -> v.getId()));

        inventoryAddProfit.forEach(inventoryCheckDetail -> {
            String key = inventoryCheckDetail.getOwerCode() + "_" + inventoryCheckDetail.getWarehouseCode() + "_" + inventoryCheckDetail.getWarehouseAreaCode() + "_" + inventoryCheckDetail.getWarehouseLocationCode() + "_" + inventoryCheckDetail.getContainerCode() + "_" + inventoryCheckDetail.getMaterialCode() + "_" + inventoryCheckDetail.getBatchCode() + "_" + inventoryCheckDetail.getMaterialProductionDate() + "_" + inventoryCheckDetail.getCustomField1() + "_" + inventoryCheckDetail.getCustomField2() + "_" + inventoryCheckDetail.getCustomField3();

            Long inventoryDetailId = inventoryDetailMap.get(key);//库存明细ID

            inventoryCheckDetail.setInventoryCheckId(inventoryCheckId);
            inventoryCheckDetail.setInventoryDetailId(inventoryDetailId);
            inventoryCheckDetail.setCheckStatus(3);//明细盘点状态(1未盘点、2已盘点、3已过账)
            BigDecimal checkCount = inventoryCheckDetail.getCheckCount();
            BigDecimal inventoryCount = inventoryCheckDetail.getInventoryCount();
            BigDecimal subtract = checkCount.subtract(inventoryCount);
            inventoryCheckDetail.setCheckSurplusCount(subtract);//盘盈数量 = 盘点数量 - 库存数量
            inventoryCheckDetail.setCheckLossesCount(new BigDecimal("0"));//盘亏数量

            inventoryCheckDetail.setCreateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setCreateTime(new Date());
            inventoryCheckDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setUpdateTime(new Date());
        });
        //新增盘盈，保存盘点明细
        if (CollUtil.isNotEmpty(inventoryAddProfit)) {
            this.saveOrUpdateBatch(inventoryAddProfit);
        }
        //新增盘盈，保存库存事务
        List<InventoryBusiness> addProfitBusinessList = new ArrayList<>();
        addDetailList.forEach(detail -> {
            String key = detail.getOwerCode() + "_" + detail.getWarehouseCode() + "_" + detail.getWarehouseAreaCode() + "_"
                    + detail.getWarehouseLocationCode() + "_" + detail.getContainerCode() + "_" + detail.getMaterialCode() + "_"
                    + detail.getBatchCode() + "_" + detail.getMaterialProductionDate() + "_" + detail.getCustomField1() + "_" + detail.getCustomField2() + "_" + detail.getCustomField3();
            JSONObject jsonObject = addOriginalMap.get(key);
            BigDecimal originalExistingCount = jsonObject.getBigDecimal("originalExistingCount");
            Long toWarehouseLocationId = jsonObject.getLong("toWarehouseLocationId");
            String toWarehouseLocationCode = jsonObject.getStr("toWarehouseLocationCode");
            Long toContainerId = jsonObject.getLong("toContainerId");
            String toContainerCode = jsonObject.getStr("toContainerCode");
            BigDecimal toOperationCount = jsonObject.getBigDecimal("toOperationCount");

            String code = codeUtils.getCodeByRule(CodeConStants.INVENTORY_BUSINESS_CODE);//库存事务编号


            //addOriginalMap
            InventoryBusiness business = ConvertUtil.convert(detail, InventoryBusiness.class);
            business.setId(null);
            business.setCode(code);
            business.setBusinessTypeCode(5);
            business.setBusinessTypeName("盘点");
            business.setInventoryDetailId(detail.getId());
            business.setExistingCount(originalExistingCount);//原数量(原现有量)
            business.setToWarehouseLocationId(toWarehouseLocationId);
            business.setToWarehouseLocationCode(toWarehouseLocationCode);
            business.setToContainerId(toContainerId);
            business.setToContainerCode(toContainerCode);
            business.setToOperationCount(toOperationCount);//操作数量
            business.setCreateBy(CurrentUserUtil.getUsername());
            business.setCreateTime(new Date());
            business.setUpdateBy(null);
            business.setUpdateTime(null);
            addProfitBusinessList.add(business);
        });
        if (CollUtil.isNotEmpty(addProfitBusinessList)) {
            iInventoryBusinessService.saveOrUpdateBatch(addProfitBusinessList);
        }

        //正常
        inventoryNormal.forEach(inventoryCheckDetail -> {
            inventoryCheckDetail.setCheckStatus(3);//明细盘点状态(1未盘点、2已盘点、3已过账)
            inventoryCheckDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setUpdateTime(new Date());

            inventoryCheckDetail.setCheckSurplusCount(new BigDecimal("0"));//盘盈数量
            inventoryCheckDetail.setCheckLossesCount(new BigDecimal("0"));//盘亏数量
        });
        //正常，仅保存 盘点明细,不改动库存明细、库存事务
        if (CollUtil.isNotEmpty(inventoryNormal)) {
            this.saveOrUpdateBatch(inventoryNormal);
        }

        //盘盈
        inventoryProfit.forEach(inventoryCheckDetail -> {
            inventoryCheckDetail.setCheckStatus(3);//明细盘点状态(1未盘点、2已盘点、3已过账)
            BigDecimal checkCount = inventoryCheckDetail.getCheckCount();
            BigDecimal inventoryCount = inventoryCheckDetail.getInventoryCount();
            BigDecimal subtract = checkCount.subtract(inventoryCount);
            inventoryCheckDetail.setCheckSurplusCount(subtract);//盘盈数量 = 盘点数量 - 库存数量
            inventoryCheckDetail.setCheckLossesCount(new BigDecimal("0"));//盘亏数量
            inventoryCheckDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setUpdateTime(new Date());
        });
        //盘盈，保存 盘点明细
        if (CollUtil.isNotEmpty(inventoryProfit)) {
            this.saveOrUpdateBatch(inventoryProfit);
        }
        //盘盈，保存 库存明细
        List<InventoryDetail> profitDetailList = new ArrayList<>();
        Map<String, JSONObject> profitOriginalMap = new HashMap<>();//记录原始库存
        inventoryProfit.forEach(inventoryCheckDetail -> {
            Long owerId = inventoryCheckDetail.getOwerId();//货主
            String owerCode = inventoryCheckDetail.getOwerCode();
            Long warehouseId = inventoryCheckDetail.getWarehouseId();//仓库
            String warehouseCode = inventoryCheckDetail.getWarehouseCode();
            Long warehouseAreaId = inventoryCheckDetail.getWarehouseAreaId();//库区
            String warehouseAreaCode = inventoryCheckDetail.getWarehouseAreaCode();
            Long warehouseLocationId = inventoryCheckDetail.getWarehouseLocationId();//库位
            String warehouseLocationCode = inventoryCheckDetail.getWarehouseLocationCode();
            Long containerId = inventoryCheckDetail.getContainerId();//容器
            String containerCode = inventoryCheckDetail.getContainerCode();
            Long materialId = inventoryCheckDetail.getMaterialId();//物料
            String materialCode = inventoryCheckDetail.getMaterialCode();
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = inventoryCheckDetail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = inventoryCheckDetail.getMaterialProductionDate();//生产日期
            String customField1 = inventoryCheckDetail.getCustomField1();//自定义字段1
            String customField2 = inventoryCheckDetail.getCustomField2();//自定义字段2
            String customField3 = inventoryCheckDetail.getCustomField3();//自定义字段3

            QueryWrapper<InventoryDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(InventoryDetail::getOwerId, owerId);
            queryWrapper.lambda().eq(InventoryDetail::getOwerCode, owerCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseId, warehouseId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseCode, warehouseCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaId, warehouseAreaId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaCode, warehouseAreaCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationId, warehouseLocationId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationCode, warehouseLocationCode);
            queryWrapper.lambda().eq(InventoryDetail::getContainerId, containerId);
            queryWrapper.lambda().eq(InventoryDetail::getContainerCode, containerCode);
            queryWrapper.lambda().eq(InventoryDetail::getMaterialId, materialId);
            queryWrapper.lambda().eq(InventoryDetail::getMaterialCode, materialCode);
            if (ObjectUtil.isNotEmpty(batchCode)) {
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if (ObjectUtil.isNotEmpty(materialProductionDate)) {
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if (ObjectUtil.isNotEmpty(customField1)) {
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if (ObjectUtil.isNotEmpty(customField2)) {
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if (ObjectUtil.isNotEmpty(customField3)) {
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3);//group by 分组，保证唯一值
            InventoryDetail inventoryDetail = inventoryDetailService.getOne(queryWrapper);

            BigDecimal checkCount = inventoryCheckDetail.getCheckCount();
            BigDecimal inventoryCount = inventoryCheckDetail.getInventoryCount();
            if (inventoryCount.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("盘盈，库存数量不能为小于0");
            }
            BigDecimal subtract = checkCount.subtract(inventoryCount);//盘点数量 - 库存数量
            if (subtract.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("盘盈，盘点数量不能小于库存数量");
            }
            if (ObjectUtil.isEmpty(inventoryDetail)) {
                throw new IllegalArgumentException("盘点明细对应的库存明细不存在");
            }

            BigDecimal existingCount = inventoryDetail.getExistingCount();
            BigDecimal add = existingCount.add(subtract);
            inventoryDetail.setExistingCount(add);
            inventoryDetail.setWarehouseLocationStatus(1);//库位状态(0未冻结 1已冻结)  盘点过账，默认冻结库位库存
            inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryDetail.setUpdateTime(new Date());


            BigDecimal originalExistingCount = existingCount;
            BigDecimal toOperationCount = subtract;

            String key = inventoryDetail.getOwerCode() + "_" + inventoryDetail.getWarehouseCode() + "_" + inventoryDetail.getWarehouseAreaCode() + "_"
                    + inventoryDetail.getWarehouseLocationCode() + "_" + inventoryDetail.getContainerCode() + "_" + inventoryDetail.getMaterialCode() + "_"
                    + inventoryDetail.getBatchCode() + "_" + inventoryDetail.getMaterialProductionDate() + "_" + inventoryDetail.getCustomField1() + "_" + inventoryDetail.getCustomField2() + "_" + inventoryDetail.getCustomField3();
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("originalExistingCount", originalExistingCount);//原数量(原现有量)
            jsonObject.set("toWarehouseLocationId", inventoryDetail.getWarehouseLocationId());//目标库位ID
            jsonObject.set("toWarehouseLocationCode", inventoryDetail.getWarehouseCode());//目标库位编号
            jsonObject.set("toContainerId", inventoryDetail.getContainerId());//目标容器ID
            jsonObject.set("toContainerCode", inventoryDetail.getContainerCode());//目标容器号
            jsonObject.set("toOperationCount", toOperationCount);//操作数量(正负)
            profitOriginalMap.put(key, jsonObject);

            profitDetailList.add(inventoryDetail);
        });
        if (CollUtil.isNotEmpty(profitDetailList)) {
            boolean b = inventoryDetailService.saveOrUpdateBatch(profitDetailList);
        }
        //盘盈，保存 库存事务
        List<InventoryBusiness> profitBusinessList = new ArrayList<>();
        profitDetailList.forEach(detail -> {
            String key = detail.getOwerCode() + "_" + detail.getWarehouseCode() + "_" + detail.getWarehouseAreaCode() + "_"
                    + detail.getWarehouseLocationCode() + "_" + detail.getContainerCode() + "_" + detail.getMaterialCode() + "_"
                    + detail.getBatchCode() + "_" + detail.getMaterialProductionDate() + "_" + detail.getCustomField1() + "_" + detail.getCustomField2() + "_" + detail.getCustomField3();
            JSONObject jsonObject = profitOriginalMap.get(key);
            BigDecimal originalExistingCount = jsonObject.getBigDecimal("originalExistingCount");
            Long toWarehouseLocationId = jsonObject.getLong("toWarehouseLocationId");
            String toWarehouseLocationCode = jsonObject.getStr("toWarehouseLocationCode");
            Long toContainerId = jsonObject.getLong("toContainerId");
            String toContainerCode = jsonObject.getStr("toContainerCode");
            BigDecimal toOperationCount = jsonObject.getBigDecimal("toOperationCount");

            String code = codeUtils.getCodeByRule(CodeConStants.INVENTORY_BUSINESS_CODE);//库存事务编号

            //profitOriginalMap
            InventoryBusiness business = ConvertUtil.convert(detail, InventoryBusiness.class);
            business.setId(null);
            business.setCode(code);
            business.setBusinessTypeCode(5);
            business.setBusinessTypeName("盘点");
            business.setInventoryDetailId(detail.getId());
            business.setExistingCount(originalExistingCount);//原数量(原现有量)
            business.setToWarehouseLocationId(toWarehouseLocationId);
            business.setToWarehouseLocationCode(toWarehouseLocationCode);
            business.setToContainerId(toContainerId);
            business.setToContainerCode(toContainerCode);
            business.setToOperationCount(toOperationCount);//操作数量
            business.setCreateBy(CurrentUserUtil.getUsername());
            business.setCreateTime(new Date());
            business.setUpdateBy(null);
            business.setUpdateTime(null);
            profitBusinessList.add(business);
        });
        if (CollUtil.isNotEmpty(profitBusinessList)) {
            iInventoryBusinessService.saveOrUpdateBatch(profitBusinessList);
        }

        //盘亏
        inventoryLosses.forEach(inventoryCheckDetail -> {
            inventoryCheckDetail.setCheckStatus(3);//明细盘点状态(1未盘点、2已盘点、3已过账)
            BigDecimal checkCount = inventoryCheckDetail.getCheckCount();
            BigDecimal inventoryCount = inventoryCheckDetail.getInventoryCount();
            BigDecimal subtract = checkCount.subtract(inventoryCount);
            inventoryCheckDetail.setCheckSurplusCount(new BigDecimal("0"));//盘盈数量
            inventoryCheckDetail.setCheckLossesCount(subtract);//盘亏数量 = 盘点数量 - 库存数量，（因为 库存数量 > 盘点数量， 所以 这里是负数）
            inventoryCheckDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryCheckDetail.setUpdateTime(new Date());
        });
        //盘亏，保存 盘点明细
        if (CollUtil.isNotEmpty(inventoryLosses)) {
            this.saveOrUpdateBatch(inventoryLosses);
        }
        //盘亏，保存 库存明细
        List<InventoryDetail> lossesDetailList = new ArrayList<>();
        Map<String, JSONObject> lossesOriginalMap = new HashMap<>();//记录原始库存
        inventoryLosses.forEach(inventoryCheckDetail -> {
            Long owerId = inventoryCheckDetail.getOwerId();//货主
            String owerCode = inventoryCheckDetail.getOwerCode();
            Long warehouseId = inventoryCheckDetail.getWarehouseId();//仓库
            String warehouseCode = inventoryCheckDetail.getWarehouseCode();
            Long warehouseAreaId = inventoryCheckDetail.getWarehouseAreaId();//库区
            String warehouseAreaCode = inventoryCheckDetail.getWarehouseAreaCode();
            Long warehouseLocationId = inventoryCheckDetail.getWarehouseLocationId();//库位
            String warehouseLocationCode = inventoryCheckDetail.getWarehouseLocationCode();
            Long containerId = inventoryCheckDetail.getContainerId();//容器
            String containerCode = inventoryCheckDetail.getContainerCode();
            Long materialId = inventoryCheckDetail.getMaterialId();//物料
            String materialCode = inventoryCheckDetail.getMaterialCode();
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = inventoryCheckDetail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = inventoryCheckDetail.getMaterialProductionDate();//生产日期
            String customField1 = inventoryCheckDetail.getCustomField1();//自定义字段1
            String customField2 = inventoryCheckDetail.getCustomField2();//自定义字段2
            String customField3 = inventoryCheckDetail.getCustomField3();//自定义字段3

            QueryWrapper<InventoryDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(InventoryDetail::getOwerId, owerId);
            queryWrapper.lambda().eq(InventoryDetail::getOwerCode, owerCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseId, warehouseId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseCode, warehouseCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaId, warehouseAreaId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaCode, warehouseAreaCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationId, warehouseLocationId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationCode, warehouseLocationCode);
//            queryWrapper.lambda().eq(InventoryDetail::getContainerId, containerId);   //容器id 乜有
            queryWrapper.lambda().eq(InventoryDetail::getContainerCode, containerCode);
//            queryWrapper.lambda().eq(InventoryDetail::getMaterialId, materialId);  // 物料id
            queryWrapper.lambda().eq(InventoryDetail::getMaterialCode, materialCode);
            if (ObjectUtil.isNotEmpty(batchCode)) {
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if (ObjectUtil.isNotEmpty(materialProductionDate)) {
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if (ObjectUtil.isNotEmpty(customField1)) {
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if (ObjectUtil.isNotEmpty(customField2)) {
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if (ObjectUtil.isNotEmpty(customField3)) {
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3);//group by 分组，保证唯一值
            InventoryDetail inventoryDetail = inventoryDetailService.getOne(queryWrapper);

            BigDecimal checkCount = inventoryCheckDetail.getCheckCount();
            BigDecimal inventoryCount = inventoryCheckDetail.getInventoryCount();
            if (checkCount.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("盘亏，盘点数量不能为小于0");
            }
            if (inventoryCount.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("盘亏，库存数量不能为小于0");
            }
            BigDecimal subtract = checkCount.subtract(inventoryCount);//盘点数量 - 库存数量
            if (subtract.compareTo(new BigDecimal("0")) > 0) {
                throw new IllegalArgumentException("盘亏，盘点数量不能大于库存数量");
            }
            if (ObjectUtil.isEmpty(inventoryDetail)) {
                throw new IllegalArgumentException("盘点明细对应的库存明细不存在");
            }
            BigDecimal existingCount = inventoryDetail.getExistingCount();
            BigDecimal newExistingCount = existingCount.add(subtract);//新的现有量 = 原现有量 + subtract   (盘亏时，subtract为负数)
            //盘亏需要判断 库存的可用量
            BigDecimal allocationCount = inventoryDetail.getAllocationCount();
            if (allocationCount.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("库存明细，分配量不能为小于0");
            }
            BigDecimal pickingCount = inventoryDetail.getPickingCount();
            if (pickingCount.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("库存明细，拣货量不能为小于0");
            }
            BigDecimal usableCount = newExistingCount.subtract(allocationCount).subtract(pickingCount);//可用量(计算字段),可用量=现有量-分配量-拣货量，数据库不存在此字段
            if (usableCount.compareTo(new BigDecimal("0")) < 0) {
                throw new IllegalArgumentException("库存明细，计算后的可用量不能为小于0");
            }
            inventoryDetail.setExistingCount(newExistingCount);
            inventoryDetail.setWarehouseLocationStatus(1);//库位状态(0未冻结 1已冻结)  盘点过账，默认冻结库位库存
            inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryDetail.setUpdateTime(new Date());

            BigDecimal originalExistingCount = existingCount;
            BigDecimal toOperationCount = subtract;
            String key = inventoryDetail.getOwerCode() + "_" + inventoryDetail.getWarehouseCode() + "_" + inventoryDetail.getWarehouseAreaCode() + "_"
                    + inventoryDetail.getWarehouseLocationCode() + "_" + inventoryDetail.getContainerCode() + "_" + inventoryDetail.getMaterialCode() + "_"
                    + inventoryDetail.getBatchCode() + "_" + inventoryDetail.getMaterialProductionDate() + "_" + inventoryDetail.getCustomField1() + "_" + inventoryDetail.getCustomField2() + "_" + inventoryDetail.getCustomField3();
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("originalExistingCount", originalExistingCount);//原数量(原现有量)
            jsonObject.set("toWarehouseLocationId", inventoryDetail.getWarehouseLocationId());//目标库位ID
            jsonObject.set("toWarehouseLocationCode", inventoryDetail.getWarehouseCode());//目标库位编号
            jsonObject.set("toContainerId", inventoryDetail.getContainerId());//目标容器ID
            jsonObject.set("toContainerCode", inventoryDetail.getContainerCode());//目标容器号
            jsonObject.set("toOperationCount", toOperationCount);//操作数量(正负)
            lossesOriginalMap.put(key, jsonObject);

            lossesDetailList.add(inventoryDetail);

        });
        if (CollUtil.isNotEmpty(lossesDetailList)) {
            boolean b = inventoryDetailService.saveOrUpdateBatch(lossesDetailList);
        }
        //盘亏，保存 库存事务
        List<InventoryBusiness> lossesProfitBusinessList = new ArrayList<>();
        lossesDetailList.forEach(detail -> {
            String key = detail.getOwerCode() + "_" + detail.getWarehouseCode() + "_" + detail.getWarehouseAreaCode() + "_"
                    + detail.getWarehouseLocationCode() + "_" + detail.getContainerCode() + "_" + detail.getMaterialCode() + "_"
                    + detail.getBatchCode() + "_" + detail.getMaterialProductionDate() + "_" + detail.getCustomField1() + "_" + detail.getCustomField2() + "_" + detail.getCustomField3();
            JSONObject jsonObject = lossesOriginalMap.get(key);
            BigDecimal originalExistingCount = jsonObject.getBigDecimal("originalExistingCount");
            Long toWarehouseLocationId = jsonObject.getLong("toWarehouseLocationId");
            String toWarehouseLocationCode = jsonObject.getStr("toWarehouseLocationCode");
            Long toContainerId = jsonObject.getLong("toContainerId");
            String toContainerCode = jsonObject.getStr("toContainerCode");
            BigDecimal toOperationCount = jsonObject.getBigDecimal("toOperationCount");

            String code = codeUtils.getCodeByRule(CodeConStants.INVENTORY_BUSINESS_CODE);//库存事务编号

            //lossesOriginalMap
            InventoryBusiness business = ConvertUtil.convert(detail, InventoryBusiness.class);
            business.setId(null);
            business.setCode(code);
            business.setBusinessTypeCode(5);
            business.setBusinessTypeName("盘点");
            business.setInventoryDetailId(detail.getId());
            business.setExistingCount(originalExistingCount);//原数量(原现有量)
            business.setToWarehouseLocationId(toWarehouseLocationId);
            business.setToWarehouseLocationCode(toWarehouseLocationCode);
            business.setToContainerId(toContainerId);
            business.setToContainerCode(toContainerCode);
            business.setToOperationCount(toOperationCount);//操作数量
            business.setCreateBy(CurrentUserUtil.getUsername());
            business.setCreateTime(new Date());
            business.setUpdateBy(null);
            business.setUpdateTime(null);
            lossesProfitBusinessList.add(business);
        });
        if (CollUtil.isNotEmpty(lossesProfitBusinessList)) {
            iInventoryBusinessService.saveOrUpdateBatch(lossesProfitBusinessList);
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> queryDetailCountByInventoryCheckIds(List<Long> inventoryCheckIds) {
        return inventoryCheckDetailMapper.queryDetailCountByInventoryCheckIds(inventoryCheckIds);
    }

    @Override
    public List<Long> queryNotCheckIdList() {
        return inventoryCheckDetailMapper.queryNotCheckIdList();
    }

    @Override
    public boolean affirmPost(InventoryCheckDetailPostForm form) {
        for (int i = 0; i < form.getDetails().size(); i++) {

            InventoryCheckDetail inventoryCheckDetail = form.getDetails().get(i);
            inventoryCheckDetail.setCheckStatus(1);
            this.getById(inventoryCheckDetail);
        }
        return false;
    }

    @Override
    public boolean withdrawPost(InventoryCheckDetailPostForm form) {

        for (int i = 0; i < form.getDetails().size(); i++) {
            InventoryCheckDetail inventoryCheckDetail = form.getDetails().get(i);
            inventoryCheckDetail.setCheckStatus(2);
            this.getById(inventoryCheckDetail);
        }
        return false;
    }


}
