package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.model.bo.OceanCounterWaybillRelationForm;
import com.jayud.mall.model.po.OceanCounterWaybillRelation;
import com.jayud.mall.mapper.OceanCounterWaybillRelationMapper;
import com.jayud.mall.model.vo.OceanCounterWaybillRelationVO;
import com.jayud.mall.service.IOceanCounterWaybillRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 货柜对应运单(订单)关联表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-15
 */
@Service
public class OceanCounterWaybillRelationServiceImpl extends ServiceImpl<OceanCounterWaybillRelationMapper,
        OceanCounterWaybillRelation> implements IOceanCounterWaybillRelationService {

    @Autowired
    OceanCounterWaybillRelationMapper oceanCounterWaybillRelationMapper;

    @Override
    public List<OceanCounterWaybillRelationVO> saveOceanCounterWaybillRelation(List<OceanCounterWaybillRelationForm> forms) {

        Long oceanCounterId = forms.get(0).getOceanCounterId();
        QueryWrapper<OceanCounterWaybillRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ocean_counter_id", oceanCounterId);
        this.remove(queryWrapper);

        List<OceanCounterWaybillRelation> oceanCounterWaybillRelations =
                ConvertUtil.convertList(forms, OceanCounterWaybillRelation.class);
        //保存
        this.saveOrUpdateBatch(oceanCounterWaybillRelations);
        List<OceanCounterWaybillRelationVO> oceanCounterWaybillRelationVOS =
                ConvertUtil.convertList(oceanCounterWaybillRelations, OceanCounterWaybillRelationVO.class);
        return oceanCounterWaybillRelationVOS;
    }
}
