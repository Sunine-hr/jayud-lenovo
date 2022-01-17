package com.jayud.scm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AcctPayFollow;
import com.jayud.scm.mapper.AcctPayFollowMapper;
import com.jayud.scm.model.vo.AcctPayFollowVO;
import com.jayud.scm.model.vo.AcctReceiptFollowVO;
import com.jayud.scm.service.IAcctPayFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 付款跟踪记录表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Service
public class AcctPayFollowServiceImpl extends ServiceImpl<AcctPayFollowMapper, AcctPayFollow> implements IAcctPayFollowService {

    @Override
    public IPage<AcctPayFollowVO> findListByAcctPayId(QueryCommonForm form) {
        Page<AcctPayFollowVO> page = new Page<>(form.getPageNum(),form.getPageSize() );
        return this.baseMapper.findListByAcctPayId(form,page);
    }
}
