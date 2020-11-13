package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.model.bo.OceanCounterForm;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.service.IOceanCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 提单对应货柜信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OceanCounterServiceImpl extends ServiceImpl<OceanCounterMapper, OceanCounter> implements IOceanCounterService {

    @Autowired
    OceanCounterMapper oceanCounterMapper;

    /**
     * <p>保存-提单对应的柜子</p>
     * 一个提单对应一个柜子，一个柜子对应N个运单，一个运单对应N个箱号。
     * @param form
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveOceanCounter(OceanCounterForm form) {
        List<OceanCounterForm> oceanCounterFormList = form.getOceanCounterFormList();
        Long obId = form.getObId();
        List<OceanCounter> oceanCounterList = ConvertUtil.convertList(oceanCounterFormList, OceanCounter.class);
        List<Long> ids = new ArrayList<>();
        oceanCounterList.forEach(oceanCounter -> {
            oceanCounter.setObId(obId);
            //id不为null，进行修改保存，其他的删除
            if(oceanCounter.getId() != null){
                ids.add(oceanCounter.getId());
            }
        });
        //先删除,这里可能要做关联验证(运单、箱号)（判断，id是否存在，存在则修改，不存在则删除）
        QueryWrapper<OceanCounter> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        queryWrapper.eq("ob_id", obId);
        List<OceanCounter> dbList = this.list(queryWrapper);
        List<Long> dbIds = new ArrayList<>();//数据库的ids
        dbList.forEach(oceanCounter -> {
            Long id = oceanCounter.getId();
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
        this.saveOrUpdateBatch(oceanCounterList);
        return CommonResult.success("保存-提单对应的柜子，成功！");
    }
}
