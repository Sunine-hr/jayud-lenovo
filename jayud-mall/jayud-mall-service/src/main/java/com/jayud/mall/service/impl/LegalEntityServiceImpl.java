package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.LegalEntityMapper;
import com.jayud.mall.model.bo.LegalEntityAuditForm;
import com.jayud.mall.model.bo.LegalEntityForm;
import com.jayud.mall.model.bo.QueryLegalEntityForm;
import com.jayud.mall.model.po.LegalEntity;
import com.jayud.mall.model.vo.LegalEntityVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ILegalEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 法人主体 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-11
 */
@Service
public class LegalEntityServiceImpl extends ServiceImpl<LegalEntityMapper, LegalEntity> implements ILegalEntityService {

    @Autowired
    LegalEntityMapper legalEntityMapper;

    @Autowired
    BaseService baseService;

    @Override
    public IPage<LegalEntityVO> findLegalEntityPage(QueryLegalEntityForm form) {
        //定义分页参数
        Page<LegalEntityVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.id"));
        IPage<LegalEntityVO> pageInfo = legalEntityMapper.findLegalEntityPage(page, form);
        return pageInfo;
    }

    @Override
    public LegalEntityVO findLegalEntityById(Long id) {
        LegalEntityVO legalEntityVO = legalEntityMapper.findLegalEntityById(id);
        return legalEntityVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLegalEntity(LegalEntityForm form) {
        AuthUser user = baseService.getUser();
        if(ObjectUtil.isEmpty(user)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        Long id = form.getId();
        LegalEntity legalEntity = new LegalEntity();
        if(ObjectUtil.isEmpty(id)){
            //新增
            legalEntity = ConvertUtil.convert(form, LegalEntity.class);
            legalEntity.setCreatedUser(user.getName());
            legalEntity.setCreatedTime(LocalDateTime.now());
        }else{
            //修改
            LegalEntityVO legalEntityVO = legalEntityMapper.findLegalEntityById(id);
            if(ObjectUtil.isEmpty(legalEntityVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "法人主体不存在");
            }
            legalEntity = ConvertUtil.convert(form, LegalEntity.class);
            legalEntity.setUpdatedUser(user.getName());
            legalEntity.setUpdatedTime(LocalDateTime.now());
        }
        this.saveOrUpdate(legalEntity);
    }

    @Override
    public void auditLegalEntity(LegalEntityAuditForm form) {
        Long id = form.getId();
        AuthUser user = baseService.getUser();
        if(ObjectUtil.isEmpty(user)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录");
        }
        LegalEntityVO legalEntityVO = legalEntityMapper.findLegalEntityById(id);
        if(ObjectUtil.isEmpty(legalEntityVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "法人主体不存在");
        }
        LegalEntity legalEntity = ConvertUtil.convert(legalEntityVO, LegalEntity.class);
        legalEntity.setAuditComment(form.getAuditComment());
        legalEntity.setAuditStatus(form.getAuditStatus());
        legalEntity.setUpdatedUser(user.getName());
        legalEntity.setUpdatedTime(LocalDateTime.now());
        this.saveOrUpdate(legalEntity);
    }

    @Override
    public List<LegalEntityVO> findLegalEntity() {
        List<LegalEntityVO> legalEntityList = legalEntityMapper.findLegalEntity();
        return legalEntityList;
    }
}