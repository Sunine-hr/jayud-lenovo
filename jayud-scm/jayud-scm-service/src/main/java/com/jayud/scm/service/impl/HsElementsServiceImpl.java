package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.BasePageForm;
import com.jayud.scm.model.po.HsElements;
import com.jayud.scm.mapper.HsElementsMapper;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.CodeElementsVO;
import com.jayud.scm.service.IHsElementsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 申报要素表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Service
public class HsElementsServiceImpl extends ServiceImpl<HsElementsMapper, HsElements> implements IHsElementsService {

    @Override
    public IPage<CodeElementsVO> findElements(String name) {
        BasePageForm basePageForm = new BasePageForm();
        Page<CodeElementsVO> page = new Page<>(basePageForm.getPageNum(),basePageForm.getPageSize() );
        return this.baseMapper.findElements(name,page);
    }
}
