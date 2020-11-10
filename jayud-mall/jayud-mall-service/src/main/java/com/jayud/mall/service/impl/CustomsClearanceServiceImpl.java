package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CustomsClearanceMapper;
import com.jayud.mall.model.bo.CustomsClearanceForm;
import com.jayud.mall.model.bo.QueryCustomsClearanceForm;
import com.jayud.mall.model.po.CustomsClearance;
import com.jayud.mall.model.vo.CustomsClearanceVO;
import com.jayud.mall.service.ICustomsClearanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 清关资料表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomsClearanceServiceImpl extends ServiceImpl<CustomsClearanceMapper, CustomsClearance> implements ICustomsClearanceService {

    @Autowired
    CustomsClearanceMapper customsClearanceMapper;

    @Override
    public IPage<CustomsClearanceVO> findCustomsClearanceByPage(QueryCustomsClearanceForm form) {
        //定义分页参数
        Page<CustomsClearanceVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<CustomsClearanceVO> pageInfo = customsClearanceMapper.findCustomsClearanceByPage(page, form);
        return pageInfo;
    }

    @Override
    public CommonResult saveCustomsData(CustomsClearanceForm form) {
        CustomsClearance customsClearance = ConvertUtil.convert(form, CustomsClearance.class);
        this.saveOrUpdate(customsClearance);
        return CommonResult.success("保存清关，成功！");
    }
}
