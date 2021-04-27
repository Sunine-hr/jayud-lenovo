package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.CustomsInfoCaseForm;
import com.jayud.mall.model.po.CustomsInfoCase;
import com.jayud.mall.mapper.CustomsInfoCaseMapper;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;
import com.jayud.mall.model.vo.CustomsInfoCaseVO;
import com.jayud.mall.service.ICustomsInfoCaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报关文件箱号 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Service
public class CustomsInfoCaseServiceImpl extends ServiceImpl<CustomsInfoCaseMapper, CustomsInfoCase> implements ICustomsInfoCaseService {
    @Autowired
    ICustomsInfoCaseService customsInfoCaseService;

    @Override
    public IPage<CustomsInfoCaseVO> findCustomsInfoCasePage(CustomsInfoCaseForm form) {
        //定义分页参数
        Page<CustomsInfoCaseVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<CustomsInfoCaseVO> pageInfo = customsInfoCaseService.findCustomsInfoCasePage(form);
        return  pageInfo;
    }

    @Override
    public void insertCustomsInfoCase(CustomsInfoCaseForm customsInfoCase) {
        customsInfoCaseService.insertCustomsInfoCase(customsInfoCase);
    }

    @Override
    public void updateCustomsInfoCase(CustomsInfoCaseForm customsInfoCase) {
        customsInfoCaseService.updateCustomsInfoCase(customsInfoCase);
    }

    @Override
    public void deleteCustomsInfoCase(Long id) {
        customsInfoCaseService.deleteCustomsInfoCase(id);
    }
}
