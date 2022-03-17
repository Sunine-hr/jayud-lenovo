package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.AddWmsShippingReviewForm;
import com.jayud.wms.model.bo.CloseTheBoxForm;
import com.jayud.wms.model.bo.InventoryDetailForm;
import com.jayud.wms.model.enums.ShippingReviewStatusEnum;
import com.jayud.wms.mapper.WmsShippingReviewMapper;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToDistributionMaterial;
import com.jayud.wms.model.po.WmsOutboundOrderInfoToMaterial;
import com.jayud.wms.model.po.WmsShippingReview;
import com.jayud.wms.model.po.WmsWaveToMaterial;
import com.jayud.wms.service.*;
import com.jayud.wms.model.vo.QueryScanInformationVO;
import com.jayud.wms.model.vo.WmsShippingReviewVO;
import com.jayud.common.constant.SysTips;
import com.jayud.common.dto.WmsShippingReviewForm;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 发运复核 服务实现类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Service
public class WmsShippingReviewServiceImpl extends ServiceImpl<WmsShippingReviewMapper, WmsShippingReview> implements IWmsShippingReviewService {


    @Autowired
    private WmsShippingReviewMapper wmsShippingReviewMapper;

    @Autowired
    private IWmsOutboundOrderInfoToMaterialService wmsOutboundOrderInfoToMaterialService;

    @Autowired
    private IWmsWaveToMaterialService wmsWaveToMaterialService;

    @Autowired
    private IWmsOutboundOrderInfoService wmsOutboundOrderInfoService;

    @Autowired
    private IWmsWaveOrderInfoService wmsWaveOrderInfoService;

    @Autowired
    private IWmsOutboundOrderInfoToDistributionMaterialService wmsOutboundOrderInfoToDistributionMaterialService;

    @Autowired
    private IInventoryDetailService inventoryDetailService;

    @Override
    public IPage<WmsShippingReviewVO> selectPage(WmsShippingReview wmsShippingReview,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsShippingReviewVO> page=new Page<WmsShippingReviewVO>(currentPage,pageSize);
        IPage<WmsShippingReviewVO> pageList= wmsShippingReviewMapper.pageList(page, wmsShippingReview);
        return pageList;
    }

    @Override
    public List<WmsShippingReviewVO> selectList(WmsShippingReview wmsShippingReview){
        return wmsShippingReviewMapper.list(wmsShippingReview);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(int id){
        wmsShippingReviewMapper.phyDelById(id);
    }



    @Override
    public List<LinkedHashMap<String, Object>> queryWmsShippingReviewForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsShippingReviewForExcel(paramMap);
    }

