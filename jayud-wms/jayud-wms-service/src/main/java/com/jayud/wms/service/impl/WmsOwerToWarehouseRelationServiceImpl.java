package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.WmsOwerToWarehouseRelation;
import com.jayud.wms.mapper.WmsOwerToWarehouseRelationMapper;
import com.jayud.wms.service.IWmsOwerToWarehouseRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 货主信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-13
 */
@Service
public class WmsOwerToWarehouseRelationServiceImpl extends ServiceImpl<WmsOwerToWarehouseRelationMapper, WmsOwerToWarehouseRelation> implements IWmsOwerToWarehouseRelationService {


    @Autowired
    private WmsOwerToWarehouseRelationMapper wmsOwerToWarehouseRelationMapper;

//    @Override
//    public IPage<WmsOwerInfo> selectPage(WmsOwerInfo wmsOwerInfo,
//                                        Integer pageNo,
//                                        Integer pageSize,
//                                        HttpServletRequest req){
//
//        Page<WmsOwerInfo> page=new Page<WmsOwerInfo>(pageNo,pageSize);
//        IPage<WmsOwerInfo> pageList= wmsOwerInfoMapper.pageList(page, wmsOwerInfo);
//        return pageList;
//    }
//
//    @Override
//    public List<WmsOwerInfo> selectList(WmsOwerInfo wmsOwerInfo){
//        return wmsOwerInfoMapper.list(wmsOwerInfo);
//    }
//
//    @Override
//    public boolean saveOrUpdateWmsCustomerInfo(WmsOwerInfoForm wmsOwerInfoForm) {
//
//       Boolean BaseResult =null;
//                WmsOwerInfo wmsOwerInfo = ConvertUtil.convert(wmsOwerInfoForm, WmsOwerInfo.class);
//        if(wmsOwerInfo.getId() != null){
//            wmsOwerInfo.setUpdateBy(CurrentUserUtil.getUsername());
//            wmsOwerInfo.setUpdateTime(new Date());
//            BaseResult = this.saveOrUpdate(wmsOwerInfo);
//        }else{
//            wmsOwerInfo.setCreateBy(CurrentUserUtil.getUsername());
//
//            wmsOwerInfo.setCreateTime(new Date());
//            BaseResult = this.saveOrUpdate(wmsOwerInfo);
//        }
//
//       Long  wmsOwerInfoId =wmsOwerInfo.getId();
//
//
//
//
//        if(result){
//            log.warn("新增或修改库区成功");
//            return true;
//        }
//        return false;
//    }

}
