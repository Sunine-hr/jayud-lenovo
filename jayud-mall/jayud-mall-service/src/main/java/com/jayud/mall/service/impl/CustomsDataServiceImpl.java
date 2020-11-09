package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.CustomsDataMapper;
import com.jayud.mall.model.bo.QueryCustomsDataForm;
import com.jayud.mall.model.po.CustomsData;
import com.jayud.mall.model.vo.CustomsDataVO;
import com.jayud.mall.service.ICustomsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报关资料表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Service
public class CustomsDataServiceImpl extends ServiceImpl<CustomsDataMapper, CustomsData> implements ICustomsDataService {

    @Autowired
    CustomsDataMapper customsDataMapper;

    @Override
    public IPage<CustomsDataVO> findCustomsDataByPage(QueryCustomsDataForm form) {
        //定义分页参数
        Page<CustomsDataVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<CustomsDataVO> pageInfo = customsDataMapper.findCustomsDataByPage(page, form);
        return pageInfo;
    }
}
