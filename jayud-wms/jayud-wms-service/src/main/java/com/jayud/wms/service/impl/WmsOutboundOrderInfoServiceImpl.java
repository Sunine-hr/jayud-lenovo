package com.jayud.wms.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.WmsOutboundOrderInfoMapper;
import com.jayud.wms.mapper.WmsOutboundOrderInfoToDistributionMaterialMapper;
import com.jayud.wms.mapper.WmsOutboundOrderInfoToMaterialMapper;
import com.jayud.wms.mapper.WmsWaveOrderInfoMapper;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.constant.AllocationStrategyConstant;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.dto.allocationStrategy.AllocationStrategyDTO;
import com.jayud.wms.model.enums.NoticeOrdertSatus;
import com.jayud.wms.model.enums.OutboundOrdertSatus;
import com.jayud.wms.model.enums.WaveOrdertSatus;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.*;
import com.jayud.wms.service.*;
import com.jayud.wms.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
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
 * 出库单 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Slf4j
@Service
public class WmsOutboundOrderInfoServiceImpl extends ServiceImpl<WmsOutboundOrderInfoMapper, WmsOutboundOrderInfo> implements IWmsOutboundOrderInfoService {

    @Autowired
    private CodeUtils codeUtils;

    @Autowired
    private IWmsOutboundNoticeOrderInfoService wmsOutboundNoticeOrderInfoService;
    @Autowired
    private IWmsOutboundOrderInfoToMaterialService wmsOutboundOrderInfoToMaterialService;
    @Autowired
    private IInventoryDetailService inventoryDetailService;
    @Autowired
    private IWmsOutboundOrderInfoToDistributionMaterialService wmsOutboundOrderInfoToDistributionMaterialService;
    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;
    @Autowired
    private IWmsPackingOffshelfTaskService wmsPackingOffshelfTaskService;

    @Autowired
    private IWmsAllocationStrategyService wmsAllocationStrategyService;
    @Autowired
    private IWarehouseLocationService warehouseLocationService;

    @Autowired
    private IWmsOutboundOrderInfoToServiceService wmsOutboundOrderInfoToServiceService;
    @Autowired
    private IWmsOutboundShippingReviewInfoService wmsOutboundShippingReviewInfoService;


    @Autowired
    private WmsOutboundOrderInfoMapper wmsOutboundOrderInfoMapper;
    @Autowired
    private WmsOutboundOrderInfoToMaterialMapper wmsOutboundOrderInfoToMaterialMapper;
    @Autowired
    private WmsOutboundOrderInfoToDistributionMaterialMapper wmsOutboundOrderInfoToDistributionMaterialMapper;
    @Autowired
    private WmsWaveOrderInfoMapper wmsWaveOrderInfoMapper;


    final BigDecimal zoreAccount = new BigDecimal(0);

    @Override
    public IPage<WmsOutboundOrderInfoVO> selectPage(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO,
                                                    Integer currentPage,
                                                    Integer pageSize,
                                                    HttpServletRequest req){

        Page<WmsOutboundOrderInfoVO> page=new Page<WmsOutboundOrderInfoVO>(currentPage,pageSize);
        IPage<WmsOutboundOrderInfoVO> pageList= wmsOutboundOrderInfoMapper.pageList(page, wmsOutboundOrderInfoVO);
        return pageList;
    }

