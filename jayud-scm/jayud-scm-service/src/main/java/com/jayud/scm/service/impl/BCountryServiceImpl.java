package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.BasePageForm;
import com.jayud.scm.model.bo.QueryCountryForm;
import com.jayud.scm.model.po.BCountry;
import com.jayud.scm.mapper.BCountryMapper;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.HsCodeFormVO;
import com.jayud.scm.service.IBCountryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 国家表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Service
public class BCountryServiceImpl extends ServiceImpl<BCountryMapper, BCountry> implements IBCountryService {

    @Override
    public IPage<BCountryVO> findCountryList(QueryCountryForm form) {
        Page<BCountryVO> page = new Page<>(form.getPageNum(),form.getPageSize() );
        return this.baseMapper.findCountryList(form,page);
    }
}
