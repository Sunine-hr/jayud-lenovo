package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryPaymentBillDetailForm;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;
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
public interface OrderPaymentBillDetailMapper extends BaseMapper<OrderPaymentBillDetail> {

    /**
     * 应付对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(Page page, @Param("form") QueryPaymentBillDetailForm form);
}