    @Override
    public List<WmsOutboundOrderInfoVO> selectList(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO){
        return wmsOutboundOrderInfoMapper.list(wmsOutboundOrderInfoVO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsOutboundOrderInfoMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundOrderInfoForExcel(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult transferOut(OutboundOrderNumberVO outboundOrderNumberVO) {
        List<String> errorList = new ArrayList<>();
        List<String> closeErrList = new ArrayList<>();
        WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO = new WmsOutboundNoticeOrderInfoVO();
        List<WmsOutboundOrderInfo> addInfoList = new ArrayList<>();
        List<WmsOutboundOrderInfoToMaterial> addMaterialList = new ArrayList<>();
        for (String orderNumber : outboundOrderNumberVO.getOrderNumberList()){
            wmsOutboundNoticeOrderInfoVO.setOrderNumber(orderNumber);
            if (checkExitOutbound(wmsOutboundNoticeOrderInfoVO.getOrderNumber())){
                errorList.add(orderNumber);
                continue;
            }
            WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVos = wmsOutboundNoticeOrderInfoService.queryByCode(wmsOutboundNoticeOrderInfoVO);
            if (!isAllClock(wmsOutboundNoticeOrderInfoVos.getThisMaterialList())){
                closeErrList.add(orderNumber);
                continue;
            }
            wmsOutboundNoticeOrderInfoVos.setOrderStatusType("2");
            WmsOutboundOrderInfo wmsOutboundOrderInfo = new WmsOutboundOrderInfo();
            BeanUtils.copyProperties(wmsOutboundNoticeOrderInfoVos,wmsOutboundOrderInfo);
            wmsOutboundOrderInfo.clearCreate();
            wmsOutboundOrderInfo.clearUpdate();
            wmsOutboundOrderInfo.setId(null);
            wmsOutboundOrderInfo.setNoticeOrderNumber(wmsOutboundNoticeOrderInfoVos.getOrderNumber());
            wmsOutboundOrderInfo.setOrderNumber(codeUtils.getCodeByRule(CodeConStants.OUTBOUND_ORDER_NUMBER));
//            wmsOutboundOrderInfo.setOrderNumber();
            addMaterialList.addAll(changeMaterial(wmsOutboundOrderInfo.getOrderNumber(),wmsOutboundNoticeOrderInfoVos.getThisMaterialList()));
            addInfoList.add(wmsOutboundOrderInfo);
        }
        if (CollUtil.isNotEmpty(addInfoList)){
            this.saveBatch(addInfoList);
        }
        if (CollUtil.isNotEmpty(addMaterialList)){
            wmsOutboundOrderInfoToMaterialService.saveBatch(addMaterialList);
        }
        String errMsg = "";
        if (!errorList.isEmpty()){
            errMsg += StringUtils.join(errorList,",")+"已转为出库单，请勿重复生成出库单！";
        }
        if (!closeErrList.isEmpty()){
            errMsg += StringUtils.join(closeErrList,",")+"缺货中！";
        }
        if (StringUtils.isNotBlank(errMsg)){
            return BaseResult.error(errMsg);
        }
        return BaseResult.ok(SysTips.CHANGE_OUTBOUND_ORDER_SUCCESS);
    }

    @Override
    public WmsOutboundOrderInfoVO queryByCode(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO) {
        List<WmsOutboundOrderInfoVO> infoList = wmsOutboundOrderInfoMapper.list(wmsOutboundOrderInfoVO);
        if (!infoList.isEmpty()){
            wmsOutboundOrderInfoVO = infoList.get(0);
            WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO = new WmsOutboundOrderInfoToMaterialVO();
            wmsOutboundOrderInfoToMaterialVO.setOrderNumber(wmsOutboundOrderInfoVO.getOrderNumber());
            List<WmsOutboundOrderInfoToMaterialVO> thisMaterialList = wmsOutboundOrderInfoToMaterialService.selectList(wmsOutboundOrderInfoToMaterialVO);
            wmsOutboundOrderInfoVO.setThisMaterialList(thisMaterialList);
            WmsOutboundOrderInfoToService wmsOutboundOrderInfoToService = new WmsOutboundOrderInfoToService();
            wmsOutboundOrderInfoToService.setOrderNumber(wmsOutboundOrderInfoVO.getOrderNumber());
            List<WmsOutboundOrderInfoToService> serviceList = wmsOutboundOrderInfoToServiceService.selectList(wmsOutboundOrderInfoToService);
            wmsOutboundOrderInfoVO.setServiceList(serviceList);
        }

        return wmsOutboundOrderInfoVO;
    }

    @Override
    public IPage<WmsOutboundOrderInfoVO> selectUnStockToWave(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO,
                                                             Integer currentPage,
                                                             Integer pageSize) {
        Page<WmsOutboundOrderInfoVO> page=new Page<WmsOutboundOrderInfoVO>(currentPage,pageSize);
        IPage<WmsOutboundOrderInfoVO> pageList= wmsOutboundOrderInfoMapper.selectUnStockToWavePage(page,wmsOutboundOrderInfoVO);
        return pageList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult allocateInventory(OutboundOrderNumberVO outboundOrderNumberVO) {
        List<String> isDistributionList = new ArrayList<>();
        List<String> isFinishList = new ArrayList<>();
        //缺货出库单
        List<String> stockList = new ArrayList<>();
        List<WmsOutboundOrderInfoToMaterialVO> updateMaterialList = new ArrayList<>();
        List<WmsOutboundOrderInfoToDistributionMaterial> addDistributionList = new ArrayList<>();
        List<String> distributionOrderList = new ArrayList<>();
        for (String orderNumber : outboundOrderNumberVO.getOrderNumberList()){
            WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO = new WmsOutboundOrderInfoVO();
            List<WmsOutboundOrderInfoToMaterialVO> materialList = new ArrayList<>();
            List<WmsOutboundOrderInfoToDistributionMaterial> distributionList = new ArrayList<>();
            wmsOutboundOrderInfoVO.setOrderNumber(orderNumber);
            wmsOutboundOrderInfoVO = this.queryByCode(wmsOutboundOrderInfoVO);
            BaseResult orderResult = checkDistribution(wmsOutboundOrderInfoVO.getOrderStatusType());
            if (!orderResult.isSuccess()){
                if (orderResult.getMsg().equals(SysTips.OUTBOUND_IS_DISTRIBUTION)){
                    isDistributionList.add(orderNumber);
                }else if (orderResult.getMsg().equals(SysTips.OUTBOUND_IS_FINISH)){
                    isFinishList.add(orderNumber);
                }
            }
            materialList = wmsOutboundOrderInfoVO.getThisMaterialList();
            boolean isLock = true;
            boolean isAllLock = true;
            //判断是否有库存
            for (int i=0;i< materialList.size();i++){
                WmsOutboundOrderInfoToMaterialVO material = materialList.get(i);
                //已分配、已出库跳过
                if (material.getStatusType().equals(OutboundOrdertSatus.ASSIGNED.getType())
                        ||material.getStatusType().equals(OutboundOrdertSatus.ISSUED.getType())){
                    continue;
                }
                String owerCode = wmsOutboundOrderInfoVO.getOwerCode();
                String warehouseCode = wmsOutboundOrderInfoVO.getWarehouseCode();
                BaseResult<List<InventoryDetail>> inventoryResult = getWarehouseByCode(owerCode,
                        warehouseCode,
                        material.getMaterialCode(),
                        material.getRequirementAccount(),
                        material.getBatchCode(),
                        material.getMaterialProductionDate(),
                        material.getCustomField1(),
                        material.getCustomField2(),material.getCustomField3());
                if (!inventoryResult.isSuccess()){
                    isLock = false;
                }
                boolean isStock = true;
                //锁定库存
                if (isLock){
                    if (lockInventory(inventoryResult.getResult()).isSuccess()){
                        material.setStatusType(OutboundOrdertSatus.ASSIGNED.getType());
                        material.setDetailList(inventoryResult.getResult());
                        material.setIsAddDistribution(true);
                        isStock = false;
                    }
                }
                if (isStock){
                    material.setStatusType(OutboundOrdertSatus.OUT_STOCK.getType());
                    isLock = false;
                    material.setIsAddDistribution(false);
                    stockList.add(material.getOrderNumber());
                }
                if (isLock){
                    distributionList = initDistribution(wmsOutboundOrderInfoVO,material,material.getDetailList());
                    if (!distributionList.isEmpty()){
                        distributionOrderList.addAll(distributionList.stream().map(x->x.getOrderNumber()).collect(Collectors.toList()));
                        addDistributionList.addAll(distributionList);
                        material.setDistributionAccount(material.getRequirementAccount());
                    }
                }else if (!isLock){
                    isAllLock = false;
                }
                updateMaterialList.add(material);
            }
            WmsOutboundOrderInfo wmsOutboundOrderInfo = new WmsOutboundOrderInfo();
            BeanUtils.copyProperties(wmsOutboundOrderInfoVO, wmsOutboundOrderInfo);
            if (isAllLock) {
                wmsOutboundOrderInfo.setOrderStatusType(OutboundOrdertSatus.ASSIGNED.getType());
            } else {
                wmsOutboundOrderInfo.setOrderStatusType(OutboundOrdertSatus.OUT_STOCK.getType());
            }
            wmsOutboundOrderInfo.setAssignorBy(CurrentUserUtil.getUsername());
            wmsOutboundOrderInfo.setAssignorTime(LocalDateTime.now());
            this.updateById(wmsOutboundOrderInfo);
        }

        if (!updateMaterialList.isEmpty()) {
            updateMaterialAccount(updateMaterialList);
        }
        if (!addDistributionList.isEmpty()){
            wmsOutboundOrderInfoToDistributionMaterialService.saveBatch(addDistributionList);
        }
        String errMsg = "";
        if (!isDistributionList.isEmpty()){
            errMsg+=StringUtils.join(isDistributionList,",")+" "+SysTips.OUTBOUND_IS_DISTRIBUTION;
        }
        if (!isFinishList.isEmpty()){
            errMsg+=StringUtils.join(isFinishList,",")+" "+SysTips.OUTBOUND_IS_FINISH;
        }
        if (!stockList.isEmpty()){
            errMsg+=StringUtils.join(stockList,",")+" "+SysTips.OUTBOUND_IS_STOCK;
        }
        if (StringUtils.isNotBlank(errMsg)){
            return BaseResult.error(errMsg);
        }
        if (outboundOrderNumberVO.getIsAuto() != null) {
            if (outboundOrderNumberVO.getIsAuto()) {
                //自动生成拣货下架
                if (CollUtil.isNotEmpty(distributionOrderList)) {
                    distributionOrderList = distributionOrderList.stream().distinct().collect(Collectors.toList());
                    outboundOrderNumberVO.setOrderNumberList(distributionOrderList);
                    outboundOrderNumberVO.setIsAllContinue(false);
                    wmsPackingOffshelfTaskService.createPackingOffShelf(outboundOrderNumberVO);
                }
            }
        }
        return BaseResult.ok(SysTips.OUTBOUND_DISTRIBUTION_SUCCESS);
    }

    @Override
    public BaseResult<List<InventoryDetail>> getWarehouseByCode(String owerCode,
                                                                String warehouseCode,
                                                                String materialCode,
                                                                BigDecimal nowAccount,
                                                                String batchCode,
                                                                LocalDateTime materialProductionDate,
                                                                String customField1,
                                                                String customField2,
                                                                String customField3) {
        List<InventoryDetail> devList = new ArrayList<>();
        if (false){
            InventoryDetail inventoryDetail = new InventoryDetail();
            inventoryDetail.setUsableCount(nowAccount);
            inventoryDetail.setWarehouseId(0L);
            inventoryDetail.setWarehouseCode("WarehouseCode");
            inventoryDetail.setWarehouseName("WarehouseName");
            inventoryDetail.setWarehouseAreaId(0L);
            inventoryDetail.setWarehouseAreaCode("WarehouseAreaCode");
            inventoryDetail.setWarehouseAreaName("WarehouseAreaName");
            inventoryDetail.setWarehouseLocationId(0L);
            inventoryDetail.setWarehouseLocationCode("WarehouseLocationCode");
            inventoryDetail.setContainerId(0L);
            inventoryDetail.setContainerCode("ContainerCode");
            WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO = new WmsMaterialBasicInfoVO();
            wmsMaterialBasicInfoVO.setMaterialCode(materialCode);
            List<WmsMaterialBasicInfoVO> basicInfoVOList = wmsMaterialBasicInfoService.selectList(wmsMaterialBasicInfoVO);
            inventoryDetail.setMaterialId(basicInfoVOList.get(0).getId());
            inventoryDetail.setMaterialCode(basicInfoVOList.get(0).getMaterialCode());
            inventoryDetail.setMaterialName(basicInfoVOList.get(0).getMaterialName());
            devList.add(inventoryDetail);
            return BaseResult.ok(devList);
        }
        List<InventoryDetail> addList = new ArrayList<>();
        InventoryDetail inventoryDetail = new InventoryDetail();
        inventoryDetail.setOwerCode(owerCode);
        inventoryDetail.setWarehouseCode(warehouseCode);
        inventoryDetail.setMaterialCode(materialCode);
        inventoryDetail.setBatchCode(batchCode);
        inventoryDetail.setMaterialProductionDate(materialProductionDate);
        inventoryDetail.setCustomField1(customField1);
        inventoryDetail.setCustomField2(customField2);
        inventoryDetail.setCustomField3(customField3);
        inventoryDetail.setWarehouseLocationStatus(0);
        inventoryDetail.setWarehouseLocationStatus2(0);
        List<InventoryDetail> inventoryDetailList = inventoryDetailService.selectList(inventoryDetail);
        if (true){
            BaseResult<List<AllocationStrategyDTO>> strategyResult = wmsAllocationStrategyService.initStrategy(materialCode, owerCode, warehouseCode);
            log.info("------strategyResult--------");
            if (strategyResult.isSuccess()){
                inventoryDetailList = wmsAllocationStrategyService.getStrategyInventory(strategyResult.getResult(),inventoryDetail);
            }else {
                inventoryDetail.setAscMsg(AllocationStrategyConstant.EMPTY_LOCATION_PARAM);
                inventoryDetailList = inventoryDetailService.selectList(inventoryDetail);
            }
        }
        if (inventoryDetailList.isEmpty()){
            return BaseResult.error();
        }
        inventoryDetailList = inventoryDetailList.stream().filter(x->x.getUsableCount().compareTo(zoreAccount) == 1).collect(Collectors.toList());
        boolean isbreak = false;
        for (int i=0;i<inventoryDetailList.size();i++){
            InventoryDetail inventoryDetails = inventoryDetailList.get(i);
            BigDecimal usableCount = inventoryDetails.getUsableCount();
            if (nowAccount.compareTo(usableCount) == 1){
                //目前所需数量大于可用数量
                inventoryDetails.setNeedCount(usableCount);
                addList.add(inventoryDetails);
            }else if (nowAccount.compareTo(usableCount) == 0){
                //目前所需数量等于可用数量
                inventoryDetails.setNeedCount(usableCount);
                addList.add(inventoryDetails);
                isbreak = true;
            }else if (nowAccount.compareTo(usableCount) == -1){
                //目前所需数量小于可用数量
                inventoryDetails.setNeedCount(nowAccount);
                addList.add(inventoryDetails);
            }
            nowAccount = nowAccount.subtract(usableCount);
            if (isbreak||nowAccount.compareTo(zoreAccount) == -1){
                break;
            }
        }
        if (nowAccount.compareTo(zoreAccount) == 1){
            log.info("货主：{},仓库：{},物料：{},所需数量：{} 库存分配缺货中!",owerCode,warehouseCode,materialCode,nowAccount);
            return BaseResult.error();
        }
        return BaseResult.ok(addList);
    }

    @Override
    public BaseResult lockInventory(List<InventoryDetail> detailList) {
        List<InventoryDetailForm> lockList = new ArrayList<>();
        detailList.forEach(detail -> {
            InventoryDetailForm form = new InventoryDetailForm();
            BeanUtils.copyProperties(detail,form);
            form.setOperationCount(detail.getNeedCount());
            lockList.add(form);
        });
        BaseResult result = inventoryDetailService.outputAllocation(lockList);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult cancelAllocateInventory(OutboundOrderNumberVO outboundOrderNumberVO) {
        boolean isWave = outboundOrderNumberVO.getIsWave();
        List<String> errList = new ArrayList<>();
        for (String orderNumber : outboundOrderNumberVO.getOrderNumberList()){
            if (checkIsWave(orderNumber)){
                continue;
            }
            if (!checkIsOffshelf(orderNumber,isWave)) {
                cancelOutputAllocation(orderNumber,isWave);
                if (isWave) {
                    this.baseMapper.updateByOrderNumber(null, orderNumber, OutboundOrdertSatus.UNASSIGNED.getType(), CurrentUserUtil.getUsername());
                    wmsOutboundOrderInfoToMaterialMapper.updateByWaveNumber(orderNumber, OutboundOrdertSatus.UNASSIGNED.getType(), CurrentUserUtil.getUsername());
                    wmsOutboundOrderInfoToDistributionMaterialMapper.delByWaveNumber(orderNumber, CurrentUserUtil.getUsername());
                    wmsWaveOrderInfoMapper.updateByWaveNumber(orderNumber, WaveOrdertSatus.CANCEL.getType(), CurrentUserUtil.getUsername());
                } else {
                    this.baseMapper.updateByOrderNumber(orderNumber, null, OutboundOrdertSatus.UNASSIGNED.getType(), CurrentUserUtil.getUsername());
                    wmsOutboundOrderInfoToMaterialMapper.updateByOrderNumber(orderNumber, OutboundOrdertSatus.UNASSIGNED.getType(), CurrentUserUtil.getUsername());
                    wmsOutboundOrderInfoToDistributionMaterialMapper.delByOrderNumber(orderNumber, CurrentUserUtil.getUsername());
                }
            }else {
                errList.add(orderNumber);
            }
        }
        String errMsg = "";
        if (!errList.isEmpty()){
            errMsg+=StringUtils.join(errList,",")+" 撤销失败，已生成拣货下架单！";
        }
        if (StringUtils.isNotBlank(errMsg)){
            return BaseResult.error(errMsg);
        }
        return BaseResult.ok(SysTips.CANCEL_OUTBOUND_DISTRIBUTION_SUCCESS);

    }

    @Override
    public boolean isChangeOrder(String noticeOrderNumber) {
        WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO = new WmsOutboundOrderInfoVO();
        wmsOutboundOrderInfoVO.setNoticeOrderNumber(noticeOrderNumber);
        List<WmsOutboundOrderInfoVO> list = selectList(wmsOutboundOrderInfoVO);
        if (!list.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public BaseResult delByIdAndOrderNumber(List<WmsOutboundOrderInfoVO> infoList) {
        for (int i=0;i< infoList.size();i++){
            if (wmsOutboundOrderInfoToDistributionMaterialService.checkIsAllocation(infoList.get(i).getOrderNumber(),null)){
                return BaseResult.error(SysTips.OUTBOUND_IS_DISTRIBUTION);
            }
        }
        infoList.forEach(info -> {
            this.removeById(info.getId());
            wmsOutboundOrderInfoToMaterialMapper.delByOrderNumber(info.getOrderNumber(),CurrentUserUtil.getUsername());
        });
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    @Override
    public boolean updateByOrderNumber(String orderNumber) {
        QueryWrapper<WmsOutboundOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WmsOutboundOrderInfo::getOrderNumber,orderNumber);
        queryWrapper.lambda().eq(WmsOutboundOrderInfo::getIsDeleted,0);
        WmsOutboundOrderInfo wmsOutboundOrderInfo = this.getOne(queryWrapper);
        wmsOutboundOrderInfo.setOrderStatusType(4);
        wmsOutboundOrderInfo.setUpdateBy(CurrentUserUtil.getUsername());
        wmsOutboundOrderInfo.setUpdateTime(new Date());
        return this.updateById(wmsOutboundOrderInfo);
    }

    @Override
    public WmsOutboundOrderInfo getWmsOutboundOrderInfoByOrderNumber(String orderNumber) {
        QueryWrapper<WmsOutboundOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WmsOutboundOrderInfo::getOrderNumber,orderNumber);
        queryWrapper.lambda().eq(WmsOutboundOrderInfo::getIsDeleted,0);
        return this.getOne(queryWrapper);
    }

    /**
     * @description 获取累计最小数量
     * @author  ciro
     * @date   2021/12/23 11:07
     * @param: materialList
     * @return: java.math.BigDecimal
     **/
    private BigDecimal getMaterialCount(List<WmsOutboundOrderInfoToMaterial> materialList){
        BigDecimal account = new BigDecimal(0);
        for (int i=0;i< materialList.size();i++){
            account = account.add(materialList.get(i).getRequirementAccount());
        }
        return account;
    }

    /**
     * @description 获取转换后重量
     * @author  ciro
     * @date   2022/1/14 11:53
     * @param: materialList
     * @return: java.math.BigDecimal
     **/
    private BigDecimal getAllHeight(List<WmsOutboundOrderInfoToMaterial> materialList){
        BigDecimal account = new BigDecimal(0);
        List<Long> matrialIdList = materialList.stream().map(x->x.getMaterialId()).collect(Collectors.toList());
        WmsMaterialBasicInfoVO wmsMaterialBasicInfo = new WmsMaterialBasicInfoVO();
        wmsMaterialBasicInfo.setIdList(matrialIdList);
        List<WmsMaterialBasicInfoVO> materialBasicInfoList = wmsMaterialBasicInfoService.selectList(wmsMaterialBasicInfo);
        Map<Long,BigDecimal> weightMap = new HashMap<>(20);
        materialBasicInfoList.forEach(x->{
            weightMap.put(x.getId(),x.getWeight());
        });
        for (int i=0;i< materialList.size();i++){
            WmsOutboundOrderInfoToMaterial wmsOutboundOrderInfoToMaterial = materialList.get(i);
            if (!weightMap.containsKey(wmsOutboundOrderInfoToMaterial.getMaterialId())){
                continue;
            }
            account = account.add(wmsOutboundOrderInfoToMaterial.getRequirementAccount().multiply(weightMap.get(wmsOutboundOrderInfoToMaterial.getMaterialId())));
        }
        return account;
    }

    /**
     * @description 初始化分配数据
     * @author  ciro
     * @date   2021/12/24 16:52
     * @param: wmsOutboundOrderInfoVO
     * @return: java.util.List<com.jayud.model.po.WmsOutboundOrderInfoToDistributionMaterial>
     **/
    private List<WmsOutboundOrderInfoToDistributionMaterial> initDistribution(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO,WmsOutboundOrderInfoToMaterialVO materialVO,List<InventoryDetail> detailList){
        List<WmsOutboundOrderInfoToDistributionMaterial> distributionList = new ArrayList<>();
        List<Long> locationIdList = new ArrayList<>();
        detailList.forEach(detail -> {
            WmsOutboundOrderInfoToDistributionMaterial materials = new WmsOutboundOrderInfoToDistributionMaterial();
            BeanUtils.copyProperties(wmsOutboundOrderInfoVO,materials);
            materials.setOrderMaterialId(materialVO.getId());
            materials.setMaterialId(detail.getMaterialId());
            materials.setMaterialCode(detail.getMaterialCode());
            materials.setMaterialName(detail.getMaterialName());
            materials.setWarehouseAreaId(detail.getWarehouseAreaId() );
            materials.setWarehouseAreaCode(detail.getWarehouseAreaCode());
            materials.setWarehouseAreaName(detail.getWarehouseAreaName());

            materials.setWarehouseLocationId(detail.getWarehouseLocationId());
            materials.setWarehouseLocationCode(detail.getWarehouseLocationCode());

            materials.setContainerId(detail.getContainerId());
            materials.setContainerCode(detail.getContainerCode());

            materials.setDistributionAccount(materialVO.getRequirementAccount());
            materials.setRealDistributionAccount(detail.getNeedCount());
            materials.setUnit(materialVO.getUnit());
            materials.setBatchCode(materialVO.getBatchCode());
            materials.setMaterialProductionDate(materialVO.getMaterialProductionDate());
            materials.setCustomField1(materialVO.getCustomField1());
            materials.setCustomField2(materialVO.getCustomField2());
            materials.setCustomField3(materialVO.getCustomField3());
            materials.setDistributionBatchCode(detail.getBatchCode());
            materials.setDistributionMaterialProductionDate(detail.getMaterialProductionDate());
            materials.setDistributionCustomField1(detail.getCustomField1());
            materials.setDistributionCustomField2(detail.getCustomField2());
            materials.setDistributionCustomField3(detail.getCustomField3());
            materials.setId(null);
            materials.clearCreate();
            materials.clearUpdate();
            materials.setWaveNumber(null);
            distributionList.add(materials);
            locationIdList.add(detail.getWarehouseLocationId());
        });
        if (CollUtil.isNotEmpty(locationIdList)){
            WarehouseLocation warehouseLocation = new WarehouseLocation();
            warehouseLocation.setLocationIdList(locationIdList);
            List<WarehouseLocationVO> locationList = warehouseLocationService.selectList(warehouseLocation);
            Map<Long,WarehouseLocationVO> locationMap = new HashMap<>();
            if (CollUtil.isNotEmpty(locationList)){
                locationList.forEach(warehouseLocations -> {
                    locationMap.put(warehouseLocations.getId(),warehouseLocations);
                });
                distributionList.forEach(distribution -> {
                    if (locationMap.containsKey(distribution.getWarehouseLocationId())){
                        WarehouseLocationVO locationVO = locationMap.get(distribution.getWarehouseLocationId());
                        distribution.setShelfId(locationVO.getShelfId());
                        distribution.setShelfCode(locationVO.getShelfCode());
                    }
                });
            }
        }
        return distributionList;
    }

    /**
     * @description 根据波次单号查询出库单数据
     * @author  ciro
     * @date   2021/12/27 11:26
     * @param: waveOrderNumber
     * @return: java.util.List<com.jayud.model.vo.WmsOutboundOrderInfoVO>
     **/
    private List<WmsOutboundOrderInfoVO> getOrderListByWave(String waveOrderNumber){
        WmsOutboundOrderInfoVO vo = new WmsOutboundOrderInfoVO();
        vo.setWaveNumber(waveOrderNumber);
        List<WmsOutboundOrderInfoVO> infoVOList = selectList(vo);
        infoVOList.forEach(x->{
            WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO = new WmsOutboundOrderInfoToMaterialVO();
            wmsOutboundOrderInfoToMaterialVO.setOrderNumber(x.getOrderNumber());
            List<WmsOutboundOrderInfoToMaterialVO> materialVOList = wmsOutboundOrderInfoToMaterialService.selectList(wmsOutboundOrderInfoToMaterialVO);
            materialVOList.forEach(message -> {
                JSONObject jsonObject = (JSONObject) JSON.toJSON(message);
                String keys = jsonObject.getString("materialCode")+"*"+
                        jsonObject.getString("batchCode")+"*"+
                        jsonObject.getString("materialProductionDate")+"*"+
                        jsonObject.getString("customField1")+"*"+
                        jsonObject.getString("customField2")+"*"+
                        jsonObject.getString("customField3")+"*";
                if (message.getMaterialMap() == null){
                    message.setMaterialMap(new HashMap<>(5));
                }
                message.getMaterialMap().put(keys,message);
            });
            x.setThisMaterialList(materialVOList);
        });
        return infoVOList;
    }

    /**
     * @description 分配波次数据
     * @author  ciro
     * @date   2021/12/27 13:37
     * @param: infoList
     * @param: distributionLis
     * @return: int
     **/
    private int allocateWave(List<WmsOutboundOrderInfoVO> infoList,List<WmsOutboundOrderInfoToDistributionMaterial> distributionLis){
        List<String> keyList =new ArrayList<>();
        //分配量
        BigDecimal allCount = new BigDecimal(0);
        distributionLis.forEach(distribution -> {
            allCount.add(distribution.getRealDistributionAccount());
            JSONObject jsonObject = (JSONObject) JSON.toJSON(distribution);
            String keys = jsonObject.getString("materialCode")+"*"+
                    jsonObject.getString("batchCode")+"*"+
                    jsonObject.getString("materialProductionDate")+"*"+
                    jsonObject.getString("customField1")+"*"+
                    jsonObject.getString("customField2")+"*"+
                    jsonObject.getString("customField3")+"*";
            if (!keyList.contains(keys)){
                keyList.add(keys);
            }
        });
        //波次分配状态
        boolean isAllWave = true;
        List<WmsOutboundOrderInfoToMaterialVO> updateMetailList = new ArrayList<>();
        for (int i=0;i<infoList.size();i++){
            WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO = infoList.get(i);
            List<WmsOutboundOrderInfoToMaterialVO> materialList = wmsOutboundOrderInfoVO.getThisMaterialList();
            boolean isAll = true;
            for (int j=0;j< materialList.size();j++){
                WmsOutboundOrderInfoToMaterialVO materialVO = materialList.get(j);
                Map<String,WmsOutboundOrderInfoToMaterialVO> materialMap = materialVO.getMaterialMap();
                for (String keys:materialMap.keySet()){
                    if (keyList.contains(keys)){
                        materialVO.setStatusType(OutboundOrdertSatus.ASSIGNED.getType());
                    }else {
                        isAll = false;
                        materialVO.setStatusType(OutboundOrdertSatus.OUT_STOCK.getType());
                    }
                    updateMetailList.add(materialVO);
                }
            }
            //出库单分配成功
            if (isAll){
                wmsOutboundOrderInfoVO.setOrderStatusType(OutboundOrdertSatus.ASSIGNED.getType());
            }else {
                isAllWave = false;
                wmsOutboundOrderInfoVO.setOrderStatusType(OutboundOrdertSatus.OUT_STOCK.getType());
            }
        }
        updateWaveOrderMaterial(infoList,updateMetailList);
        if (isAllWave){
            return OutboundOrdertSatus.ASSIGNED.getType();
        }else {
            return OutboundOrdertSatus.OUT_STOCK.getType();
        }
    }

    /**
     * @description 波次-保存出库单，物料
     * @author  ciro
     * @date   2021/12/27 13:53
     * @param: infoList
     * @param: materialVoList
     * @return: void
     **/
    private void updateWaveOrderMaterial(List<WmsOutboundOrderInfoVO> infoList,List<WmsOutboundOrderInfoToMaterialVO> materialVoList){
        List<WmsOutboundOrderInfo> orderList = new ArrayList<>();
        infoList.forEach(x->{
            WmsOutboundOrderInfo info = new WmsOutboundOrderInfo();
            BeanUtils.copyProperties(x,info);
            orderList.add(info);
        });
        this.updateBatchById(orderList);
        List<WmsOutboundOrderInfoToMaterial> materialList = new ArrayList<>();
        materialVoList.forEach(x->{
            WmsOutboundOrderInfoToMaterial material = new WmsOutboundOrderInfoToMaterial();
            BeanUtils.copyProperties(x,material);
            materialList.add(material);
        });
        wmsOutboundOrderInfoToMaterialService.updateBatchById(materialList);
    }

    /**
     * @description 判断是否转为出库单
     * @author  ciro
     * @date   2021/12/28 13:47
     * @param: noticeOrderNumber
     * @return: boolean
     **/
    private boolean checkExitOutbound(String noticeOrderNumber){
        WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO = new WmsOutboundOrderInfoVO();
        wmsOutboundOrderInfoVO.setNoticeOrderNumber(noticeOrderNumber);
        List<WmsOutboundOrderInfoVO> list = wmsOutboundOrderInfoMapper.list(wmsOutboundOrderInfoVO);
        if (!list.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * @description 判断出库单是否可以进行分配
     * @author  ciro
     * @date   2021/12/28 17:08
     * @param: type
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult checkDistribution(Integer type){
        if (type.equals(OutboundOrdertSatus.ASSIGNED.getType())){
            return BaseResult.error(SysTips.OUTBOUND_IS_DISTRIBUTION);
        }else if (type.equals(OutboundOrdertSatus.ISSUED.getType())){
            return BaseResult.error(SysTips.OUTBOUND_IS_FINISH);
        }
        return BaseResult.ok();
    }

    /**
     * @description 更新物料分配数量
     * @author  ciro
     * @date   2021/12/29 16:41
     * @param: distributionList
     * @return: void
     **/
    private void updateMaterialAccount(List<WmsOutboundOrderInfoToMaterialVO> updateMaterialList ){
        List<WmsOutboundOrderInfoToMaterial> materialList = new ArrayList<>();
        updateMaterialList.forEach(materialVO -> {
            WmsOutboundOrderInfoToMaterial material = new WmsOutboundOrderInfoToMaterial();
            BeanUtils.copyProperties(materialVO,material);
            materialList.add(material);
        });
        wmsOutboundOrderInfoToMaterialService.updateBatchById(materialList);
    }

    /**
     * @description 判断是否生成拣货下架单
     * @author  ciro
     * @date   2021/12/30 9:51
     * @param: orderNumber
     * @return: boolean
     **/
    @Override
    public boolean checkIsOffshelf(String orderNumber, boolean isWave){
        WmsPackingOffshelfTask wmsPackingOffshelfTask = new WmsPackingOffshelfTask();
        if (isWave){
            wmsPackingOffshelfTask.setWaveNumber(orderNumber);
        }else {
            wmsPackingOffshelfTask.setOrderNumber(orderNumber);
        }
        List<WmsPackingOffshelfTask> taskList = wmsPackingOffshelfTaskService.selectList(wmsPackingOffshelfTask);
        if (taskList.isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * @description 撤销库存分配
     * @author  ciro
     * @date   2021/12/30 14:39
     * @param: orderNumber
     * @param: isWave
     * @return: com.jyd.component.commons.result.Result
     **/
    @Override
    public BaseResult cancelOutputAllocation(String orderNumber, boolean isWave){
        List<InventoryDetailForm> detailFormList = wmsOutboundOrderInfoToDistributionMaterialService.getInventoryByOrderNumber(orderNumber, isWave);
        return inventoryDetailService.cancelOutputAllocation(detailFormList);
    }

    @Override
    public List<WmsOutboundOrderInfoVO> selectUnStockToWaveList(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO) {
        List<WmsOutboundOrderInfoVO> list = this.baseMapper.selectUnStockToWaveList(wmsOutboundOrderInfoVO);
        return list;
    }

    @Override
    public void finishOrder(List<String> orderNumberList) {
        LambdaQueryWrapper<WmsOutboundOrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(WmsOutboundOrderInfo::getOrderNumber,orderNumberList);
        lambdaQueryWrapper.eq(WmsOutboundOrderInfo::getIsDeleted,false);
        List<WmsOutboundOrderInfo> infoList = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(infoList)){
            infoList.forEach(info->{
                info.setOrderStatusType(OutboundOrdertSatus.ISSUED.getType());
            });
            this.updateBatchById(infoList);
        }

    }

    @Override
    public BaseResult saveInfo(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO) {
        changComfirm(wmsOutboundOrderInfoVO);
        WmsOutboundOrderInfo wmsOutboundOrderInfo =  new WmsOutboundOrderInfo();
        BeanUtils.copyProperties(wmsOutboundOrderInfoVO,wmsOutboundOrderInfo);
        if (CollUtil.isNotEmpty(wmsOutboundOrderInfoVO.getThisMaterialList())){
            List<WmsOutboundOrderInfoToMaterial> materialList = new ArrayList<>();
            wmsOutboundOrderInfoVO.getThisMaterialList().forEach(materal -> {
                WmsOutboundOrderInfoToMaterial materials = new WmsOutboundOrderInfoToMaterial();
                BeanUtils.copyProperties(materal,materials);
                materialList.add(materials);
            });
        }
        wmsOutboundOrderInfoToServiceService.saveService(wmsOutboundOrderInfo.getOrderNumber(),wmsOutboundOrderInfoVO.getServiceList());
        allOut(wmsOutboundOrderInfo);
        this.updateById(wmsOutboundOrderInfo);
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    @Override
    public BaseResult cancelOrder(DeleteForm deleteForm) {
        LambdaQueryWrapper<WmsOutboundOrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfo::getIsDeleted,false);
        lambdaQueryWrapper.in(WmsOutboundOrderInfo::getId,deleteForm.getIds());
        List<WmsOutboundOrderInfo> infoList = this.list(lambdaQueryWrapper);
        List<String> errList = new ArrayList<>();
        if (CollUtil.isNotEmpty(infoList)){
            for (WmsOutboundOrderInfo info : infoList){
                //判断是否出库
                if (info.getOrderStatusType() == 4){
                    errList.add(info.getOrderNumber());
                    continue;
                }
                //判断存在出库数据
                if (!checkCancel(info.getOrderNumber())){
                    errList.add(info.getOrderNumber());
                    continue;
                }
                LambdaQueryWrapper<WmsOutboundOrderInfoToMaterial> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(WmsOutboundOrderInfoToMaterial::getIsDeleted,false);
                lambdaQueryWrapper1.eq(WmsOutboundOrderInfoToMaterial::getOrderNumber,info.getOrderNumber());
                List<WmsOutboundOrderInfoToMaterial> materialList = wmsOutboundOrderInfoToMaterialService.list(lambdaQueryWrapper1);
                if (CollUtil.isNotEmpty(materialList)){
                    Map<Long,BigDecimal> msg = materialList.stream().collect(Collectors.toMap(x->x.getInventoryDetailId(),x->x.getRequirementAccount()));
                    inventoryDetailService.canceloutputAllocationByIds(msg);
                    //删除物料
                    wmsOutboundOrderInfoToMaterialMapper.delByOrderNumber(info.getOrderNumber(),CurrentUserUtil.getUsername());
                }
                this.baseMapper.logicDelById(info.getId(),CurrentUserUtil.getUsername());
            }
        }
        if (CollUtil.isNotEmpty(errList)){
            return BaseResult.error(StringUtils.join(errList, StrUtil.C_COMMA)+" 已出库，请勿删除！");
        }
        return BaseResult.ok(SysTips.DEL_SUCCESS);
    }

    @Override
    public BaseResult changeToReview(DeleteForm deleteForm) {
        LambdaQueryWrapper<WmsOutboundOrderInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfo::getIsDeleted,false);
        lambdaQueryWrapper.in(WmsOutboundOrderInfo::getId,deleteForm.getIds());
        List<WmsOutboundOrderInfo> lists = this.list(lambdaQueryWrapper);
        List<String> errList = new ArrayList<>();
        if (CollUtil.isNotEmpty(lists)){
            lists.forEach(info->{
                if (info.getOrderStatusType()==4){
                    WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO = new WmsOutboundOrderInfoVO();
                    wmsOutboundOrderInfoVO.setOrderNumber(info.getOrderNumber());
                    wmsOutboundOrderInfoVO = queryByCode(wmsOutboundOrderInfoVO);
                    wmsOutboundShippingReviewInfoService.changeToReview(wmsOutboundOrderInfoVO);
                }else {
                    errList.add(info.getOrderNumber());
                }
            });
        }
        if (CollUtil.isNotEmpty(errList)){
            return BaseResult.error(StringUtils.join(errList,StrUtil.C_COMMA)+" 未完全完全出库！");
        }
        return null;
    }

    /**
     * @description 判断是否分配波次
     * @author  ciro
     * @date   2022/1/12 18:19
     * @param: orderNumber
     * @return: boolean
     **/
    public boolean checkIsWave(String orderNumber){
        boolean isWave = false;
        WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO = new WmsOutboundOrderInfoVO();
        wmsOutboundOrderInfoVO.setOrderNumber(orderNumber);
        List<WmsOutboundOrderInfoVO> infoLists = selectList(wmsOutboundOrderInfoVO);
        if (CollectionUtil.isNotEmpty(infoLists)){
            if (StringUtils.isNotBlank(infoLists.get(0).getWaveNumber())){
                isWave = true;
            }
        }else {
            isWave = true;
        }
        return isWave;
    }

    private List<WmsOutboundOrderInfoToMaterial> changeMaterial(String orderNumber,List<WmsOutboundNoticeOrderInfoToMaterialVO> noticeMaterialList){
        List<WmsOutboundOrderInfoToMaterial> materialList = new ArrayList<>();
        noticeMaterialList.forEach(noticeMaterial -> {
            WmsOutboundOrderInfoToMaterial material = new WmsOutboundOrderInfoToMaterial();
            BeanUtils.copyProperties(noticeMaterial,material);
            material.clearCreate();
            material.clearUpdate();
            material.setId(null);
            material.setOrderNumber(orderNumber);
            material.setRequirementAccount(noticeMaterial.getAccount());
            material.setDistributionAccount(noticeMaterial.getAccount());
            materialList.add(material);
        });
        return materialList;
    }


    /**
     * @description 判断是否全部锁定
     * @author  ciro
     * @date   2022/4/6 11:58
     * @param: noticeMaterialList
     * @return: boolean
     **/
    public boolean isAllClock(List<WmsOutboundNoticeOrderInfoToMaterialVO> noticeMaterialList){
        Map<Long,BigDecimal> accountMap = noticeMaterialList.stream().collect(Collectors.toMap(x->x.getInventoryDetailId(),x->x.getAccount()));
        if (inventoryDetailService.outputAllocationByMsg(accountMap).isSuccess()){
            return true;
        }else {
            return false;
        }
    }

    private void changComfirm(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO){
        LambdaQueryWrapper<WmsOutboundOrderInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getOrderNumber,wmsOutboundOrderInfoVO.getOrderNumber());
        List<WmsOutboundOrderInfoToMaterial> lastMaterList = wmsOutboundOrderInfoToMaterialService.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(lastMaterList)){
            List<String> lastComfirIdList = lastMaterList.stream().filter(x->x.getStatusType() == 4).map(x->String.valueOf(x.getId())).collect(Collectors.toList());
            List<String> thisComfirIdList = wmsOutboundOrderInfoVO.getThisMaterialList().stream().filter(x->x.getStatusType() == 4).map(x->String.valueOf(x.getId())).collect(Collectors.toList());
            for (String id:thisComfirIdList){
                if (!lastComfirIdList.contains(id)){
                    wmsOutboundOrderInfoVO.setComfirmId(CurrentUserUtil.getUserId());
                    wmsOutboundOrderInfoVO.setComfirmName(CurrentUserUtil.getUserRealName());
                }
            }
        }
    }

    /**
     * @description 全部出库
     * @author  ciro
     * @date   2022/4/6 17:59
     * @param: info
     * @return: void
     **/
    private void allOut(WmsOutboundOrderInfo info){
        LambdaQueryWrapper<WmsOutboundOrderInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getOrderNumber,info.getOrderNumber());
        List<WmsOutboundOrderInfoToMaterial> materialList = wmsOutboundOrderInfoToMaterialService.list(lambdaQueryWrapper);
        boolean isAll = true;
        Map<Long,BigDecimal> msg = new HashMap<>();
        for (WmsOutboundOrderInfoToMaterial material : materialList){
            msg.put(material.getInventoryDetailId(),material.getRequirementAccount());
            if(material.getStatusType() != 4){
                isAll = false;
                break;
            }
        }
        if (isAll){
            info.setOrderStatusType(4);
        }
    }

    /**
     * @description 判断是否取消
     * @author  ciro
     * @date   2022/4/7 11:02
     * @param: orderNumber
     * @return: boolean
     **/
    private boolean checkCancel(String orderNumber){
        LambdaQueryWrapper<WmsOutboundOrderInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundOrderInfoToMaterial::getOrderNumber,orderNumber);
        List<WmsOutboundOrderInfoToMaterial> materialList = wmsOutboundOrderInfoToMaterialService.list(lambdaQueryWrapper);
        boolean isCancel = true;
        if (CollUtil.isNotEmpty(materialList)){
            for (WmsOutboundOrderInfoToMaterial material : materialList){
                if (material.getStatusType() != 4){
                    isCancel = false;
                    break;
                }
            }
        }else {
            isCancel = false;
        }
        return isCancel;
    }

}
