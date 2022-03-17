package com.jayud.wms.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.common.utils.ListUtils;
import com.jayud.wms.mapper.WmsMaterialToLoactionRelationMapper;
import com.jayud.wms.model.po.WmsMaterialBasicInfo;
import com.jayud.wms.model.po.WmsMaterialToLoactionRelation;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.wms.service.IWmsMaterialBasicInfoService;
import com.jayud.wms.service.IWmsMaterialToLoactionRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物料和库位关系 服务实现类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Service
public class WmsMaterialToLoactionRelationServiceImpl extends ServiceImpl<WmsMaterialToLoactionRelationMapper, WmsMaterialToLoactionRelation> implements IWmsMaterialToLoactionRelationService {

    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;

    @Autowired
    private WmsMaterialToLoactionRelationMapper wmsMaterialToLoactionRelationMapper;

    @Override
    public IPage<WmsMaterialToLoactionRelation> selectPage(WmsMaterialToLoactionRelation wmsMaterialToLoactionRelation,
                                                           HttpServletRequest req) {

        Page<WmsMaterialToLoactionRelation> page = new Page<WmsMaterialToLoactionRelation>(wmsMaterialToLoactionRelation.getCurrentPage(), wmsMaterialToLoactionRelation.getPageSize());
        IPage<WmsMaterialToLoactionRelation> pageList = wmsMaterialToLoactionRelationMapper.pageList(page, wmsMaterialToLoactionRelation);
        return pageList;
    }

    @Override
    public List<WmsMaterialToLoactionRelation> selectList(WmsMaterialToLoactionRelation wmsMaterialToLoactionRelation) {
        return wmsMaterialToLoactionRelationMapper.list(wmsMaterialToLoactionRelation);
    }

    @Override
    public WmsMaterialBasicInfoVO queryByMaterialId(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        WmsMaterialToLoactionRelation relation = new WmsMaterialToLoactionRelation();
        relation.setMaterialBasicInfoId(wmsMaterialBasicInfoVO.getId());
        List<WmsMaterialToLoactionRelation> relationLists = selectList(relation);
        wmsMaterialBasicInfoVO.setThisLoactionSelect(relationLists);
        wmsMaterialBasicInfoVO.setLastLoactionSelect(relationLists);
        return wmsMaterialBasicInfoVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        WmsMaterialBasicInfo wmsMaterialBasicInfo = wmsMaterialBasicInfoService.getById(wmsMaterialBasicInfoVO.getId());
        if (wmsMaterialBasicInfo == null) {
            return BaseResult.error(SysTips.MATERIAL_NOT_EXIT);
        }
        Map<Long,WmsMaterialToLoactionRelation> relationMap = new HashMap<>(20);
        wmsMaterialBasicInfoVO.getThisLoactionSelect().forEach(x->{
            relationMap.put(x.getLocaltionId(),x);
        });
        List<String> thisSelectList = getLocationId(wmsMaterialBasicInfoVO.getThisLoactionSelect(), false);
        List<String> lastSelectList = getLocationId(wmsMaterialBasicInfoVO.getLastLoactionSelect(), true);
        List<String> addLocationIdList = ListUtils.getDiff(lastSelectList, thisSelectList);
        List<String> delLocationIdList = ListUtils.getDiff(thisSelectList, lastSelectList);
        List<WmsMaterialToLoactionRelation> addList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(addLocationIdList)) {
            addLocationIdList.forEach(id -> {
                WmsMaterialToLoactionRelation wmsMaterialToLoactionRelations = relationMap.get(Long.parseLong(id));
                WmsMaterialToLoactionRelation wmsMaterialToLoactionRelation = new WmsMaterialToLoactionRelation();
                BeanUtils.copyProperties(wmsMaterialToLoactionRelations,wmsMaterialToLoactionRelation);
                wmsMaterialToLoactionRelation.setMaterialBasicInfoId(wmsMaterialBasicInfoVO.getId());
                wmsMaterialToLoactionRelation.setLocaltionId(Long.parseLong(id));
                addList.add(wmsMaterialToLoactionRelation);
            });
            this.saveBatch(addList);
        }
        if (CollectionUtil.isNotEmpty(delLocationIdList)) {
            wmsMaterialToLoactionRelationMapper.delByLocationIdAndMaterialId(wmsMaterialBasicInfoVO.getId(), delLocationIdList, CurrentUserUtil.getUsername());
        }
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByMaterialId(long materialId) {
        wmsMaterialToLoactionRelationMapper.delByLocationIdAndMaterialId(materialId, null, CurrentUserUtil.getUsername());
    }

    @Override
    public BaseResult checkRecommend(String warehouseCode, String warehouseAreaCode, String locationCode) {
        WmsMaterialToLoactionRelation relation = new WmsMaterialToLoactionRelation();
        if (StringUtils.isNotBlank(warehouseCode)) {
            relation.setWarehouseCode(warehouseCode);
        }
        if (StringUtils.isNotBlank(warehouseAreaCode)) {
            relation.setWarehouseAreaCode(warehouseAreaCode);
        }
        if (StringUtils.isNotBlank(locationCode)) {
            relation.setLocaltionCode(locationCode);
        }
        List<WmsMaterialToLoactionRelation> relationList = selectList(relation);
        if (CollectionUtil.isNotEmpty(relationList)) {
            WmsMaterialBasicInfo info = wmsMaterialBasicInfoService.getById(relationList.get(0).getMaterialBasicInfoId());
            return BaseResult.error("请取消" + info.getMaterialName() + "(" + info.getMaterialCode() + ")" + "物料上架推荐库位！");
        }
        return BaseResult.ok();
    }

    @Override
    public List<WmsMaterialToLoactionRelation> getByCondition(WmsMaterialToLoactionRelation condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }


    /**
     * @description 获取库位id
     * @author ciro
     * @date 2021/12/16 14:09
     * @param: list
     * @return: java.util.List<java.lang.String>
     **/
    private List<String> getLocationId(List<WmsMaterialToLoactionRelation> list, boolean isLast) {
        List<String> localIdList = new ArrayList<>();
        if (list == null) {
            return localIdList;
        }
        list.forEach(vo -> {
            localIdList.add(String.valueOf(vo.getLocaltionId()));
        });
        return localIdList;
    }


}
