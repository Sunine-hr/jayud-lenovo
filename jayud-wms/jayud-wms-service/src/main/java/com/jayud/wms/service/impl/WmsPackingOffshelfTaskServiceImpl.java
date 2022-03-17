package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.WmsOutboundOrderInfoMapper;
import com.jayud.wms.mapper.WmsPackingOffshelfTaskMapper;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.bo.QueryShelfOrderTaskForm;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.dto.OutboundToPacking.WmsOutboundOrderMsgDTO;
import com.jayud.wms.model.dto.OutboundToPacking.WmsOutboundOrderToDistributeMaterialDTO;
import com.jayud.wms.model.dto.OutboundToPacking.WmsOutboundOrderToMaterialDTO;
import com.jayud.wms.model.dto.OutboundToPacking.WmsOutboundToPackingDTO;
import com.jayud.wms.model.enums.*;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.*;
import com.jayud.wms.service.*;
import com.jayud.wms.utils.CodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 拣货下架任务 服务实现类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Service
public class WmsPackingOffshelfTaskServiceImpl extends ServiceImpl<WmsPackingOffshelfTaskMapper, WmsPackingOffshelfTask> implements IWmsPackingOffshelfTaskService {

    @Autowired
    private IWmsOutboundOrderInfoToDistributionMaterialService wmsOutboundOrderInfoToDistributionMaterialService;
    @Autowired
    private IWmsOutboundOrderInfoService wmsOutboundOrderInfoService;
    @Autowired
    private IWmsShippingReviewService wmsShippingReviewService;
    @Autowired
    private IWmsWaveOrderInfoService wmsWaveOrderInfoService;
    @Autowired
    private IWmsOutboundOrderInfoToMaterialService wmsOutboundOrderInfoToMaterialService;
    @Autowired
    private IWmsWaveToMaterialService wmsWaveToMaterialService;
    @Autowired
    private IWmsWaveToOutboundInfoService wmsWaveToOutboundInfoService;
    @Autowired
    private IWmsPackingOffshelfOrderService wmsPackingOffshelfOrderService;
    @Autowired
    public IInventoryDetailService inventoryDetailService;
    @Autowired
    private IWarehouseLocationService warehouseLocationService;


    @Autowired
    private WmsPackingOffshelfTaskMapper wmsPackingOffshelfTaskMapper;

    @Autowired
    private WmsOutboundOrderInfoMapper wmsOutboundOrderInfoMapper;

    @Autowired
    private CodeUtils codeUtils;

    @Override
    public IPage<WmsPackingOffshelfTask> selectPage(WmsPackingOffshelfTask wmsPackingOffshelfTask,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsPackingOffshelfTask> page=new Page<WmsPackingOffshelfTask>(currentPage,pageSize);
        IPage<WmsPackingOffshelfTask> pageList= wmsPackingOffshelfTaskMapper.pageList(page, wmsPackingOffshelfTask);
        return pageList;
    }

