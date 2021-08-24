package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.HgBill;
import com.jayud.scm.mapper.HgBillMapper;
import com.jayud.scm.service.IHgBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报关单主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Service
public class HgBillServiceImpl extends ServiceImpl<HgBillMapper, HgBill> implements IHgBillService {

}
