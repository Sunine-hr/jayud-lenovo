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
    public QuotationTypeReturnVO findQuotationTypeBy() {
        //1	整柜
        QueryWrapper<QuotationType> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("fid", 1);
        List<QuotationType> list1 = quotationTypeMapper.selectList(queryWrapper1);
        List<QuotationTypeVO> quotationTypeVOS1 = ConvertUtil.convertList(list1, QuotationTypeVO.class);
        //2	散柜
        QueryWrapper<QuotationType> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("fid", 2);
        List<QuotationType> list2 = quotationTypeMapper.selectList(queryWrapper2);
        List<QuotationTypeVO> quotationTypeVOS2 = ConvertUtil.convertList(list2, QuotationTypeVO.class);

        //返回对象
        QuotationTypeReturnVO quotationTypeReturnVO = new QuotationTypeReturnVO();
        quotationTypeReturnVO.setFullContainer(quotationTypeVOS1);
        quotationTypeReturnVO.setScatteredArk(quotationTypeVOS2);

        return quotationTypeReturnVO;
    }
}