    @Override
    public List<WmsPackingOffshelfTask> selectList(WmsPackingOffshelfTask wmsPackingOffshelfTask){
        return wmsPackingOffshelfTaskMapper.list(wmsPackingOffshelfTask);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsPackingOffshelfTaskMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsPackingOffshelfTaskForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsPackingOffshelfTaskForExcel(paramMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult createPackingOffShelf(OutboundOrderNumberVO outboundOrderNumberVO) {
        if (outboundOrderNumberVO.getOrderNumberList().isEmpty()){
            return BaseResult.error("请选择编号！");
        }
        List<String> exitList = new ArrayList<>();
        List<String> notAllocateInventory = new ArrayList<>();
        List<WmsPackingOffshelfTask> taskList = new ArrayList<>();
        List<String> successList = new ArrayList<>();
        for (int i=0;i<outboundOrderNumberVO.getOrderNumberList().size();i++){
            String orderNumber = outboundOrderNumberVO.getOrderNumberList().get(i);
            if (outboundOrderNumberVO.getIsAllContinue()) {
                if (!checkIsAllocateInventory(orderNumber, outboundOrderNumberVO.getIsWave())) {
                    notAllocateInventory.add(orderNumber);
                    continue;
                }
            }
            WmsOutboundOrderInfoToDistributionMaterial distributionMaterial = new WmsOutboundOrderInfoToDistributionMaterial();
            if (outboundOrderNumberVO.getIsWave()){
                distributionMaterial.setWaveNumber(orderNumber);
            }else {
                distributionMaterial.setOrderNumber(orderNumber);
            }
            String packingNumber = "";
            List<WmsOutboundOrderInfoToDistributionMaterial> distributionMaterialList = wmsOutboundOrderInfoToDistributionMaterialService.selectList(distributionMaterial);
            List<WmsOutboundOrderInfoToDistributionMaterial> addList = getPackingList(distributionMaterialList,orderNumber,outboundOrderNumberVO.getIsWave());
            if (addList.isEmpty()){
                exitList.add(orderNumber);
                continue;
            }
            int orders = 1;
            if (distributionMaterialList.size() == addList.size()){
                packingNumber = codeUtils.getCodeByRule(CodeConStants.PACKING_OFF_SHELF);
            }else {
                packingNumber = getPackingNumberByNumber(orderNumber,outboundOrderNumberVO.getIsWave());
                orders = getNextOrder(orderNumber,outboundOrderNumberVO.getIsWave());
            }
            for (int j=0;j<distributionMaterialList.size();j++){
                WmsPackingOffshelfTask tasks = initTask(orderNumber,packingNumber,outboundOrderNumberVO.getIsWave(),distributionMaterialList.get(j));
                tasks.setSorts(orders);
                taskList.add(tasks);
                orders+=1;
            }
            successList.add(orderNumber);
        }
        if (!taskList.isEmpty()) {
            this.saveBatch(taskList);
            BaseResult result = savePickingInventory(taskList,false);
            List<String> detailNumberList = taskList.stream().map(x->x.getTaskDetailNumber()).distinct().collect(Collectors.toList());
            changePackingAccount(detailNumberList);
        }
        //波次修改拣货下架数量、分配数量
        if (!successList.isEmpty()&&outboundOrderNumberVO.getIsWave()){
            changeWavePackingAccount(successList,false);
        }
        String msg = "";
        if (!exitList.isEmpty()){
            msg +=StringUtils.join(exitList,",")+"已生成，请勿重新生成！";
            return BaseResult.error(StringUtils.join(exitList,",")+"已生成，请勿重新生成！");
        }
        if (!notAllocateInventory.isEmpty()){
            msg +=StringUtils.join(notAllocateInventory,",")+"未分配或完全分配，请勿生成！";
        }
        if (StringUtils.isNotBlank(msg)){
            return BaseResult.error(msg);
        }
        return BaseResult.ok(SysTips.CREATE_PACKING_SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult cancelPackingOffShelf(OutboundOrderNumberVO outboundOrderNumberVO) {
        boolean isWave = outboundOrderNumberVO.getIsWave();
        List<String> inPogressList = new ArrayList<>();
        List<String> finishList = new ArrayList<>();
        List<WmsPackingOffshelfTask> cancelList = new ArrayList<>();
        //撤销单个出库单号-转出库明细号
        getAllDetailNumberList(outboundOrderNumberVO);
        List<String> successList = new ArrayList<>();
        for (int i=0;i<outboundOrderNumberVO.getDetailNumberList().size();i++){
            String detailNumber = outboundOrderNumberVO.getDetailNumberList().get(i);
            BaseResult checkResult = checkCancelStatus(detailNumber,isWave);
            if (!checkResult.isSuccess()){
                if (checkResult.getCode().equals(OffshelfTaskStatusEnum.PACKING.getStatus())){
                    inPogressList.add(detailNumber);
                }else if (checkResult.getCode().equals(OffshelfTaskStatusEnum.FINISH_PACKING.getStatus())){
                    finishList.add(detailNumber);
                }
            }else {
                WmsPackingOffshelfTask task = new WmsPackingOffshelfTask();
                task.setTaskDetailNumber(detailNumber);
                List<WmsPackingOffshelfTask> taskList = selectList(task);
                cancelList.addAll(taskList);
                wmsPackingOffshelfTaskMapper.delByNumber(null, null,detailNumber, CurrentUserUtil.getUsername());
                successList.add(detailNumber);
            }
        }
        if (!successList.isEmpty()){
            if (!cancelList.isEmpty()){
                savePickingInventory(cancelList,true);
            }
            if (outboundOrderNumberVO.getIsWave()){
                //波次取消拣货下架数量
                cancelChangeAcount(successList);
            }
        }
        if (!inPogressList.isEmpty()|| !finishList.isEmpty()){
            String returnMsg = "";
            if (!inPogressList.isEmpty()){
                if (outboundOrderNumberVO.getIsAllOrder()){
                    if (outboundOrderNumberVO.getIsAllOrder()) {
                        inPogressList = getOrdeNumberByDetail(inPogressList, isWave);
                    }
                }
                returnMsg += StringUtils.join(inPogressList,",") + OffshelfTaskStatusEnum.PACKING.getDesc()+";";
            }
            if (!finishList.isEmpty()){
                if (outboundOrderNumberVO.getIsAllOrder()){
                    if (outboundOrderNumberVO.getIsAllOrder()) {
                        finishList = getOrdeNumberByDetail(finishList, isWave);
                    }
                }
                returnMsg += StringUtils.join(finishList,",") + OffshelfTaskStatusEnum.FINISH_PACKING.getDesc()+";";
            }
            return BaseResult.error(returnMsg);
        }
        return BaseResult.ok(SysTips.CANCEL_PACKING_SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult receivePackingOffShelf(WmsPackingOffshelfTask wmsPackingOffshelfTask) {
        WmsPackingOffshelfTask task = getTask(wmsPackingOffshelfTask);
        if (task == null){
            return BaseResult.error();
        }
        task.setIsIssue(true);
        task.setStatus(OffshelfTaskStatusEnum.PACKING.getStatus());
        task.setReceiveTime(LocalDateTime.now());
        this.updateById(task);
        return BaseResult.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult finishPackingOffShelf(WmsPackingOffshelfTask wmsPackingOffshelfTask) {
        WmsPackingOffshelfTask task = getTask(wmsPackingOffshelfTask);
        if (task.getStatus().equals(OffshelfTaskStatusEnum.FINISH_PACKING.getStatus())){
            return BaseResult.error(SysTips.PACKING_FINISH_ERROR);
        }
        if (task.getStatus().equals(OffshelfTaskStatusEnum.PACKING.getStatus())){
            return BaseResult.error(SysTips.PACKING_ERROR);
        }
        if (task.getWaitOffshelfAccount().compareTo(wmsPackingOffshelfTask.getRealOffshelfAccount())!=0){
            return BaseResult.error(SysTips.PACKING_ACCOUNT_ERROR);
        }
        if (!task.getOffshelfLocationCode().equals(wmsPackingOffshelfTask.getOffshelfLocationCode())){
            return BaseResult.error(SysTips.PACKING_LOCATION_ERROR);
        }
        if (!task.getMaterialCode().equals(wmsPackingOffshelfTask.getMaterialCode())){
            return BaseResult.error(SysTips.PACKING_MATERIALCODE_ERROR);
        }
        task.setRealOffshelfAccount(task.getWaitOffshelfAccount());
        task.setStatus(OffshelfTaskStatusEnum.FINISH_PACKING.getStatus());
        task.setFinishTime(LocalDateTime.now());
        task.setSorts(0);
        this.updateById(task);
        if (wmsPackingOffshelfTask.getIsEnd() != null){
            if (wmsPackingOffshelfTask.getIsEnd()){
                WmsOutboundOrderInfoToDistributionMaterial distributionMaterial = wmsOutboundOrderInfoToDistributionMaterialService.getById(wmsPackingOffshelfTask.getAllocationId());
                if (distributionMaterial != null){
                    List<WmsOutboundOrderInfoToDistributionMaterial> materialList = new ArrayList<>();
                    materialList.add(distributionMaterial);
                    endOrderToPackingTask(materialList);
                }
            }
        }
        String orderNumber = "";
        boolean isWave = true;
        if (StringUtils.isNotBlank(task.getOrderNumber())){
            orderNumber = task.getOrderNumber();
            isWave = false;
        }else {
            orderNumber = task.getWaveNumber();
            changeWaveMsg(orderNumber);
        }
        comfirmChangeOrder(orderNumber,isWave);
        OutboundOrderNumberVO outboundOrderNumberVO = new OutboundOrderNumberVO();
        if (StringUtils.isNotBlank(task.getWaveNumber())){
            outboundOrderNumberVO.setOrderNumber(task.getWaveNumber());
            outboundOrderNumberVO.setIsWave(true);
        }else {
            outboundOrderNumberVO.setOrderNumber(task.getOrderNumber());
            outboundOrderNumberVO.setIsWave(false);
        }
        outboundOrderNumberVO.setIsEnd(wmsPackingOffshelfTask.getIsEnd());
        checkAllFinish(outboundOrderNumberVO);
        return BaseResult.ok(SysTips.PACKING_FINISH_SUCCESS);
    }

    @Override
    public BaseResult getPackingTask(WmsPackingOffshelfVO wmsPackingOffshelfVO) {
        return BaseResult.ok(wmsPackingOffshelfTaskMapper.getPackingTask(wmsPackingOffshelfVO));
    }

    @Override
    public BaseResult getPackingTaskDetail(WmsPackingOffshelfVO wmsPackingOffshelfVO) {
        String descMsg = wmsPackingOffshelfVO.getDescMsg();
        String ascMsg = wmsPackingOffshelfVO.getAscMsg();
        List<WmsPackingOffshelfVO> packingList = wmsPackingOffshelfTaskMapper.getPackingTask(wmsPackingOffshelfVO);
        if (!packingList.isEmpty()){
            wmsPackingOffshelfVO = packingList.get(0);
            LambdaQueryWrapper<WmsPackingOffshelfTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getIsDeleted,false);
            lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getPackingOffshelfNumber,wmsPackingOffshelfVO.getPackingOffshelfNumber());
            lambdaQueryWrapper.ne(WmsPackingOffshelfTask::getStatus, OffshelfTaskStatusEnum.FINISH_PACKING.getStatus());
            if (StringUtils.isNotBlank(descMsg)){
                lambdaQueryWrapper.orderByDesc(WmsPackingOffshelfTask::getWarehouseLocationCode);
            }
            if (StringUtils.isNotBlank(ascMsg)){
                lambdaQueryWrapper.orderByAsc(WmsPackingOffshelfTask::getWarehouseLocationCode);
            }
            lambdaQueryWrapper.orderByAsc(WmsPackingOffshelfTask::getSorts);
            List<WmsPackingOffshelfTask> detailList = this.list(lambdaQueryWrapper);
            wmsPackingOffshelfVO.setTaskList(detailList);
        }
        return BaseResult.ok(wmsPackingOffshelfVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult skipTaskDetail(String detailNumber) {
        WmsPackingOffshelfTask task = getByDetailNumber(detailNumber);
        if (task == null){
            return BaseResult.error("拣货下架任务已删除！");
        }
        if (task.getStatus().equals(OffshelfTaskStatusEnum.FINISH_PACKING.getStatus())){
            return BaseResult.error("拣货下架任务已完成！");
        }
        String orderNumber = "";
        boolean isWave = true;
        if (StringUtils.isNotBlank(task.getOrderNumber())){
            orderNumber = task.getOrderNumber();
            isWave = false;
        }else {
            orderNumber = task.getWaveNumber();
        }
        List<WmsPackingOffshelfTask> taskList = getTaskListASCOrder(orderNumber,isWave);
        WmsPackingOffshelfTask remove = new WmsPackingOffshelfTask();
        for (WmsPackingOffshelfTask task1 : taskList){
            if (task1.getTaskDetailNumber().equals(detailNumber)){
                taskList.remove(task1);
                remove = task1;
                break;
            }
        }
        taskList.add(remove);
        for (int i=0;i<taskList.size();i++){
            taskList.get(i).setSorts(i+1);
        }
        this.updateBatchById(taskList);
        return BaseResult.ok();
    }

    @Override
    public BaseResult selectDeliveryReport(QueryShelfOrderTaskForm queryShelfOrderTaskForm) {
        List<DeliveryReportVO> warehouseingReportVOS = this.baseMapper.selectDeliveryReport(queryShelfOrderTaskForm);
        return BaseResult.ok(warehouseingReportVOS);
    }

    @Override
    public BaseResult<WmsPackingOffshelfTaskVO> selectTaskAndLocationByMsg(WmsPackingOffshelfTaskVO wmsPackingOffshelfTaskVO) {
        //获取库位
        WarehouseLocation location = new WarehouseLocation();
        location.setShelfCode(wmsPackingOffshelfTaskVO.getShelfCode());
        List<WarehouseLocationVO> locationList = warehouseLocationService.selectList(location);
        wmsPackingOffshelfTaskVO.setLocationList(locationList);
        //获取拣货下架数据
        getSourceTask(wmsPackingOffshelfTaskVO);
        //设置库位状态
        setLocationStatus(wmsPackingOffshelfTaskVO);
        return BaseResult.ok(wmsPackingOffshelfTaskVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult finishTaskAndEndOrder(WmsPackingOffshelfTaskVO wmsPackingOffshelfTaskVO) {
        List<WmsPackingOffshelfTask> finishTaskList = new ArrayList<>();
        String workbenchCode = CurrentUserUtil.getCurrrentUserWorkbenchCode();
        wmsPackingOffshelfTaskVO.getTaskList().forEach(task -> {
            if (task.getStatus().equals(OffshelfTaskStatusEnum.FINISH_PACKING.getStatus())){
                task.setDeliveryWorkstationCode(workbenchCode);
                task.setRealOffshelfAccount(task.getWaitOffshelfAccount());
                finishTaskList.add(task);
            }
        });
        if (CollUtil.isNotEmpty(finishTaskList)){
            this.updateBatchById(finishTaskList);
            List<Long> disIdList = finishTaskList.stream().map(x->x.getAllocationId()).collect(Collectors.toList());
            LambdaQueryWrapper<WmsOutboundOrderInfoToDistributionMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(WmsOutboundOrderInfoToDistributionMaterial::getId,disIdList);
            List<WmsOutboundOrderInfoToDistributionMaterial> distributionList = wmsOutboundOrderInfoToDistributionMaterialService.list(lambdaQueryWrapper);
            endOrderToPackingTask(distributionList);
            wmsPackingOffshelfTaskVO.setTaskList(finishTaskList);
            changeMaterialEndStatus(wmsPackingOffshelfTaskVO);
        }
        return BaseResult.ok();
    }

    /**
     * @description 初始化拣货下架数据
     * @author  ciro
     * @date   2021/12/25 11:34
     * @param: orderNumber
     * @param: packingNumber
     * @param: isWave
     * @param: material
     * @return: com.jayud.model.po.WmsPackingOffshelfTask
     **/
    private WmsPackingOffshelfTask initTask(String orderNumber,String packingNumber,boolean isWave,WmsOutboundOrderInfoToDistributionMaterial material){
        String packingDetailNumber = codeUtils.getCodeByRule(CodeConStants.PACKING_OFF_SHELF_DETAIL);
        WmsPackingOffshelfTask task = new WmsPackingOffshelfTask();
        BeanUtils.copyProperties(material,task);
        task.setId(null);
        task.clearCreate();
        task.clearUpdate();
        task.setAllocationId(material.getId());
        if (isWave){
            task.setWaveNumber(orderNumber);
        }else {
            task.setOrderNumber(orderNumber);
        }
        task.setOffshelfLocationId(material.getWarehouseLocationId());
        task.setOffshelfLocationCode(material.getWarehouseLocationCode());
        task.setOffshelfLocationName("拣货下架库位name");
        task.setWaitOffshelfAccount(material.getRealDistributionAccount());
        task.setDeliveryWorkstationId(0L);
        task.setDeliveryWorkstationCode("工作站code");
        task.setDeliveryWorkstationName("工作站name");
        task.setDeliverySowingId(0L);
        task.setDeliverySowingCode("播种位code");
        task.setDeliverySowingName("播种位name");
        task.setRealOffshelfAccount(new BigDecimal(0));
        task.setPackingOffshelfNumber(packingNumber);
        task.setTaskDetailNumber(packingDetailNumber);
        return task;
    }


    /**
     * @description 检查是否全部完成分配
     * @author  ciro
     * @date   2021/12/29 13:44
     * @param: orderNumber
     * @param: isWave
     * @return: boolean
     **/
    private boolean checkIsAllocateInventory(String orderNumber,boolean isWave){
        boolean isAll = false;
        if (isWave){
            WmsWaveOrderInfo wmsWaveOrderInfo = new WmsWaveOrderInfo();
            wmsWaveOrderInfo.setWaveNumber(orderNumber);
            List<WmsWaveOrderInfo> list = wmsWaveOrderInfoService.selectList(wmsWaveOrderInfo);
            if (!list.isEmpty()){
                if (list.get(0).getStatus().equals(WaveOrdertSatus.ASSIGNED.getType())){
                    isAll = true;
                }
            }
        }else {
            WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO = new WmsOutboundOrderInfoVO();
            wmsOutboundOrderInfoVO.setOrderNumber(orderNumber);
            List<WmsOutboundOrderInfoVO> list = wmsOutboundOrderInfoService.selectList(wmsOutboundOrderInfoVO);
            if (!list.isEmpty()){
                if (list.get(0).getOrderStatusType().equals(OutboundOrdertSatus.ASSIGNED.getType())){
                    isAll = true;
                }
            }

        }
        return isAll;
    }

    /**
     * @description 检查是否取消下架状态
     * @author  ciro
     * @date   2021/12/29 9:48
     * @param: detailNumber
     * @param: isWave
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult checkCancelStatus(String detailNumber,boolean isWave){
        boolean isProgress = false;
        boolean isFinish = false;
        WmsPackingOffshelfTask task = new WmsPackingOffshelfTask();
        task.setTaskDetailNumber(detailNumber);
        List<WmsPackingOffshelfTask> taskList = selectList(task);
        //判断是否在拣货中或者拣货完成
        for (int i=0;i<taskList.size();i++){
            if (taskList.get(i).getStatus().equals(OffshelfTaskStatusEnum.PACKING.getStatus())){
                isProgress = true;
                break;
            }
            if (taskList.get(i).getStatus().equals(OffshelfTaskStatusEnum.FINISH_PACKING.getStatus())){
                isFinish = true;
                break;
            }
        }
        if (isProgress){
            return BaseResult.error(OffshelfTaskStatusEnum.PACKING.getStatus(),OffshelfTaskStatusEnum.PACKING.getDesc());
        }
        if (isFinish){
            return BaseResult.ok(OffshelfTaskStatusEnum.FINISH_PACKING.getDesc());
        }
        return BaseResult.ok();
    }

    /**
     * @description 根据条件查询
     * @author  ciro
     * @date   2021/12/29 10:04
     * @param: outboundOrderNumberVO
     * @return: com.jayud.model.po.WmsPackingOffshelfTask
     **/
    private WmsPackingOffshelfTask getTask(WmsPackingOffshelfTask tasks){
        LambdaQueryWrapper<WmsPackingOffshelfTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getTaskDetailNumber,tasks.getTaskDetailNumber());
        lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getIsDeleted,false);
        WmsPackingOffshelfTask task = this.getOne(lambdaQueryWrapper);
        return task;
    }

    /**
     * @description 检查是否全部完成
     * @author  ciro
     * @date   2021/12/29 10:06
     * @param: outboundOrderNumberVO
     * @return: void
     **/
    private BaseResult checkAllFinish(OutboundOrderNumberVO outboundOrderNumberVO){
        boolean isFinish = true;
        WmsPackingOffshelfTask wmsPackingOffshelfTask = new WmsPackingOffshelfTask();
        if (outboundOrderNumberVO.getIsWave()){
            wmsPackingOffshelfTask.setWaveNumber(outboundOrderNumberVO.getOrderNumber());
        }else {
            wmsPackingOffshelfTask.setOrderNumber(outboundOrderNumberVO.getOrderNumber());
        }
        List<WmsPackingOffshelfTask> list = selectList(wmsPackingOffshelfTask);
        if (outboundOrderNumberVO.getIsWave()){
            changeWaveOrderMsg(outboundOrderNumberVO.getOrderNumber());
        }
        for (int i=0;i<list.size();i++){
            if (!list.get(i).getStatus().equals(OffshelfTaskStatusEnum.FINISH_PACKING.getStatus())){
                isFinish = false;
                break;
            }
        }
        if (isFinish){
            List<WmsShippingReview> shippingReviewList = new ArrayList<>();
            list.forEach(task -> {
                shippingReviewList.add(initShipping(task));
            });
            if (!shippingReviewList.isEmpty()){
                wmsShippingReviewService.saveBatch(shippingReviewList);
            }
            if (outboundOrderNumberVO.getIsEnd() != null) {
                if (outboundOrderNumberVO.getIsEnd()) {
                    finishOrder(outboundOrderNumberVO.getOrderNumber(), outboundOrderNumberVO.getIsWave());
                }
            }
        }
        return BaseResult.ok();
    }

    /**
     * @description 初始化发运复核数据
     * @author  ciro
     * @date   2021/12/29 10:32
     * @param: task
     * @return: com.jayud.model.po.WmsShippingReview
     **/
    private WmsShippingReview initShipping(WmsPackingOffshelfTask task){
        WmsOutboundOrderInfoToDistributionMaterial material = wmsOutboundOrderInfoToDistributionMaterialService.getById(task.getAllocationId());
        WmsShippingReview shippingReview = new WmsShippingReview();
        BeanUtils.copyProperties(task,shippingReview);
        shippingReview.setWareNumber(task.getWaveNumber());
        shippingReview.setId(null);
        shippingReview.clearCreate();
        shippingReview.clearUpdate();
        shippingReview.setPackingAccount(task.getWaitOffshelfAccount().intValue());
        shippingReview.setPackingOffshelfId(task.getId());
        shippingReview.setPackingOffshelfNumber(task.getPackingOffshelfNumber());
        shippingReview.setNotScannedAccount(task.getRealOffshelfAccount().intValue());
        shippingReview.setPackingOffshelfDetailNumber(task.getTaskDetailNumber());
        return shippingReview;
    }

    /**
     * @description 过滤需要生成下架单物料信息
     * @author  ciro
     * @date   2021/12/29 15:17
     * @param: distributionMaterialList
     * @param: orderNumber
     * @param: isWave
     * @return: java.util.List<com.jayud.model.po.WmsOutboundOrderInfoToDistributionMaterial>
     **/
    private List<WmsOutboundOrderInfoToDistributionMaterial> getPackingList(List<WmsOutboundOrderInfoToDistributionMaterial> distributionMaterialList,
                                                                            String orderNumber,
                                                                            boolean isWave){
        List<WmsOutboundOrderInfoToDistributionMaterial> reList = new ArrayList<>();
        WmsPackingOffshelfTask task = new WmsPackingOffshelfTask();
        if (isWave){
            task.setWaveNumber(orderNumber);
        }else {
            task.setOrderNumber(orderNumber);
        }
        List<Long> exitList = new ArrayList<>();
        List<WmsPackingOffshelfTask> taskList = selectList(task);
        if (taskList.isEmpty()){
            return distributionMaterialList;
        }
        if (CollUtil.isNotEmpty(taskList)){
            exitList = taskList.stream().map(x->x.getAllocationId()).distinct().collect(Collectors.toList());
        }
        for (WmsOutboundOrderInfoToDistributionMaterial material : distributionMaterialList){
            if (!exitList.contains(material.getId())){
                reList.add(material);
            }
        }
        return reList;
    }

    /**
     * @description 根据编号获取下架单号
     * @author  ciro
     * @date   2021/12/29 15:29
     * @param: orderNumber
     * @param: isWave
     * @return: java.lang.String
     **/
    private String getPackingNumberByNumber(String orderNumber,boolean isWave){
        WmsPackingOffshelfTask task = new WmsPackingOffshelfTask();
        if (isWave){
            task.setWaveNumber(orderNumber);
        }else {
            task.setOrderNumber(orderNumber);
        }
        List<WmsPackingOffshelfTask> taskList = selectList(task);
        return taskList.get(0).getPackingOffshelfNumber();
    }

    /**
     * @description 查出所有任务明细
     * @author  ciro
     * @date   2021/12/29 16:04
     * @param: outboundOrderNumberVO
     * @return: com.jayud.model.vo.OutboundOrderNumberVO
     **/
    private OutboundOrderNumberVO getAllDetailNumberList(OutboundOrderNumberVO outboundOrderNumberVO){
        List<String> reList = new ArrayList<>();
        WmsPackingOffshelfTask wmsPackingOffshelfTask = new WmsPackingOffshelfTask();
        if (outboundOrderNumberVO.getOrderNumberList() != null){
            if (!outboundOrderNumberVO.getOrderNumberList().isEmpty()){
                outboundOrderNumberVO.getOrderNumberList().forEach(x->{
                    if (outboundOrderNumberVO.getIsWave()){
                        wmsPackingOffshelfTask.setWaveNumber(x);
                    }else {
                        wmsPackingOffshelfTask.setOrderNumber(x);
                    }
                });
                List<WmsPackingOffshelfTask> taskLists = selectList(wmsPackingOffshelfTask);
                taskLists.forEach(x->{
                    reList.add(x.getTaskDetailNumber());
                });
                outboundOrderNumberVO.setIsAllOrder(true);
            }
        }
        if (!reList.isEmpty()){
            List<String> detailList = new ArrayList<>();
            if (outboundOrderNumberVO.getDetailNumberList()!=null){
                detailList = outboundOrderNumberVO.getDetailNumberList();
            }
            detailList.addAll(reList);
            outboundOrderNumberVO.setDetailNumberList(detailList);
        }
        return outboundOrderNumberVO;
    }

    /**
     * @description 根据任务明细号查询出库单号
     * @author  ciro
     * @date   2021/12/29 16:11
     * @param: detailList
     * @return: java.util.List<java.lang.String>
     **/
    private List<String> getOrdeNumberByDetail(List<String> detailList,boolean isWave){
        List<String> orderList = new ArrayList<>();
        LambdaQueryWrapper<WmsPackingOffshelfTask> taskLambdaQueryWrapper = new LambdaQueryWrapper<>();
        taskLambdaQueryWrapper.in(WmsPackingOffshelfTask::getTaskDetailNumber,detailList);
        taskLambdaQueryWrapper.eq(WmsPackingOffshelfTask::getIsDeleted,false);
        List<WmsPackingOffshelfTask> taskLit = this.list(taskLambdaQueryWrapper);
        taskLit.forEach(task->{
            if (!orderList.contains(task.getOrderNumber())){
                if (isWave){
                    orderList.add(task.getWaveNumber());
                }else {
                    orderList.add(task.getOrderNumber());
                }
            }
        });
        return orderList;
    }

    /**
     * @description 获取物料类型数量
     * @author  ciro
     * @date   2022/1/5 11:21
     * @param: orderNumber
     * @param: waveNumber
     * @return: int
     **/
    private int getMaterialCount(String orderNumber,String waveNumber){
        if (StringUtils.isNotBlank(orderNumber)){
            WmsOutboundOrderInfoToMaterialVO materialVO = new WmsOutboundOrderInfoToMaterialVO();
            materialVO.setOrderNumber(orderNumber);
            List<WmsOutboundOrderInfoToMaterialVO> list = wmsOutboundOrderInfoToMaterialService.selectList(materialVO);
            return list.size();
        }
        if (StringUtils.isNotBlank(waveNumber)){
            WmsWaveToMaterial material = new WmsWaveToMaterial();
            material.setWaveNumber(waveNumber);
            List<WmsWaveToMaterial> list = wmsWaveToMaterialService.selectList(material);
            return list.size();
        }
        return 0;
    }

    /**
     * @description 修改波次下架信息
     * @author  ciro
     * @date   2022/1/5 11:25
     * @param: waveNumber
     * @return: void
     **/
    private void changeWaveOrderMsg(String waveNumber){
        WmsWaveToOutboundInfo info = new WmsWaveToOutboundInfo();
        info.setWaveNumber(waveNumber);
        List<WmsWaveToOutboundInfo> infoList = wmsWaveToOutboundInfoService.selectList(info);
        WmsPackingOffshelfTask task = new WmsPackingOffshelfTask();
        task.setWaveNumber(waveNumber);
        List<WmsPackingOffshelfTask> taskList = selectList(task);
        List<String> packingList = new ArrayList<>();
        //拣货量
        BigDecimal packCount = new BigDecimal(0);
        //完成订单数
        int finishCount = 0;

        for (WmsPackingOffshelfTask x :taskList){
            if (x.getStatus().equals(OffshelfTaskStatusEnum.FINISH_PACKING.getStatus())){
                packingList.add(x.getMaterialCode()+"*"+x.getBatchCode()+"*"+x.getMaterialProductionDate()+"*"+x.getCustomField1()+"*"+x.getCustomField2()+"*"+x.getCustomField3());
                packCount = packCount.add(x.getWaitOffshelfAccount());
            }
        }

        for (WmsWaveToOutboundInfo x:infoList){
            WmsOutboundOrderInfoVO infoVO = new WmsOutboundOrderInfoVO();
            infoVO.setOrderNumber(x.getOrderNumber());
            WmsOutboundOrderInfoVO infos = wmsOutboundOrderInfoService.queryByCode(infoVO);
            //是否整个订单完成
            boolean isAll = true;
            for (WmsOutboundOrderInfoToMaterialVO y:infos.getThisMaterialList()){
                String keys = y.getMaterialCode()+"*"+y.getBatchCode()+"*"+y.getMaterialProductionDate()+"*"+y.getCustomField1()+"*"+y.getCustomField2()+"*"+y.getCustomField3();
                if (!packingList.contains(keys)){
                    isAll = false;
                }
            }
            if (isAll){
                finishCount+=1;
            }
        }
        WmsWaveOrderInfo wmsWaveOrderInfo = new WmsWaveOrderInfo();
        wmsWaveOrderInfo.setWaveNumber(waveNumber);
        List<WmsWaveOrderInfo> waveOrderInfoList = wmsWaveOrderInfoService.selectList(wmsWaveOrderInfo);
        if (!waveOrderInfoList.isEmpty()){
            wmsWaveOrderInfo = waveOrderInfoList.get(0);
            wmsWaveOrderInfo.setFinishOrderAccount(finishCount);
            wmsWaveOrderInfo.setPackingAccount(packCount);
            wmsWaveOrderInfoService.updateById(wmsWaveOrderInfo);
        }
    }

    /**
     * @description 修改下架单数据
     * @author  ciro
     * @date   2022/1/6 15:56
     * @param: detailNumberList
     * @return: void
     **/
    private void changePackingAccount(List<String> detailNumberList){
        WmsPackingOffshelfTask wmsPackingOffshelfTask = new WmsPackingOffshelfTask();
        wmsPackingOffshelfTask.setDetailNumberList(detailNumberList);
        List<WmsPackingOffshelfTask> taskList = selectList(wmsPackingOffshelfTask);
        if (!taskList.isEmpty()){
            List<String> packingNumberList = taskList.stream().map(x->x.getPackingOffshelfNumber()).distinct().collect(Collectors.toList());
            OutboundOrderNumberVO outboundOrderNumberVO = new OutboundOrderNumberVO();
            outboundOrderNumberVO.setOrderNumberList(packingNumberList);
            wmsPackingOffshelfOrderService.saveOrder(outboundOrderNumberVO);
        }
    }

    /**
     * @description 返回下一个排序
     * @author  ciro
     * @date   2022/1/13 10:19
     * @param: orderNumber
     * @param: isWave
     * @return: int
     **/
    private int getNextOrder(String orderNumber,boolean isWave){
        List<WmsPackingOffshelfTask> list = getTaskListASCOrder(orderNumber, isWave);
        if (CollectionUtil.isNotEmpty(list)){
            return list.get(list.size()-1).getSorts()+1;
        }
        return 1;
    }

    /**
     * @description 升序获取未完成拣货下架单
     * @author  ciro
     * @date   2022/1/13 10:23
     * @param: orderNumber
     * @param: isWave
     * @return: java.util.List<com.jayud.model.po.WmsPackingOffshelfTask>
     **/
    private List<WmsPackingOffshelfTask> getTaskListASCOrder(String orderNumber,boolean isWave){
        LambdaQueryWrapper<WmsPackingOffshelfTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (isWave){
            lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getWaveNumber,orderNumber);
        }else {
            lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getOrderNumber,orderNumber);
        }
        lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getIsDeleted,false);
        lambdaQueryWrapper.ne(WmsPackingOffshelfTask::getStatus,OffshelfTaskStatusEnum.FINISH_PACKING.getStatus());
        lambdaQueryWrapper.orderByAsc(WmsPackingOffshelfTask::getSorts);
        List<WmsPackingOffshelfTask> list = this.list(lambdaQueryWrapper);
        return list;
    }

    /**
     * @description 确认修改其他订单状态
     * @author  ciro
     * @date   2022/1/13 10:29
     * @param: orderNumber
     * @param: isWave
     * @return: void
     **/
    private void comfirmChangeOrder(String orderNumber,boolean isWave){
        List<WmsPackingOffshelfTask> list = getTaskListASCOrder(orderNumber, isWave);
        if (CollectionUtil.isNotEmpty(list)){
            for (int i=0;i<list.size();i++){
                list.get(i).setSorts(i+1);
            }
        }
        if (!list.isEmpty()) {
            this.updateBatchById(list);
        }
    }

    /**
     * @description 根据详情号获取
     * @author  ciro
     * @date   2022/1/13 11:08
     * @param: detailNumber
     * @return: com.jayud.model.po.WmsPackingOffshelfTask
     **/
    private WmsPackingOffshelfTask getByDetailNumber(String detailNumber){
        LambdaQueryWrapper<WmsPackingOffshelfTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getTaskDetailNumber,detailNumber);
        lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getIsDeleted,false);
        WmsPackingOffshelfTask wmsPackingOffshelfTask = this.getOne(lambdaQueryWrapper);
        return wmsPackingOffshelfTask;
    }

    /**
     * @description 保存库存拣货下架数量
     * @author  ciro
     * @date   2022/1/13 16:15
     * @param: taskList
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult savePickingInventory(List<WmsPackingOffshelfTask> taskList,boolean isCancel){
        List<InventoryDetailForm> detailFormList = initPackingInventory(taskList);
        if (!detailFormList.isEmpty()){
            if (isCancel){
                return inventoryDetailService.cancelPicking(detailFormList);
            }else {
                return inventoryDetailService.generatePicking(detailFormList);
            }
        }
        return BaseResult.ok();
    }


    /**
     * @description 初始化拣货库存数据
     * @author  ciro
     * @date   2022/1/13 16:12
     * @param: taskList
     * @return: java.util.List<com.jayud.model.bo.InventoryDetailForm>
     **/
    private List<InventoryDetailForm> initPackingInventory(List<WmsPackingOffshelfTask> taskList){
        List<InventoryDetailForm> detailFormList = new ArrayList<>();
        taskList.forEach(task -> {
            InventoryDetailForm inventoryDetailForm = new InventoryDetailForm();
            inventoryDetailForm.setOwerCode(task.getOwerCode());
            inventoryDetailForm.setWarehouseCode(task.getWarehouseCode());
            inventoryDetailForm.setMaterialCode(task.getMaterialCode());


            BeanUtils.copyProperties(task,inventoryDetailForm);
            inventoryDetailForm.setId(null);
            inventoryDetailForm.setOperationCount(task.getWaitOffshelfAccount());
            inventoryDetailForm.setWarehouseLocationStatus(0);
            inventoryDetailForm.setWarehouseAreaId(task.getWarehouseAreaId());
            detailFormList.add(inventoryDetailForm);
        });
        return detailFormList;
    }

    private void changeWaveMsg(String waveNumber){
        LambdaQueryWrapper<WmsPackingOffshelfTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getWaveNumber,waveNumber);
        lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getStatus,OffshelfTaskStatusEnum.FINISH_PACKING.getStatus());
        List<WmsPackingOffshelfTask> taskList = new ArrayList<>();
        if (!taskList.isEmpty()) {
            BigDecimal allCount = new BigDecimal(0);
            for (WmsPackingOffshelfTask task : taskList) {
                allCount = allCount.add(task.getRealOffshelfAccount());
            }
            WmsWaveOrderInfo info = wmsWaveOrderInfoService.queryByWaveOrderNumber(waveNumber);
            info.setPackingAccount(allCount);
            wmsWaveOrderInfoService.save(info);
        }
    }

