package com.jayud.airfreight.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.airfreight.model.bo.QueryAirOrderForm;
import com.jayud.airfreight.model.po.AirOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.airfreight.model.vo.AirOrderFormVO;
import com.jayud.airfreight.model.vo.AirOrderInfoVO;
import com.jayud.airfreight.model.vo.AirOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 空运订单表 Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-30
 */
@Mapper
public interface AirOrderMapper extends BaseMapper<AirOrder> {

    IPage<AirOrderFormVO> findByPage(Page page, @Param("form") QueryAirOrderForm form, @Param("legalIds") List<Long> legalIds);

    AirOrderVO getAirOrder(Long airOrderId);

    Integer getNumByStatus(@Param("status") String status, @Param("legalIds") List<Long> legalIds);

    List<AirOrderInfoVO> getByMainOrderNo(@Param("mainOrderNos") List<String> mainOrderNos);
}