    @Override
    public boolean saveWmsShippingReview(List<AddWmsShippingReviewForm> form) {
        List<WmsShippingReview> wmsShippingReviews = ConvertUtil.convertList(form, WmsShippingReview.class);
        for (WmsShippingReview wmsShippingReview : wmsShippingReviews) {
            wmsShippingReview.setReviewerBy(CurrentUserUtil.getUsername());
            wmsShippingReview.setReviewerTime(LocalDateTime.now());
            wmsShippingReview.setUpdateBy(CurrentUserUtil.getUsername());
            wmsShippingReview.setUpdateTime(new Date());
            wmsShippingReview.setIsEnd(1);
        }
        boolean result = this.saveOrUpdateBatch(wmsShippingReviews);
        if(result){
            log.warn("复核提交成功");
//            //修改对应物料的状态
//            List<WmsOutboundOrderInfoToMaterial> wmsOutboundOrderInfoToMaterials = new ArrayList<>();
//            List<WmsWaveToMaterial> wmsWaveToMaterials = new ArrayList<>();
//            for (AddWmsShippingReviewForm addWmsShippingReviewForm : form) {
//                if(addWmsShippingReviewForm.getOrderNumber() != null){
//                    WmsOutboundOrderInfoToMaterial outboundOrderInfoToMaterial = new WmsOutboundOrderInfoToMaterial();
//                    outboundOrderInfoToMaterial.setId(addWmsShippingReviewForm.getWmsMaterialId());
//                    outboundOrderInfoToMaterial.setUpdateBy(CurrentUserUtil.getUsername());
//                    outboundOrderInfoToMaterial.setUpdateTime(new Date());
//                    outboundOrderInfoToMaterial.setStatusType(4);
//                    wmsOutboundOrderInfoToMaterials.add(outboundOrderInfoToMaterial);
//                }
//                if(addWmsShippingReviewForm.getWareNumber() != null){
//                    WmsWaveToMaterial wmsWaveToMaterial = new WmsWaveToMaterial();
//                    wmsWaveToMaterial.setId(addWmsShippingReviewForm.getWmsMaterialId());
//                    wmsWaveToMaterial.setUpdateBy(CurrentUserUtil.getUsername());
//                    wmsWaveToMaterial.setUpdateTime(new Date());
//                    wmsWaveToMaterial.setStatusType(4);
//                    wmsWaveToMaterials.add(wmsWaveToMaterial);
//                }
//            }
//            boolean result1 = this.wmsWaveToMaterialService.updateBatchById(wmsWaveToMaterials);
//            boolean result2 = this.wmsOutboundOrderInfoToMaterialService.updateBatchById(wmsOutboundOrderInfoToMaterials);
//            if(result1){
//                log.warn("修改波次物料信息状态成功");
//            }
//            if(result2){
//                log.warn("修改出库单物料信息状态成功");
//            }
//            //判断是否已完成复核
//            for (AddWmsShippingReviewForm addWmsShippingReviewForm : form) {
//                if(addWmsShippingReviewForm.getOrderNumber() != null){
//                    List<WmsOutboundOrderInfoToMaterial> wmsOutboundOrderInfoToMaterials1 = wmsOutboundOrderInfoToMaterialService.getOutboundOrderInfoToMaterialByWaveNumber(addWmsShippingReviewForm.getOrderNumber());
//                    if(CollectionUtil.isEmpty(wmsOutboundOrderInfoToMaterials1)){
//                        boolean result3 = wmsOutboundOrderInfoService.updateByOrderNumber(addWmsShippingReviewForm.getOrderNumber());
//                        if(result3){
//                            log.warn("修改出库单状态成功");
//                        }
//                    }
//                }
//                if(addWmsShippingReviewForm.getWareNumber() != null){
//                    List<WmsWaveToMaterial> wmsWaveToMaterials1 = wmsWaveToMaterialService.getWmsWaveToMaterialByWaveNumber(addWmsShippingReviewForm.getWareNumber());
//                    if(CollectionUtil.isEmpty(wmsWaveToMaterials1)){
//                        boolean result3 = wmsWaveOrderInfoService.updateByWareNumber(addWmsShippingReviewForm.getWareNumber());
//                        if(result3){
//                            log.warn("修改波次单状态成功");
//                        }
//                    }
//                }
//            }

        }
        return result;
    }