    /**
     * @description 修改分配数量、拣货下架数量
     * @author  ciro
     * @date   2022/1/14 9:58
     * @param: waveOrderNumberList
     * @param: isCancel
     * @return: void
     **/
    private void changeWavePackingAccount(List<String> waveOrderNumberList,boolean isCancel){
        WmsWaveOrderInfo info = new WmsWaveOrderInfo();
        info.setWaveOrderNumberList(waveOrderNumberList);
        List<WmsWaveOrderInfo> infoList = wmsWaveOrderInfoService.selectList(info);
        infoList.forEach(infos -> {
            if (isCancel){
                infos.setAllocationAccount(infos.getPackingAccount());
                infos.setPackingAccount(new BigDecimal(0));
            }else {
                infos.setPackingAccount(infos.getAllocationAccount());
                infos.setAllocationAccount(new BigDecimal(0));
            }
        });
        wmsWaveOrderInfoService.updateBatchById(infoList);
    }

    /**
     * @description 取消拣货下架修改数量
     * @author  ciro
     * @date   2022/1/14 10:12
     * @param: detailNumberList
     * @return: void
     **/
    private void cancelChangeAcount(List<String> detailNumberList){
        WmsPackingOffshelfTask task = new WmsPackingOffshelfTask();
        task.setDetailNumberList(detailNumberList);
        List<WmsPackingOffshelfTask> taskList = selectList(task);
        List<String> waveNumberList = taskList.stream().filter(x->StringUtils.isNotBlank(x.getWaveNumber())).map(x->x.getWaveNumber()).collect(Collectors.toList());
        changeWavePackingAccount(waveNumberList,true);
    }

