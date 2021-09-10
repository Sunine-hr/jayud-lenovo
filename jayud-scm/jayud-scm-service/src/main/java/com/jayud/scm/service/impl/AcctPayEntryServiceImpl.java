package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.AcctPayEntry;
import com.jayud.scm.mapper.AcctPayEntryMapper;
import com.jayud.scm.service.IAcctPayEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应付款表（付款单明细表） 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Service
public class AcctPayEntryServiceImpl extends ServiceImpl<AcctPayEntryMapper, AcctPayEntry> implements IAcctPayEntryService {

}
