package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.wms.mapper.WmsOutboundShippingReviewInfoMapper;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfo;
import com.jayud.wms.model.vo.WmsOutboundShippingReviewInfoVO;
import com.jayud.wms.service.IWmsOutboundShippingReviewInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial;
import com.jayud.wms.mapper.WmsOutboundShippingReviewInfoToMaterialMapper;
import com.jayud.wms.service.IWmsOutboundShippingReviewInfoToMaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * wms-出库发运复核物料信息 服务实现类
 *
 * @author jayud
 * @since 2022-04-07
 */
@Slf4j
@Service
public class WmsOutboundShippingReviewInfoToMaterialServiceImpl extends ServiceImpl<WmsOutboundShippingReviewInfoToMaterialMapper, WmsOutboundShippingReviewInfoToMaterial> implements IWmsOutboundShippingReviewInfoToMaterialService {

    @Autowired
    private IWmsOutboundShippingReviewInfoService wmsOutboundShippingReviewInfoService;


    @Autowired
    private WmsOutboundShippingReviewInfoToMaterialMapper wmsOutboundShippingReviewInfoToMaterialMapper;


    @Autowired
    private WmsOutboundShippingReviewInfoMapper wmsOutboundShippingReviewInfoMapper;

    @Override
    public IPage<WmsOutboundShippingReviewInfoToMaterial> selectPage(WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsOutboundShippingReviewInfoToMaterial> page=new Page<WmsOutboundShippingReviewInfoToMaterial>(currentPage,pageSize);
        IPage<WmsOutboundShippingReviewInfoToMaterial> pageList= wmsOutboundShippingReviewInfoToMaterialMapper.pageList(page, wmsOutboundShippingReviewInfoToMaterial);
        return pageList;
    }

    @Override
    public List<WmsOutboundShippingReviewInfoToMaterial> selectList(WmsOutboundShippingReviewInfoToMaterial wmsOutboundShippingReviewInfoToMaterial){
        return wmsOutboundShippingReviewInfoToMaterialMapper.list(wmsOutboundShippingReviewInfoToMaterial);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        wmsOutboundShippingReviewInfoToMaterialMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        wmsOutboundShippingReviewInfoToMaterialMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundShippingReviewInfoToMaterialForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundShippingReviewInfoToMaterialForExcel(paramMap);
    }

    @Override
    public BaseResult<List<WmsOutboundShippingReviewInfoToMaterial>> comfirmReview(WmsOutboundShippingReviewInfoVO reviewInfo) {
        //箱数为空
        List<String> nullBoxList = reviewInfo.getMaterialList().stream().filter(x->x.getBoxNumber()==null).map(x->x.getMaterialCode()).collect(Collectors.toList());
        //箱数不为空
        List<WmsOutboundShippingReviewInfoToMaterial> materialList = reviewInfo.getMaterialList().stream().filter(x->x.getBoxNumber()!=null).collect(Collectors.toList());
        Map<Long,Integer> msg = materialList.stream().collect(Collectors.toMap(x->x.getId(),x->x.getBoxNumber()));
        List<Long> idList = materialList.stream().map(x->x.getId()).collect(Collectors.toList());
        LambdaQueryWrapper<WmsOutboundShippingReviewInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundShippingReviewInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.in(WmsOutboundShippingReviewInfoToMaterial::getId,idList);
        materialList = this.list(lambdaQueryWrapper);
        List<String> errList = new ArrayList<>();
        if (CollUtil.isNotEmpty(materialList)){
            List<WmsOutboundShippingReviewInfoToMaterial> successList = new ArrayList<>();
            materialList.forEach(materials->{
                if (materials.getIsReview()){
                    errList.add(materials.getMaterialCode());
                }else {
                    materials.setBoxNumber(msg.get(materials.getId()));
                    materials.setIsReview(true);
                    successList.add(materials);
                }
            });
            if (CollUtil.isNotEmpty(successList)){
                this.updateBatchById(successList);
                changeInfoStatus(successList.get(0).getShippingReviewOrderNumber());
            }
        }
        String errMsg = "";
        if (CollUtil.isNotEmpty(nullBoxList)){
            errMsg += StringUtils.join(nullBoxList, StrUtil.C_COMMA) + " 箱数为空！ ";
        }
        if (CollUtil.isNotEmpty(errList)){
            errMsg += StringUtils.join(errList, StrUtil.C_COMMA) + " 已确认复核，请勿重复确认！ ";
        }
        if (StringUtils.isNotBlank(errMsg)){
            return BaseResult.error(errMsg);
        }
        return BaseResult.ok();
    }