    @Override
    public boolean CloseTheBox(CloseTheBoxForm form) {
        //关箱，判断该出库单是否关过箱
        WmsShippingReview wmsShippingReview = this.getById(form.getIds().get(0));
        QueryWrapper<WmsShippingReview> queryWrapper = new QueryWrapper<>();
        if(wmsShippingReview.getWareNumber() != null){
            queryWrapper.lambda().eq(WmsShippingReview::getWareNumber,wmsShippingReview.getWareNumber());
        }
        if(wmsShippingReview.getOrderNumber() != null){
            queryWrapper.lambda().eq(WmsShippingReview::getOrderNumber,wmsShippingReview.getOrderNumber());
        }
        queryWrapper.lambda().eq(WmsShippingReview::getIsDeleted,0);
        queryWrapper.lambda().ne(WmsShippingReview::getBoxNumber,null);
        List<WmsShippingReview> wmsShippingReviewList = this.list(queryWrapper);
        List<WmsShippingReview> wmsShippingReviews = new ArrayList<>();
        if(CollectionUtil.isEmpty(wmsShippingReviewList)){
            for (Long id : form.getIds()) {
                WmsShippingReview wmsShippingReview1 = new WmsShippingReview();
                wmsShippingReview1.setId(id);
                wmsShippingReview1.setBoxNumber(form.getBoxNumber());
                wmsShippingReview1.setUpdateBy(CurrentUserUtil.getUsername());
                wmsShippingReview1.setUpdateTime(new Date());
                wmsShippingReview1.setIsEnd(2);
                wmsShippingReviews.add(wmsShippingReview1);
            }
//        }else{
//            Integer boxNumber = 0;
//            if(wmsShippingReview.getWareNumber() != null){
//                boxNumber = this.baseMapper.getBoxNumberByWareNumber(wmsShippingReview.getWareNumber());
//            }
//            if(wmsShippingReview.getOrderNumber() != null){
//                boxNumber = this.baseMapper.getBoxNumberByWareNumber(wmsShippingReview.getOrderNumber());
//            }
//            for (Long id : form.getIds()) {
//                WmsShippingReview wmsShippingReview1 = new WmsShippingReview();
//                wmsShippingReview1.setId(id);
//                wmsShippingReview1.setBoxNumber(boxNumber + 1);
//                wmsShippingReview1.setUpdateBy(CurrentUserUtil.getUsername());
//                wmsShippingReview1.setUpdateTime(new Date());
//                wmsShippingReview1.setIsEnd(2);
//                wmsShippingReviews.add(wmsShippingReview1);
//            }
        }
        boolean result = this.updateBatchById(wmsShippingReviews);
        if(result){
            log.warn("关箱成功");
//            //修改对应物料的状态
//            List<WmsOutboundOrderInfoToMaterial> wmsOutboundOrderInfoToMaterials = new ArrayList<>();
//            List<WmsWaveToMaterial> wmsWaveToMaterials = new ArrayList<>();
//            for (Long integer : form.getIds()) {
//                WmsShippingReview wmsShippingReview1 = this.getById(integer);
//                if(wmsShippingReview1.getOrderNumber() != null){
//                    WmsOutboundOrderInfoToMaterial outboundOrderInfoToMaterial = new WmsOutboundOrderInfoToMaterial();
//                    outboundOrderInfoToMaterial.setId(wmsShippingReview1.getAllocationId());
//                    outboundOrderInfoToMaterial.setUpdateBy(CurrentUserUtil.getUsername());
//                    outboundOrderInfoToMaterial.setUpdateTime(new Date());
//                    outboundOrderInfoToMaterial.setStatusType(4);
//                    wmsOutboundOrderInfoToMaterials.add(outboundOrderInfoToMaterial);
//                }
//                if(wmsShippingReview1.getWareNumber() != null){
//                    WmsWaveToMaterial wmsWaveToMaterial = new WmsWaveToMaterial();
//                    wmsWaveToMaterial.setId(wmsShippingReview1.getAllocationId());
//                    wmsWaveToMaterial.setUpdateBy(CurrentUserUtil.getUsername());
//                    wmsWaveToMaterial.setUpdateTime(new Date());
//                    wmsWaveToMaterial.setStatusType(4);
//                    wmsWaveToMaterials.add(wmsWaveToMaterial);
//                }
//            }
//            if(CollectionUtil.isNotEmpty(wmsWaveToMaterials)){
//                boolean result1 = this.wmsWaveToMaterialService.updateBatchById(wmsWaveToMaterials);
//                if(result1){
//                    log.warn("修改波次物料信息状态成功");
//                }
//            }
//            if(CollectionUtil.isNotEmpty(wmsOutboundOrderInfoToMaterials)){
//                boolean result2 = this.wmsOutboundOrderInfoToMaterialService.updateBatchById(wmsOutboundOrderInfoToMaterials);
//
//                if(result2){
//                    log.warn("修改出库单物料信息状态成功");
//                }
//            }
//
//            //判断是否已完成复核
//            for (Long integer : form.getIds()) {
//                WmsShippingReview wmsShippingReview1 = this.getById(integer);
//                if(wmsShippingReview1.getOrderNumber() != null){
//                    List<WmsOutboundOrderInfoToMaterial> wmsOutboundOrderInfoToMaterials1 = wmsOutboundOrderInfoToMaterialService.getOutboundOrderInfoToMaterialByWaveNumber(wmsShippingReview1.getOrderNumber());
//                    if(CollectionUtil.isEmpty(wmsOutboundOrderInfoToMaterials1)){
//                        boolean result3 = wmsOutboundOrderInfoService.updateByOrderNumber(wmsShippingReview1.getOrderNumber());
//                        if(result3){
//                            log.warn("修改出库单状态成功");
//                        }
//                    }
//                }
//                if(wmsShippingReview1.getWareNumber() != null){
//                    List<WmsWaveToMaterial> wmsWaveToMaterials1 = wmsWaveToMaterialService.getWmsWaveToMaterialByWaveNumber(wmsShippingReview1.getWareNumber());
//                    if(CollectionUtil.isEmpty(wmsWaveToMaterials1)){
//                        boolean result3 = wmsWaveOrderInfoService.updateByWareNumber(wmsShippingReview1.getWareNumber());
//                        if(result3){
//                            log.warn("修改波次单状态成功");
//                        }
//                    }
//                }
//            }
        }

        return result;
    }

