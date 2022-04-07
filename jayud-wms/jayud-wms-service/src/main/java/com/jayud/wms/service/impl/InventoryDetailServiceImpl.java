package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.fegin.AuthClient;
import com.jayud.wms.mapper.InventoryDetailMapper;
import com.jayud.wms.model.bo.InventoryAdjustmentForm;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.bo.InventoryMovementForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.InventoryReportVO;
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
 * 库存明细表 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Service
public class InventoryDetailServiceImpl extends ServiceImpl<InventoryDetailMapper, InventoryDetail> implements IInventoryDetailService {


    @Autowired
    private InventoryDetailMapper inventoryDetailMapper;
    @Autowired
    private IInventoryBusinessService iInventoryBusinessService;
    @Autowired
    private IWarehouseLocationService warehouseLocationService;
    @Autowired
    private IWarehouseAreaService warehouseAreaService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private CodeUtils codeUtils;
    @Autowired
    private SysUserOwerPermissionService sysUserOwerPermissionService;
    @Autowired
    private SysUserWarehousePermissionService sysUserWarehousePermissionService;
    @Autowired
    private AuthClient authClient;

    @Override
    public IPage selectMaterialPage(InventoryDetail inventoryDetail, Integer pageNo, Integer pageSize, HttpServletRequest req) {
        Page<InventoryDetail> page=new Page<InventoryDetail>(pageNo, pageSize);
        IPage<InventoryDetail> pageList= inventoryDetailMapper.selectMaterialPage(page, inventoryDetail);
        return pageList;
    }

    @Override
    public List<LinkedHashMap<String, Object>> exportInventoryMaterialForExcel(Map<String, Object> paramMap) {
        AuthUserDetail authUserDetail = CurrentUserUtil.getUserDetail();
        List<String> owerIdList = sysUserOwerPermissionService.getOwerIdByUserId(String.valueOf(authUserDetail.getId()));
        paramMap.put("owerIdList",owerIdList);
        List<String> warehouseIdList = sysUserWarehousePermissionService.getWarehouseIdByUserId(String.valueOf(authUserDetail.getId()));
        paramMap.put("warehouseIdList",warehouseIdList);
        return this.baseMapper.exportInventoryMaterialForExcel(paramMap);
    }

    @Override
    public IPage<InventoryDetail> selectPage(InventoryDetail inventoryDetail,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){
        AuthUserDetail authUserDetail = CurrentUserUtil.getUserDetail();
//        List<String> owerIdList = sysUserOwerPermissionService.getOwerIdByUserId(String.valueOf(authUserDetail.getId()));
//        List<String> warehouseIdList = sysUserWarehousePermissionService.getWarehouseIdByUserId(String.valueOf(authUserDetail.getId()));
//        inventoryDetail.setOwerIdList(owerIdList);
//        inventoryDetail.setWarehouseIdList(warehouseIdList);

        Page<InventoryDetail> page=new Page<InventoryDetail>(pageNo, pageSize);
        IPage<InventoryDetail> pageList= inventoryDetailMapper.pageList(page, inventoryDetail);
        return pageList;
    }

