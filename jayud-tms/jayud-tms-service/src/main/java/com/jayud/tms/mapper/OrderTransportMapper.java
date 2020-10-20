package com.jayud.tms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.model.vo.InputOrderTransportVO;
import com.jayud.tms.model.vo.OrderTransportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 中港运输订单 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderTransportMapper extends BaseMapper<OrderTransport> {

    /**
     * 获取订单详情
     * @param mainOrderNo
     * @return
     */
    public InputOrderTransportVO getOrderTransport(@Param("mainOrderNo") String mainOrderNo);

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderTransportVO> findTransportOrderByPage(Page page, @Param("form") QueryOrderTmsForm form);
}
