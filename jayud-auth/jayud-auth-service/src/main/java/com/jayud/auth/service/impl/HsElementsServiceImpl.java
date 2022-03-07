package com.jayud.auth.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.auth.mapper.HsElementsMapper;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.po.HsElements;
import com.jayud.auth.model.vo.CodeElementsVO;
import com.jayud.auth.service.IHsElementsService;
import org.springframework.stereotype.Service;


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
    public IPage<CodeElementsVO> findElements(QueryForm form) {

        Page<CodeElementsVO> page = new Page<>(form.getPageNum(),form.getPageSize() );
        return this.baseMapper.findElements(form,page);
    }
}
