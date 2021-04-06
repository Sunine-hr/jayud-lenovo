package com.jayud.Inlandtransport.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.Inlandtransport.model.bo.QueryOrderForm;
import com.jayud.Inlandtransport.model.po.OrderInlandTransport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportDetails;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportFormVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 内陆订单 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
@Mapper
public interface OrderInlandTransportMapper extends BaseMapper<OrderInlandTransport> {

    IPage<OrderInlandTransportFormVO> findByPage(Page<OrderInlandTransport> page,
                                                 @Param("form") QueryOrderForm form,
                                                 @Param("legalIds") List<Long> legalIds);

    List<OrderInlandTransportDetails> getOrderInfoByMainOrderNos(@Param("mainOrderNos") List<String> mainOrderNos);

    Integer getNumByStatus(@Param("status") String status, @Param("legalIds") List<Long> legalIds);
    
}
