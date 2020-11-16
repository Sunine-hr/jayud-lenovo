package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryNotPaidBillForm;
import com.jayud.finance.bo.QueryReceiveBillForm;
import com.jayud.finance.bo.QueryReceiveBillNumForm;
import com.jayud.finance.po.OrderReceivableBill;
import com.jayud.finance.vo.OrderPaymentBillNumVO;
import com.jayud.finance.vo.OrderReceiveBillVO;
import com.jayud.finance.vo.ReceiveNotPaidBillVO;
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
public interface OrderReceivableBillMapper extends BaseMapper<OrderReceivableBill> {

    /**
     * 主订单应收出账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderReceiveBillVO> findReceiveBillByPage(Page page, @Param("form") QueryReceiveBillForm form);

    /**
     * 子订单应收出账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderReceiveBillVO> findReceiveSubBillByPage(Page page, @Param("form") QueryReceiveBillForm form);

    /**
     * 已生成对账单列表
     * @param form
     * @return
     */
    List<OrderPaymentBillNumVO> findReceiveBillNum(@Param("form") QueryReceiveBillNumForm form);

    /**
     * 应收未出账单列表分页查询
     * @param form
     * @return
     */
    IPage<ReceiveNotPaidBillVO> findNotPaidBillByPage(Page page, @Param("form") QueryNotPaidBillForm form);
}
