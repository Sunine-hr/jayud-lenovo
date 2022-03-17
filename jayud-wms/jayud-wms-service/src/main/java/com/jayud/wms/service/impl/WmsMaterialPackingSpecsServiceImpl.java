package com.jayud.wms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.WmsMaterialPackingSpecsMapper;
import com.jayud.wms.model.enums.PackingEnum;
import com.jayud.wms.model.po.WmsMaterialBasicInfo;
import com.jayud.wms.model.po.WmsMaterialPackingSpecs;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.wms.service.IWmsMaterialBasicInfoService;
import com.jayud.wms.service.IWmsMaterialPackingSpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料-包装规格 服务实现类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Service
public class WmsMaterialPackingSpecsServiceImpl extends ServiceImpl<WmsMaterialPackingSpecsMapper, WmsMaterialPackingSpecs> implements IWmsMaterialPackingSpecsService {


    @Autowired
    private WmsMaterialPackingSpecsMapper wmsMaterialPackingSpecsMapper;
    @Autowired
    private IWmsMaterialBasicInfoService wmsMaterialBasicInfoService;

    @Override
    public IPage<WmsMaterialPackingSpecs> selectPage(WmsMaterialPackingSpecs wmsMaterialPackingSpecs,
                                                     HttpServletRequest req) {

        Page<WmsMaterialPackingSpecs> page = new Page<WmsMaterialPackingSpecs>(wmsMaterialPackingSpecs.getCurrentPage(), wmsMaterialPackingSpecs.getPageSize());
        IPage<WmsMaterialPackingSpecs> pageList = wmsMaterialPackingSpecsMapper.pageList(page, wmsMaterialPackingSpecs);
        return pageList;
    }

    @Override
    public List<WmsMaterialPackingSpecs> selectList(WmsMaterialPackingSpecs wmsMaterialPackingSpecs) {
        return wmsMaterialPackingSpecsMapper.list(wmsMaterialPackingSpecs);
    }

    @Override
    public WmsMaterialBasicInfoVO selectByMaterialId(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        WmsMaterialPackingSpecs wmsMaterialPackingSpecs = new WmsMaterialPackingSpecs();
        if (wmsMaterialBasicInfoVO.getId() == null) {
            WmsMaterialBasicInfo tmp = this.wmsMaterialBasicInfoService.getOne(new QueryWrapper<>(new WmsMaterialBasicInfo().setMaterialCode(wmsMaterialBasicInfoVO.getMaterialCode())));
            wmsMaterialPackingSpecs.setMaterialBasicInfoId(tmp.getId());
        } else {
            wmsMaterialPackingSpecs.setMaterialBasicInfoId(wmsMaterialBasicInfoVO.getId());
        }

        List<WmsMaterialPackingSpecs> list = wmsMaterialPackingSpecsMapper.list(wmsMaterialPackingSpecs);
        if (list == null) {
            list = initPacking();
        }
        wmsMaterialBasicInfoVO.setPackingList(list);
        return wmsMaterialBasicInfoVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO) {
        if (wmsMaterialBasicInfoVO.getIsAdd()) {
            setMaterialId(wmsMaterialBasicInfoVO.getPackingList(), wmsMaterialBasicInfoVO.getId());
            this.saveBatch(wmsMaterialBasicInfoVO.getPackingList());
        } else {
            this.updateBatchById(wmsMaterialBasicInfoVO.getPackingList());
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delByMaterialId(long materialId) {
        wmsMaterialPackingSpecsMapper.delByMaterialId(materialId, CurrentUserUtil.getUsername());
    }

    @Override
    public List<WmsMaterialPackingSpecs> getInitList() {
        return initPacking();
    }

    @Override
    public List<WmsMaterialPackingSpecs> getByCondition(WmsMaterialPackingSpecs condition) {
        return this.baseMapper.selectList(new QueryWrapper<>(condition));
    }

    @Override
    public List<String> getUnitByMaterialId(long materialId) {
        List<WmsMaterialPackingSpecs> list = selectWrapperByMaterialId(materialId);
        List<String> unitList = list.stream().map(x -> x.getUnit()).collect(Collectors.toList());
        return unitList;
    }


    /**
     * @description 根据物料id查询包装规格
     * @author ciro
     * @date 2021/12/16 16:42
     * @param: materialId
     * @return: java.util.List<com.jayud.model.po.WmsMaterialPackingSpecs>
     **/
    private List<WmsMaterialPackingSpecs> selectWrapperByMaterialId(long materialId) {
        LambdaQueryWrapper<WmsMaterialPackingSpecs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WmsMaterialPackingSpecs::getMaterialBasicInfoId, materialId);
        lambdaQueryWrapper.eq(WmsMaterialPackingSpecs::getIsDeleted, false);
        List<WmsMaterialPackingSpecs> list = wmsMaterialPackingSpecsMapper.selectList(lambdaQueryWrapper);
        return list;
    }


    /**
     * @description
     * @author ciro
     * @date 2021/12/16 16:35
     * @param:
     * @return: java.util.List<com.jayud.model.po.WmsMaterialPackingSpecs>
     **/
    private List<WmsMaterialPackingSpecs> initPacking() {
        List<WmsMaterialPackingSpecs> list = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            WmsMaterialPackingSpecs wmsMaterialPackingSpecs = new WmsMaterialPackingSpecs();
            if (i == 1) {
                wmsMaterialPackingSpecs.setAccount(1);
                wmsMaterialPackingSpecs.setSpecsType_text(PackingEnum.MAIN_PACKING.getSpecsTypeName());
            } else {
                wmsMaterialPackingSpecs.setAccount(0);
                if (i == 2) {
                    wmsMaterialPackingSpecs.setSpecsType_text(PackingEnum.SMALL_PACKING.getSpecsTypeName());
                } else if (i == 3) {
                    wmsMaterialPackingSpecs.setSpecsType_text(PackingEnum.MEDIUM_PACKING.getSpecsTypeName());
                } else if (i == 4) {
                    wmsMaterialPackingSpecs.setSpecsType_text(PackingEnum.LARGE_PACKING.getSpecsTypeName());
                }
            }
            wmsMaterialPackingSpecs.setSpecsType(i);
            list.add(wmsMaterialPackingSpecs);
        }

        return list;
    }

    /**
     * @description list赋值物料id
     * @author ciro
     * @date 2021/12/17 12:41
     * @param: list
     * @param: materialId
     * @return: java.util.List<com.jayud.model.po.WmsMaterialPackingSpecs>
     **/
    private List<WmsMaterialPackingSpecs> setMaterialId(List<WmsMaterialPackingSpecs> list, long materialId) {
        list.forEach(wmsMaterialPackingSpecs -> {
            wmsMaterialPackingSpecs.setMaterialBasicInfoId(materialId);
        });
        return list;
    }

}
