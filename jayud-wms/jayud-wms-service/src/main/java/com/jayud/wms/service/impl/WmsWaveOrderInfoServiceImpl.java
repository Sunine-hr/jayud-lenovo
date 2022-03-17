package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.*;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.enums.OutboundOrdertSatus;
import com.jayud.wms.model.enums.WaveOrdertSatus;
import com.jayud.wms.model.enums.WavedOrdertSatus;
import com.jayud.wms.model.po.*;
import com.jayud.wms.model.vo.OutboundOrderNumberVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToMaterialVO;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoVO;
import com.jayud.wms.model.vo.WmsWaveInfoVO;
import com.jayud.wms.service.*;
import com.jayud.wms.utils.CodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 波次单 服务实现类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Service
public class WmsWaveOrderInfoServiceImpl extends ServiceImpl<WmsWaveOrderInfoMapper, WmsWaveOrderInfo> implements IWmsWaveOrderInfoService {

    @Autowired
    private IWmsWaveToOutboundInfoService wmsWaveToOutboundInfoService;
    @Autowired
    private IWmsWaveToMaterialService wmsWaveToMaterialService;
    @Autowired
    private IWmsOutboundOrderInfoToDistributionMaterialService wmsOutboundOrderInfoToDistributionMaterialService;
    @Autowired
    private IWmsOutboundOrderInfoService wmsOutboundOrderInfoService;
    @Autowired
    private IWmsOutboundOrderInfoToMaterialService wmsOutboundOrderInfoToMaterialService;

    @Autowired
    private WmsWaveOrderInfoMapper wmsWaveOrderInfoMapper;
    @Autowired
    private WmsOutboundOrderInfoMapper wmsOutboundOrderInfoMapper;
    @Autowired
    private WmsOutboundOrderInfoToMaterialMapper wmsOutboundOrderInfoToMaterialMapper;
    @Autowired
    private WmsWaveToOutboundInfoMapper wmsWaveToOutboundInfoMapper;
    @Autowired
    private WmsOutboundOrderInfoToDistributionMaterialMapper wmsOutboundOrderInfoToDistributionMaterialMapper;
    @Autowired
    private WmsWaveToMaterialMapper wmsWaveToMaterialMapper;

    @Autowired
    private CodeUtils codeUtils;

    @Override
    public IPage<WmsWaveOrderInfo> selectPage(WmsWaveOrderInfo wmsWaveOrderInfo,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsWaveOrderInfo> page=new Page<WmsWaveOrderInfo>(currentPage,pageSize);
        IPage<WmsWaveOrderInfo> pageList= wmsWaveOrderInfoMapper.pageList(page, wmsWaveOrderInfo);
        return pageList;
    }