    /**
     * @description 拣货下架完成结束整个单
     * @author  ciro
     * @date   2022/1/26 9:55
     * @param: orderNumber
     * @param: isWave
     * @return: void
     **/
    private void finishOrder(String orderNumber,boolean isWave){
        List<String> orderNumberList = new ArrayList<>();
        if (isWave){
            WmsWaveToOutboundInfo info = new WmsWaveToOutboundInfo();
            info.setWaveNumber(orderNumber);
            List<WmsWaveToOutboundInfo> infoList = wmsWaveToOutboundInfoService.selectList(info);
            if (CollUtil.isNotEmpty(infoList)){
                orderNumberList = infoList.stream().map(x->x.getOrderNumber()).collect(Collectors.toList());
            }
        }else {
            orderNumberList.add(orderNumber);
        }
        if (CollUtil.isNotEmpty(orderNumberList)){
            wmsOutboundOrderInfoService.finishOrder(orderNumberList);
            wmsOutboundOrderInfoToMaterialService.finishOrder(orderNumberList);
        }
    }


    /**
     * @description 到拣货下架完成出库
     * @author  ciro
     * @date   2022/3/11 14:11
     * @param: materialList
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult endOrderToPackingTask(List<WmsOutboundOrderInfoToDistributionMaterial> materialList){
        if (CollUtil.isNotEmpty(materialList)) {
            List<InventoryDetailForm> detailFormList = wmsOutboundOrderInfoToDistributionMaterialService.initInventoryDetail(materialList);
            if (CollUtil.isNotEmpty(detailFormList)){
                //完成出库
                BaseResult output = inventoryDetailService.output(detailFormList);
            }
        }
        return BaseResult.ok();
    }

    /**
     * @description 获取最先的拣货下架任务
     * @author  ciro
     * @date   2022/3/10 14:17
     * @param: wmsPackingOffshelfTaskVO
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult getSourceTask(WmsPackingOffshelfTaskVO wmsPackingOffshelfTaskVO){
        LambdaQueryWrapper<WmsPackingOffshelfTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(wmsPackingOffshelfTaskVO.getWarehouseCode())) {
            lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getWarehouseCode, wmsPackingOffshelfTaskVO.getWarehouseCode());
        }
        if (StrUtil.isNotBlank(wmsPackingOffshelfTaskVO.getOwerCode())) {
            lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getOwerCode, wmsPackingOffshelfTaskVO.getOwerCode());
        }
        if (StrUtil.isNotBlank(wmsPackingOffshelfTaskVO.getShelfCode())) {
            lambdaQueryWrapper.eq(WmsPackingOffshelfTask::getShelfCode, wmsPackingOffshelfTaskVO.getShelfCode());
        }
        lambdaQueryWrapper.ne(WmsPackingOffshelfTask::getStatus,OffshelfTaskStatusEnum.FINISH_PACKING.getStatus());
        lambdaQueryWrapper.orderByAsc(WmsPackingOffshelfTask::getCreateTime);
        List<WmsPackingOffshelfTask> taskLists = this.list(lambdaQueryWrapper);
        List<WmsPackingOffshelfTask> taskList = new ArrayList<>();
        if (CollUtil.isNotEmpty(taskLists)){
            String taskNumber = taskLists.get(0).getPackingOffshelfNumber();
            for (WmsPackingOffshelfTask task : taskLists){
                if (!taskNumber.equals(task.getPackingOffshelfNumber())){
                    break;
                }
                taskList.add(task);
            }
            wmsPackingOffshelfTaskVO.setTaskList(taskList);
        }
        return BaseResult.ok();
    }

    /**
     * @description 设置库位状态
     * @author  ciro
     * @date   2022/3/10 14:40
     * @param: packingOffshelfTaskVO
     * @return: void
     **/
    private void setLocationStatus(WmsPackingOffshelfTaskVO packingOffshelfTaskVO){
        Map<String,WmsPackingOffshelfTask> locationMap = new HashMap<>();
        List<WmsPackingOffshelfTask> taskList = new ArrayList<>();
        //筛选需拣货下架数据
        if(CollUtil.isNotEmpty(packingOffshelfTaskVO.getTaskList())){
            for (WmsPackingOffshelfTask task : packingOffshelfTaskVO.getTaskList()){
                locationMap.put(task.getWarehouseLocationCode(),task);
                if (!task.getStatus().equals(WarehouseLocationStatusEnum.FINISHED.getStatus())){
                    taskList.add(task);
                }
            }
            packingOffshelfTaskVO.setTaskList(taskList);
        }
        List<WarehouseLocationVO> locationList = packingOffshelfTaskVO.getLocationList();
        if (CollUtil.isNotEmpty(locationList)){
            //设置库位状态
            for (WarehouseLocationVO location : locationList){
                if (locationMap.containsKey(location.getCode())){
                    WmsPackingOffshelfTask task = locationMap.get(location.getCode());
                    location.setLocationStatus(task.getStatus());
                    location.setMaterialCode(task.getMaterialCode());
                    location.setMaterialCount(task.getWaitOffshelfAccount());
                    if (task.getStatus().equals(WarehouseLocationStatusEnum.WAIT.getStatus())){
                        location.setColorCode(ColorCodeEnum.ORANGE.getTypeCode());
                    }else if (task.getStatus().equals(WarehouseLocationStatusEnum.FINISHED.getStatus())){
                        location.setColorCode(ColorCodeEnum.WHITE.getTypeCode());
                    }
                }else {
                    location.setLocationStatus(WarehouseLocationStatusEnum.IRRELEVANT.getStatus());
                    location.setColorCode(ColorCodeEnum.WHITE.getTypeCode());
                }
            }
        }
    }