    @Override
    public List<InventoryDetail> selectList(InventoryDetail inventoryDetail){
        return inventoryDetailMapper.list(inventoryDetail);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryInventoryDetailForExcel(Map<String, Object> paramMap) {
        AuthUserDetail authUserDetail = CurrentUserUtil.getUserDetail();
        List<String> owerIdList = sysUserOwerPermissionService.getOwerIdByUserId(String.valueOf(authUserDetail.getId()));
        paramMap.put("owerIdList",owerIdList);
        List<String> warehouseIdList = sysUserWarehousePermissionService.getWarehouseIdByUserId(String.valueOf(authUserDetail.getId()));
        paramMap.put("warehouseIdList",warehouseIdList);
        return this.baseMapper.queryInventoryDetailForExcel(paramMap);
    }

    /**
     * 入库
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult input(List<InventoryDetailForm> list) {

        List<InventoryDetail> detailList = new ArrayList<>();//准备数据
        Map<String, JSONObject> originalMap = new HashMap<>();//记录原始库存
        if(CollUtil.isEmpty(list)){
            throw new IllegalArgumentException("库存操作信息不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) + (入仓号 + 重量 + 体积 + 单位) -> 确定唯一组合值
        List<String> collect = list.stream()
                .map(k -> (k.getOwerCode()+"_"+k.getWarehouseCode()+"_"+k.getWarehouseAreaCode()+"_"+k.getWarehouseLocationCode()+"_"+k.getContainerCode()+"_"+k.getMaterialCode()+"_"+k.getBatchCode()+"_"+k.getMaterialProductionDate()+"_"+k.getCustomField1()+"_"+k.getCustomField2()+"_"+k.getCustomField3()+"_"+k.getInWarehouseNumber()+"_"+k.getWeight()+"_"+k.getVolume()+"_"+k.getUnit()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3)+ (入仓号+重量+体积+单位),唯一组合值不能重复");
        }
        list.forEach(detail -> {
            //货主 + 仓库 + 库区 + 库位 + 容器 + 物料，确认库位的库存信息
//            Long owerId = detail.getOwerId();//货主ID
//            String owerCode = detail.getOwerCode();//货主编号
//            if(ObjectUtil.isEmpty(owerCode)){
//                throw new IllegalArgumentException("货主不能为空");
//            }
            Long warehouseId = detail.getWarehouseId();//仓库ID
            String warehouseCode = detail.getWarehouseCode();//仓库编号
            if(ObjectUtil.isEmpty(warehouseCode)){
                throw new IllegalArgumentException("仓库不能为空");
            }
            Long warehouseAreaId = detail.getWarehouseAreaId();//库区ID
            String warehouseAreaCode = detail.getWarehouseAreaCode();//库区编号
            if(ObjectUtil.isEmpty(warehouseAreaCode)){
                throw new IllegalArgumentException("库区不能为空");
            }
            Long warehouseLocationId = detail.getWarehouseLocationId();//库位ID
            String warehouseLocationCode = detail.getWarehouseLocationCode();//库位编号
            if(ObjectUtil.isEmpty(warehouseLocationCode)){
                throw new IllegalArgumentException("库位不能为空");
            }
            Long containerId = detail.getContainerId();//容器id
            String containerCode = detail.getContainerCode();//容器号
//            if(ObjectUtil.isEmpty(containerCode)){
//                throw new IllegalArgumentException("容器不能为空");
//            }
            Long materialId = detail.getMaterialId();//物料ID
            String materialCode = detail.getMaterialCode();//物料编号
            if(ObjectUtil.isEmpty(materialCode)){
                throw new IllegalArgumentException("物料不能为空");
            }
            BigDecimal operationCount = detail.getOperationCount();//入库操作数量
            if(ObjectUtil.isEmpty(operationCount)){
                throw new IllegalArgumentException("操作数量不能为空");
            }
            if(operationCount.compareTo(new BigDecimal("0")) <= 0){
                throw new IllegalArgumentException("操作数量必须大于0");
            }

            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = detail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = detail.getMaterialProductionDate();//生产日期
            String customField1 = detail.getCustomField1();//自定义字段1
            String customField2 = detail.getCustomField2();//自定义字段2
            String customField3 = detail.getCustomField3();//自定义字段3


            QueryWrapper<InventoryDetail> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().eq(InventoryDetail::getOwerId, owerId);
//            queryWrapper.lambda().eq(InventoryDetail::getOwerCode, owerCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseId, warehouseId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseCode, warehouseCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaId, warehouseAreaId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaCode, warehouseAreaCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationId, warehouseLocationId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationCode, warehouseLocationCode);
            queryWrapper.lambda().eq(InventoryDetail::getContainerCode, containerCode);
            queryWrapper.lambda().eq(InventoryDetail::getMaterialCode, materialCode);
            queryWrapper.lambda().eq(InventoryDetail::getMaterialName, detail.getMaterialName());
            queryWrapper.lambda().eq(InventoryDetail::getInWarehouseNumber, detail.getInWarehouseNumber());
            queryWrapper.lambda().eq(InventoryDetail::getWeight, detail.getWeight());
            queryWrapper.lambda().eq(InventoryDetail::getVolume, detail.getVolume());
            queryWrapper.lambda().eq(InventoryDetail::getUnit, detail.getUnit());
            if(ObjectUtil.isNotEmpty(batchCode)){
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if(ObjectUtil.isNotEmpty(materialProductionDate)){
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if(ObjectUtil.isNotEmpty(customField1)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if(ObjectUtil.isNotEmpty(customField2)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if(ObjectUtil.isNotEmpty(customField3)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3,
                    InventoryDetail::getInWarehouseNumber, InventoryDetail::getWeight, InventoryDetail::getVolume, InventoryDetail::getUnit);//group by 分组，保证唯一值
            InventoryDetail inventoryDetail = this.baseMapper.selectOne(queryWrapper);

            BigDecimal originalExistingCount = new BigDecimal("0");//记录原始库存的物料数量
            BigDecimal toOperationCount = new BigDecimal("0");//记录操作的数量
            if(ObjectUtil.isNotEmpty(inventoryDetail)){

                Integer warehouseLocationStatus = inventoryDetail.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
                if(warehouseLocationStatus == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
                }
                Integer warehouseLocationStatus2 = inventoryDetail.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
                if(warehouseLocationStatus2 == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
                }

                originalExistingCount = inventoryDetail.getExistingCount();
                //库位上有此物料，在库存明细的现有量上进行追加
                BigDecimal existingCount = inventoryDetail.getExistingCount().add(operationCount);
                inventoryDetail.setExistingCount(existingCount);
                inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setUpdateTime(new Date());
            }else{
                originalExistingCount = new BigDecimal("0");
                //库位上无此物料，直接新增
                inventoryDetail = ConvertUtil.convert(detail, InventoryDetail.class);
                inventoryDetail.setId(null);
                BigDecimal existingCount = operationCount;
                inventoryDetail.setExistingCount(existingCount);
                inventoryDetail.setCreateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setCreateTime(new Date());
            }
            toOperationCount = operationCount;

            String key = detail.getOwerCode()+"_"+detail.getWarehouseCode()+"_"+detail.getWarehouseAreaCode()+"_"
                    +detail.getWarehouseLocationCode()+"_"+detail.getContainerCode()+"_"+detail.getMaterialCode()+"_"
                    +detail.getBatchCode()+"_"+detail.getMaterialProductionDate()+"_"+detail.getCustomField1()+"_"+detail.getCustomField2()+"_"+detail.getCustomField3()+"_"
                    +detail.getInWarehouseNumber()+"_"+detail.getWeight()+"_"+detail.getVolume()+"_"+detail.getUnit();
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("originalExistingCount", originalExistingCount);
            jsonObject.set("toOperationCount", toOperationCount);
            originalMap.put(key, jsonObject);
            detailList.add(inventoryDetail);//库存明细
        });


        //保存库存明细表
        boolean b = this.saveOrUpdateBatch(detailList);

        //保存库存事务表
        List<InventoryBusiness> businessList = new ArrayList<>();
        detailList.forEach(detail -> {
            String key = detail.getOwerCode()+"_"+detail.getWarehouseCode()+"_"+detail.getWarehouseAreaCode()+"_"
                    +detail.getWarehouseLocationCode()+"_"+detail.getContainerCode()+"_"+detail.getMaterialCode()+"_"
                    +detail.getBatchCode()+"_"+detail.getMaterialProductionDate()+"_"+detail.getCustomField1()+"_"+detail.getCustomField2()+"_"+detail.getCustomField3()+"_"
                    +detail.getInWarehouseNumber()+"_"+detail.getWeight()+"_"+detail.getVolume()+"_"+detail.getUnit();
            JSONObject jsonObject = originalMap.get(key);
            BigDecimal originalExistingCount = jsonObject.getBigDecimal("originalExistingCount");
            BigDecimal toOperationCount = jsonObject.getBigDecimal("toOperationCount");

            String code = codeUtils.getCodeByRule(CodeConStants.INVENTORY_BUSINESS_CODE);//库存事务编号

            InventoryBusiness business = ConvertUtil.convert(detail, InventoryBusiness.class);
            business.setId(null);
            business.setCode(code);
            business.setBusinessTypeCode(1);
            business.setBusinessTypeName("入库");
            business.setInventoryDetailId(detail.getId());
            business.setExistingCount(originalExistingCount);//原数量(原现有量)
            business.setToWarehouseLocationId(detail.getWarehouseLocationId());
            business.setToWarehouseLocationCode(detail.getWarehouseLocationCode());
            business.setToContainerId(detail.getContainerId());
            business.setToContainerCode(detail.getContainerCode());
            business.setToOperationCount(toOperationCount);//操作数量
            business.setCreateBy(CurrentUserUtil.getUsername());
            business.setCreateTime(new Date());
            business.setUpdateBy(null);
            business.setUpdateTime(null);
            businessList.add(business);
        });
        iInventoryBusinessService.saveOrUpdateBatch(businessList);
        return BaseResult.ok();
    }

    /**
     * 出库分配 -> 进行库存占用 分配量
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult outputAllocation(List<InventoryDetailForm> list) {

        List<InventoryDetail> detailList = new ArrayList<>();//准备数据
        if(CollUtil.isEmpty(list)){
            throw new IllegalArgumentException("库存操作信息不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) + (入仓号 + 重量 + 体积 + 单位) -> 确定唯一组合值
        List<String> collect = list.stream()
                .map(k -> (k.getOwerCode()+"_"+k.getWarehouseCode()+"_"+k.getWarehouseAreaCode()+"_"+k.getWarehouseLocationCode()+"_"+k.getContainerCode()+"_"+k.getMaterialCode()+"_"+k.getBatchCode()+"_"+k.getMaterialProductionDate()+"_"+k.getCustomField1()+"_"+k.getCustomField2()+"_"+k.getCustomField3()+"_"+k.getInWarehouseNumber()+"_"+k.getWeight()+"_"+k.getVolume()+"_"+k.getUnit()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3)+ (入仓号+重量+体积+单位),唯一组合值不能重复");
        }
        list.forEach(detail -> {
            //货主 + 仓库 + 库区 + 库位 + 容器 + 物料，确认库位的库存信息
            Long owerId = detail.getOwerId();//货主ID
            String owerCode = detail.getOwerCode();//货主编号
            if(ObjectUtil.isEmpty(owerCode)){
                throw new IllegalArgumentException("货主不能为空");
            }
            Long warehouseId = detail.getWarehouseId();//仓库ID
            String warehouseCode = detail.getWarehouseCode();//仓库编号
            if(ObjectUtil.isEmpty(warehouseCode)){
                throw new IllegalArgumentException("仓库不能为空");
            }
            Long warehouseAreaId = detail.getWarehouseAreaId();//库区ID
            String warehouseAreaCode = detail.getWarehouseAreaCode();//库区编号
            if(ObjectUtil.isEmpty(warehouseAreaCode)){
                throw new IllegalArgumentException("库区不能为空");
            }
            Long warehouseLocationId = detail.getWarehouseLocationId();//库位ID
            String warehouseLocationCode = detail.getWarehouseLocationCode();//库位编号
            if(ObjectUtil.isEmpty(warehouseLocationCode)){
                throw new IllegalArgumentException("库位不能为空");
            }
            Long containerId = detail.getContainerId();//容器id
            String containerCode = detail.getContainerCode();//容器号
            if(ObjectUtil.isEmpty(containerCode)){
                throw new IllegalArgumentException("容器不能为空");
            }
            Long materialId = detail.getMaterialId();//物料ID
            String materialCode = detail.getMaterialCode();//物料编号
            if(ObjectUtil.isEmpty(materialCode)){
                throw new IllegalArgumentException("物料不能为空");
            }
            Long materialTypeId = detail.getMaterialTypeId();//物料类型ID
            String materialType = detail.getMaterialType();//物料类型
            BigDecimal operationCount = detail.getOperationCount();//出库操作数量
            if(ObjectUtil.isEmpty(operationCount)){
                throw new IllegalArgumentException("操作数量不能为空");
            }
            if(operationCount.compareTo(new BigDecimal("0")) <= 0){
                throw new IllegalArgumentException("操作数量必须大于0");
            }
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = detail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = detail.getMaterialProductionDate();//生产日期
            String customField1 = detail.getCustomField1();//自定义字段1
            String customField2 = detail.getCustomField2();//自定义字段2
            String customField3 = detail.getCustomField3();//自定义字段3

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
            queryWrapper.lambda().eq(InventoryDetail::getInWarehouseNumber, detail.getInWarehouseNumber());
            queryWrapper.lambda().eq(InventoryDetail::getWeight, detail.getWeight());
            queryWrapper.lambda().eq(InventoryDetail::getVolume, detail.getVolume());
            queryWrapper.lambda().eq(InventoryDetail::getUnit, detail.getUnit());
            if(ObjectUtil.isNotEmpty(batchCode)){
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if(ObjectUtil.isNotEmpty(materialProductionDate)){
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if(ObjectUtil.isNotEmpty(customField1)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if(ObjectUtil.isNotEmpty(customField2)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if(ObjectUtil.isNotEmpty(customField3)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3);//group by 分组，保证唯一值
            InventoryDetail inventoryDetail = this.baseMapper.selectOne(queryWrapper);

            if(ObjectUtil.isNotEmpty(inventoryDetail)){
                Integer warehouseLocationStatus = inventoryDetail.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
                if(warehouseLocationStatus == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
                }
                Integer warehouseLocationStatus2 = inventoryDetail.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
                if(warehouseLocationStatus2 == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
                }

                //库位上有此物料，在库存明细的`分配量`上进行追加，需要先判断可用量
                BigDecimal usableCount = inventoryDetail.getUsableCount();//可用量=现有量-分配量-拣货量，数据库不存在此字段
                if(usableCount.compareTo(operationCount) < 0){
                    throw new IllegalArgumentException("可用量 不能小于 出库操作数量");
                }
                BigDecimal allocationCount = inventoryDetail.getAllocationCount().add(operationCount);
                inventoryDetail.setAllocationCount(allocationCount);
                inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setUpdateTime(new Date());
            }else{
                //库位上无此物料，直接失败
                throw new IllegalArgumentException("货主+仓库+库区+库位+容器+物料，库位上无此物料");
            }
            detailList.add(inventoryDetail);//库存明细
        });


        //保存库存明细表
        boolean b = this.saveOrUpdateBatch(detailList);

        //保存库存事务表 这里的出库，只进行分配占用，不记录库存事务，在真正进行 现有量变动时，再记录库存事务
        //iInventoryBusinessService.saveOrUpdateBatch(businessList);
        return BaseResult.ok();
    }

    /**
     * 库存调整
     * @param bo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult adjustment(InventoryAdjustmentForm bo) {
        Long id = bo.getId();
        InventoryDetail inventoryDetail = this.getById(id);
        if(ObjectUtil.isEmpty(inventoryDetail)){
            throw new IllegalArgumentException("库存明细不存在");
        }
        BigDecimal originalExistingCount = inventoryDetail.getExistingCount();// 原始现有量
        BigDecimal toExistingCount = bo.getToExistingCount();//目标现有量(期待的现有量)
        if(ObjectUtil.isEmpty(toExistingCount)){
            throw new IllegalArgumentException("目标现有量，不能为空");
        }
        if(toExistingCount.compareTo(new BigDecimal("0")) < 0){
            throw new IllegalArgumentException("目标现有量，不能小于0");
        }
        BigDecimal subtract = toExistingCount.subtract(originalExistingCount);//操作数量（调整数量） = 目标现有量 - 原始现有量
        bo.setOperationCount(subtract);
        BigDecimal operationCount = bo.getOperationCount();//操作数量（调整数量）
        Integer warehouseLocationStatus = inventoryDetail.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
//        if(warehouseLocationStatus == 1){
//            throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
//        }
//        Integer warehouseLocationStatus2 = inventoryDetail.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
//        if(warehouseLocationStatus2 == 1){
//            throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
//        }

        BigDecimal usableCount = inventoryDetail.getUsableCount();//可用量(计算字段),可用量=现有量-分配量-拣货量，数据库不存在此字段
        if(ObjectUtil.isEmpty(operationCount)){
            throw new IllegalArgumentException("操作数量不能为空");
        }
        //操作数量 可以是正数，也可以是负数，但不能为0
        if(operationCount.compareTo(new BigDecimal("0")) == 0 ){
            throw new IllegalArgumentException("操作数量不能为0");
        }
        BigDecimal calculate = usableCount.add(operationCount);//计算 新的可用量 = 可用量 + 操作数量
        if(calculate.compareTo(new BigDecimal("0")) < 0){
            throw new IllegalArgumentException("调整后的可用量不能小于0");
        }

        //修改库存明细
        BigDecimal existingCount = inventoryDetail.getExistingCount();
        BigDecimal newExistingCount = existingCount.add(operationCount);//新的现有量 = 现有量 + 操作数量
        inventoryDetail.setExistingCount(newExistingCount);//现有量
//        inventoryDetail.setUnit(bo.getUnit());//单位
//        inventoryDetail.setWeight(bo.getWeight());//重量
//        inventoryDetail.setVolume(bo.getVolume());//体积
        inventoryDetail.setRemark(bo.getRemark()); //调整原因
        this.saveOrUpdate(inventoryDetail);

        BaseResult  baseResult = authClient.getOrderFeign(CodeConStants.INVENTORY_BUSINESS_CODE, new Date());
        String code = baseResult.getResult().toString();

//        String code = codeUtils.getCodeByRule(CodeConStants.INVENTORY_BUSINESS_CODE);//库存事务编号

        //
        //保存库存事务表
        InventoryBusiness business = ConvertUtil.convert(inventoryDetail, InventoryBusiness.class);
        business.setId(null);
        business.setCode(code);
        business.setBusinessTypeCode(3);
        business.setBusinessTypeName("调整");
        business.setInventoryDetailId(inventoryDetail.getId());
        business.setExistingCount(existingCount);//原数量(原现有量)
        business.setToWarehouseLocationId(inventoryDetail.getWarehouseLocationId());
        business.setToWarehouseLocationCode(inventoryDetail.getWarehouseLocationCode());
        business.setToContainerId(inventoryDetail.getContainerId());
        business.setToContainerCode(inventoryDetail.getContainerCode());
        business.setToOperationCount(operationCount);//操作数量(正负)
        business.setCreateBy(CurrentUserUtil.getUsername());
        business.setCreateTime(new Date());
        business.setUpdateBy(null);
        business.setUpdateTime(null);

        iInventoryBusinessService.saveOrUpdate(business);

        return BaseResult.ok();
    }

    /**
     * 库存移动
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult movement(List<InventoryMovementForm> list) {

        if(CollUtil.isEmpty(list)){
            throw new IllegalArgumentException("库存移动信息，不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) -> 确定唯一组合值
        List<String> collect = list.stream()
                .map(k -> (k.getOwerCode()+"_"+k.getWarehouseCode()+"_"+k.getWarehouseAreaCode()+"_"+k.getWarehouseLocationCode()+"_"+k.getContainerCode()+"_"+k.getMaterialCode()+"_"+k.getBatchCode()+"_"+k.getMaterialProductionDate()+"_"+k.getCustomField1()+"_"+k.getCustomField2()+"_"+k.getCustomField3()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3),唯一组合值不能重复");
        }

        //考虑多个原库位 转移到 同一个目标库位 的情况，直接不能进行批量保存，要先进行数据汇总，统一保存
        List<InventoryDetail> fromList = new ArrayList<>();
        List<InventoryDetail> toList = new ArrayList<>();
        Map<String, BigDecimal> toMap = new HashMap<>();
        Map<String, JSONObject> fromOriginalMap = new HashMap<>();//记录from原始库存
//        Map<String, JSONObject> toOriginalMap = new HashMap<>();//记录to原始库存
        list.forEach(movement -> {
            //原库位信息
            Long inventoryDetailId = movement.getInventoryDetailId();
            InventoryDetail from = this.getById(inventoryDetailId);
            Integer formWarehouseLocationStatus = from.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
            if(formWarehouseLocationStatus == 1){
                throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
            }
            Integer formWarehouseLocationStatus2 = from.getWarehouseLocationStatus2();
            if(formWarehouseLocationStatus2 == 1){
                throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
            }
            //目标库位信息
            Long materialId = movement.getMaterialId();//物料ID
            String materialCode = movement.getMaterialCode();//物料编号

            Long toContainerId = movement.getToContainerId();//目标 容器id
            String toContainerCode = movement.getToContainerCode();//目标 容器号
            if(ObjectUtil.isEmpty(toContainerCode)){
                throw new IllegalArgumentException("目标容器号不能为空");
            }
            Long toWarehouseLocationId = movement.getToWarehouseLocationId();//目标 库位ID
            String toWarehouseLocationCode = movement.getToWarehouseLocationCode();//目标 库位编号
            WarehouseLocation location = warehouseLocationService.getById(toWarehouseLocationId);
            if(ObjectUtil.isEmpty(location)){
                throw new IllegalArgumentException("目标库位不存在");
            }
            Long toWarehouseAreaId = location.getWarehouseAreaId();//目标 库区ID
            WarehouseArea area = warehouseAreaService.getById(toWarehouseAreaId);
            String toWarehouseAreaCode = area.getCode();//目标 库区编号
            String toWarehouseAreaName = area.getName();//目标 库区名称
            if(ObjectUtil.isEmpty(toWarehouseAreaCode)){
                throw new IllegalArgumentException("找不到目标库位对应的库区");
            }
            Long toWarehouseId = location.getWarehouseId();//目标 仓库ID
            Warehouse warehouse = warehouseService.getById(toWarehouseId);
            String toWarehouseCode = warehouse.getCode();//目标 仓库编号
            String toWarehouseName = warehouse.getName();//目标 仓库名称
            if(ObjectUtil.isEmpty(toWarehouseCode)){
                throw new IllegalArgumentException("找不到目标库位对应的仓库");
            }
            Long owerId = movement.getOwerId();//货主ID
            String owerCode = movement.getOwerCode();//货主编号
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = movement.getBatchCode();//批次号
            LocalDateTime materialProductionDate = movement.getMaterialProductionDate();//生产日期
            String customField1 = movement.getCustomField1();//自定义字段1
            String customField2 = movement.getCustomField2();//自定义字段2
            String customField3 = movement.getCustomField3();//自定义字段3

            if(!from.getWarehouseId().equals(toWarehouseId)){
                throw new IllegalArgumentException("原库位 和 目标库位，不属于同一个仓库，不能操作");
            }
            if(from.getWarehouseLocationCode().equals(toWarehouseLocationCode)){
                throw new IllegalArgumentException("原库位 和 目标库位，库位号不能相同");
            }
            BigDecimal toOperationCount = movement.getToOperationCount();//操作数量(正负)
            if(ObjectUtil.isEmpty(toOperationCount)){
                throw new IllegalArgumentException("操作数量不能为空");
            }
            if(toOperationCount.compareTo(new BigDecimal("0")) <= 0){
                throw new IllegalArgumentException("操作数量必须大于0");
            }
            BigDecimal usableCount = from.getUsableCount();//可用量(计算字段),可用量=现有量-分配量-拣货量，数据库不存在此字段
            if(usableCount.compareTo(toOperationCount) < 0){
                throw new IllegalArgumentException("可用量不能小于操作数量");
            }

            BigDecimal fromExistingCount = from.getExistingCount();//from现有量
            BigDecimal subtract = fromExistingCount.subtract(toOperationCount);//现有量 - 操作量
            from.setExistingCount(subtract);
            fromList.add(from);//from 原库位list

            String fromKey = from.getOwerCode()+"_"+from.getWarehouseCode()+"_"+from.getWarehouseAreaCode()+"_"
                    +from.getWarehouseLocationCode()+"_"+from.getContainerCode()+"_"+from.getMaterialCode()+"_"
                    +from.getBatchCode()+"_"+from.getMaterialProductionDate()+"_"+from.getCustomField1()+"_"+from.getCustomField2()+"_"+from.getCustomField3();
            JSONObject formJsonObject = new JSONObject();
            formJsonObject.set("originalExistingCount", fromExistingCount);//原始数量 原数量(原现有量)
            formJsonObject.set("toWarehouseLocationId", toWarehouseLocationId);//目标库位ID
            formJsonObject.set("toWarehouseLocationCode", toWarehouseLocationCode);//目标库位编号
            formJsonObject.set("toContainerId", toContainerId);//目标容器ID
            formJsonObject.set("toContainerCode", toContainerCode);//目标容器号
            formJsonObject.set("toOperationCount", toOperationCount);//操作数量  从原库位 移动了 toOperationCount个 到目标库位
            fromOriginalMap.put(fromKey, formJsonObject);

            //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3)，确认 目标库位的库存信息
            QueryWrapper<InventoryDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(InventoryDetail::getOwerId, owerId);
            queryWrapper.lambda().eq(InventoryDetail::getOwerCode, owerCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseId, toWarehouseId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseCode, toWarehouseCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaId, toWarehouseAreaId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseAreaCode, toWarehouseAreaCode);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationId, toWarehouseLocationId);
            queryWrapper.lambda().eq(InventoryDetail::getWarehouseLocationCode, toWarehouseLocationCode);
            queryWrapper.lambda().eq(InventoryDetail::getContainerId, toContainerId);
            queryWrapper.lambda().eq(InventoryDetail::getContainerCode, toContainerCode);
            queryWrapper.lambda().eq(InventoryDetail::getMaterialId, materialId);
            queryWrapper.lambda().eq(InventoryDetail::getMaterialCode, materialCode);
            if(ObjectUtil.isNotEmpty(batchCode)){
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if(ObjectUtil.isNotEmpty(materialProductionDate)){
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if(ObjectUtil.isNotEmpty(customField1)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if(ObjectUtil.isNotEmpty(customField2)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if(ObjectUtil.isNotEmpty(customField3)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3);//group by 分组，保证唯一值
            InventoryDetail to = this.baseMapper.selectOne(queryWrapper);

            if(ObjectUtil.isNotEmpty(to)){
                Integer toWarehouseLocationStatus = to.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
                if(toWarehouseLocationStatus == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
                }
                Integer toWarehouseLocationStatus2 = to.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
                if(toWarehouseLocationStatus2 == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
                }

                //库位上有此物料
                to.setUpdateBy(CurrentUserUtil.getUsername());
                to.setUpdateTime(new Date());
            }else{
                //库位上无此物料
                to = ConvertUtil.convert(from, InventoryDetail.class);
                to.setId(null);
                to.setWarehouseAreaId(toWarehouseAreaId);//库区ID
                to.setWarehouseAreaCode(toWarehouseAreaCode);//库区编号
                to.setWarehouseAreaName(toWarehouseAreaName);//库区名称
                to.setWarehouseLocationId(toWarehouseLocationId);//库位ID
                to.setWarehouseLocationCode(toWarehouseLocationCode);//库位编号
                to.setContainerId(toContainerId);//容器id
                to.setContainerCode(toContainerCode);//容器号
                to.setExistingCount(new BigDecimal("0"));//现有量
                to.setAllocationCount(new BigDecimal("0"));//分配量
                to.setPickingCount(new BigDecimal("0"));//拣货量
                to.setRemark(null);//备注信息
                to.setCreateBy(CurrentUserUtil.getUsername());
                to.setCreateTime(new Date());
                to.setUpdateBy(null);
                to.setUpdateTime(null);
            }

            BigDecimal toExistingCount = to.getExistingCount();//to 现有量

            //操作数量 库存移动
            String toKey = to.getOwerCode()+"_"+to.getWarehouseCode()+"_"+to.getWarehouseAreaCode()+"_"
                    +to.getWarehouseLocationCode()+"_"+to.getContainerCode()+"_"+to.getMaterialCode()+"_"
                    +to.getBatchCode()+"_"+to.getMaterialProductionDate()+"_"+to.getCustomField1()+"_"+to.getCustomField2()+"_"+to.getCustomField3();
            BigDecimal sum = toMap.get(toKey);
            if(ObjectUtil.isNotEmpty(sum)){
                BigDecimal add = sum.add(toOperationCount);
                toMap.put(toKey, add);
            }else{
                toList.add(to);//这里只记录新增的目标库位记录 考虑多个原库位 转移到 同一个目标库位 的情况，直接不能进行批量保存，要先进行数据汇总，统一保存
                toMap.put(toKey, toOperationCount);
            }
        });

        //保存库存明细表
        fromList.forEach(from -> {
            from.setUpdateBy(CurrentUserUtil.getUsername());
            from.setUpdateTime(new Date());
        });
        this.saveOrUpdateBatch(fromList);
        toList.forEach(to -> {
            String toKey = to.getOwerCode()+"_"+to.getWarehouseCode()+"_"+to.getWarehouseAreaCode()+"_"
                    +to.getWarehouseLocationCode()+"_"+to.getContainerCode()+"_"+to.getMaterialCode()+"_"
                    +to.getBatchCode()+"_"+to.getMaterialProductionDate()+"_"+to.getCustomField1()+"_"+to.getCustomField2()+"_"+to.getCustomField3();
            BigDecimal toOperationCount = toMap.get(toKey);//获取新增的操作数量，做合并

            BigDecimal existingCount = to.getExistingCount();
            BigDecimal add = existingCount.add(toOperationCount);//现有量 + 操作量
            to.setExistingCount(add);
        });
        this.saveOrUpdateBatch(toList);

        //保存库存事务表
        List<InventoryBusiness> fromBusinessList = new ArrayList<>();
        fromList.forEach(from -> {
            String fromKey = from.getOwerCode()+"_"+from.getWarehouseCode()+"_"+from.getWarehouseAreaCode()+"_"
                    +from.getWarehouseLocationCode()+"_"+from.getContainerCode()+"_"+from.getMaterialCode()+"_"
                    +from.getBatchCode()+"_"+from.getMaterialProductionDate()+"_"+from.getCustomField1()+"_"+from.getCustomField2()+"_"+from.getCustomField3();
            JSONObject jsonObject = fromOriginalMap.get(fromKey);
            BigDecimal originalExistingCount = jsonObject.getBigDecimal("originalExistingCount");//原始数量 原数量(原现有量)
            Long toWarehouseLocationId = jsonObject.getLong("toWarehouseLocationId");//目标库位ID
            String toWarehouseLocationCode = jsonObject.getStr("toWarehouseLocationCode");//目标库位编号
            Long toContainerId = jsonObject.getLong("toContainerId");//目标容器ID
            String toContainerCode = jsonObject.getStr("toContainerCode");//目标容器号
            BigDecimal toOperationCount = jsonObject.getBigDecimal("toOperationCount");//操作数量  从原库位 移动了 toOperationCount个 到目标库位

            String code = codeUtils.getCodeByRule(CodeConStants.INVENTORY_BUSINESS_CODE);//库存事务编号

            InventoryBusiness business = ConvertUtil.convert(from, InventoryBusiness.class);
            business.setId(null);
            business.setCode(code);
            business.setBusinessTypeCode(4);
            business.setBusinessTypeName("移动");
            business.setInventoryDetailId(from.getId());
            business.setExistingCount(originalExistingCount);//原数量(原现有量)

            business.setToWarehouseLocationId(toWarehouseLocationId);
            business.setToWarehouseLocationCode(toWarehouseLocationCode);
            business.setToContainerId(toContainerId);
            business.setToContainerCode(toContainerCode);
            business.setToOperationCount(toOperationCount);//操作数量(正负)
            business.setCreateBy(CurrentUserUtil.getUsername());
            business.setCreateTime(new Date());
            business.setUpdateBy(null);
            business.setUpdateTime(null);
            fromBusinessList.add(business);

        });
        iInventoryBusinessService.saveOrUpdateBatch(fromBusinessList);
        return BaseResult.ok();
    }

    /**
     * 撤销出库分配
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult cancelOutputAllocation(List<InventoryDetailForm> list) {
        List<InventoryDetail> detailList = new ArrayList<>();//准备数据
        if(CollUtil.isEmpty(list)){
            throw new IllegalArgumentException("库存操作信息不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) -> 确定唯一组合值
        List<String> collect = list.stream()
                .map(k -> (k.getOwerCode()+"_"+k.getWarehouseCode()+"_"+k.getWarehouseAreaCode()+"_"+k.getWarehouseLocationCode()+"_"+k.getContainerCode()+"_"+k.getMaterialCode()+"_"+k.getBatchCode()+"_"+k.getMaterialProductionDate()+"_"+k.getCustomField1()+"_"+k.getCustomField2()+"_"+k.getCustomField3()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3),唯一组合值不能重复");
        }
        list.forEach(detail -> {
            //货主 + 仓库 + 库区 + 库位 + 容器 + 物料，确认库位的库存信息
            Long owerId = detail.getOwerId();//货主ID
            String owerCode = detail.getOwerCode();//货主编号
            if(ObjectUtil.isEmpty(owerCode)){
                throw new IllegalArgumentException("货主不能为空");
            }
            Long warehouseId = detail.getWarehouseId();//仓库ID
            String warehouseCode = detail.getWarehouseCode();//仓库编号
            if(ObjectUtil.isEmpty(warehouseCode)){
                throw new IllegalArgumentException("仓库不能为空");
            }
            Long warehouseAreaId = detail.getWarehouseAreaId();//库区ID
            String warehouseAreaCode = detail.getWarehouseAreaCode();//库区编号
            if(ObjectUtil.isEmpty(warehouseAreaCode)){
                throw new IllegalArgumentException("库区不能为空");
            }
            Long warehouseLocationId = detail.getWarehouseLocationId();//库位ID
            String warehouseLocationCode = detail.getWarehouseLocationCode();//库位编号
            if(ObjectUtil.isEmpty(warehouseLocationCode)){
                throw new IllegalArgumentException("库位不能为空");
            }
            Long containerId = detail.getContainerId();//容器id
            String containerCode = detail.getContainerCode();//容器号
            if(ObjectUtil.isEmpty(containerCode)){
                throw new IllegalArgumentException("容器不能为空");
            }
            Long materialId = detail.getMaterialId();//物料ID
            String materialCode = detail.getMaterialCode();//物料编号
            if(ObjectUtil.isEmpty(materialCode)){
                throw new IllegalArgumentException("物料不能为空");
            }
            Long materialTypeId = detail.getMaterialTypeId();//物料类型ID
            String materialType = detail.getMaterialType();//物料类型
            BigDecimal operationCount = detail.getOperationCount();//操作数量 --> 这里指 撤销出库分配的数量
            if(ObjectUtil.isEmpty(operationCount)){
                throw new IllegalArgumentException("操作数量不能为空");
            }
            if(operationCount.compareTo(new BigDecimal("0")) <= 0){
                throw new IllegalArgumentException("操作数量必须大于0");
            }
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = detail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = detail.getMaterialProductionDate();//生产日期
            String customField1 = detail.getCustomField1();//自定义字段1
            String customField2 = detail.getCustomField2();//自定义字段2
            String customField3 = detail.getCustomField3();//自定义字段3

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
            if(ObjectUtil.isNotEmpty(batchCode)){
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if(ObjectUtil.isNotEmpty(materialProductionDate)){
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if(ObjectUtil.isNotEmpty(customField1)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if(ObjectUtil.isNotEmpty(customField2)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if(ObjectUtil.isNotEmpty(customField3)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3);//group by 分组，保证唯一值
            InventoryDetail inventoryDetail = this.baseMapper.selectOne(queryWrapper);

            if(ObjectUtil.isNotEmpty(inventoryDetail)){
                Integer warehouseLocationStatus = inventoryDetail.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
                if(warehouseLocationStatus == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
                }
                Integer warehouseLocationStatus2 = inventoryDetail.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
                if(warehouseLocationStatus2 == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
                }

                //库位上有此物料，在库存明细的`分配量`上进行减少

                BigDecimal allocationCount = inventoryDetail.getAllocationCount();//分配量
                if(allocationCount.compareTo(operationCount) < 0){
                    throw new IllegalArgumentException("当前分配量 不能小于 出库操作数量");
                }
                BigDecimal subtract = allocationCount.subtract(operationCount);//原分配量 - 撤销的出库分配量
                inventoryDetail.setAllocationCount(subtract);
                //重新计算后，可用量不能小于0
                BigDecimal usableCount = inventoryDetail.getExistingCount().subtract(inventoryDetail.getAllocationCount()).subtract(inventoryDetail.getPickingCount());//可用量=现有量-分配量-拣货量
                if(usableCount.compareTo(new BigDecimal("0")) < 0){
                    throw new IllegalArgumentException("重新计算后，可用量不能小于0");
                }
                inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setUpdateTime(new Date());
            }else{
                //库位上无此物料，直接失败
                throw new IllegalArgumentException("货主+仓库+库区+库位+容器+物料，库位上无此物料");
            }
            detailList.add(inventoryDetail);//库存明细
        });

        //保存库存明细表
        boolean b = this.saveOrUpdateBatch(detailList);

        //保存库存事务表 这里的撤销出库分配，只进行分配占用，不记录库存事务，在真正进行 现有量变动时，再记录库存事务
        //iInventoryBusinessService.saveOrUpdateBatch(businessList);
        return BaseResult.ok();
    }

    /**
     * 生成拣货
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult generatePicking(List<InventoryDetailForm> list) {
        List<InventoryDetail> detailList = new ArrayList<>();//准备数据
        if(CollUtil.isEmpty(list)){
            throw new IllegalArgumentException("库存操作信息不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) -> 确定唯一组合值
        List<String> collect = list.stream()
                .map(k -> (k.getOwerCode()+"_"+k.getWarehouseCode()+"_"+k.getWarehouseAreaCode()+"_"+k.getWarehouseLocationCode()+"_"+k.getContainerCode()+"_"+k.getMaterialCode()+"_"+k.getBatchCode()+"_"+k.getMaterialProductionDate()+"_"+k.getCustomField1()+"_"+k.getCustomField2()+"_"+k.getCustomField3()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3),唯一组合值不能重复");
        }
        list.forEach(detail -> {
            //货主 + 仓库 + 库区 + 库位 + 容器 + 物料，确认库位的库存信息
            Long owerId = detail.getOwerId();//货主ID
            String owerCode = detail.getOwerCode();//货主编号
            if(ObjectUtil.isEmpty(owerCode)){
                throw new IllegalArgumentException("货主不能为空");
            }
            Long warehouseId = detail.getWarehouseId();//仓库ID
            String warehouseCode = detail.getWarehouseCode();//仓库编号
            if(ObjectUtil.isEmpty(warehouseCode)){
                throw new IllegalArgumentException("仓库不能为空");
            }
            Long warehouseAreaId = detail.getWarehouseAreaId();//库区ID
            String warehouseAreaCode = detail.getWarehouseAreaCode();//库区编号
            if(ObjectUtil.isEmpty(warehouseAreaCode)){
                throw new IllegalArgumentException("库区不能为空");
            }
            Long warehouseLocationId = detail.getWarehouseLocationId();//库位ID
            String warehouseLocationCode = detail.getWarehouseLocationCode();//库位编号
            if(ObjectUtil.isEmpty(warehouseLocationCode)){
                throw new IllegalArgumentException("库位不能为空");
            }
            Long containerId = detail.getContainerId();//容器id
            String containerCode = detail.getContainerCode();//容器号
            if(ObjectUtil.isEmpty(containerCode)){
                throw new IllegalArgumentException("容器不能为空");
            }
            Long materialId = detail.getMaterialId();//物料ID
            String materialCode = detail.getMaterialCode();//物料编号
            if(ObjectUtil.isEmpty(materialCode)){
                throw new IllegalArgumentException("物料不能为空");
            }
            Long materialTypeId = detail.getMaterialTypeId();//物料类型ID
            String materialType = detail.getMaterialType();//物料类型
            BigDecimal operationCount = detail.getOperationCount();//操作数量  --> 拣货数量
            if(ObjectUtil.isEmpty(operationCount)){
                throw new IllegalArgumentException("操作数量不能为空");
            }
            if(operationCount.compareTo(new BigDecimal("0")) <= 0){
                throw new IllegalArgumentException("操作数量必须大于0");
            }
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = detail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = detail.getMaterialProductionDate();//生产日期
            String customField1 = detail.getCustomField1();//自定义字段1
            String customField2 = detail.getCustomField2();//自定义字段2
            String customField3 = detail.getCustomField3();//自定义字段3

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
            if(ObjectUtil.isNotEmpty(batchCode)){
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if(ObjectUtil.isNotEmpty(materialProductionDate)){
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if(ObjectUtil.isNotEmpty(customField1)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if(ObjectUtil.isNotEmpty(customField2)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if(ObjectUtil.isNotEmpty(customField3)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3);//group by 分组，保证唯一值
            InventoryDetail inventoryDetail = this.baseMapper.selectOne(queryWrapper);

            if(ObjectUtil.isNotEmpty(inventoryDetail)){
                Integer warehouseLocationStatus = inventoryDetail.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
                if(warehouseLocationStatus == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
                }
                Integer warehouseLocationStatus2 = inventoryDetail.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
                if(warehouseLocationStatus2 == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
                }

                //库位上有此物料，在库存明细的`分配量`上进行扣减，扣减的`分配量`需要转移到`拣货量`
                BigDecimal allocationCount = inventoryDetail.getAllocationCount();//分配量
                BigDecimal pickingCount = inventoryDetail.getPickingCount();//拣货量
                if(allocationCount.compareTo(operationCount) < 0){
                    throw new IllegalArgumentException("分配量 不能小于 拣货操作数量");
                }
                BigDecimal newAllocationCount = allocationCount.subtract(operationCount);// 分配量 - 操作数量
                BigDecimal newPickingCount = pickingCount.add(operationCount);// 拣货量 + 操作数量
                inventoryDetail.setAllocationCount(newAllocationCount);
                inventoryDetail.setPickingCount(newPickingCount);
                //重新计算后，可用量不能小于0
                BigDecimal usableCount = inventoryDetail.getExistingCount().subtract(inventoryDetail.getAllocationCount()).subtract(inventoryDetail.getPickingCount());//可用量=现有量-分配量-拣货量
                if(usableCount.compareTo(new BigDecimal("0")) < 0){
                    throw new IllegalArgumentException("重新计算后，可用量不能小于0");
                }
                inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setUpdateTime(new Date());
            }else{
                //库位上无此物料，直接失败
                throw new IllegalArgumentException("货主+仓库+库区+库位+容器+物料，库位上无此物料");
            }
            detailList.add(inventoryDetail);//库存明细
        });

        //保存库存明细表
        boolean b = this.saveOrUpdateBatch(detailList);

        //保存库存事务表 这里的生成拣货，只进行拣货占用，不记录库存事务，在真正进行 现有量变动时，再记录库存事务
        //iInventoryBusinessService.saveOrUpdateBatch(businessList);
        return BaseResult.ok();
    }

    /**
     * 撤销拣货
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult cancelPicking(List<InventoryDetailForm> list) {
        List<InventoryDetail> detailList = new ArrayList<>();//准备数据
        if(CollUtil.isEmpty(list)){
            throw new IllegalArgumentException("库存操作信息不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) -> 确定唯一组合值
        List<String> collect = list.stream()
                .map(k -> (k.getOwerCode()+"_"+k.getWarehouseCode()+"_"+k.getWarehouseAreaCode()+"_"+k.getWarehouseLocationCode()+"_"+k.getContainerCode()+"_"+k.getMaterialCode()+"_"+k.getBatchCode()+"_"+k.getMaterialProductionDate()+"_"+k.getCustomField1()+"_"+k.getCustomField2()+"_"+k.getCustomField3()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3),唯一组合值不能重复");
        }

        list.forEach(detail -> {
            //货主 + 仓库 + 库区 + 库位 + 容器 + 物料，确认库位的库存信息
            Long owerId = detail.getOwerId();//货主ID
            String owerCode = detail.getOwerCode();//货主编号
            if(ObjectUtil.isEmpty(owerCode)){
                throw new IllegalArgumentException("货主不能为空");
            }
            Long warehouseId = detail.getWarehouseId();//仓库ID
            String warehouseCode = detail.getWarehouseCode();//仓库编号
            if(ObjectUtil.isEmpty(warehouseCode)){
                throw new IllegalArgumentException("仓库不能为空");
            }
            Long warehouseAreaId = detail.getWarehouseAreaId();//库区ID
            String warehouseAreaCode = detail.getWarehouseAreaCode();//库区编号
            if(ObjectUtil.isEmpty(warehouseAreaCode)){
                throw new IllegalArgumentException("库区不能为空");
            }
            Long warehouseLocationId = detail.getWarehouseLocationId();//库位ID
            String warehouseLocationCode = detail.getWarehouseLocationCode();//库位编号
            if(ObjectUtil.isEmpty(warehouseLocationCode)){
                throw new IllegalArgumentException("库位不能为空");
            }
            Long containerId = detail.getContainerId();//容器id
            String containerCode = detail.getContainerCode();//容器号
            if(ObjectUtil.isEmpty(containerCode)){
                throw new IllegalArgumentException("容器不能为空");
            }
            Long materialId = detail.getMaterialId();//物料ID
            String materialCode = detail.getMaterialCode();//物料编号
            if(ObjectUtil.isEmpty(materialCode)){
                throw new IllegalArgumentException("物料不能为空");
            }
            Long materialTypeId = detail.getMaterialTypeId();//物料类型ID
            String materialType = detail.getMaterialType();//物料类型
            BigDecimal operationCount = detail.getOperationCount();//操作数量 --> 撤销拣货数量
            if(ObjectUtil.isEmpty(operationCount)){
                throw new IllegalArgumentException("操作数量不能为空");
            }
            if(operationCount.compareTo(new BigDecimal("0")) <= 0){
                throw new IllegalArgumentException("操作数量必须大于0");
            }
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = detail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = detail.getMaterialProductionDate();//生产日期
            String customField1 = detail.getCustomField1();//自定义字段1
            String customField2 = detail.getCustomField2();//自定义字段2
            String customField3 = detail.getCustomField3();//自定义字段3

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
            if(ObjectUtil.isNotEmpty(batchCode)){
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if(ObjectUtil.isNotEmpty(materialProductionDate)){
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if(ObjectUtil.isNotEmpty(customField1)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if(ObjectUtil.isNotEmpty(customField2)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if(ObjectUtil.isNotEmpty(customField3)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3);//group by 分组，保证唯一值
            InventoryDetail inventoryDetail = this.baseMapper.selectOne(queryWrapper);

            if(ObjectUtil.isNotEmpty(inventoryDetail)){
                Integer warehouseLocationStatus = inventoryDetail.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
                if(warehouseLocationStatus == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
                }
                Integer warehouseLocationStatus2 = inventoryDetail.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
                if(warehouseLocationStatus2 == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
                }

                //库位上有此物料，在库存明细的`拣货量`上进行扣减，扣减的`拣货量`需要转移到`分配量`
                BigDecimal pickingCount = inventoryDetail.getPickingCount();//拣货量
                BigDecimal allocationCount = inventoryDetail.getAllocationCount();//分配量
                BigDecimal newPickingCount = pickingCount.subtract(operationCount);// 拣货量 - 操作数量
                BigDecimal newAllocationCount = allocationCount.add(operationCount);// 分配量 + 操作数量
                inventoryDetail.setAllocationCount(newAllocationCount);
                inventoryDetail.setPickingCount(newPickingCount);
                //重新计算后，可用量不能小于0
                BigDecimal usableCount = inventoryDetail.getExistingCount().subtract(inventoryDetail.getAllocationCount()).subtract(inventoryDetail.getPickingCount());//可用量=现有量-分配量-拣货量
                if(usableCount.compareTo(new BigDecimal("0")) < 0){
                    throw new IllegalArgumentException("重新计算后，可用量不能小于0");
                }
                inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setUpdateTime(new Date());
            }else{
                //库位上无此物料，直接失败
                throw new IllegalArgumentException("货主+仓库+库区+库位+容器+物料，库位上无此物料");
            }
            detailList.add(inventoryDetail);//库存明细
        });

        //保存库存明细表
        boolean b = this.saveOrUpdateBatch(detailList);

        //保存库存事务表 这里的撤销拣货，只进行拣货占用，不记录库存事务，在真正进行 现有量变动时，再记录库存事务
        //iInventoryBusinessService.saveOrUpdateBatch(businessList);
        return BaseResult.ok();
    }

    @Override
    public List<InventoryDetail> queryInventoryDetailForCheck(Map<String, Object> paramMap) {
        return this.baseMapper.queryInventoryDetailForCheck(paramMap);
    }

    /**
     * 出库  这里是`真正的出库`，同步扣减 拣货量、现有量
     * @param list
     * @return
     */
    @Override
    public BaseResult output(List<InventoryDetailForm> list) {
        List<InventoryDetail> detailList = new ArrayList<>();//准备数据
        Map<String, JSONObject> originalMap = new HashMap<>();//记录原始库存
        if(CollUtil.isEmpty(list)){
            throw new IllegalArgumentException("库存操作信息不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3)  + (入仓号 + 重量 + 体积 + 单位)-> 确定唯一组合值
        List<String> collect = list.stream()
                .map(k -> (k.getOwerCode()+"_"+k.getWarehouseCode()+"_"+k.getWarehouseAreaCode()+"_"+k.getWarehouseLocationCode()+"_"+k.getContainerCode()+"_"+k.getMaterialCode()+"_"+k.getBatchCode()+"_"+k.getMaterialProductionDate()+"_"+k.getCustomField1()+"_"+k.getCustomField2()+"_"+k.getCustomField3()+"_"+k.getInWarehouseNumber()+"_"+k.getWeight()+"_"+k.getVolume()+"_"+k.getUnit()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3)+ (入仓号+重量+体积+单位),唯一组合值不能重复");
        }
        list.forEach(detail -> {
            //货主 + 仓库 + 库区 + 库位 + 容器 + 物料，确认库位的库存信息
            Long owerId = detail.getOwerId();//货主ID
            String owerCode = detail.getOwerCode();//货主编号
            if(ObjectUtil.isEmpty(owerCode)){
                throw new IllegalArgumentException("货主不能为空");
            }
            Long warehouseId = detail.getWarehouseId();//仓库ID
            String warehouseCode = detail.getWarehouseCode();//仓库编号
            if(ObjectUtil.isEmpty(warehouseCode)){
                throw new IllegalArgumentException("仓库不能为空");
            }
            Long warehouseAreaId = detail.getWarehouseAreaId();//库区ID
            String warehouseAreaCode = detail.getWarehouseAreaCode();//库区编号
            if(ObjectUtil.isEmpty(warehouseAreaCode)){
                throw new IllegalArgumentException("库区不能为空");
            }
            Long warehouseLocationId = detail.getWarehouseLocationId();//库位ID
            String warehouseLocationCode = detail.getWarehouseLocationCode();//库位编号
            if(ObjectUtil.isEmpty(warehouseLocationCode)){
                throw new IllegalArgumentException("库位不能为空");
            }
            Long containerId = detail.getContainerId();//容器id
            String containerCode = detail.getContainerCode();//容器号
            if(ObjectUtil.isEmpty(containerCode)){
                throw new IllegalArgumentException("容器不能为空");
            }
            Long materialId = detail.getMaterialId();//物料ID
            String materialCode = detail.getMaterialCode();//物料编号
            if(ObjectUtil.isEmpty(materialCode)){
                throw new IllegalArgumentException("物料不能为空");
            }
            Long materialTypeId = detail.getMaterialTypeId();//物料类型ID
            String materialType = detail.getMaterialType();//物料类型
            BigDecimal operationCount = detail.getOperationCount();//出库操作数量
            if(ObjectUtil.isEmpty(operationCount)){
                throw new IllegalArgumentException("操作数量不能为空");
            }
            if(operationCount.compareTo(new BigDecimal("0")) <= 0){
                throw new IllegalArgumentException("操作数量必须大于0");
            }
            //(批次号 + 生产日期 + 字段1 + 字段2 + 字段3)
            String batchCode = detail.getBatchCode();//批次号
            LocalDateTime materialProductionDate = detail.getMaterialProductionDate();//生产日期
            String customField1 = detail.getCustomField1();//自定义字段1
            String customField2 = detail.getCustomField2();//自定义字段2
            String customField3 = detail.getCustomField3();//自定义字段3

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
            queryWrapper.lambda().eq(InventoryDetail::getInWarehouseNumber, detail.getInWarehouseNumber());
            queryWrapper.lambda().eq(InventoryDetail::getWeight, detail.getWeight());
            queryWrapper.lambda().eq(InventoryDetail::getVolume, detail.getVolume());
            queryWrapper.lambda().eq(InventoryDetail::getUnit, detail.getUnit());
            if(ObjectUtil.isNotEmpty(batchCode)){
                queryWrapper.lambda().eq(InventoryDetail::getBatchCode, batchCode);
            }
            if(ObjectUtil.isNotEmpty(materialProductionDate)){
                queryWrapper.lambda().eq(InventoryDetail::getMaterialProductionDate, materialProductionDate);
            }
            if(ObjectUtil.isNotEmpty(customField1)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField1, customField1);
            }
            if(ObjectUtil.isNotEmpty(customField2)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField2, customField2);
            }
            if(ObjectUtil.isNotEmpty(customField3)){
                queryWrapper.lambda().eq(InventoryDetail::getCustomField3, customField3);
            }
            queryWrapper.lambda().groupBy(InventoryDetail::getOwerCode, InventoryDetail::getWarehouseCode, InventoryDetail::getWarehouseAreaCode,
                    InventoryDetail::getWarehouseLocationCode, InventoryDetail::getContainerCode, InventoryDetail::getMaterialCode,
                    InventoryDetail::getBatchCode, InventoryDetail::getMaterialProductionDate, InventoryDetail::getCustomField1, InventoryDetail::getCustomField2, InventoryDetail::getCustomField3);//group by 分组，保证唯一值
            InventoryDetail inventoryDetail = this.baseMapper.selectOne(queryWrapper);

            if(ObjectUtil.isNotEmpty(inventoryDetail)){
                Integer warehouseLocationStatus = inventoryDetail.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
                if(warehouseLocationStatus == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
                }
                Integer warehouseLocationStatus2 = inventoryDetail.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
                if(warehouseLocationStatus2 == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
                }


                //库位上有此物料，在库存明细的`分配量`上进行追加，需要先判断可用量
//                BigDecimal usableCount = inventoryDetail.getUsableCount();//可用量=现有量-分配量-拣货量，数据库不存在此字段
//                if(usableCount.compareTo(operationCount) < 0){
//                    throw new IllegalArgumentException("可用量 不能小于 出库操作数量");
//                }
                BigDecimal pickingCount = inventoryDetail.getPickingCount();//拣货量
                if(pickingCount.compareTo(operationCount) < 0){
                    throw new IllegalArgumentException("拣货量 不能小于 出库操作数量");
                }
                BigDecimal existingCount = inventoryDetail.getExistingCount();//现有量
                BigDecimal newAllocationCount = pickingCount.subtract(operationCount);//拣货量 - 出库操作数量
                BigDecimal newPickingCount = pickingCount.subtract(operationCount);//拣货量 - 出库操作数量
                inventoryDetail.setPickingCount(newPickingCount);
                inventoryDetail.setAllocationCount(newAllocationCount);
                BigDecimal subtract = inventoryDetail.getExistingCount().subtract(inventoryDetail.getAllocationCount()).subtract(inventoryDetail.getPickingCount());//可用量=现有量-分配量-拣货量
                if(subtract.compareTo(new BigDecimal("0")) < 0){
                    throw new IllegalArgumentException("最新计算的可用量，不能小于0");
                }
                inventoryDetail.setUpdateBy(CurrentUserUtil.getUsername());
                inventoryDetail.setUpdateTime(new Date());

                BigDecimal originalExistingCount = existingCount;
                BigDecimal toOperationCount = operationCount;
                String key = detail.getOwerCode()+"_"+detail.getWarehouseCode()+"_"+detail.getWarehouseAreaCode()+"_"
                        +detail.getWarehouseLocationCode()+"_"+detail.getContainerCode()+"_"+detail.getMaterialCode()+"_"
                        +detail.getBatchCode()+"_"+detail.getMaterialProductionDate()+"_"+detail.getCustomField1()+"_"+detail.getCustomField2()+"_"+detail.getCustomField3();
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("originalExistingCount", originalExistingCount);
                jsonObject.set("toWarehouseLocationId", detail.getWarehouseLocationId());
                jsonObject.set("toWarehouseLocationCode", detail.getWarehouseLocationCode());
                jsonObject.set("toContainerId", detail.getContainerId());
                jsonObject.set("toContainerCode", detail.getContainerCode());
                jsonObject.set("toOperationCount", toOperationCount.negate());//出库时取反 (-this)
                originalMap.put(key, jsonObject);

            }else{
                //库位上无此物料，直接失败
                throw new IllegalArgumentException("货主+仓库+库区+库位+容器+物料，库位上无此物料");
            }

            detailList.add(inventoryDetail);//库存明细
        });

        //保存库存明细表
        boolean b = this.saveOrUpdateBatch(detailList);

        //保存库存事务表
        List<InventoryBusiness> businessList = new ArrayList<>();
        detailList.forEach(detail -> {
            String key = detail.getOwerCode()+"_"+detail.getWarehouseCode()+"_"+detail.getWarehouseAreaCode()+"_"
                    +detail.getWarehouseLocationCode()+"_"+detail.getContainerCode()+"_"+detail.getMaterialCode()+"_"
                    +detail.getBatchCode()+"_"+detail.getMaterialProductionDate()+"_"+detail.getCustomField1()+"_"+detail.getCustomField2()+"_"+detail.getCustomField3();
            JSONObject jsonObject = originalMap.get(key);
            BigDecimal originalExistingCount = jsonObject.getBigDecimal("originalExistingCount");
            Long toWarehouseLocationId = jsonObject.getLong("toWarehouseLocationId");
            String toWarehouseLocationCode = jsonObject.getStr("toWarehouseLocationCode");
            Long toContainerId = jsonObject.getLong("toContainerId");
            String toContainerCode = jsonObject.getStr("toContainerCode");
            BigDecimal toOperationCount = jsonObject.getBigDecimal("toOperationCount");

            String code = codeUtils.getCodeByRule(CodeConStants.INVENTORY_BUSINESS_CODE);//库存事务编号

            InventoryBusiness business = ConvertUtil.convert(detail, InventoryBusiness.class);
            business.setId(null);
            business.setCode(code);
            business.setBusinessTypeCode(2);
            business.setBusinessTypeName("出库");
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
            businessList.add(business);
        });
        iInventoryBusinessService.saveOrUpdateBatch(businessList);

        return BaseResult.ok();
    }

    @Override
    public BaseResult selectInventoryReport(QueryShelfOrderTaskForm queryShelfOrderTaskForm) {
        List<InventoryReportVO> inventoryReportVOS = this.baseMapper.selectInventoryReport(queryShelfOrderTaskForm);
        return BaseResult.ok(inventoryReportVOS);
    }

}