    @Override
    public List<WmsWaveOrderInfo> selectList(WmsWaveOrderInfo wmsWaveOrderInfo){
        return wmsWaveOrderInfoMapper.list(wmsWaveOrderInfo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsWaveOrderInfoMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsWaveOrderInfoForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsWaveOrderInfoForExcel(paramMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult createWave(WmsWaveInfoVO wmsWaveInfoVO) {
        if (wmsWaveInfoVO.getOrderNumberList().isEmpty()){
            return BaseResult.error(SysTips.EMPTY_OUTBOUND_ORDER_ERROR);
        }
        BaseResult sameResult = checkOrderSame(wmsWaveInfoVO);
        if (!sameResult.isSuccess()){
           return sameResult;
        }
        BaseResult orderResult = checkIsToWave(wmsWaveInfoVO.getOrderNumberList());
        if (!orderResult.isSuccess()){
            return orderResult;
        }
        BaseResult inventoryResult = checkInventory(wmsWaveInfoVO.getOrderNumberList());
        if (!inventoryResult.isSuccess()){
            return inventoryResult;
        }
        String waveOrderNumber = "";
        if (StringUtils.isBlank(wmsWaveInfoVO.getWaveNumber())){
            wmsWaveInfoVO.setIsAdd(true);
        }else {
            List<WmsWaveOrderInfo> waveList = selectList(wmsWaveInfoVO);
            if (!waveList.isEmpty()){
                WmsWaveOrderInfo info = waveList.get(0);
                BeanUtils.copyProperties(info,wmsWaveInfoVO);
            }
            wmsWaveInfoVO.setIsAdd(false);
        }
        if (wmsWaveInfoVO.getIsAdd()){
            if (!checkIsAllocation(wmsWaveInfoVO.getOrderNumberList())){
                return BaseResult.error("存在分配出库单！");
            }
            waveOrderNumber = codeUtils.getCodeByRule(CodeConStants.WAVE_ORDER);
        }else {
            waveOrderNumber = wmsWaveInfoVO.getWaveNumber();
        }
        WmsOutboundOrderInfoVO orderInfoVO = new WmsOutboundOrderInfoVO();
        orderInfoVO.setOrderNumberList(wmsWaveInfoVO.getOrderNumberList());
        List<WmsOutboundOrderInfoVO> orderList = wmsOutboundOrderInfoMapper.list(orderInfoVO);

        WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO = new WmsOutboundOrderInfoToMaterialVO();
        wmsOutboundOrderInfoToMaterialVO.setOrderNumberList(wmsWaveInfoVO.getOrderNumberList());
        List<WmsOutboundOrderInfoToMaterialVO> materialList = wmsOutboundOrderInfoToMaterialMapper.list(wmsOutboundOrderInfoToMaterialVO);

        WmsWaveOrderInfo info = new WmsWaveOrderInfo();
        if (wmsWaveInfoVO.getIsAdd()){
            info = initWaveInfo(orderList.get(0));
            info.setWaveNumber(waveOrderNumber);
        }else {
            BeanUtils.copyProperties(wmsWaveInfoVO,info);
        }
        info.setAllOrderAccount(wmsWaveInfoVO.getOrderNumberList().size());
        BigDecimal allAccount = new BigDecimal(0);

        for (int i=0;i< orderList.size();i++){
            allAccount = allAccount.add(orderList.get(i).getAllCount());
        }
        info.setAllMaterialAccount(allAccount);
        this.addWaveOutOrder(waveOrderNumber,wmsWaveInfoVO.getOrderNumberList());
        wmsWaveToMaterialService.addWaveToMaterial(materialList,waveOrderNumber);
        if (wmsWaveInfoVO.getIsAdd()) {
            this.save(info);
        }else {
            this.updateById(info);
        }
        wmsOutboundOrderInfoMapper.createWaveRelation(waveOrderNumber,wmsWaveInfoVO.getOrderNumberList(),CurrentUserUtil.getUsername());
        return BaseResult.ok(SysTips.CREATE_WAVE_SUCCESS);
    }

    @Override
    public WmsWaveInfoVO queryByWaveNumber(String waveNumber) {
        WmsWaveInfoVO vo = new WmsWaveInfoVO();
        WmsWaveOrderInfo info = new WmsWaveOrderInfo();
        info.setWaveNumber(waveNumber);
        List<WmsWaveOrderInfo> infoList = wmsWaveOrderInfoMapper.list(info);
        if (!infoList.isEmpty()){
            BeanUtils.copyProperties(infoList.get(0),vo);
            WmsWaveToMaterial material = new WmsWaveToMaterial();
            material.setWaveNumber(vo.getWaveNumber());
            vo.setMaterialList(wmsWaveToMaterialService.selectList(material));
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult changeOrder(WmsWaveInfoVO wmsWaveInfoVO) {
        delNull(wmsWaveInfoVO);
        if (wmsOutboundOrderInfoToDistributionMaterialService.checkIsAllocation(null,wmsWaveInfoVO.getWaveNumber())){
            return BaseResult.error(SysTips.WAVE_IS_ALLOCATEll);
        }
        wmsWaveToOutboundInfoMapper.delByWaveNumber(wmsWaveInfoVO.getWaveNumber(),CurrentUserUtil.getUsername());
        wmsWaveToMaterialMapper.delByWaveNumber(wmsWaveInfoVO.getWaveNumber(),CurrentUserUtil.getUsername());
        wmsOutboundOrderInfoMapper.delWaveRelation(wmsWaveInfoVO.getWaveNumber(),CurrentUserUtil.getUsername());
        List<String> orderNumberList = wmsWaveInfoVO.getOrderInfoList().stream().map(x->x.getOrderNumber()).distinct().collect(Collectors.toList());
        wmsWaveInfoVO.getOrderNumberList().clear();
        wmsWaveInfoVO.getOrderNumberList().addAll(orderNumberList);
        if (!wmsWaveInfoVO.getIsDel()){
            return createWave(wmsWaveInfoVO);
        }
        return BaseResult.ok();
    }

    @Override
    public BaseResult delWave(OutboundOrderNumberVO outboundOrderNumberVO) {
        List<String> errList = new ArrayList<>();
        outboundOrderNumberVO.getOrderNumberList().forEach(number -> {
            WmsWaveInfoVO wmsWaveInfoVO = new WmsWaveInfoVO();
            wmsWaveInfoVO.setWaveNumber(number);
            wmsWaveInfoVO.setIsDel(true);
            if (!changeOrder(wmsWaveInfoVO).isSuccess()){
                errList.add(number);
            }
        });
        if (errList.isEmpty()){
            return BaseResult.error(StringUtils.join(errList,",")+SysTips.WAVE_IS_ALLOCATEll);
        }
        return BaseResult.ok();
    }

    /**
     * @description 保存关系
     * @author  ciro
     * @date   2021/12/27 9:52
     * @param: waveOrderNumber
     * @param: outOrderNumber
     * @return: void
     **/
    private void addWaveOutOrder(String waveOrderNumber,List<String> outOrderNumber){
        List<WmsWaveToOutboundInfo> relationList = new ArrayList<>();
        outOrderNumber.forEach(x -> {
            WmsWaveToOutboundInfo info = new WmsWaveToOutboundInfo();
            info.setWaveNumber(waveOrderNumber);
            info.setOrderNumber(x);
            relationList.add(info);
        });
        wmsWaveToOutboundInfoService.saveBatch(relationList);
    }

    @Override
    public boolean updateByWareNumber(String wareNumber) {
        QueryWrapper<WmsWaveOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WmsWaveOrderInfo::getWaveNumber,wareNumber);
        queryWrapper.lambda().eq(WmsWaveOrderInfo::getIsDeleted,0);
        WmsWaveOrderInfo wmsWaveOrderInfo = this.getOne(queryWrapper);
        wmsWaveOrderInfo.setStatus(6);
        wmsWaveOrderInfo.setUpdateBy(CurrentUserUtil.getUsername());
        wmsWaveOrderInfo.setUpdateTime(new Date());
        return this.updateById(wmsWaveOrderInfo);
    }

    @Override
    public WmsWaveOrderInfo getWmsWaveOrderInfoByWaveNumber(String orderNumber) {
        QueryWrapper<WmsWaveOrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WmsWaveOrderInfo::getWaveNumber,orderNumber);
        queryWrapper.lambda().eq(WmsWaveOrderInfo::getIsDeleted,0);
        return this.getOne(queryWrapper);
    }

    @Override
    public BaseResult delWave(WmsWaveInfoVO wmsWaveInfoVO) {
        List<String> errorWaveList = new ArrayList<>();
        List<String> delWaveList = new ArrayList<>();
        wmsWaveInfoVO.getWaveOrderNumberList().forEach(waveOrderNumber -> {
            List<String> orderNumberList = getOrderNumberList(waveOrderNumber);
            WmsOutboundOrderInfoToMaterialVO materialVO = new WmsOutboundOrderInfoToMaterialVO();
            materialVO.setOrderNumberList(orderNumberList);
            List<WmsOutboundOrderInfoToMaterialVO> infoList = wmsOutboundOrderInfoToMaterialService.selectList(materialVO);
            List<String> errList = new ArrayList<>();
            for (int i=0;i<infoList.size();i++){
                WmsOutboundOrderInfoToMaterialVO material = infoList.get(i);
                if (material.getStatusType().equals(OutboundOrdertSatus.ASSIGNED.getType())
                        ||material.getStatusType().equals(OutboundOrdertSatus.ISSUED.getType())){
                    errList.add(material.getOrderNumber());
                    break;
                }
            }
            if (!errList.isEmpty()){
                errorWaveList.add(waveOrderNumber);
            }else {
                delWaveList.add(waveOrderNumber);
            }
        });
        if (!delWaveList.isEmpty()){
            wmsWaveInfoVO.setWaveOrderNumberList(delWaveList);
            wmsWaveOrderInfoMapper.delByParam(wmsWaveInfoVO,CurrentUserUtil.getUsername());
            wmsWaveToOutboundInfoMapper.delByParam(wmsWaveInfoVO,CurrentUserUtil.getUsername());
        }
        if (!errorWaveList.isEmpty()){
            return BaseResult.error(StringUtils.join(errorWaveList,",")+" 删除失败！");
        }
        return BaseResult.ok();
    }

    @Override
    public BaseResult allocateWaveInventory(OutboundOrderNumberVO outboundOrderNumberVO) {
        List<String> stockList = new ArrayList<>();
        List<WmsWaveOrderInfo> updateWaveList = new ArrayList<>();
        List<WmsWaveToMaterial> updateWaveMaterialList = new ArrayList<>();
        List<WmsOutboundOrderInfo> updateOrderList = new ArrayList<>();
        List<WmsOutboundOrderInfoToMaterial> updateMaterialList = new ArrayList<>();
        List<WmsOutboundOrderInfoToDistributionMaterial> addDistributionMaterialList = new ArrayList<>();
        List<String> distributionList = new ArrayList<>();
        for (int i=0;i<outboundOrderNumberVO.getOrderNumberList().size();i++){
            List<String> loclKeyList = new ArrayList<>();
            //波次物料分配总量
            BigDecimal distributionAllAccount = new BigDecimal(0);
            String waveOrderNumber = outboundOrderNumberVO.getOrderNumberList().get(i);
            WmsWaveInfoVO wmsWaveInfoVO = this.queryByWaveNumber(waveOrderNumber);
            if (wmsWaveInfoVO.getStatus().equals(WavedOrdertSatus.ASSIGNED.getType())||wmsWaveInfoVO.getStatus().equals(WavedOrdertSatus.ISSUED.getType())){
                distributionList.add(waveOrderNumber);
                continue;
            }
            boolean isLock = true;
            boolean isAllLock = true;
            for (int j =0;j<wmsWaveInfoVO.getMaterialList().size();j++){
                WmsWaveToMaterial material = wmsWaveInfoVO.getMaterialList().get(j);
                if (material.getStatusType().equals(WavedOrdertSatus.ASSIGNED.getType())||material.getStatusType().equals(WavedOrdertSatus.ISSUED.getType())){
                    distributionAllAccount = distributionAllAccount.add(material.getDistributionAccount());
                    continue;
                }
                BaseResult<List<InventoryDetail>> inventoryResult = wmsOutboundOrderInfoService.getWarehouseByCode(wmsWaveInfoVO.getOwerCode(),
                                                                                                                wmsWaveInfoVO.getWarehouseCode(),
                                                                                                                material.getMaterialCode(),
                                                                                                                material.getRequirementAccount(),
                                                                                                                material.getBatchCode(),
                                                                                                                material.getMaterialProductionDate(),
                                                                                                                material.getCustomField1(),
                                                                                                                material.getCustomField2(),material.getCustomField3());
                boolean isStock = true;
                if (!inventoryResult.isSuccess()){
                    isLock = false;
                    isStock = false;
                }
                if (isLock){
                    if (wmsOutboundOrderInfoService.lockInventory(inventoryResult.getResult()).isSuccess()){
                        String keys = material.getMaterialCode()  +"*"+material.getBatchCode() +"*"+material.getMaterialProductionDate() +"*"+material.getCustomField1() +"*"+material.getCustomField2()+"*"+material.getCustomField3();
                        loclKeyList.add(keys);
                        material.setStatusType(WavedOrdertSatus.ASSIGNED.getType());
                        material.setDetailList(inventoryResult.getResult());
                        //物料分配总量
                        BigDecimal thisAccount = addDistributionAccount(inventoryResult.getResult());
                        material.setDistributionAccount(thisAccount);
                        distributionAllAccount = distributionAllAccount.add(thisAccount);
                        addDistributionMaterialList.addAll(initDistribution(wmsWaveInfoVO,material,inventoryResult.getResult()));
                    }else {
                        material.setStatusType(WavedOrdertSatus.OUT_STOCK.getType());
                    }
                }else {
                    material.setStatusType(WavedOrdertSatus.OUT_STOCK.getType());
                }
                if (!isStock){
                    isAllLock = false;
                }
                updateWaveMaterialList.add(material);
            }
            if (isAllLock){
                wmsWaveInfoVO.setStatus(WavedOrdertSatus.ASSIGNED.getType());
            }else {
                wmsWaveInfoVO.setStatus(WavedOrdertSatus.OUT_STOCK.getType());
                stockList.add(waveOrderNumber);
            }
            wmsWaveInfoVO.setAllocationAccount(distributionAllAccount);
            updateWaveList.add(wmsWaveInfoVO);
            changeOrderStatus(updateOrderList,updateMaterialList,waveOrderNumber,loclKeyList);
        }
        if (!updateWaveList.isEmpty()){
            this.updateBatchById(updateWaveList);
        }
        if (!updateWaveMaterialList.isEmpty()){
            wmsWaveToMaterialService.updateBatchById(updateWaveMaterialList);
        }
        if (!addDistributionMaterialList.isEmpty()){
            wmsOutboundOrderInfoToDistributionMaterialService.saveBatch(addDistributionMaterialList);
        }
        if (!updateOrderList.isEmpty()){
            wmsOutboundOrderInfoService.updateBatchById(updateOrderList);
        }
        if (!updateMaterialList.isEmpty()){
            wmsOutboundOrderInfoToMaterialService.updateBatchById(updateMaterialList);
        }
        String errMsg = "";
        if (!distributionList.isEmpty()){
            errMsg+=StringUtils.join(distributionList,",")+" 已分配，请勿重新分配！";
        }
        if (!stockList.isEmpty()){
            errMsg+=StringUtils.join(stockList,",")+" 分配失败！";
        }
        if (StringUtils.isNotBlank(errMsg)){
            return BaseResult.error(errMsg);
        }
        return BaseResult.ok();
    }

    @Override
    public BaseResult caneclAllocateWaveInventory(OutboundOrderNumberVO outboundOrderNumberVO) {
        List<String> cancelList = new ArrayList<>();
        List<String> packList = new ArrayList<>();
        outboundOrderNumberVO.getOrderNumberList().forEach(waveNumber -> {
            if (!wmsOutboundOrderInfoService.checkIsOffshelf(waveNumber,true)){
                cancelList.add(waveNumber);
            }else {
                packList.add(waveNumber);
            }
        });

        cancelList.forEach(waveOrderNumber -> {
            if (wmsOutboundOrderInfoService.cancelOutputAllocation(waveOrderNumber,true).isSuccess()) {
                this.baseMapper.updateByWaveNumber(waveOrderNumber, WaveOrdertSatus.CANCEL.getType(), CurrentUserUtil.getUsername());
                wmsWaveToMaterialMapper.updateByWaveNumber(waveOrderNumber, WaveOrdertSatus.CANCEL.getType(), CurrentUserUtil.getUsername());
                wmsOutboundOrderInfoToDistributionMaterialMapper.delByWaveNumber(waveOrderNumber, CurrentUserUtil.getUsername());
            }
        });
        String errMsg = "";
        if (!packList.isEmpty()){
            errMsg = StringUtils.join(packList,",") + " 已生成拣货下架，请先撤销拣货下架！";
            return BaseResult.error(errMsg);
        }
        return BaseResult.ok();
    }

    @Override
    public WmsWaveOrderInfo queryByWaveOrderNumber(String waveOrderNumber) {
        WmsWaveOrderInfo info = new WmsWaveOrderInfo();
        info.setWaveNumber(waveOrderNumber);
        List<WmsWaveOrderInfo> infoList = selectList(info);
        if (!infoList.isEmpty()){
            info = infoList.get(0);
            WmsOutboundOrderInfoVO orderInfoVO = new WmsOutboundOrderInfoVO();
            orderInfoVO.setWaveNumber(waveOrderNumber);
            List<WmsOutboundOrderInfoVO> orderInfoVOList = wmsOutboundOrderInfoService.selectList(orderInfoVO);
            info.setOrderInfoList(orderInfoVOList);
        }
        return info;
    }

    @Override
    public BaseResult addOrderRelation(OutboundOrderNumberVO outboundOrderNumberVO) {
        this.baseMapper.insertMiddel(outboundOrderNumberVO.getOrderNumber(),outboundOrderNumberVO.getOrderNumberList());
        return BaseResult.ok();
    }

    @Override
    public BaseResult delOrderRelation(OutboundOrderNumberVO outboundOrderNumberVO) {
        return null;
    }


    /**
     * @description 判断是否全部未分配
     * @author  ciro
     * @date   2021/12/31 10:31
     * @param: orderNumberList
     * @return: boolean
     **/
    private boolean checkIsAllocation(List<String> orderNumberList){
        WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO = new WmsOutboundOrderInfoVO();
        wmsOutboundOrderInfoVO.setOrderNumberList(orderNumberList);
        List<WmsOutboundOrderInfoVO> list = wmsOutboundOrderInfoMapper.list(wmsOutboundOrderInfoVO);
        boolean isSuccess = true;
        for (int i=0;i<list.size();i++){
            if (!list.get(i).getOrderStatusType().equals(OutboundOrdertSatus.UNASSIGNED.getType())){
                isSuccess = false;
                break;
            }
        }
        return isSuccess;
    }

    /**
     * @description 初始化波次数据
     * @author  ciro
     * @date   2021/12/31 11:16
     * @param: orderInfo
     * @return: com.jayud.model.po.WmsWaveOrderInfo
     **/
    private WmsWaveOrderInfo initWaveInfo(WmsOutboundOrderInfo orderInfo){
        WmsWaveOrderInfo waveOrderInfo = new WmsWaveOrderInfo();
        waveOrderInfo.setWarehouseId(orderInfo.getWarehouseId());
        waveOrderInfo.setWarehouseCode(orderInfo.getWarehouseCode());
        waveOrderInfo.setWarehouseName(orderInfo.getWarehouseName());
        waveOrderInfo.setOwerId(orderInfo.getOwerId());
        waveOrderInfo.setOwerCode(orderInfo.getOwerCode());
        waveOrderInfo.setOwerName(orderInfo.getOwerName());
        return waveOrderInfo;
    }

    /**
     * @description 根据波次获取出库单
     * @author  ciro
     * @date   2021/12/31 11:43
     * @param: waveOrderNumber
     * @return: java.util.List<java.lang.String>
     **/
    private List<String> getOrderNumberList(String waveOrderNumber){
        List<String> orderNumberList = new ArrayList<>();
        WmsWaveToMaterial wave = new WmsWaveToMaterial();
        wave.setWaveNumber(waveOrderNumber);
        List<WmsWaveToMaterial> materialList = wmsWaveToMaterialService.selectList(wave);
        if (!materialList.isEmpty()){
            materialList.forEach(material -> {
                orderNumberList.add(material.getOrderNumber());
            });
        }
        return orderNumberList;
    }

    /**
     * @description 获取分配总量
     * @author  ciro
     * @date   2021/12/31 15:04
     * @param: detailList
     * @return: java.math.BigDecimal
     **/
    private BigDecimal addDistributionAccount(List<InventoryDetail> detailList){
        BigDecimal account = new BigDecimal(0);
        for (int i=0;i<detailList.size();i++){
            account = account.add(detailList.get(i).getNeedCount());
        }
        return account;
    }

    /**
     * @description 初始化分配后数据
     * @author  ciro
     * @date   2021/12/31 15:12
     * @param: wmsWaveOrderInfo
     * @param: material
     * @param: detailList
     * @return: java.util.List<com.jayud.model.po.WmsOutboundOrderInfoToDistributionMaterial>
     **/
    private List<WmsOutboundOrderInfoToDistributionMaterial> initDistribution(WmsWaveOrderInfo wmsWaveOrderInfo,WmsWaveToMaterial material,List<InventoryDetail> detailList){
        List<WmsOutboundOrderInfoToDistributionMaterial> distributionList = new ArrayList<>();
        detailList.forEach(detail -> {
            WmsOutboundOrderInfoToDistributionMaterial materials = new WmsOutboundOrderInfoToDistributionMaterial();
            BeanUtils.copyProperties(wmsWaveOrderInfo,materials);
            materials.setWaveNumber(wmsWaveOrderInfo.getWaveNumber());
            materials.setOrderMaterialId(material.getId());
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

            materials.setDistributionAccount(material.getRequirementAccount());
            materials.setRealDistributionAccount(detail.getNeedCount());
            materials.setUnit(material.getUnit());
            materials.setBatchCode(material.getBatchCode());
            materials.setMaterialProductionDate(material.getMaterialProductionDate());
            materials.setCustomField1(material.getCustomField1());
            materials.setCustomField2(material.getCustomField2());
            materials.setCustomField3(material.getCustomField3());
            materials.setId(null);
            materials.clearCreate();
            materials.clearUpdate();
            distributionList.add(materials);
        });
        return distributionList;
    }

    /**
     * @description 获取更新数据
     * @author  ciro
     * @date   2021/12/31 16:52
     * @param: updateOrderList
     * @param: updateMaterialList
     * @param: waveOrderNumber
     * @param: lockList
     * @return: void
     **/
    private void changeOrderStatus(List<WmsOutboundOrderInfo> updateOrderList,List<WmsOutboundOrderInfoToMaterial> updateMaterialList,String waveOrderNumber,List<String> lockList){
        WmsWaveToOutboundInfo wmsWaveToOutboundInfo = new WmsWaveToOutboundInfo();
        wmsWaveToOutboundInfo.setWaveNumber(waveOrderNumber);
        List<WmsWaveToOutboundInfo> outboundList = wmsWaveToOutboundInfoService.selectList(wmsWaveToOutboundInfo);
        outboundList.forEach(outbound -> {
            BigDecimal allAccount = new BigDecimal(0);
            boolean isAll = true;
            WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO = new WmsOutboundOrderInfoVO();
            wmsOutboundOrderInfoVO.setOrderNumber(outbound.getOrderNumber());
            wmsOutboundOrderInfoVO = wmsOutboundOrderInfoService.queryByCode(wmsOutboundOrderInfoVO);
            for (int i=0;i< wmsOutboundOrderInfoVO.getThisMaterialList().size();i++){
                WmsOutboundOrderInfoToMaterialVO materialVO =  wmsOutboundOrderInfoVO.getThisMaterialList().get(i);
                if (materialVO.getStatusType().equals(OutboundOrdertSatus.ASSIGNED.getType())||materialVO.getStatusType().equals(OutboundOrdertSatus.ISSUED.getType())){
                    allAccount = allAccount.add(materialVO.getDistributionAccount());
                }else {
                    String keys = materialVO.getMaterialCode()  +"*"+materialVO.getBatchCode() +"*"+materialVO.getMaterialProductionDate() +"*"+materialVO.getCustomField1() +"*"+materialVO.getCustomField2()+"*"+materialVO.getCustomField3();
                    if (lockList.contains(keys)){
                        allAccount = allAccount.add(materialVO.getRequirementAccount());
                        materialVO.setDistributionAccount(materialVO.getRequirementAccount());
                        materialVO.setStatusType(OutboundOrdertSatus.ASSIGNED.getType());
                    }else {
                        isAll = false;
                        materialVO.setStatusType(OutboundOrdertSatus.OUT_STOCK.getType());
                    }
                    updateMaterialList.add(materialVO);
                }
            }
            if (isAll){
                wmsOutboundOrderInfoVO.setOrderStatusType(OutboundOrdertSatus.ASSIGNED.getType());
            }else {
                wmsOutboundOrderInfoVO.setOrderStatusType(OutboundOrdertSatus.OUT_STOCK.getType());
            }
            updateOrderList.add(wmsOutboundOrderInfoVO);
        });
    }

    /**
     * @description 判断出库单是否同个仓库同个货主
     * @author  ciro
     * @date   2022/1/4 13:47
     * @param: wmsWaveInfoVO
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult checkOrderSame(WmsWaveInfoVO wmsWaveInfoVO){
        WmsOutboundOrderInfoVO infoVo = new WmsOutboundOrderInfoVO();
        infoVo.setOrderNumberList(wmsWaveInfoVO.getOrderNumberList());
        List<String> warehouseList = new ArrayList<>();
        List<String> owerList = new ArrayList<>();
        List<WmsOutboundOrderInfoVO> orderList = wmsOutboundOrderInfoService.selectList(infoVo);
        orderList.forEach(info -> {
            if (!warehouseList.contains(info.getWarehouseCode())){
                warehouseList.add(info.getWarehouseCode());
            }
            if (!owerList.contains(info.getOwerCode())){
                owerList.add(info.getOwerCode());
            }
        });
        if (warehouseList.size() != 1 || owerList.size() != 1){
            return BaseResult.error(SysTips.TO_WAVE_NOT_SAME_ERROR);
        }
        return BaseResult.ok();
    }

    /**
     * @description 检查出库单是否关联波次单
     * @author  ciro
     * @date   2022/1/4 16:01
     * @param: orderNumberList
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult checkIsToWave(List<String> orderNumberList){
        WmsWaveToOutboundInfo wmsWaveToOutboundInfo = new WmsWaveToOutboundInfo();
        wmsWaveToOutboundInfo.setOrderNumberList(orderNumberList);
        List<WmsWaveToOutboundInfo> list = wmsWaveToOutboundInfoService.selectList(wmsWaveToOutboundInfo);
        if (!list.isEmpty()){
            return BaseResult.error("出库单已关联波次单！");
        }
        return BaseResult.ok();
    }

    /**
     * @description 选择是否存在分配
     * @author  ciro
     * @date   2022/1/4 16:57
     * @param: orderNumberList
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult checkInventory(List<String> orderNumberList){
        WmsOutboundOrderInfoToMaterialVO wmsOutboundOrderInfoToMaterialVO = new WmsOutboundOrderInfoToMaterialVO();
        wmsOutboundOrderInfoToMaterialVO.setOrderNumberList(orderNumberList);
        List<WmsOutboundOrderInfoToMaterialVO> list = wmsOutboundOrderInfoToMaterialService.selectList(wmsOutboundOrderInfoToMaterialVO);
        for (WmsOutboundOrderInfoToMaterialVO materialVO : list){
            if (materialVO.getStatusType().equals(OutboundOrdertSatus.ASSIGNED.getType())||materialVO.getStatusType().equals(OutboundOrdertSatus.ISSUED.getType())){
                return BaseResult.error("存在分配出库单！");
            }
        }
        return BaseResult.ok();
    }

    /**
     * @description 删除null数据
     * @author  ciro
     * @date   2022/1/20 17:38
     * @param: wmsWaveInfoVO
     * @return: com.jayud.model.vo.WmsWaveInfoVO
     **/
    private WmsWaveInfoVO delNull(WmsWaveInfoVO wmsWaveInfoVO){
        List<WmsOutboundOrderInfoVO> orderInfoList = wmsWaveInfoVO.getOrderInfoList();
        List<WmsOutboundOrderInfoVO> notNullOrderInfoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(orderInfoList)){
            orderInfoList.forEach(x->{
                if (x!=null){
                    notNullOrderInfoList.add(x);
                }
            });
        }
        wmsWaveInfoVO.setOrderInfoList(notNullOrderInfoList);
        return wmsWaveInfoVO;
    }


}
