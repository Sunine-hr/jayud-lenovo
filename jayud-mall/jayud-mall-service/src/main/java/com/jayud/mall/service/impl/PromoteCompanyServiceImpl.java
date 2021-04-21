package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.PromoteCompanyMapper;
import com.jayud.mall.model.bo.QueryPromoteCompanyForm;
import com.jayud.mall.model.bo.SavePromoteCompanyForm;
import com.jayud.mall.model.po.PromoteCompany;
import com.jayud.mall.model.vo.PromoteCompanyVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IPromoteCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    BaseService baseService;

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
    @Transactional(rollbackFor = Exception.class)
    public void savePromoteCompany(SavePromoteCompanyForm form) {
        AuthUser user = baseService.getUser();
        Integer companyId = form.getCompanyId();
        if(ObjectUtil.isEmpty(companyId)){
            //新增
            PromoteCompany promoteCompany = ConvertUtil.convert(form, PromoteCompany.class);
            promoteCompany.setCreateId(user.getId());
            promoteCompany.setCreateName(user.getName());
            this.saveOrUpdate(promoteCompany);
        }else{
            PromoteCompanyVO promoteCompanyVO = promoteCompanyMapper.findPromoteCompanyByCompanyId(companyId);
            if(ObjectUtil.isEmpty(promoteCompanyVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "公司不存在");
            }
            //编辑
            PromoteCompany promoteCompany = ConvertUtil.convert(promoteCompanyVO, PromoteCompany.class);
            promoteCompany.setCompanyName(form.getCompanyName());
            promoteCompany.setContacts(form.getContacts());
            promoteCompany.setPhone(form.getPhone());
            promoteCompany.setCompanyAddress(form.getCompanyAddress());
            promoteCompany.setCreateId(user.getId());
            promoteCompany.setCreateName(user.getName());
            this.saveOrUpdate(promoteCompany);
        }
    }

    @Override
    public List<PromoteCompanyVO> findPromoteCompanyByParentId(Integer parentId) {
        return promoteCompanyMapper.findPromoteCompanyByParentId(parentId);
    }

    @Override
    public PromoteCompanyVO findPromoteCompanyByCompanyId(Integer companyId) {
        return promoteCompanyMapper.findPromoteCompanyByCompanyId(companyId);
    }

    @Override
    public List<PromoteCompanyVO> findPromoteCompanyParent() {
        return promoteCompanyMapper.findPromoteCompanyParent();
    }
}
