package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BTaxClassCode;
import com.jayud.scm.mapper.BTaxClassCodeMapper;
import com.jayud.scm.model.vo.BTaxClassCodeVO;
import com.jayud.scm.model.vo.HsCodeFormVO;
import com.jayud.scm.service.IBTaxClassCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 税务分类表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-29
 */
@Service
public class BTaxClassCodeServiceImpl extends ServiceImpl<BTaxClassCodeMapper, BTaxClassCode> implements IBTaxClassCodeService {

    @Override
    public IPage<BTaxClassCodeVO> findByPage(QueryForm form) {
        Page<BTaxClassCodeVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }
}
