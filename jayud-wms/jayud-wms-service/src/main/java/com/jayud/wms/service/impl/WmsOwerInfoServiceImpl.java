package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.bo.WmsOwerInfoForm;
import com.jayud.wms.model.po.WmsOwerInfo;
import com.jayud.wms.model.po.WmsOwerToWarehouseRelation;
import com.jayud.wms.mapper.WmsOwerInfoMapper;
import com.jayud.wms.mapper.WmsOwerToWarehouseRelationMapper;
import com.jayud.wms.service.IWmsOwerInfoService;
import com.jayud.wms.model.vo.WmsOwerInfoVO;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 货主信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-13
 */
@Service
public class WmsOwerInfoServiceImpl extends ServiceImpl<WmsOwerInfoMapper, WmsOwerInfo> implements IWmsOwerInfoService {


    @Autowired
    private WmsOwerInfoMapper wmsOwerInfoMapper;
    @Autowired
    private WmsOwerToWarehouseRelationMapper wmsOwerToWarehouseRelationMapper;

    @Override
    public IPage<WmsOwerInfo> selectPage(WmsOwerInfo wmsOwerInfo,
                                         Integer pageNo,
                                         Integer pageSize,
                                         HttpServletRequest req) {

        Page<WmsOwerInfo> page = new Page<WmsOwerInfo>(pageNo, pageSize);
        IPage<WmsOwerInfo> pageList = wmsOwerInfoMapper.pageList(page, wmsOwerInfo);
        return pageList;
    }

    @Override
    public List<WmsOwerInfo> selectList(WmsOwerInfo wmsOwerInfo) {
        return wmsOwerInfoMapper.list(wmsOwerInfo);
    }

    @Override
    public boolean saveOrUpdateWmsCustomerInfo(WmsOwerInfoForm wmsOwerInfoForm) {

        Boolean result = null;
        WmsOwerInfo wmsOwerInfo = ConvertUtil.convert(wmsOwerInfoForm, WmsOwerInfo.class);
        if (wmsOwerInfo.getId() != null) {

            // 把货主和仓库信息删除
            WmsOwerToWarehouseRelation wmsOwerToWarehouseRelation  =new WmsOwerToWarehouseRelation();
            wmsOwerToWarehouseRelation.setOwerId(wmsOwerInfo.getId());  //货主ID
            wmsOwerToWarehouseRelationMapper.updateWmsOwerToWarehouseRelationId(wmsOwerToWarehouseRelation);

            wmsOwerInfo.setUpdateBy(CurrentUserUtil.getUsername());
            wmsOwerInfo.setUpdateTime(new Date());
            result = this.saveOrUpdate(wmsOwerInfo);
            Long  id=wmsOwerInfo.getId();
            // 然后再添加
            for (int i = 0; i <wmsOwerInfoForm.getWarehouseList().size() ; i++) {
                WmsOwerToWarehouseRelation  wmsOwerToWarehouseRelation1  = new  WmsOwerToWarehouseRelation();
                wmsOwerToWarehouseRelation1.setOwerId(id);
                wmsOwerToWarehouseRelation1.setCreateBy(CurrentUserUtil.getUsername());
                wmsOwerToWarehouseRelation1.setCreateTime(new Date());
                wmsOwerToWarehouseRelation1.setWarehouseId(wmsOwerInfoForm.getWarehouseList().get(i));
                wmsOwerToWarehouseRelationMapper.insert(wmsOwerToWarehouseRelation1);
            }

        } else {
            wmsOwerInfo.setCreateBy(CurrentUserUtil.getUsername());
            wmsOwerInfo.setCreateTime(new Date());
//            wmsOwerInfo.setOwerCode(WmsOwerInfo.ENCODING_OWER+String.valueOf(System.currentTimeMillis())); //编码
            result = this.saveOrUpdate(wmsOwerInfo);
             Long  wId =wmsOwerInfo.getId();
            for (int i = 0; i <wmsOwerInfoForm.getWarehouseList().size() ; i++) {
                WmsOwerToWarehouseRelation  wmsOwerToWarehouseRelation1  = new  WmsOwerToWarehouseRelation();
                wmsOwerToWarehouseRelation1.setOwerId(wId);
                wmsOwerToWarehouseRelation1.setCreateBy(CurrentUserUtil.getUsername());
                wmsOwerToWarehouseRelation1.setCreateTime(new Date());
                wmsOwerToWarehouseRelation1.setWarehouseId(wmsOwerInfoForm.getWarehouseList().get(i));
                wmsOwerToWarehouseRelationMapper.insert(wmsOwerToWarehouseRelation1);
            }
        }

        if (result) {
            log.warn("新增或修改库区成功");
            return true;
        }
        return false;
    }

    //校验货主和 编号
    @Override
    public WmsOwerInfo getWmsCustomerInfoCodeName(String owerCode, String owerName) {

        WmsOwerInfo  wmsOwerInfo =wmsOwerInfoMapper.getWmsWmsOwerInfoCodeName(owerCode,owerName);
        return wmsOwerInfo;
    }

    @Override
    public WmsOwerInfoVO getWmsCustomerInfoVOCodeName(Long id) {
        WmsOwerInfoVO   wmsOwerInfoVO = wmsOwerInfoMapper.findWmsWmsOwerInfoCodeNameOne(id);
        return wmsOwerInfoVO;
    }

    //根据仓库ID查询货主信息
    @Override
    public List<WmsOwerInfo> selectWmsOwerInfoWarehouseIdList(WmsOwerInfoForm wmsOwerInfoForm) {

        List<WmsOwerInfo> wmsOwerInfos = wmsOwerInfoMapper.selectWmsOwerInfoWarehouseIdList(wmsOwerInfoForm);
        return wmsOwerInfos;
    }

    //导出部分
    @Override
    public List<LinkedHashMap<String, Object>> queryWmsOwerInfoForExcel(WmsOwerInfo wmsOwerInfo,
                                                                        Integer pageNo,
                                                                        Integer pageSize,
                                                                        HttpServletRequest req) {
//        Page<WmsOwerInfo> page = new Page<WmsOwerInfo>(pageNo, pageSize);
        return  wmsOwerInfoMapper.queryWmsOwerInfoForExcel(wmsOwerInfo);
    }

    @Override
    public WmsOwerInfo selectByOwerMsg(Long owerId, String owerCode) {
        if (owerId == null && StringUtils.isBlank(owerCode)){
            return null;
        }
        LambdaQueryWrapper<WmsOwerInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (owerId != null){
            lambdaQueryWrapper.eq(WmsOwerInfo::getId,owerId);
        }
        if (StringUtils.isNotBlank(owerCode)){
            lambdaQueryWrapper.eq(WmsOwerInfo::getOwerCode,owerCode);
        }
        lambdaQueryWrapper.eq(WmsOwerInfo::getIsDeleted,false);
        return this.getOne(lambdaQueryWrapper);
    }

}
