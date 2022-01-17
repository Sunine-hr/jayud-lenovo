package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.mapper.LegalEntityMapper;
import com.jayud.scm.model.bo.QueryLegalEntityForm;
import com.jayud.scm.model.po.LegalEntity;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.LegalEntityVO;
import com.jayud.scm.service.ILegalEntityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LegalEntityServiceImpl extends ServiceImpl<LegalEntityMapper, LegalEntity> implements ILegalEntityService {

    @Override
    public IPage<LegalEntityVO> findLegalEntityByPage(QueryLegalEntityForm form) {
        //定义分页参数
        Page<SystemUser> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("le.id"));
        IPage<LegalEntityVO> pageInfo = this.baseMapper.findLegalEntityByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<LegalEntityVO> findLegalEntity(Map<String, String> param) {
        QueryWrapper queryWrapper = new QueryWrapper();
        for (String key : param.keySet()) {
            String value = String.valueOf(param.get(key));
            queryWrapper.eq(key, value);
        }
        return ConvertUtil.convertList(baseMapper.selectList(queryWrapper), LegalEntityVO.class);
    }

    @Override
    public LegalEntity getLegalEntityByLegalName(String name, Integer auditStatus) {
        QueryWrapper<LegalEntity> condition = new QueryWrapper();
        condition.lambda().eq(LegalEntity::getLegalName, name);
        if (auditStatus != null) {
            condition.lambda().eq(LegalEntity::getAuditStatus, auditStatus);
        }
        return baseMapper.selectOne(condition);
    }


    /**
     * 根据法人id集合配对code
     */
    @Override
    public Boolean matchingCodeByLegalIds(List<Long> legalEntityIds, String code) {
        QueryWrapper<LegalEntity> condition = new QueryWrapper<>();
        condition.lambda().in(LegalEntity::getId, legalEntityIds)
                .eq(LegalEntity::getLegalCode, code);
        return this.count(condition) > 0;
    }

    @Override
    public List<LegalEntity> getByCondition(LegalEntity legalEntity) {
        return this.baseMapper.selectList(new QueryWrapper<>(legalEntity));
    }


}
