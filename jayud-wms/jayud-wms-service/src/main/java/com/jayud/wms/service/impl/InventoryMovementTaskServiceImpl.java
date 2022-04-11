package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.InventoryMovementTaskMapper;
import com.jayud.wms.model.bo.InventoryMovementTaskAppCompletedForm;
import com.jayud.wms.model.bo.InventoryMovementTaskCompletedForm;
import com.jayud.wms.model.bo.QualityInventoryMovementTaskForm;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.enums.MovementTypeCodeEnum;
import com.jayud.wms.model.enums.TaskStatusCodeEnum;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.InventoryMovementTaskAppVO;
import com.jayud.wms.model.vo.QualityInventoryMovementTaskVO;
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
 * 库存移库任务 服务实现类
 *
 * @author jyd
 * @since 2021-12-30
 */
@Service
public class InventoryMovementTaskServiceImpl extends ServiceImpl<InventoryMovementTaskMapper, InventoryMovementTask> implements IInventoryMovementTaskService {


    @Autowired
    private InventoryMovementTaskMapper inventoryMovementTaskMapper;
    @Autowired
    private IInventoryDetailService inventoryDetailService;
    @Autowired
    private CodeUtils codeUtils;
    @Autowired
    private IWarehouseLocationService warehouseLocationService;
    @Autowired
    private IWarehouseAreaService warehouseAreaService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IInventoryBusinessService inventoryBusinessService;
    @Autowired
    private IContainerService containerService;
    @Autowired
    private SysUserOwerPermissionService sysUserOwerPermissionService;
    @Autowired
    private SysUserWarehousePermissionService sysUserWarehousePermissionService;

    @Override
    public IPage<InventoryMovementTask> selectPage(InventoryMovementTask inventoryMovementTask,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<InventoryMovementTask> page=new Page<InventoryMovementTask>(pageNo, pageSize);
        IPage<InventoryMovementTask> pageList= inventoryMovementTaskMapper.pageList(page, inventoryMovementTask);
        return pageList;
    }