    @Override
    public boolean singleReviewSubmission(AddWmsShippingReviewForm form) {

//        WmsShippingReview shippingReview = this.getById(form.getId());

        WmsShippingReview wmsShippingReview = ConvertUtil.convert(form, WmsShippingReview.class);
        wmsShippingReview.setScannedAccount(wmsShippingReview.getScannedAccount()+1);
        wmsShippingReview.setNotScannedAccount(wmsShippingReview.getNotScannedAccount()-1);
        if(wmsShippingReview.getNotScannedAccount().equals(0)){
            wmsShippingReview.setIsEnd(1);
        }else {
            wmsShippingReview.setIsEnd(0);
        }
        wmsShippingReview.setCreateBy(CurrentUserUtil.getUsername());
        wmsShippingReview.setCreateTime(new Date());

        wmsShippingReview.setIsEnd(1);
        boolean result = this.save(wmsShippingReview);
        if(result) {
            log.warn("单件复核提交成功");
        }
        return result;
    }

    @Override
    public List<QueryScanInformationVO> queryScanInformation(String orderNumber, String materialCode) {
        return this.baseMapper.queryScanInformation(orderNumber,materialCode);
    }

    @Override
    public List<LinkedHashMap<String, Object>> exportShipmentReviewVariance(Map<String, Object> paramMap) {
        return this.baseMapper.exportShipmentReviewVariance(paramMap);
    }

