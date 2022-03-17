package com.jayud.wms.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.wms.model.po.WmsMaterialBasicInfo;
import com.jayud.wms.mapper.WmsMaterialBasicInfoMapper;
import com.jayud.wms.service.*;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.common.constant.SysTips;
import com.jayud.common.BaseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 物料基本信息 服务实现类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Service
public class WmsMaterialBasicInfoServiceImpl extends ServiceImpl<WmsMaterialBasicInfoMapper, WmsMaterialBasicInfo> implements IWmsMaterialBasicInfoService {

    @Autowired
    private IWmsMaterialToLoactionRelationService wmsMaterialToLoactionRelationService;
    @Autowired
    private IWmsMaterialBarCodeService wmsMaterialBarCodeService;
    @Autowired
    private IWmsMaterialPackingSpecsService wmsMaterialPackingSpecsService;
    @Autowired
    private IWmsWaveToMaterialService wmsWaveToMaterialService;
    @Autowired
    private IWmsMaterialToAttributeService wmsMaterialToAttributeService;

    @Autowired
    private WmsMaterialBasicInfoMapper wmsMaterialBasicInfoMapper;

    @Override
    public IPage<WmsMaterialBasicInfoVO> selectPage(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO,
                                                    Integer currentPage,
                                                    Integer pageSize,
                                                    HttpServletRequest req) {

        Page<WmsMaterialBasicInfoVO> page = new Page<WmsMaterialBasicInfoVO>(currentPage, pageSize);
        IPage<WmsMaterialBasicInfoVO> pageList = wmsMaterialBasicInfoMapper.pageList(page, wmsMaterialBasicInfoVO);
        pageList.getRecords().forEach(info -> {
            info.setUnitList(wmsMaterialPackingSpecsService.getUnitByMaterialId(info.getId()));
        });
        return pageList;
    }

    @Override
    public List<WmsMaterialBasicInfoVO> selectList(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        return wmsMaterialBasicInfoMapper.list(wmsMaterialBasicInfoVO);
    }

    @Override
    public IPage<WmsMaterialBasicInfoVO> selectWmsMaterialBasicInfoVOListList(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO,
                                                                              Integer currentPage,
                                                                              Integer pageSize,
                                                                              HttpServletRequest req) {
        Page<WmsMaterialBasicInfoVO> page = new Page<WmsMaterialBasicInfoVO>(currentPage, pageSize);
        IPage<WmsMaterialBasicInfoVO> list = wmsMaterialBasicInfoMapper.pageList(page, wmsMaterialBasicInfoVO);

        list.getRecords().stream().forEach(wmbiv -> {
            WmsMaterialBasicInfoVO wmsMaterialBasicInfoFrom = new WmsMaterialBasicInfoVO();
            wmsMaterialBasicInfoFrom.setId(wmbiv.getId());
            WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO1 = wmsMaterialPackingSpecsService.selectByMaterialId(wmsMaterialBasicInfoFrom);
            wmbiv.setPackingList(wmsMaterialBasicInfoVO1.getPackingList());
            wmsMaterialBasicInfoVO1.getPackingList().stream().forEach(v -> {
                if (v.getSpecsType() == 1) {
                    wmbiv.setUnit(v.getUnit());
                }
            });
        });
        return list;
    }

