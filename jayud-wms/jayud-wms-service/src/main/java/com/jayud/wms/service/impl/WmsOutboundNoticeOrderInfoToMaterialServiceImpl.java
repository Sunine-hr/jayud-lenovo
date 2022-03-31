package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ListUtils;
import com.jayud.wms.mapper.WmsOutboundNoticeOrderInfoToMaterialMapper;
import com.jayud.wms.model.po.InventoryDetail;
import com.jayud.wms.model.po.WmsOutboundNoticeOrderInfoToMaterial;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoToMaterialVO;
import com.jayud.wms.model.vo.WmsOutboundNoticeOrderInfoVO;
import com.jayud.wms.service.IInventoryDetailService;
import com.jayud.wms.service.IWmsOutboundNoticeOrderInfoToMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 出库通知订单-物料信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Service
public class WmsOutboundNoticeOrderInfoToMaterialServiceImpl extends ServiceImpl<WmsOutboundNoticeOrderInfoToMaterialMapper, WmsOutboundNoticeOrderInfoToMaterial> implements IWmsOutboundNoticeOrderInfoToMaterialService {

    @Autowired
    private IInventoryDetailService iInventoryDetailService;

    @Autowired
    private WmsOutboundNoticeOrderInfoToMaterialMapper wmsOutboundNoticeOrderInfoToMaterialMapper;

    @Override
    public IPage<WmsOutboundNoticeOrderInfoToMaterialVO> selectPage(WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO,
                                                                    Integer currentPage,
                                                                    Integer pageSize,
                                                                    HttpServletRequest req){

        Page<WmsOutboundNoticeOrderInfoToMaterialVO> page=new Page<WmsOutboundNoticeOrderInfoToMaterialVO>(currentPage,pageSize);
        IPage<WmsOutboundNoticeOrderInfoToMaterialVO> pageList= wmsOutboundNoticeOrderInfoToMaterialMapper.pageList(page, wmsOutboundNoticeOrderInfoToMaterialVO);
        return pageList;
    }