    /**
     * 根据订单号查询未发运复核的数据
     * @param orderNumber
     * @return
     */
    @Override
    public List<WmsShippingReview> getWmsShippingReviewByOrderNumber(String orderNumber) {
        QueryWrapper<WmsShippingReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WmsShippingReview::getOrderNumber,orderNumber);
        queryWrapper.lambda().eq(WmsShippingReview::getIsDeleted,0);
        queryWrapper.lambda().eq(WmsShippingReview::getIsEnd,0);
        queryWrapper.lambda().gt(WmsShippingReview::getNotScannedAccount,0);
        return this.list(queryWrapper);
    }
    /**
     * 根据波次号查询未发运复核的数据
     * @param orderNumber
     * @return
     */
    @Override
    public List<WmsShippingReview> getWmsShippingReviewByWaveNumber(String orderNumber) {
        QueryWrapper<WmsShippingReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WmsShippingReview::getWareNumber,orderNumber);
        queryWrapper.lambda().eq(WmsShippingReview::getIsDeleted,0);
        queryWrapper.lambda().eq(WmsShippingReview::getIsEnd,0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWmsShippingReviewStatusByOrderNumber(String orderNumber) {
        QueryWrapper<WmsShippingReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WmsShippingReview::getOrderNumber,orderNumber);
        queryWrapper.lambda().eq(WmsShippingReview::getIsDeleted,0);
        List<WmsShippingReview> wmsShippingReviews = this.list(queryWrapper);
        List<WmsOutboundOrderInfoToMaterial> wmsOutboundOrderInfoToMaterials = new ArrayList<>();

        if(CollectionUtil.isEmpty(wmsShippingReviews)){
            return false;
        }
        for (WmsShippingReview wmsShippingReview : wmsShippingReviews) {
            wmsShippingReview.setIsEnd(3);
        }
        boolean result = this.updateBatchById(wmsShippingReviews);
        if(result){
            log.warn("修改发运复核单状态为发运成功");
        }

        List<InventoryDetailForm> detailFormList = getPackingInventory(orderNumber, false);
        BaseResult output = inventoryDetailService.output(detailFormList);
        if(output.isSuccess()){
            wmsOutboundOrderInfoToDistributionMaterialService.cancleDistributionToUnpacking(orderNumber,false);
            log.warn("扣除库存成功");
        }else{
            return false;
        }

        for (WmsShippingReview wmsShippingReview : wmsShippingReviews) {
            WmsOutboundOrderInfoToMaterial outboundOrderInfoToMaterial = new WmsOutboundOrderInfoToMaterial();
            outboundOrderInfoToMaterial.setId(wmsShippingReview.getAllocationId());
            outboundOrderInfoToMaterial.setUpdateBy(CurrentUserUtil.getUsername());
            outboundOrderInfoToMaterial.setUpdateTime(new Date());
            outboundOrderInfoToMaterial.setStatusType(4);
            wmsOutboundOrderInfoToMaterials.add(outboundOrderInfoToMaterial);

        }
        boolean result2 = this.wmsOutboundOrderInfoToMaterialService.updateBatchById(wmsOutboundOrderInfoToMaterials);
        if(result2){
            log.warn("修改出库单物料信息状态成功");
            boolean result3 = wmsOutboundOrderInfoService.updateByOrderNumber(orderNumber);
            if(result3){
                log.warn("修改出库单状态成功");
            }
        }
        return result2;

    }

    @Override
    public boolean updateWmsShippingReviewStatusByWaveNumber(String orderNumber) {
        QueryWrapper<WmsShippingReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WmsShippingReview::getWareNumber,orderNumber);
        queryWrapper.lambda().eq(WmsShippingReview::getIsDeleted,0);
        List<WmsShippingReview> wmsShippingReviews = this.list(queryWrapper);

        if(CollectionUtil.isEmpty(wmsShippingReviews)){
            return false;
        }
        for (WmsShippingReview wmsShippingReview : wmsShippingReviews) {
            wmsShippingReview.setIsEnd(3);
        }
        boolean result = this.updateBatchById(wmsShippingReviews);
        if(result){
            log.warn("修改发运复核单状态为发运成功");
        }

        List<InventoryDetailForm> detailFormList = getPackingInventory(orderNumber, true);
        BaseResult output = inventoryDetailService.output(detailFormList);
        if(output.isSuccess()){
            wmsOutboundOrderInfoToDistributionMaterialService.cancleDistributionToUnpacking(orderNumber,true);
            log.warn("扣除库存成功");
        }else{
            return false;
        }

        List<WmsWaveToMaterial> wmsWaveToMaterials = new ArrayList<>();
        for (WmsShippingReview wmsShippingReview : wmsShippingReviews) {
            WmsWaveToMaterial wmsWaveToMaterial = new WmsWaveToMaterial();
            wmsWaveToMaterial.setId(wmsShippingReview.getAllocationId());
            wmsWaveToMaterial.setUpdateBy(CurrentUserUtil.getUsername());
            wmsWaveToMaterial.setUpdateTime(new Date());
            wmsWaveToMaterial.setStatusType(4);
            wmsWaveToMaterials.add(wmsWaveToMaterial);
        }
        boolean result1 = this.wmsWaveToMaterialService.updateBatchById(wmsWaveToMaterials);
        if(result1){
            log.warn("修改波次物料信息状态成功");
            boolean result3 = wmsWaveOrderInfoService.updateByWareNumber(orderNumber);
            if(result3){
                log.warn("修改波次单状态成功");
            }
        }
        return result1;
    }

    @Override
    public boolean unpacking(Long id) {
        WmsShippingReview wmsShippingReview = new WmsShippingReview();
        wmsShippingReview.setId(id);
        wmsShippingReview.setUpdateBy(CurrentUserUtil.getUsername());
        wmsShippingReview.setUpdateTime(new Date());
        wmsShippingReview.setIsEnd(1);
        wmsShippingReview.setBoxNumber(null);
        return this.updateById(wmsShippingReview);
    }

    @Override
    public BaseResult confirmForApp(WmsShippingReviewForm wmsShippingReviewForm) {
        if(wmsShippingReviewForm.getId() == null){
            return BaseResult.error("请选择数据！");
        }
        if(wmsShippingReviewForm.getScannedAccount() == null){
            return BaseResult.error("请填写扫描数量！");
        }
        WmsShippingReview shippingReview = this.getById(wmsShippingReviewForm.getId());
        if (shippingReview != null){
            if (shippingReview.getNotScannedAccount().equals(wmsShippingReviewForm.getScannedAccount())){
                shippingReview.setScannedAccount(wmsShippingReviewForm.getScannedAccount());
                shippingReview.setNotScannedAccount(0);
            }else {
                return BaseResult.error(SysTips.SHIPPER_REVIEW_SCANNED_ACCOUNT_ERROR);
            }
            this.updateById(shippingReview);
            if (wmsShippingReviewForm.getIsEnd().equals(ShippingReviewStatusEnum.CLOSE_BOX.getType())){
                closeBox(shippingReview.getOrderNumber(),shippingReview.getWareNumber());
            }
        }
        return BaseResult.ok();
    }


    /**
     * @description 关箱
     * @author  ciro
     * @date   2022/1/14 15:06
     * @param: orderNumber
     * @param: waveNumber
     * @return: void
     **/
    private void closeBox(String orderNumber,String waveNumber){
        WmsShippingReview shippingReview = new WmsShippingReview();
        if (StringUtils.isNotBlank(orderNumber)){
            shippingReview.setOrderNumber(orderNumber);
        }else if (StringUtils.isNotBlank(waveNumber)){
            shippingReview.setWareNumber(waveNumber);
        }
        List<WmsShippingReviewVO> shippingReviewList = selectList(shippingReview);
        List<Integer> boxList = shippingReviewList.stream().filter(x->x.getBoxNumber()!= null).distinct().map(x->Integer.parseInt(x.getBoxNumber())).collect(Collectors.toList());
        int boxNumber = 0;
        if (CollectionUtil.isEmpty(boxList)){
            boxNumber+= 1;
        }else {
            Collections.reverse(boxList);
            boxNumber =  boxList.get(0)+1;
        }
        List<WmsShippingReviewVO> updateList = shippingReviewList.stream().filter(x->x.getScannedAccount() != 0 && x.getBoxNumber() == null).collect(Collectors.toList());
        List<WmsShippingReview> reviewList = new ArrayList<>();
        for (WmsShippingReviewVO wmsShippingReviewVO : updateList){
            WmsShippingReview wmsShippingReview = new WmsShippingReview();
            BeanUtils.copyProperties(wmsShippingReviewVO,wmsShippingReview);
            wmsShippingReview.setBoxNumber(String.valueOf(boxNumber));
            wmsShippingReview.setIsEnd(2);
            reviewList.add(wmsShippingReview);
        }
        if (!reviewList.isEmpty()){
            this.updateBatchById(reviewList);
        }
    }

    /**
     * @description 获取发运复核后分配原始数据
     * @author  ciro
     * @date   2022/1/20 14:02
     * @param: orderNumber
     * @param: isWave
     * @return: java.util.List<com.jayud.model.bo.InventoryDetailForm>
     **/
    private List<InventoryDetailForm> getPackingInventory(String orderNumber,boolean isWave){
        List<InventoryDetailForm> detailFormList = new ArrayList<>();
        LambdaQueryWrapper<WmsShippingReview> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsShippingReview::getIsDeleted,false);
        if (isWave){
            lambdaQueryWrapper.eq(WmsShippingReview::getWareNumber,orderNumber);
        }else {
            lambdaQueryWrapper.eq(WmsShippingReview::getOrderNumber,orderNumber);
        }
        List<WmsShippingReview> reviewList = this.list(lambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(reviewList)) {
            //获取分配后的物料id对象
            List<Long> allocationIdList = reviewList.stream().map(x -> x.getAllocationId()).collect(Collectors.toList());

            WmsOutboundOrderInfoToDistributionMaterial distributionMaterial = new WmsOutboundOrderInfoToDistributionMaterial();
            distributionMaterial.setIdList(allocationIdList);
            List<WmsOutboundOrderInfoToDistributionMaterial> materialList = wmsOutboundOrderInfoToDistributionMaterialService.selectList(distributionMaterial);
            if (CollectionUtil.isNotEmpty(materialList)) {
                detailFormList = wmsOutboundOrderInfoToDistributionMaterialService.initInventoryDetail(materialList);
            }
        }
        return detailFormList;
    }


}
