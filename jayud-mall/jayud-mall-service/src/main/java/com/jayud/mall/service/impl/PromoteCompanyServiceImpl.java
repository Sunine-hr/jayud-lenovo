package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.PromoteCompanyMapper;
import com.jayud.mall.model.bo.QueryPromoteCompanyForm;
import com.jayud.mall.model.bo.SavePromoteCompanyForm;
import com.jayud.mall.model.po.PromoteCompany;
import com.jayud.mall.model.vo.PromoteCompanyVO;
import com.jayud.mall.service.IPromoteCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 推广公司表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-20
 */
@Service
public class PromoteCompanyServiceImpl extends ServiceImpl<PromoteCompanyMapper, PromoteCompany> implements IPromoteCompanyService {

    @Autowired
    PromoteCompanyMapper promoteCompanyMapper;

    @Override
    public IPage<PromoteCompanyVO> findPromoteCompanyByPage(QueryPromoteCompanyForm form) {
        //定义分页参数
        Page<PromoteCompanyVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.company_id"));
        IPage<PromoteCompanyVO> pageInfo = promoteCompanyMapper.findPromoteCompanyByPage(page, form);
        return pageInfo;
    }

    @Override
    public void savePromoteCompany(SavePromoteCompanyForm form) {

    }

    @Override
    public List<PromoteCompanyVO> findPromoteCompanyByParentId(Integer parentId) {
        return promoteCompanyMapper.findPromoteCompanyByParentId(parentId);
    }
}
