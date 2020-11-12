package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.model.bo.OceanCounterForm;
import com.jayud.mall.model.po.OceanCounter;
import com.jayud.mall.mapper.OceanCounterMapper;
import com.jayud.mall.service.IOceanCounterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        queryWrapper.notIn("id", ids);//存在的id，则不删除，使用not in
        this.remove(queryWrapper);
        //再保存
        this.saveOrUpdateBatch(oceanCounterList);
        return CommonResult.success("保存-提单对应的柜子，成功！");
    }
}