    @Override
    public List<WmsOutboundNoticeOrderInfoToMaterialVO> selectList(WmsOutboundNoticeOrderInfoToMaterialVO wmsOutboundNoticeOrderInfoToMaterialVO){
        return wmsOutboundNoticeOrderInfoToMaterialMapper.list(wmsOutboundNoticeOrderInfoToMaterialVO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsOutboundNoticeOrderInfoToMaterialMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundNoticeOrderInfoToMaterialForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundNoticeOrderInfoToMaterialForExcel(paramMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMaterial(WmsOutboundNoticeOrderInfoVO wmsOutboundNoticeOrderInfoVO) {
        WmsOutboundNoticeOrderInfoToMaterialVO materialVO = new WmsOutboundNoticeOrderInfoToMaterialVO();
        materialVO.setOrderNumber(wmsOutboundNoticeOrderInfoVO.getOrderNumber());
        List<WmsOutboundNoticeOrderInfoToMaterialVO> materialList = selectList(materialVO);
        List<WmsOutboundNoticeOrderInfoToMaterialVO> thisMaterialList = wmsOutboundNoticeOrderInfoVO.getThisMaterialList();
        Map<String,WmsOutboundNoticeOrderInfoToMaterialVO> materialMap = new HashMap<>(20);
        thisMaterialList.forEach(x->{
            materialMap.put(x.getMaterialCode(),x);
        });
        List<String> lastCodeList = new ArrayList<>();
        List<String> thisCodeList = new ArrayList<>();
        materialList.forEach(x->{
            lastCodeList.add(x.getMaterialCode());
        });
        thisMaterialList.forEach(x->{
            thisCodeList.add(x.getMaterialCode());
        });
        List<String> deleteList = ListUtils.getDiff(thisCodeList,lastCodeList);
        List<String> saveList = ListUtils.getDiff(lastCodeList,thisCodeList);
        List<String> updateList = ListUtils.getSame(thisCodeList,lastCodeList);
        if (CollUtil.isNotEmpty(deleteList)){
            wmsOutboundNoticeOrderInfoToMaterialMapper.delteByOrderNumberAndMaterialCode(wmsOutboundNoticeOrderInfoVO.getOrderNumber(), deleteList,null, CurrentUserUtil.getUsername());
        }
        if (CollUtil.isNotEmpty(updateList)){
            List<WmsOutboundNoticeOrderInfoToMaterialVO> updateMaterList = new ArrayList<>();
            updateList.forEach(code -> {
                updateMaterList.add(materialMap.get(code));
            });
            if (!updateMaterList.isEmpty()){
                addBatch(updateMaterList,wmsOutboundNoticeOrderInfoVO.getOrderNumber(),false);
            }
        }
        if (CollUtil.isNotEmpty(saveList)){
            List<WmsOutboundNoticeOrderInfoToMaterialVO> saveMaterList = new ArrayList<>();
            saveList.forEach(code -> {
                saveMaterList.add(materialMap.get(code));
            });
            if (!saveMaterList.isEmpty()){
                addBatch(saveMaterList,wmsOutboundNoticeOrderInfoVO.getOrderNumber(),true);
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByOrderNumber(String orderNumber) {
        wmsOutboundNoticeOrderInfoToMaterialMapper.delteByOrderNumberAndMaterialCode(orderNumber, null,null, CurrentUserUtil.getUsername());
    }

    @Override
    public List<WmsOutboundNoticeOrderInfoToMaterialVO> getInventoryMetailDetailList(InventoryDetail inventoryDetail) {
        List<InventoryDetail> detailList = iInventoryDetailService.selectList(inventoryDetail);
        List<WmsOutboundNoticeOrderInfoToMaterialVO> materialList = new ArrayList<>();
        if (CollUtil.isNotEmpty(detailList)){
            detailList.forEach(detail -> {
                WmsOutboundNoticeOrderInfoToMaterialVO material = new WmsOutboundNoticeOrderInfoToMaterialVO();
                material.setMaterialName(detail.getMaterialName());
                material.setMaterialCode(detail.getMaterialCode());
                material.setMaterialSpecification(detail.getMaterialSpecification());
                material.setAccount(detail.getExistingCount().subtract(detail.getAllocationCount()).subtract(detail.getPickingCount()));
                material.setBatchCode(detail.getBatchCode());
                material.setMaterialProductionDate(detail.getMaterialProductionDate());
                material.setCustomField1(detail.getCustomField1());
                material.setCustomField2(detail.getCustomField2());
                material.setCustomField3(detail.getCustomField3());
                material.setInWarehouseNumber(detail.getInWarehouseNumber());
                material.setWeight(detail.getWeight());
                material.setVolume(detail.getVolume());
                material.setWarehouseLocationId(detail.getWarehouseLocationId());
                material.setWarehouseLocationCode(detail.getWarehouseLocationCode());
                material.setInventoryDetailId(detail.getId());
                materialList.add(material);

            });
        }
        return materialList;
    }

    /**
     * @description 批量保存
     * @author  ciro
     * @date   2021/12/22 16:41
     * @param: addList  新增集合
     * @param: orderNumber  出库通知单编号
     * @param: isAdd  是否新增
     * @return: void
     **/
    private void addBatch(List<WmsOutboundNoticeOrderInfoToMaterialVO> addList,String orderNumber,boolean isAdd){
        List<WmsOutboundNoticeOrderInfoToMaterial> list = new ArrayList<>();
        addList.forEach(materials -> {
            WmsOutboundNoticeOrderInfoToMaterial material = new WmsOutboundNoticeOrderInfoToMaterial();
            BeanUtils.copyProperties(materials,material);
            material.setOrderNumber(orderNumber);
            list.add(material);
        });
        if (isAdd){
            this.saveBatch(list);
        }else {
            this.updateBatchById(list);
        }
    }






}
