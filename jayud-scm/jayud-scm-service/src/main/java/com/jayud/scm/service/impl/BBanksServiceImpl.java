package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BBanks;
import com.jayud.scm.mapper.BBanksMapper;
import com.jayud.scm.model.vo.BBanksVO;
import com.jayud.scm.model.vo.BDataDicVO;
import com.jayud.scm.service.IBBanksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公司银行账户 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Service
public class BBanksServiceImpl extends ServiceImpl<BBanksMapper, BBanks> implements IBBanksService {

    @Override
    public IPage<BBanksVO> findByPage(QueryForm form) {
        Page<BBanksVO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page);
    }
}
