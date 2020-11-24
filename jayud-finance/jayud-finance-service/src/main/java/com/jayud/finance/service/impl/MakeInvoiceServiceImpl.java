package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.finance.bo.MakeInvoiceForm;
import com.jayud.finance.mapper.MakeInvoiceMapper;
import com.jayud.finance.po.MakeInvoice;
import com.jayud.finance.service.IMakeInvoiceService;
import com.jayud.finance.vo.MakeInvoiceVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class MakeInvoiceServiceImpl extends ServiceImpl<MakeInvoiceMapper, MakeInvoice> implements IMakeInvoiceService {

    @Override
    public List<MakeInvoiceVO> findInvoiceList(String billNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        List<MakeInvoice> makeInvoices = list(queryWrapper);
        return ConvertUtil.convertList(makeInvoices,MakeInvoiceVO.class);
    }

    @Override
    public Boolean makeInvoice(MakeInvoiceForm form) {
        MakeInvoice makeInvoic = ConvertUtil.convert(form,MakeInvoice.class);
        return save(makeInvoic);
    }

    @Override
    public Boolean makeInvoiceDel(Long invoiceId) {
        MakeInvoice makeInvoice = new MakeInvoice();
        makeInvoice.setId(invoiceId);
        makeInvoice.setStatus("0");
        makeInvoice.setCreatedTime(LocalDateTime.now());
        makeInvoice.setCreatedUser(UserOperator.getToken());
        return updateById(makeInvoice);
    }
}