    @Override
    public WmsMaterialBasicInfoVO selectById(long id) {
        WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO = new WmsMaterialBasicInfoVO();
        wmsMaterialBasicInfoVO.setId(id);
        List<WmsMaterialBasicInfoVO> list = wmsMaterialBasicInfoMapper.list(wmsMaterialBasicInfoVO);
        if (!list.isEmpty()) {
            wmsMaterialBasicInfoVO = list.get(0);
            wmsMaterialToLoactionRelationService.queryByMaterialId(wmsMaterialBasicInfoVO);
            wmsMaterialBarCodeService.selectByMaterialId(wmsMaterialBasicInfoVO);
            wmsMaterialPackingSpecsService.selectByMaterialId(wmsMaterialBasicInfoVO);
            wmsMaterialToAttributeService.selectByMaterialId(wmsMaterialBasicInfoVO);
            return wmsMaterialBasicInfoVO;
        }
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delByMaterialId(long id) {
        this.removeById(id);
        wmsMaterialToLoactionRelationService.delByMaterialId(id);
        wmsMaterialBarCodeService.delByMaterialId(id);
        wmsMaterialPackingSpecsService.delByMaterialId(id);
        wmsMaterialToAttributeService.delByMaterialId(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        if (ObjectUtil.isNull(wmsMaterialBasicInfoVO.getId())) {
            wmsMaterialBasicInfoVO.setIsAdd(true);
        } else {
            wmsMaterialBasicInfoVO.setIsAdd(false);
        }
        BaseResult result = checkData(wmsMaterialBasicInfoVO);
        if (!result.isSuccess()) {
            return result;
        }
        if (wmsMaterialBasicInfoVO.getIsAdd()) {
            this.save(wmsMaterialBasicInfoVO);
        } else {
            this.updateById(wmsMaterialBasicInfoVO);
        }
        wmsMaterialToLoactionRelationService.add(wmsMaterialBasicInfoVO);
        wmsMaterialBarCodeService.add(wmsMaterialBasicInfoVO);
        wmsMaterialPackingSpecsService.add(wmsMaterialBasicInfoVO);
        wmsMaterialToAttributeService.add(wmsMaterialBasicInfoVO);
        if (wmsMaterialBasicInfoVO.getIsAdd()) {
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }
        return BaseResult.ok(SysTips.EDIT_SUCCESS);
    }

    @Override
    public List<WmsMaterialBasicInfo> getByCondition(WmsMaterialBasicInfo condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }

    @Override
    public Map<String, BigDecimal> getOverchargePolicy(List<String> materialCodes) {
        QueryWrapper<WmsMaterialBasicInfo> condition = new QueryWrapper<>();
        condition.lambda().in(WmsMaterialBasicInfo::getMaterialCode, materialCodes)
                .eq(WmsMaterialBasicInfo::getIsAllowOvercharge, true);
        List<WmsMaterialBasicInfo> wmsMaterialBasicInfos = this.baseMapper.selectList(condition);
        Map<String, BigDecimal> map = wmsMaterialBasicInfos.stream().collect(Collectors.toMap(e -> e.getMaterialCode(), e -> e.getOverchargeRatio()));
        return map;
    }

    @Override
    public WmsMaterialBasicInfoVO selectByCode(String materialCode) {
        WmsMaterialBasicInfoVO basicInfoVO = new WmsMaterialBasicInfoVO();
        basicInfoVO.setMaterialCode(materialCode);
        List<WmsMaterialBasicInfoVO> list = selectList(basicInfoVO);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public BaseResult checkRecommend(String warehouseCode, String warehouseAreaCode) {
        WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO = new WmsMaterialBasicInfoVO();
        if (StringUtils.isNotBlank(warehouseCode)) {
            wmsMaterialBasicInfoVO.setRecommendedWarehouseCode(warehouseCode);
        }
        if (StringUtils.isNotBlank(warehouseAreaCode)) {
            wmsMaterialBasicInfoVO.setRecommendedWarehouseAreaCode(warehouseAreaCode);
        }
        List<WmsMaterialBasicInfoVO> basicInfoVOList = selectList(wmsMaterialBasicInfoVO);
        if (CollectionUtil.isNotEmpty(basicInfoVOList)) {
            return BaseResult.error("请取消" + basicInfoVOList.get(0).getMaterialName() + "(" + basicInfoVOList.get(0).getMaterialCode() + ")" + "物料上架推荐库区！");
        }
        return BaseResult.ok();
    }

    @Override
    public List<WmsMaterialBasicInfo> getByMaterialCodes(Set<String> materialCodes) {
        QueryWrapper<WmsMaterialBasicInfo> condition = new QueryWrapper<>();
        condition.lambda().in(WmsMaterialBasicInfo::getMaterialCode, materialCodes);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public WmsMaterialBasicInfo getByMaterialCode(String materialCode) {
        QueryWrapper<WmsMaterialBasicInfo> condition = new QueryWrapper<>();
        condition.lambda().in(WmsMaterialBasicInfo::getMaterialCode, materialCode);
        return this.getOne(condition);
    }

    /**
     * @description 检查数据是否正确
     * @author ciro
     * @date 2021/12/23 18:00
     * @param: wmsMaterialBasicInfoVO
     * @return: com.jyd.component.commons.result.Result
     **/
    private BaseResult checkData(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        WmsMaterialBasicInfoVO checkVO = new WmsMaterialBasicInfoVO();
        if (wmsMaterialBasicInfoVO.getIsAdd()) {
            checkVO.setMaterialCode(wmsMaterialBasicInfoVO.getMaterialCode());
            List<WmsMaterialBasicInfoVO> voList = wmsMaterialBasicInfoMapper.list(checkVO);
            if (!voList.isEmpty()) {
                return BaseResult.error(SysTips.MATERIAL_CODE_REPEAT);
            }
            checkVO.setMaterialCode(null);
            checkVO.setMaterialName(wmsMaterialBasicInfoVO.getMaterialName());
            List<WmsMaterialBasicInfoVO> voLists = wmsMaterialBasicInfoMapper.list(checkVO);
            if (!voLists.isEmpty()) {
                return BaseResult.error(SysTips.MATERIAL_NAME_REPEAT);
            }
        } else {
            WmsMaterialBasicInfo wmsMaterialBasicInfo = this.getById(wmsMaterialBasicInfoVO.getId());

            if (!wmsMaterialBasicInfo.getMaterialCode().equals(wmsMaterialBasicInfoVO.getMaterialCode())) {
                checkVO.setMaterialCode(wmsMaterialBasicInfoVO.getMaterialCode());
                List<WmsMaterialBasicInfoVO> voList = wmsMaterialBasicInfoMapper.list(checkVO);
                if (!voList.isEmpty()) {
                    return BaseResult.error(SysTips.MATERIAL_CODE_REPEAT);
                }
            }
            if (!wmsMaterialBasicInfo.getMaterialName().equals(wmsMaterialBasicInfoVO.getMaterialName())) {
                checkVO.setMaterialCode(null);
                checkVO.setMaterialName(wmsMaterialBasicInfoVO.getMaterialName());
                List<WmsMaterialBasicInfoVO> voLists = wmsMaterialBasicInfoMapper.list(checkVO);
                if (!voLists.isEmpty()) {
                    return BaseResult.error(SysTips.MATERIAL_NAME_REPEAT);
                }
            }
        }
        return BaseResult.ok();
    }


}