    /**
     * @description 修改物料状态--道科
     * @author  ciro
     * @date   2022/3/10 16:17
     * @param: packingOffshelfTaskVO
     * @return: void
     **/
    private void changeMaterialEndStatus(WmsPackingOffshelfTaskVO packingOffshelfTaskVO){
        WmsOutboundOrderMsgDTO wmsOutboundOrderMsgDTO = getOrderToPackingRelation(packingOffshelfTaskVO);
        if (wmsOutboundOrderMsgDTO.getOrderStatus().equals(OutboundOrdertSatus.ISSUED.getType())){
            WmsOutboundOrderInfo wmsOutboundOrderInfo = wmsOutboundOrderInfoService.getById(wmsOutboundOrderMsgDTO.getOrderId());
            wmsOutboundOrderInfo.setOrderStatusType(OutboundOrdertSatus.ISSUED.getType());
            wmsOutboundOrderInfoService.updateById(wmsOutboundOrderInfo);
        }
        List<WmsOutboundOrderInfoToMaterial> updateMaterialList = new ArrayList<>();
        wmsOutboundOrderMsgDTO.getMaterialList().forEach(material -> {
            if (material.getMaterialStatus().equals(OutboundOrdertSatus.ISSUED.getType())){
                WmsOutboundOrderInfoToMaterial materials = wmsOutboundOrderInfoToMaterialService.getById(material.getMaterialId());
                materials.setStatusType(OutboundOrdertSatus.ISSUED.getType());
                updateMaterialList.add(materials);
            }
        });
        if (CollUtil.isNotEmpty(updateMaterialList)){
            wmsOutboundOrderInfoToMaterialService.updateBatchById(updateMaterialList);
        }
    }

