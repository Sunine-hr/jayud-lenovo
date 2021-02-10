package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.oceanship.po.Terms;
import com.jayud.oceanship.mapper.TermsMapper;
import com.jayud.oceanship.service.ITermsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Service
public class TermsServiceImpl extends ServiceImpl<TermsMapper, Terms> implements ITermsService {

    @Override
    public List<InitComboxVO> initTerms() {
        List<Terms> terms = this.baseMapper.list();
        List<InitComboxVO> list = new ArrayList<>();
        for (Terms term : terms) {
            InitComboxVO initComboxVO = new InitComboxVO();
            initComboxVO.setId(term.getId().longValue());
            initComboxVO.setName(term.getName());
            list.add(initComboxVO);
        }
        return list;
    }
}
