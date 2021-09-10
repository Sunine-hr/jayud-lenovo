package com.jayud.scm.service.impl;

import com.jayud.scm.model.po.TaxInvoice;
import com.jayud.scm.mapper.TaxInvoiceMapper;
import com.jayud.scm.service.ITaxInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 销项票主表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@Service
public class TaxInvoiceServiceImpl extends ServiceImpl<TaxInvoiceMapper, TaxInvoice> implements ITaxInvoiceService {

}