    /**
     * @description 获取出库单-拣货下架数据
     * @author  ciro
     * @date   2022/3/10 18:08
     * @param: packingOffshelfTaskVO
     * @return: com.jayud.model.dto..OutboundToPacking.WmsOutboundOrderMsgDTO
     **/
    private WmsOutboundOrderMsgDTO getOrderToPackingRelation(WmsPackingOffshelfTaskVO packingOffshelfTaskVO){
        String orderNumber = packingOffshelfTaskVO.getTaskList().get(0).getOrderNumber();
        List<WmsOutboundToPackingDTO> orderToPackingList = wmsPackingOffshelfTaskMapper.selectOutboundToPackingMsg(orderNumber);
        WmsOutboundOrderMsgDTO wmsOutboundOrderMsgDTO = new WmsOutboundOrderMsgDTO();
        Map<Long, WmsOutboundOrderToMaterialDTO> materialMap = new HashMap<>();
        Map<Long, List<WmsOutboundOrderToDistributeMaterialDTO>> distributeMap = new HashMap<>();
        if (CollUtil.isNotEmpty(orderToPackingList)){
            WmsOutboundToPackingDTO packings = orderToPackingList.get(0);
            wmsOutboundOrderMsgDTO.setOrderId(packings.getOrderId());
            wmsOutboundOrderMsgDTO.setOrderNumber(packings.getOrderNumber());
            wmsOutboundOrderMsgDTO.setNoticeOrderNumber(packings.getNoticeOrderNumber());
            wmsOutboundOrderMsgDTO.setOrderStatus(packings.getOrderStatus());
            orderToPackingList.forEach(packing -> {
                if (!materialMap.containsKey(packing.getMaterialId())){
                    WmsOutboundOrderToMaterialDTO wmsOutboundOrderToMaterialDTO = new WmsOutboundOrderToMaterialDTO();
                    BeanUtils.copyProperties(packing,wmsOutboundOrderToMaterialDTO);
                    materialMap.put(packing.getMaterialId(),wmsOutboundOrderToMaterialDTO);
                }
                if (!distributeMap.containsKey(packing.getMaterialId())){
                    distributeMap.put(packing.getMaterialId(),new ArrayList<>());
                }
                WmsOutboundOrderToDistributeMaterialDTO wmsOutboundOrderToDistributeMaterialDTO = new WmsOutboundOrderToDistributeMaterialDTO();
                BeanUtils.copyProperties(packing,wmsOutboundOrderToDistributeMaterialDTO);
                distributeMap.get(packing.getMaterialId()).add(wmsOutboundOrderToDistributeMaterialDTO);

            });
        }
        List<WmsOutboundOrderToMaterialDTO> materialList = new ArrayList<>();
        boolean orderAllFinish = true;
        for (Long materialId : materialMap.keySet()){
            WmsOutboundOrderToMaterialDTO material = materialMap.get(materialId);
            //完全分配做处理
            if (!material.getMaterialStatus().equals(OutboundOrdertSatus.ASSIGNED.getType())){
                continue;
            }
            if (distributeMap.containsKey(materialId)){
                List<WmsOutboundOrderToDistributeMaterialDTO> disList = distributeMap.get(materialId);
                boolean allFinish = true;
                for (WmsOutboundOrderToDistributeMaterialDTO disbute:disList){
                    if (disbute.getPackingMaterialStatus().equals(OffshelfTaskStatusEnum.WAIT_PACKING.getStatus())){
                        allFinish = false;
                        orderAllFinish = false;
                    }
                }
                material.setDistributeList(disList);
                if (allFinish){
                    material.setMaterialStatus(OutboundOrdertSatus.ISSUED.getType());
                }
                materialList.add(material);
            }
        }
        wmsOutboundOrderMsgDTO.setMaterialList(materialList);
        if (orderAllFinish){
            //完全分配处理
            if (wmsOutboundOrderMsgDTO.getOrderStatus().equals(OutboundOrdertSatus.ASSIGNED.getType())) {
                wmsOutboundOrderMsgDTO.setOrderStatus(OutboundOrdertSatus.ISSUED.getType());
            }
        }
        return wmsOutboundOrderMsgDTO;
    }

}
