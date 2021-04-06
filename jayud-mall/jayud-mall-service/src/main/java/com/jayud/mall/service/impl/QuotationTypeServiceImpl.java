package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.QuotationTypeMapper;
import com.jayud.mall.model.po.QuotationType;
import com.jayud.mall.model.vo.QuotationTypeReturnVO;
import com.jayud.mall.model.vo.QuotationTypeVO;
import com.jayud.mall.service.IQuotationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    QuotationTypeMapper quotationTypeMapper;

    @Override
    public List<QuotationTypeReturnVO> findQuotationTypeBy() {
        QueryWrapper<QuotationType> quotationTypeQueryWrapper = new QueryWrapper<>();
        quotationTypeQueryWrapper.eq("fid", 0);
        quotationTypeQueryWrapper.select().orderByAsc("sort");
        List<QuotationType> quotationTypes = quotationTypeMapper.selectList(quotationTypeQueryWrapper);
        List<QuotationTypeReturnVO> quotationTypeReturnVOS = ConvertUtil.convertList(quotationTypes, QuotationTypeReturnVO.class);
        quotationTypeReturnVOS.forEach(quotationTypeReturnVO -> {
            String fid = quotationTypeReturnVO.getId();
            QueryWrapper<QuotationType> childrenQueryWrapper = new QueryWrapper<>();
            childrenQueryWrapper.eq("fid", fid);
            childrenQueryWrapper.select().orderByAsc("sort");
            List<QuotationType> quotationTypeList = quotationTypeMapper.selectList(childrenQueryWrapper);
            List<QuotationTypeVO> children = ConvertUtil.convertList(quotationTypeList, QuotationTypeVO.class);
            quotationTypeReturnVO.setChildren(children);
        });
        return quotationTypeReturnVOS;
    }
}
