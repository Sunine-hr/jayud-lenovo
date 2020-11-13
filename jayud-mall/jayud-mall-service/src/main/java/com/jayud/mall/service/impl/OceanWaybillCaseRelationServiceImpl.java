package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OceanWaybillCaseRelationMapper;
import com.jayud.mall.model.bo.OceanWaybillCaseRelationForm;
import com.jayud.mall.model.po.OceanWaybillCaseRelation;
import com.jayud.mall.service.IOceanWaybillCaseRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 运单对应箱号关联表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-11
 */
@Service
public class OceanWaybillCaseRelationServiceImpl extends ServiceImpl<OceanWaybillCaseRelationMapper, OceanWaybillCaseRelation> implements IOceanWaybillCaseRelationService {

    @Autowired
    OceanWaybillCaseRelationMapper oceanWaybillCaseRelationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveOceanWaybillCaseRelation(OceanWaybillCaseRelationForm form) {
        List<OceanWaybillCaseRelationForm> oceanWaybillCaseRelationFormList = form.getOceanWaybillCaseRelationFormList();
        Long oceanWaybillId = form.getOceanWaybillId();
        List<OceanWaybillCaseRelation> oceanWaybillCaseRelationList = ConvertUtil.convertList(oceanWaybillCaseRelationFormList, OceanWaybillCaseRelation.class);
        List<Long> ids = new ArrayList<>();//修改的ids
        oceanWaybillCaseRelationList.forEach(oceanWaybillCaseRelation -> {
            oceanWaybillCaseRelation.setOceanWaybillId(oceanWaybillId);
            //id不为null，进行修改保存，其他的删除
            if(oceanWaybillCaseRelation.getId() != null){
                ids.add(oceanWaybillCaseRelation.getId());
            }
        });
        //先删除,这里可能要做关联验证(运单、箱号)（判断，id是否存在，存在则修改，不存在则删除）
        QueryWrapper<OceanWaybillCaseRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        queryWrapper.eq("ocean_waybill_id", oceanWaybillId);
        List<OceanWaybillCaseRelation> dbList = this.list(queryWrapper);
        List<Long> dbIds = new ArrayList<>();//数据库的ids
        dbList.forEach(oceanWaybillCaseRelation -> {
            Long id = oceanWaybillCaseRelation.getId();
            dbIds.add(id);
        });

        //dbIds 关于 ids 的补集（或者差集） (由属于dbIds而不属于ids的元素组成的集合，称为dbIds关于ids的相对补集)
        dbIds.removeAll(ids); //修改时被删除的记录
        if(dbIds.size() == 0){
            dbIds.add(0L);
        }
        queryWrapper.in("id", dbIds);//查询需要删除的id
        this.remove(queryWrapper);
        //再保存
        this.saveOrUpdateBatch(oceanWaybillCaseRelationList);
        return CommonResult.success("保存-运单对应的箱号，成功！");
    }
}
