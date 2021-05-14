package com.jayud.trailer.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.trailer.bo.QueryTrailerOrderForm;
import com.jayud.trailer.po.TrailerOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.trailer.vo.TrailerOrderFormVO;
import com.jayud.trailer.vo.TrailerOrderInfoVO;
import com.jayud.trailer.vo.TrailerOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 拖车订单表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Mapper
public interface TrailerOrderMapper extends BaseMapper<TrailerOrder> {

    /**
     * 获取订单详情
     * @param id
     * @return
     */
    TrailerOrderVO getTrailerOrder(@Param("id") Long id);

    /**
     * 分页获取订单信息
     * @param page
     * @param form
     * @param legalIds
     * @return
     */
    IPage<TrailerOrderFormVO> findByPage(@Param("page") Page<TrailerOrderFormVO> page, @Param("form")QueryTrailerOrderForm form,@Param("legalIds") List<Long> legalIds);

    /**
     * 根据主订单号查询所有详情
     * @param mainOrderNos
     * @return
     */
    List<TrailerOrderInfoVO> getTrailerInfoByMainOrderNos(@Param("mainOrderNos") List<String> mainOrderNos);
}
