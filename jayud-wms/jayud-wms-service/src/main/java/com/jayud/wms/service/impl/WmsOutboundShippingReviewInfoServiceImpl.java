package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.wms.mapper.WmsOutboundShippingReviewInfoToMaterialMapper;
import com.jayud.wms.model.bo.DeleteForm;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfoToMaterial;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoVO;
import com.jayud.wms.model.vo.WmsOutboundShippingReviewInfoVO;
import com.jayud.wms.service.IWmsOutboundShippingReviewInfoToMaterialService;
import com.jayud.wms.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.model.po.WmsOutboundShippingReviewInfo;
import com.jayud.wms.mapper.WmsOutboundShippingReviewInfoMapper;
import com.jayud.wms.service.IWmsOutboundShippingReviewInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * wms-出库发运复核 服务实现类
 *
 * @author jayud
 * @since 2022-04-07
 */
@Slf4j
@Service
public class WmsOutboundShippingReviewInfoServiceImpl extends ServiceImpl<WmsOutboundShippingReviewInfoMapper, WmsOutboundShippingReviewInfo> implements IWmsOutboundShippingReviewInfoService {

    @Autowired
    private IWmsOutboundShippingReviewInfoToMaterialService wmsOutboundShippingReviewInfoToMaterialService;
    @Autowired
    private WmsOutboundShippingReviewInfoMapper wmsOutboundShippingReviewInfoMapper;
    @Autowired
    private WmsOutboundShippingReviewInfoToMaterialMapper wmsOutboundShippingReviewInfoToMaterialMapper;
    @Autowired
    private CodeUtils codeUtils;

    @Override
    public IPage<WmsOutboundShippingReviewInfo> selectPage(WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<WmsOutboundShippingReviewInfo> page=new Page<WmsOutboundShippingReviewInfo>(currentPage,pageSize);
        IPage<WmsOutboundShippingReviewInfo> pageList= wmsOutboundShippingReviewInfoMapper.pageList(page, wmsOutboundShippingReviewInfo);
        return pageList;
    }

    @Override
    public List<WmsOutboundShippingReviewInfo> selectList(WmsOutboundShippingReviewInfo wmsOutboundShippingReviewInfo){
        return wmsOutboundShippingReviewInfoMapper.list(wmsOutboundShippingReviewInfo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        wmsOutboundShippingReviewInfoMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        wmsOutboundShippingReviewInfoMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOutboundShippingReviewInfoForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryWmsOutboundShippingReviewInfoForExcel(paramMap);
    }

    @Override
    public BaseResult changeToReview(WmsOutboundOrderInfoVO wmsOutboundOrderInfoVO) {

        WmsOutboundShippingReviewInfo info = new WmsOutboundShippingReviewInfo();
        BeanUtils.copyProperties(wmsOutboundOrderInfoVO,info);
        info.clearCreate();
        info.clearUpdate();
        info.setId(null);
        info.setOrderSourceType(null);
        info.setShippingReviewOrderNumber(codeUtils.getCodeByRule(CodeConStants.SHIPPING_REVIEW));
        this.save(info);
        List<WmsOutboundShippingReviewInfoToMaterial> materialList = new ArrayList<>();
        wmsOutboundOrderInfoVO.getThisMaterialList().forEach(material -> {
            WmsOutboundShippingReviewInfoToMaterial materials = new WmsOutboundShippingReviewInfoToMaterial();
            BeanUtils.copyProperties(material,materials);
            materials.setId(null);
            materials.clearCreate();
            materials.clearUpdate();
            materials.setShippingReviewOrderNumber(info.getShippingReviewOrderNumber());
            materials.setOrderMaterialId(materials.getId());
            materials.setAccount(material.getRequirementAccount());
            materials.setRemark(null);
            materialList.add(materials);
        });
        if (CollUtil.isNotEmpty(materialList)){
            wmsOutboundShippingReviewInfoToMaterialService.saveBatch(materialList);
        }
        return null;
    }

    @Override
    public WmsOutboundShippingReviewInfoVO queryByCode(WmsOutboundShippingReviewInfo info) {
        WmsOutboundShippingReviewInfoVO vo = new WmsOutboundShippingReviewInfoVO();
        List<WmsOutboundShippingReviewInfo> infoList = selectList(info);
        if (CollUtil.isNotEmpty(infoList)){
            BeanUtils.copyProperties(infoList.get(0),vo);
            WmsOutboundShippingReviewInfoToMaterial material = new WmsOutboundShippingReviewInfoToMaterial();
            material.setShippingReviewOrderNumber(infoList.get(0).getShippingReviewOrderNumber());
            List<WmsOutboundShippingReviewInfoToMaterial> materialList = wmsOutboundShippingReviewInfoToMaterialService.selectList(material);
            if (CollUtil.isNotEmpty(materialList)){
                vo.setMaterialList(materialList);
            }else {
                vo.setMaterialList(new ArrayList<>());
            }
        }
        return vo;
    }

    @Override
    public void delByOrderNumbers(DeleteForm deleteForm) {
        this.baseMapper.delByOrderNumbers(deleteForm.getNumberList(),CurrentUserUtil.getUsername());
        wmsOutboundShippingReviewInfoToMaterialMapper.delByOrderNumbers(deleteForm.getNumberList(),CurrentUserUtil.getUsername());
    }


}
