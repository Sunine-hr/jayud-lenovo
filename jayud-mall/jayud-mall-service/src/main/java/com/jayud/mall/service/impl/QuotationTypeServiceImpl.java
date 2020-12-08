package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.QuotationType;
import com.jayud.mall.mapper.QuotationTypeMapper;
import com.jayud.mall.service.IQuotationTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报价类型表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-08
 */
@Service
public class QuotationTypeServiceImpl extends ServiceImpl<QuotationTypeMapper, QuotationType> implements IQuotationTypeService {

}