    @Override
    public List<InventoryMovementTask> selectList(InventoryMovementTask inventoryMovementTask){
        return inventoryMovementTaskMapper.list(inventoryMovementTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryMovementTask saveOrUpdateInventoryMovementTask(InventoryMovementTask inventoryMovementTask) {
        Long id = inventoryMovementTask.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
            inventoryMovementTask.setCreateBy(CurrentUserUtil.getUsername());
            inventoryMovementTask.setCreateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<InventoryMovementTask> inventoryMovementTaskQueryWrapper = new QueryWrapper<>();
            //inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getCode, inventoryMovementTask.getCode());
            //inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getIsDeleted, 0);
            //List<InventoryMovementTask> list = this.list(inventoryMovementTaskQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}

        }else{
            //修改 --> update 更新人、更新时间
            inventoryMovementTask.setUpdateBy(CurrentUserUtil.getUsername());
            inventoryMovementTask.setUpdateTime(new Date());

            //没有用到code判断重复时，可以注释
            //QueryWrapper<InventoryMovementTask> inventoryMovementTaskQueryWrapper = new QueryWrapper<>();
            //inventoryMovementTaskQueryWrapper.lambda().ne(InventoryMovementTask::getId, id);
            //inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getCode, inventoryMovementTask.getCode());
            //inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getIsDeleted, 0);
            //List<InventoryMovementTask> list = this.list(inventoryMovementTaskQueryWrapper);
            //if(CollUtil.isNotEmpty(list)){
            //    throw new IllegalArgumentException("编号已存在，操作失败");
            //}
        }
        this.saveOrUpdate(inventoryMovementTask);
        return inventoryMovementTask;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delInventoryMovementTask(int id) {
        InventoryMovementTask inventoryMovementTask = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(inventoryMovementTask)){
            throw new IllegalArgumentException("库存移库任务不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        inventoryMovementTask.setUpdateBy(CurrentUserUtil.getUsername());
        inventoryMovementTask.setUpdateTime(new Date());
        inventoryMovementTask.setIsDeleted(true);
        this.saveOrUpdate(inventoryMovementTask);
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryInventoryMovementTaskForExcel(Map<String, Object> paramMap) {

        AuthUserDetail authUserDetail = CurrentUserUtil.getUserDetail();
        List<String> owerIdList = sysUserOwerPermissionService.getOwerIdByUserId(String.valueOf(authUserDetail.getId()));
        paramMap.put("owerIdList",owerIdList);
        List<String> warehouseIdList = sysUserWarehousePermissionService.getWarehouseIdByUserId(String.valueOf(authUserDetail.getId()));
        paramMap.put("warehouseIdList",warehouseIdList);

        return this.baseMapper.queryInventoryMovementTaskForExcel(paramMap);
    }

    /**
     * 生成库存移动任务
     * @param form
     * @return
     */
    @Override
    public QualityInventoryMovementTaskVO generateInventoryMovementTasks(QualityInventoryMovementTaskForm form) {
        Integer movementTypeCode = form.getMovementTypeCode();//移库类型代码(1物料移库，2容器移库，3库位移库)
        if(ObjectUtil.isEmpty(movementTypeCode)){
            throw new IllegalArgumentException("移库类型不能为空");
        }
        List<InventoryDetail> list = form.getInventoryDetails();//原库存明细 准备生成移库任务
        if(CollUtil.isEmpty(list)){
            throw new IllegalArgumentException("库存明细不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) -> 确定唯一组合值
        List<String> collect = list.stream()
                .map(k -> (k.getOwerCode()+"_"+k.getWarehouseCode()+"_"+k.getWarehouseAreaCode()+"_"+k.getWarehouseLocationCode()+"_"+k.getContainerCode()+"_"+k.getMaterialCode()+"_"+k.getBatchCode()+"_"+k.getMaterialProductionDate()+"_"+k.getCustomField1()+"_"+k.getCustomField2()+"_"+k.getCustomField3()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3),唯一组合值不能重复");
        }
        List<InventoryMovementTask> movementTasks = new ArrayList<>();
        list.forEach(detail -> {
            Long inventoryDetailId = detail.getId();//原库存明细ID
            InventoryDetail from = inventoryDetailService.getById(inventoryDetailId);
            Integer formWarehouseLocationStatus = from.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
            if(formWarehouseLocationStatus == 1){
                throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
            }
            Integer warehouseLocationStatus2 = from.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
            if(warehouseLocationStatus2 == 1){
                throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
            }
            BigDecimal existingCount = from.getExistingCount();//现有量
            BigDecimal usableCount = from.getUsableCount();//可用量(计算字段),可用量=现有量-分配量-拣货量，数据库不存在此字段
            if(existingCount.compareTo(new BigDecimal("0")) < 0){
                throw new IllegalArgumentException("库存明细，现有量不能小于0");
            }
            if(usableCount.compareTo(new BigDecimal("0")) < 0){
                throw new IllegalArgumentException("库存明细，可用量不能小于0");
            }
            if(movementTypeCode != 1){
                // movementTypeCode 为 2容器移库，3库位移库
                // 需要判断  现有量=可用量 才可以按照库位生成移库任务，即 分配量、拣货量 不能存数据记录
                if(existingCount.compareTo(usableCount) != 0){
                    throw new IllegalArgumentException("库存明细，现有量与可用量必须相同");
                }

            }

            //构造数据 -->  库存移库任务（实际上是移库明细）
            InventoryMovementTask inventoryMovementTask = ConvertUtil.convert(detail, InventoryMovementTask.class);
            inventoryMovementTask.setId(null);
            inventoryMovementTask.clearCreate();
            inventoryMovementTask.clearUpdate();

            inventoryMovementTask.setInventoryDetailId(inventoryDetailId);//原库存明细ID

            if(movementTypeCode != 1){
                // movementTypeCode 为 2容器移库，3库位移库
                // 代表需要把容器 里面的物料 全部移动完
                inventoryMovementTask.setToOperationCount(existingCount);
                inventoryMovementTask.setToContainerId(detail.getContainerId());//整个容器都搬走，所有直接填 原容器id
                inventoryMovementTask.setToContainerCode(detail.getContainerCode());
            }else{
                inventoryMovementTask.setToOperationCount(new BigDecimal("0"));//操作数量(移动数量)
            }
            movementTasks.add(inventoryMovementTask);
        });
        QualityInventoryMovementTaskVO vo = ConvertUtil.convert(form, QualityInventoryMovementTaskVO.class);
        vo.setMovementTasks(movementTasks);//移库任务
        return vo;
    }

    /**
     * 库存移动任务确认
     * @param form
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean inventoryMovementTaskConfirmation(QualityInventoryMovementTaskForm form) {
        Integer movementTypeCode = form.getMovementTypeCode();//移库类型代码(1物料移库，2容器移库，3库位移库)
        if(ObjectUtil.isEmpty(movementTypeCode)){
            throw new IllegalArgumentException("移库类型不能为空");
        }
        List<InventoryMovementTask> list = form.getMovementTasks();//移库任务明细
        if(CollUtil.isEmpty(list)){
            throw new IllegalArgumentException("移库任务明细不能为空");
        }
        //(货主 + 仓库 + 库区 + 库位 + 容器 + 物料) + (批次号 + 生产日期 + 字段1 + 字段2 + 字段3) -> 确定唯一组合值
        List<String> collect = list.stream()
                .map(k -> (k.getOwerCode()+"_"+k.getWarehouseCode()+"_"+k.getWarehouseAreaCode()+"_"+k.getWarehouseLocationCode()+"_"+k.getContainerCode()+"_"+k.getMaterialCode()+"_"+k.getBatchCode()+"_"+k.getMaterialProductionDate()+"_"+k.getCustomField1()+"_"+k.getCustomField2()+"_"+k.getCustomField3()))
                .collect(Collectors.toList());
        long count = collect.stream().distinct().count();
        if (collect.size() != count) {
            throw new IllegalArgumentException("(货主+仓库+库区+库位+容器+物料)+(批次号+生产日期+字段1+字段2+字段3),唯一组合值不能重复");
        }

        List<InventoryMovementTask> movementTasks = new ArrayList<>();//移库任务
        //准备冻结 from 和 to
        List<InventoryDetail> fromList = new ArrayList<>();//from 需要被冻结
        List<InventoryDetail> toList = new ArrayList<>();//to 需要被冻结
        String mainCode = codeUtils.getCodeByRule(CodeConStants.INVENTORY_MOVEMENT_MAIN_CODE);//库存移库主任务号
        list.forEach(movement -> {
            Long inventoryDetailId = movement.getInventoryDetailId();//原库存明细ID
            InventoryDetail from = inventoryDetailService.getById(inventoryDetailId);
            fromList.add(from);

            Integer formWarehouseLocationStatus = from.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
            if(formWarehouseLocationStatus == 1){
                throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
            }
            Integer warehouseLocationStatus2 = from.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
            if(warehouseLocationStatus2 == 1){
                throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
            }

            Long warehouseId = from.getWarehouseId();

            //目标库位信息
            Long materialId = movement.getMaterialId();//物料ID
            String materialCode = movement.getMaterialCode();//物料编号

            Long toContainerId = movement.getToContainerId();//目标 容器id
            String toContainerCode = movement.getToContainerCode();//目标 容器号
            if(ObjectUtil.isEmpty(toContainerCode)){
                throw new IllegalArgumentException("目标容器号不能为空");
            }
            QueryWrapper<Container> containerQueryWrapper = new QueryWrapper<>();
            containerQueryWrapper.lambda().eq(Container::getWarehouseId, warehouseId);
            containerQueryWrapper.lambda().eq(Container::getCode, toContainerCode);
            containerQueryWrapper.lambda().groupBy(Container::getWarehouseId, Container::getCode);
            Container toContainer = containerService.getOne(containerQueryWrapper);
            if(ObjectUtil.isEmpty(toContainer)){
                throw new IllegalArgumentException("目标容器不存在");
            }
            toContainerId = toContainer.getId();

            Long toWarehouseLocationId = movement.getToWarehouseLocationId();//目标 库位ID
            String toWarehouseLocationCode = movement.getToWarehouseLocationCode();//目标 库位编号
            if(ObjectUtil.isEmpty(toWarehouseLocationCode)){
                throw new IllegalArgumentException("目标库位编号不能为空");
            }
            QueryWrapper<WarehouseLocation> warehouseLocationQueryWrapper = new QueryWrapper<>();
            warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getWarehouseId, warehouseId);
            warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getCode, toWarehouseLocationCode);
            warehouseLocationQueryWrapper.lambda().eq(WarehouseLocation::getIsDeleted, 0);
            warehouseLocationQueryWrapper.lambda().groupBy(WarehouseLocation::getWarehouseId, WarehouseLocation::getCode);
            WarehouseLocation toLocation = warehouseLocationService.getOne(warehouseLocationQueryWrapper);
            if(ObjectUtil.isEmpty(toLocation)){
                throw new IllegalArgumentException("目标库位不存在");
            }
            toWarehouseLocationId = toLocation.getId();

            Long toWarehouseAreaId = toLocation.getWarehouseAreaId();//目标 库区ID
            WarehouseArea area = warehouseAreaService.getById(toWarehouseAreaId);
            String toWarehouseAreaCode = area.getCode();//目标 库区编号
            String toWarehouseAreaName = area.getName();//目标 库区名称
            if(ObjectUtil.isEmpty(toWarehouseAreaCode)){
                throw new IllegalArgumentException("找不到目标库位对应的库区");
            }
            Long toWarehouseId = toLocation.getWarehouseId();//目标 仓库ID
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


            BigDecimal existingCount = from.getExistingCount();//现有量
            if(existingCount.compareTo(new BigDecimal("0")) < 0){
                throw new IllegalArgumentException("库存明细，现有量不能小于0");
            }
            if(usableCount.compareTo(new BigDecimal("0")) < 0){
                throw new IllegalArgumentException("库存明细，可用量不能小于0");
            }
            if(movementTypeCode != 1){
                // movementTypeCode 为 2容器移库，3库位移库
                // 需要判断  现有量=可用量 才可以按照库位生成移库任务，即 分配量、拣货量 不能存数据记录
                if(existingCount.compareTo(usableCount) != 0){
                    throw new IllegalArgumentException("库存明细，现有量与可用量必须相同");
                }
                if(usableCount.compareTo(toOperationCount) != 0){
                    throw new IllegalArgumentException("可用量与操作数量必须相同");
                }
            }

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
            InventoryDetail to = inventoryDetailService.getOne(queryWrapper);

            if(ObjectUtil.isNotEmpty(to)){
                //to 目标库位存在 库存明细
                Integer toWarehouseLocationStatus = to.getWarehouseLocationStatus();//库位状态-盘点(0未冻结 1已冻结)
                if(toWarehouseLocationStatus == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//盘点冻结
                }
                Integer toWarehouseLocationStatus2 = from.getWarehouseLocationStatus2();//库位状态2-移库(0未冻结 1已冻结)
                if(toWarehouseLocationStatus2 == 1){
                    throw new IllegalArgumentException("库存已被冻结，不能操作");//移库冻结
                }
                toList.add(to);
            }

            String detailCode = codeUtils.getCodeByRule(CodeConStants.INVENTORY_MOVEMENT_DETAIL_CODE);//库存移库明细任务号

            //构造数据 -->  库存移库任务（实际上是移库明细）
            InventoryMovementTask inventoryMovementTask = ConvertUtil.convert(movement, InventoryMovementTask.class);
            inventoryMovementTask.setId(null);
            inventoryMovementTask.clearCreate();
            inventoryMovementTask.clearUpdate();

            inventoryMovementTask.setMovementTypeCode(movementTypeCode);//移库类型代码(1物料移库，2容器移库，3库位移库)
            inventoryMovementTask.setMovementTypeName(MovementTypeCodeEnum.getDesc(movementTypeCode));//移库类型名称
            inventoryMovementTask.setMainCode(mainCode);//主任务号
            inventoryMovementTask.setDetailCode(detailCode);//明细任务号
            inventoryMovementTask.setTaskStatusCode(1);//任务状态代码(1待移库，2已移库)
            inventoryMovementTask.setTaskStatusName(TaskStatusCodeEnum.ONE.getDesc());

            inventoryMovementTask.setInventoryDetailId(inventoryDetailId);//原库存明细ID
            inventoryMovementTask.setToWarehouseLocationId(toWarehouseLocationId);
            inventoryMovementTask.setToWarehouseLocationCode(toWarehouseLocationCode);
            inventoryMovementTask.setToContainerId(toContainerId);
            inventoryMovementTask.setToContainerCode(toContainerCode);
            inventoryMovementTask.setToOperationCount(toOperationCount);

            inventoryMovementTask.setCreateBy(CurrentUserUtil.getUsername());
            inventoryMovementTask.setCreateTime(new Date());

            movementTasks.add(inventoryMovementTask);

        });

        //保存移库任务
        this.saveOrUpdateBatch(movementTasks);

        //保存库存明细
        fromList.forEach(from -> {
            //移库冻结
            from.setWarehouseLocationStatus2(1);//库位状态2-移库(0未冻结 1已冻结)
            from.setUpdateBy(CurrentUserUtil.getUsername());
            from.setUpdateTime(new Date());
        });
        if(CollUtil.isNotEmpty(fromList)){
            inventoryDetailService.saveOrUpdateBatch(fromList);
        }
        //不冻结目标库位
//        toList.forEach(to -> {
//            //移库冻结
//            to.setWarehouseLocationStatus2(1);//库位状态2-移库(0未冻结 1已冻结)
//            to.setUpdateBy(CurrentUserUtil.getUsername());
//            to.setUpdateTime(new Date());
//        });
//        if(CollUtil.isNotEmpty(toList)){
//            inventoryDetailService.saveOrUpdateBatch(toList);
//        }
        return true;
    }

    /**
     * 库存移动任务完成 真正转移库存，现有量 变化
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean inventoryMovementTaskCompleted(InventoryMovementTaskCompletedForm form) {
        String detailCode = form.getDetailCode();//明细任务号
        if(StrUtil.isEmpty(detailCode)){
            throw new IllegalArgumentException("明细任务号不能为空");
        }
        QueryWrapper<InventoryMovementTask> inventoryMovementTaskQueryWrapper = new QueryWrapper<>();
        inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getDetailCode, detailCode);
        inventoryMovementTaskQueryWrapper.lambda().groupBy(InventoryMovementTask::getDetailCode);
        InventoryMovementTask movement = this.baseMapper.selectOne(inventoryMovementTaskQueryWrapper);
        if(ObjectUtil.isEmpty(movement)){
            throw new IllegalArgumentException("库存移库任务不存在");
        }

        Integer taskStatusCode = movement.getTaskStatusCode();//任务状态代码(1待移库，2已移库)
        if(taskStatusCode != 1){
            throw new IllegalArgumentException("库存移库任务状态不正确，无法移库");
        }

        List<InventoryDetail> fromList = new ArrayList<>();//from
        List<InventoryDetail> toList = new ArrayList<>();//to
        Map<String, BigDecimal> toMap = new HashMap<>();
        Map<String, JSONObject> fromOriginalMap = new HashMap<>();//记录from原始库存

        // from
        Long inventoryDetailId = movement.getInventoryDetailId();
        InventoryDetail from = inventoryDetailService.getById(inventoryDetailId);
        if(ObjectUtil.isEmpty(from)){
            throw new IllegalArgumentException("原库位信息不存在");
        }

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
        BigDecimal existingCount = from.getExistingCount();//现有量

        if(existingCount.compareTo(new BigDecimal("0")) < 0){
            throw new IllegalArgumentException("库存明细，现有量不能小于0");
        }
        if(usableCount.compareTo(new BigDecimal("0")) < 0){
            throw new IllegalArgumentException("库存明细，可用量不能小于0");
        }

        Integer movementTypeCode = movement.getMovementTypeCode();
        if(movementTypeCode != 1){
            // movementTypeCode 为 2容器移库，3库位移库
            // 需要判断  现有量=可用量 才可以按照库位生成移库任务，即 分配量、拣货量 不能存数据记录
            if(existingCount.compareTo(usableCount) != 0){
                throw new IllegalArgumentException("库存明细，现有量与可用量必须相同");
            }
            if(usableCount.compareTo(toOperationCount) != 0){
                throw new IllegalArgumentException("可用量与操作数量必须相同");
            }
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
        InventoryDetail to = inventoryDetailService.getOne(queryWrapper);

        if(ObjectUtil.isNotEmpty(to)){
            //to 目标库位存在 库存明细
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
            to.setWarehouseLocationStatus(0);//库位状态-盘点(0未冻结 1已冻结)
            to.setWarehouseLocationStatus2(0);//库位状态2-移库(0未冻结 1已冻结)
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

        //保存库存移库任务
        movement.setTaskStatusCode(TaskStatusCodeEnum.TWO.getCode());
        movement.setTaskStatusName(TaskStatusCodeEnum.TWO.getDesc());
        movement.setUpdateBy(CurrentUserUtil.getUsername());
        movement.setUpdateTime(new Date());
        this.saveOrUpdate(movement);

        //保存库存明细表
        fromList.forEach(from1 -> {
            from1.setWarehouseLocationStatus2(0);//库位状态2-移库(0未冻结 1已冻结)  取消冻结
            from1.setUpdateBy(CurrentUserUtil.getUsername());
            from1.setUpdateTime(new Date());
        });
        inventoryDetailService.saveOrUpdateBatch(fromList);
        toList.forEach(to1 -> {
            String toKey1 = to1.getOwerCode()+"_"+to1.getWarehouseCode()+"_"+to1.getWarehouseAreaCode()+"_"
                    +to1.getWarehouseLocationCode()+"_"+to1.getContainerCode()+"_"+to1.getMaterialCode()+"_"
                    +to1.getBatchCode()+"_"+to1.getMaterialProductionDate()+"_"+to1.getCustomField1()+"_"+to1.getCustomField2()+"_"+to1.getCustomField3();
            BigDecimal toOperationCount1 = toMap.get(toKey1);//获取新增的操作数量，做合并

            BigDecimal existingCount1 = to1.getExistingCount();
            BigDecimal add1 = existingCount1.add(toOperationCount1);//现有量 + 操作量
            to1.setExistingCount(add1);
        });
        inventoryDetailService.saveOrUpdateBatch(toList);

        //保存库存事务表
        List<InventoryBusiness> fromBusinessList = new ArrayList<>();
        fromList.forEach(from1 -> {
            String fromKey1 = from1.getOwerCode()+"_"+from1.getWarehouseCode()+"_"+from1.getWarehouseAreaCode()+"_"
                    +from1.getWarehouseLocationCode()+"_"+from1.getContainerCode()+"_"+from1.getMaterialCode()+"_"
                    +from1.getBatchCode()+"_"+from1.getMaterialProductionDate()+"_"+from1.getCustomField1()+"_"+from1.getCustomField2()+"_"+from1.getCustomField3();
            JSONObject jsonObject = fromOriginalMap.get(fromKey1);
            BigDecimal originalExistingCount1 = jsonObject.getBigDecimal("originalExistingCount");//原始数量 原数量(原现有量)
            Long toWarehouseLocationId1 = jsonObject.getLong("toWarehouseLocationId");//目标库位ID
            String toWarehouseLocationCode1 = jsonObject.getStr("toWarehouseLocationCode");//目标库位编号
            Long toContainerId1 = jsonObject.getLong("toContainerId");//目标容器ID
            String toContainerCode1 = jsonObject.getStr("toContainerCode");//目标容器号
            BigDecimal toOperationCount1 = jsonObject.getBigDecimal("toOperationCount");//操作数量  从原库位 移动了 toOperationCount个 到目标库位

            String code = codeUtils.getCodeByRule(CodeConStants.INVENTORY_BUSINESS_CODE);//库存事务编号

            InventoryBusiness business = ConvertUtil.convert(from1, InventoryBusiness.class);
            business.setId(null);
            business.setCode(code);
            business.setBusinessTypeCode(4);
            business.setBusinessTypeName("移动");
            business.setInventoryDetailId(from1.getId());
            business.setExistingCount(originalExistingCount1);//原数量(原现有量)

            business.setToWarehouseLocationId(toWarehouseLocationId1);
            business.setToWarehouseLocationCode(toWarehouseLocationCode1);
            business.setToContainerId(toContainerId1);
            business.setToContainerCode(toContainerCode1);
            business.setToOperationCount(toOperationCount1);//操作数量(正负)
            business.setCreateBy(CurrentUserUtil.getUsername());
            business.setCreateTime(new Date());
            business.setUpdateBy(null);
            business.setUpdateTime(null);
            fromBusinessList.add(business);

        });
        inventoryBusinessService.saveOrUpdateBatch(fromBusinessList);

        return true;
    }

    @Override
    public IPage<InventoryMovementTaskAppVO> selectPageByFeign(InventoryMovementTask inventoryMovementTask,
                                                               Integer pageNo,
                                                               Integer pageSize,
                                                               HttpServletRequest req) {

        AuthUserDetail authUserDetail = CurrentUserUtil.getUserDetail();
        List<String> owerIdList = sysUserOwerPermissionService.getOwerIdByUserId(String.valueOf(authUserDetail.getId()));
        List<String> warehouseIdList = sysUserWarehousePermissionService.getWarehouseIdByUserId(String.valueOf(authUserDetail.getId()));
        inventoryMovementTask.setOwerIdList(owerIdList);
        inventoryMovementTask.setWarehouseIdList(warehouseIdList);
        Page<InventoryMovementTaskAppVO> page=new Page<InventoryMovementTaskAppVO>(pageNo, pageSize);
        IPage<InventoryMovementTaskAppVO> pageList= inventoryMovementTaskMapper.selectPageByFeign(page, inventoryMovementTask);
        return pageList;
    }

    /**
     * 去移库(获取移库详情、获取下一条移库详情)
     * @param mainCode
     * @return
     */
    @Override
    public InventoryMovementTaskAppVO queryInventoryMovementTaskByMainCode(String mainCode) {
        InventoryMovementTaskAppVO vo = inventoryMovementTaskMapper.queryInventoryMovementTaskByMainCode(mainCode);
        if(ObjectUtil.isEmpty(vo)){
            throw new IllegalArgumentException("移库任务不存在");
        }
        QueryWrapper<InventoryMovementTask> inventoryMovementTaskQueryWrapper = new QueryWrapper<>();
        inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getMainCode, mainCode);
        inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getTaskStatusCode, 1);//任务状态代码(1待移库，2已移库)
        inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getIsDeleted, 0);//是否删除，0未删除，1已删除
        inventoryMovementTaskQueryWrapper.lambda().groupBy(InventoryMovementTask::getMainCode, InventoryMovementTask::getDetailCode);
        inventoryMovementTaskQueryWrapper.lambda().orderByAsc(InventoryMovementTask::getId);
        List<InventoryMovementTask> list = this.list(inventoryMovementTaskQueryWrapper);
        if(CollUtil.isNotEmpty(list)){
            InventoryMovementTask inventoryMovementTask = list.get(0);
            InventoryMovementTaskAppCompletedForm convert = ConvertUtil.convert(inventoryMovementTask, InventoryMovementTaskAppCompletedForm.class);
            vo.setCompleted(convert);
        }else{
            vo.setCompleted(null);
        }
        return vo;
    }

    /**
     * 移库确认
     * 如果有多条 确认当前任务后，返回下一条的任务，没有下一条任务，返回空
     * @param inventoryMovementTaskAppVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryMovementTaskAppVO inventoryMovementTaskCompletedByApp(InventoryMovementTaskAppVO inventoryMovementTaskAppVO) {
        Integer movementTypeCode = inventoryMovementTaskAppVO.getMovementTypeCode();//移库类型代码(1物料移库，2容器移库，3库位移库)
        if(ObjectUtil.isEmpty(movementTypeCode)){
            throw new IllegalArgumentException("移库类型不能为空");
        }
        String mainCode = inventoryMovementTaskAppVO.getMainCode();
        InventoryMovementTaskAppVO vo = inventoryMovementTaskMapper.queryInventoryMovementTaskByMainCode(mainCode);
        if(ObjectUtil.isEmpty(vo)){
            throw new IllegalArgumentException("移库任务不存在");
        }
        InventoryMovementTaskAppCompletedForm completed = inventoryMovementTaskAppVO.getCompleted();//最新的一条需要确认的移库任务
        String mainCode1 = completed.getMainCode();//主任务号
        String detailCode = completed.getDetailCode();//明细任务号

        QueryWrapper<InventoryMovementTask> inventoryMovementTaskQueryWrapper = new QueryWrapper<>();
        inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getMainCode, mainCode1);
        inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getDetailCode, detailCode);
        inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getTaskStatusCode, 1);//任务状态代码(1待移库，2已移库)
        inventoryMovementTaskQueryWrapper.lambda().eq(InventoryMovementTask::getIsDeleted, 0);//是否删除，0未删除，1已删除
        inventoryMovementTaskQueryWrapper.lambda().groupBy(InventoryMovementTask::getMainCode, InventoryMovementTask::getDetailCode);
        inventoryMovementTaskQueryWrapper.lambda().orderByAsc(InventoryMovementTask::getId);
        InventoryMovementTask inventoryMovementTask = this.getOne(inventoryMovementTaskQueryWrapper);//通过明细和分组，获取唯一值
        if(ObjectUtil.isEmpty(inventoryMovementTask)){
            throw new IllegalArgumentException("移库任务明细不存在");
        }
        String warehouseLocationCode = inventoryMovementTask.getWarehouseLocationCode();//原库位编号
        String materialCode = inventoryMovementTask.getMaterialCode();//物料编号
        String toWarehouseLocationCode = inventoryMovementTask.getToWarehouseLocationCode();//目标库位编号
        if(StrUtil.isEmpty(toWarehouseLocationCode)){
            throw new IllegalArgumentException("目标库位编号不能为空");
        }
        String toContainerCode = inventoryMovementTask.getToContainerCode();//目标容器号
        if(StrUtil.isEmpty(toContainerCode)){
            throw new IllegalArgumentException("目标容器号不能为空");
        }

        //验证 目标库位编号、目标容器号
        String sourceWarehouseLocationCode = completed.getSourceWarehouseLocationCode();//原库位 by App
        String actMaterialCode = completed.getActMaterialCode();//实际物料编号 by App
        String actualWarehouseLocationCode = completed.getActualWarehouseLocationCode();//实际移库库位 by App
        String actualContainerCode = completed.getActualContainerCode();//实际移库容器号 by App

        //Integer movementTypeCode = inventoryMovementTaskAppVO.getMovementTypeCode();//移库类型代码(1物料移库，2容器移库，3库位移库)
        if(movementTypeCode == 3){
            //3库位移库 验证 目标库位编号
            if(!warehouseLocationCode.equals(sourceWarehouseLocationCode)){
                throw new IllegalArgumentException("原库位 与 APP扫码 不相同");
            }
            if(!toWarehouseLocationCode.equals(actualWarehouseLocationCode)){
                throw new IllegalArgumentException("目标库位编号 与 APP扫码实际移库库位 不相同");
            }
        }else if(movementTypeCode == 2){
            //2容器移库 验证 目标库位编号、目标容器号
            if(!toWarehouseLocationCode.equals(actualWarehouseLocationCode)){
                throw new IllegalArgumentException("目标库位编号 与 APP扫码实际移库库位 不相同");
            }
            if(!toContainerCode.equals(actualContainerCode)){
                throw new IllegalArgumentException("目标容器号 与 APP扫码实际移库容器号 不相同");
            }
        }else if(movementTypeCode == 1){
            //1物料移库 验证 目标库位编号、目标容器号
            if(!materialCode.equals(actMaterialCode)){
                throw new IllegalArgumentException("物料编号 与 APP扫码 不相同");
            }
            if(!toWarehouseLocationCode.equals(actualWarehouseLocationCode)){
                throw new IllegalArgumentException("目标库位编号 与 APP扫码实际移库库位 不相同");
            }
            if(!toContainerCode.equals(actualContainerCode)){
                throw new IllegalArgumentException("目标容器号 与 APP扫码实际移库容器号 不相同");
            }
        }

        // ==> 库存移动任务完成 真正转移库存，现有量 变化
        InventoryMovementTaskCompletedForm form = new InventoryMovementTaskCompletedForm();
        form.setDetailCode(detailCode);
        boolean b = this.inventoryMovementTaskCompleted(form);
        // ==> 获取移库详情、获取下一条移库详情
        InventoryMovementTaskAppVO vo1 = this.queryInventoryMovementTaskByMainCode(mainCode);

        return vo1;
    }

    @Override
    public List<InventoryMovementTask> getByCondition(InventoryMovementTask condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }

}
