package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OceanWaybillMapper;
import com.jayud.mall.model.bo.OceanWaybillForm;
import com.jayud.mall.model.po.OceanWaybill;
import com.jayud.mall.service.IOceanWaybillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 运单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-11
 */
@Service
public class OceanWaybillServiceImpl extends ServiceImpl<OceanWaybillMapper, OceanWaybill> implements IOceanWaybillService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveOceanWaybill(OceanWaybillForm form) {
        List<OceanWaybillForm> oceanWaybillFormList = form.getOceanWaybillFormList();//运单list
        Integer oceanCounterId = form.getOceanCounterId();//货柜信息id
        List<OceanWaybill> oceanWaybillList = ConvertUtil.convertList(oceanWaybillFormList, OceanWaybill.class);
        List<Long> ids = new ArrayList<>();//修改的id
        oceanWaybillList.forEach(oceanWaybill -> {
            oceanWaybill.setOceanCounterId(oceanCounterId);
            //id不为null，进行修改保存，其他的删除
            if(oceanWaybill.getId() != null){
                ids.add(oceanWaybill.getId());
            }
        });
        //先删除,这里可能要做关联验证(运单、箱号)（判断，id是否存在，存在则修改，不存在则删除）

        QueryWrapper<OceanWaybill> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id");
        queryWrapper.eq("ocean_counter_id", oceanCounterId);
        List<OceanWaybill> dbList = this.list(queryWrapper);
        List<Long> dbIds = new ArrayList<>();//数据库的ids
        dbList.forEach(oceanWaybill -> {
            Long id = oceanWaybill.getId();
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
        this.saveOrUpdateBatch(oceanWaybillList);
        return CommonResult.success("保存-柜子对应的运单，成功！");
    }
}
