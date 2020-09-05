package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.model.bo.QueryLegalEntityForm;
import com.jayud.model.po.LegalEntity;
import com.jayud.model.po.SystemUser;
import com.jayud.model.vo.LegalEntityVO;
import com.jayud.oauth.mapper.LegalEntityMapper;
import com.jayud.oauth.service.ILegalEntityService;
import org.springframework.stereotype.Service;

@Service
public class LegalEntityServiceImpl extends ServiceImpl<LegalEntityMapper, LegalEntity> implements ILegalEntityService {

    @Override
    public IPage<LegalEntityVO> findLegalEntityByPage(QueryLegalEntityForm form) {
        //定义分页参数
        Page<SystemUser> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("su.id"));
        IPage<LegalEntityVO> pageInfo = this.baseMapper.findLegalEntityByPage(page, form);
        return pageInfo;
    }


}
