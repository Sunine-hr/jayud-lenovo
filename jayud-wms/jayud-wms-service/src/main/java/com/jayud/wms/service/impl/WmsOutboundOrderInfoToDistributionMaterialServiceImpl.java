package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToDistributionMaterial;
import com.jayud.wms.model.po.WmsPackingOffshelfTask;
import com.jayud.wms.mapper.WmsOutboundOrderInfoToDistributionMaterialMapper;
import com.jayud.wms.service.IInventoryDetailService;
import com.jayud.wms.service.IWmsOutboundOrderInfoToDistributionMaterialService;
import com.jayud.wms.service.IWmsPackingOffshelfTaskService;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoToDistributionMaterialVO;
import com.jayud.common.BaseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 出库单-分配物料信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Service
public class WmsOutboundOrderInfoToDistributionMaterialServiceImpl extends ServiceImpl<WmsOutboundOrderInfoToDistributionMaterialMapper, WmsOutboundOrderInfoToDistributionMaterial> implements IWmsOutboundOrderInfoToDistributionMaterialService {


    @Autowired
    private WmsOutboundOrderInfoToDistributionMaterialMapper wmsOutboundOrderInfoToDistributionMaterialMapper;

    @Autowired
    private IWmsPackingOffshelfTaskService wmsPackingOffshelfTaskService;
    @Autowired
    private IInventoryDetailService inventoryDetailService;

    @Override
    public IPage<WmsOutboundOrderInfoToDistributionMaterial> selectPage(WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsOutboundOrderInfoToDistributionMaterial> page=new Page<WmsOutboundOrderInfoToDistributionMaterial>(currentPage,pageSize);
        IPage<WmsOutboundOrderInfoToDistributionMaterial> pageList= wmsOutboundOrderInfoToDistributionMaterialMapper.pageList(page, wmsOutboundOrderInfoToDistributionMaterial);
        return pageList;
    }

    @Override
    public List<WmsOutboundOrderInfoToDistributionMaterial> selectList(WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial){
        return wmsOutboundOrderInfoToDistributionMaterialMapper.list(wmsOutboundOrderInfoToDistributionMaterial);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsOutboundOrderInfoToDistributionMaterialMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundOrderInfoToDistributionMaterialForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundOrderInfoToDistributionMaterialForExcel(paramMap);
    }

    @Override
    public boolean checkIsAllocation(String orderNumber, String waveNumber) {
        WmsOutboundOrderInfoToDistributionMaterial wmsOutboundOrderInfoToDistributionMaterial = new WmsOutboundOrderInfoToDistributionMaterial();
        wmsOutboundOrderInfoToDistributionMaterial.setOrderNumber(orderNumber);
        wmsOutboundOrderInfoToDistributionMaterial.setWaveNumber(waveNumber);
        List<WmsOutboundOrderInfoToDistributionMaterial> list = selectList(wmsOutboundOrderInfoToDistributionMaterial);
        if (list.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public List<InventoryDetailForm> getInventoryByOrderNumber(String orderNumber, boolean isWave) {
        WmsOutboundOrderInfoToDistributionMaterial distributionMaterial = new WmsOutboundOrderInfoToDistributionMaterial();
        if (isWave){
            distributionMaterial.setWaveNumber(orderNumber);
        }else {
            distributionMaterial.setOrderNumber(orderNumber);
        }
        List<WmsOutboundOrderInfoToDistributionMaterial> distributionMaterialList = this.selectList(distributionMaterial);
        List<InventoryDetailForm> detailFormList = new ArrayList<>();
        if (CollUtil.isNotEmpty(distributionMaterialList)){
            detailFormList = initInventoryDetail(distributionMaterialList);
        }
        return detailFormList;
    }

    @Override
    public BaseResult cancleDistributionToUnpacking(String orderNumber,boolean isWave) {
        WmsPackingOffshelfTask task = new WmsPackingOffshelfTask();
        if (isWave){
            task.setWaveNumber(orderNumber);
        }else {
            task.setOrderNumber(orderNumber);
        }
        List<WmsPackingOffshelfTask> taskList = wmsPackingOffshelfTaskService.selectList(task);
        if (CollUtil.isNotEmpty(taskList)) {
            List<Long> idList = taskList.stream().map(x -> x.getAllocationId()).collect(Collectors.toList());
            LambdaQueryWrapper<WmsOutboundOrderInfoToDistributionMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.notIn(WmsOutboundOrderInfoToDistributionMaterial::getId, idList);
            if (isWave){
                lambdaQueryWrapper.eq(WmsOutboundOrderInfoToDistributionMaterial::getWaveNumber, orderNumber);
            } else {
                lambdaQueryWrapper.eq(WmsOutboundOrderInfoToDistributionMaterial::getOrderNumber, orderNumber);
            }
            List<WmsOutboundOrderInfoToDistributionMaterial> materialList = this.list(lambdaQueryWrapper);
            if (CollUtil.isNotEmpty(materialList)) {
                List<InventoryDetailForm> detailFormList = initInventoryDetail(materialList);
                BaseResult cancelResult = inventoryDetailService.cancelOutputAllocation(detailFormList);
                if (cancelResult.isSuccess()){
                    materialList.forEach(material->{
                        material.setIsDeleted(true);
                    });
                    this.updateBatchById(materialList);
                }
            }
        }
        return BaseResult.ok();
    }

    /**
     * @description 初始化分配数据
     * @author  ciro
     * @date   2022/1/24 13:49
     * @param: distributionMaterialList
     * @return: java.util.List<com.jayud.model.bo.InventoryDetailForm>
     **/
    @Override
    public List<InventoryDetailForm> initInventoryDetail(List<WmsOutboundOrderInfoToDistributionMaterial> distributionMaterialList){
        List<InventoryDetailForm> detailFormList = new ArrayList<>();
        distributionMaterialList.forEach(distributionMaterials -> {
            InventoryDetailForm form = new InventoryDetailForm();
            BeanUtils.copyProperties(distributionMaterials,form);
            form.setOperationCount(distributionMaterials.getRealDistributionAccount());
            form.setWarehouseLocationStatus(0);
            form.setWarehouseLocationStatus2(0);
            form.setBatchCode(distributionMaterials.getDistributionBatchCode());
            form.setMaterialProductionDate(distributionMaterials.getDistributionMaterialProductionDate());
            form.setCustomField1(distributionMaterials.getDistributionCustomField1());
            form.setCustomField2(distributionMaterials.getDistributionCustomField2());
            form.setCustomField3(distributionMaterials.getDistributionCustomField3());
            detailFormList.add(form);
        });
        return detailFormList;
    }

    @Override
    public List<WmsOutboundOrderInfoToDistributionMaterialVO> selectListByOrderMaterialIds(List<Long> orderMaterialIds) {
        return wmsOutboundOrderInfoToDistributionMaterialMapper.selectListByOrderMaterialIds(orderMaterialIds);
    }


}
