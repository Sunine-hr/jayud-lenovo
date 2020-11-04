package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryPaymentBillForm;
import com.jayud.finance.po.OrderPaymentBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.finance.vo.OrderPaymentBillVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Mapper
public interface OrderPaymentBillMapper extends BaseMapper<OrderPaymentBill> {

    IPage<OrderPaymentBillVO> findPaymentBillByPage(Page page, @Param("form") QueryPaymentBillForm form);

}
