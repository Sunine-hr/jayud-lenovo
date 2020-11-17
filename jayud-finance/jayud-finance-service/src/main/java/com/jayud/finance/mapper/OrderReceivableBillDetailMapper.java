package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryPaymentBillDetailForm;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Mapper
public interface OrderReceivableBillDetailMapper extends BaseMapper<OrderReceivableBillDetail> {

    /**
     * 应收对账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderPaymentBillDetailVO> findReceiveBillDetailByPage(Page page, @Param("form") QueryPaymentBillDetailForm form);

    /**
     * 导出应收对账单分页查询
     * @param form
     * @return
     */
    List<OrderPaymentBillDetailVO> findReceiveBillDetailByPage(@Param("form") QueryPaymentBillDetailForm form);

}