    @Override
    public BaseResult<List<WmsOutboundShippingReviewInfoToMaterial>> cancelReview(WmsOutboundShippingReviewInfoVO reviewInfo) {
        List<Long> idList = reviewInfo.getMaterialList().stream().map(x->x.getId()).collect(Collectors.toList());
        LambdaQueryWrapper<WmsOutboundShippingReviewInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundShippingReviewInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.in(WmsOutboundShippingReviewInfoToMaterial::getId,idList);
        List<WmsOutboundShippingReviewInfoToMaterial> materialList = this.list(lambdaQueryWrapper);
        List<String> errList = new ArrayList<>();
        if (CollUtil.isNotEmpty(materialList)){
            List<WmsOutboundShippingReviewInfoToMaterial> successList = new ArrayList<>();
            materialList.forEach(material -> {
                if (material.getIsReview()){
                    successList.add(material);
                }else {
                    errList.add(material.getMaterialCode());
                }
            });
            if (CollUtil.isNotEmpty(successList)){
                this.updateBatchById(successList);
                changeInfoStatus(successList.get(0).getShippingReviewOrderNumber());
            }
        }
        if (CollUtil.isNotEmpty(errList)){
            return BaseResult.error(StringUtils.join(errList,StrUtil.C_COMMA)+" 已撤销，请勿重复撤销！");
        }
        return BaseResult.ok();
    }


    /**
     * @description 修改发运复核状态
     * @author  ciro
     * @date   2022/4/7 14:52
     * @param: reviewOrderNumber
     * @return: void
     **/
    private void changeInfoStatus(String reviewOrderNumber){
        LambdaQueryWrapper<WmsOutboundShippingReviewInfoToMaterial> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsOutboundShippingReviewInfoToMaterial::getIsDeleted,false);
        lambdaQueryWrapper.eq(WmsOutboundShippingReviewInfoToMaterial::getShippingReviewOrderNumber,reviewOrderNumber);
        List<WmsOutboundShippingReviewInfoToMaterial> materialList = this.list(lambdaQueryWrapper);
        if (CollUtil.isNotEmpty(materialList)){
            int unfinshCount = 0;
            int finishCount = 0;
            for (WmsOutboundShippingReviewInfoToMaterial material : materialList){
                if (material.getIsReview()){
                    finishCount+=1;
                }else {
                    unfinshCount += 1;
                }
            }
            String statusType = "";
            if (finishCount == 0){
                statusType = "1";
            }else if (unfinshCount == 0){
                statusType = "3";
            }else {
                statusType="2";
            }
            LambdaQueryWrapper<WmsOutboundShippingReviewInfo> lambdaQueryWrappers = new LambdaQueryWrapper<>();
            lambdaQueryWrappers.eq(WmsOutboundShippingReviewInfo::getIsDeleted,false);
            lambdaQueryWrappers.eq(WmsOutboundShippingReviewInfo::getShippingReviewOrderNumber,reviewOrderNumber);
            WmsOutboundShippingReviewInfo info = wmsOutboundShippingReviewInfoService.getOne(lambdaQueryWrappers);
            info.setOrderStatusType(statusType);
            info.setComfirmId(CurrentUserUtil.getUserId());
            info.setComfirmName(CurrentUserUtil.getUserRealName());
            info.setComfirmTime(new Date());
            wmsOutboundShippingReviewInfoService.updateById(info);
        }
    }

}
