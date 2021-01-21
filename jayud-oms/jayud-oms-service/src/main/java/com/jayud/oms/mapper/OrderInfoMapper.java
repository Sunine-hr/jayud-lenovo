package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.InitGoCustomsAuditForm;
import com.jayud.oms.model.bo.QueryOrderInfoForm;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.InitGoCustomsAuditVO;
import com.jayud.oms.model.vo.InputMainOrderVO;
import com.jayud.oms.model.vo.OrderDataCountVO;
import com.jayud.oms.model.vo.OrderInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 主订单基础数据表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 分页查询订单
     * @param page
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findOrderInfoByPage(Page page, @Param("form") QueryOrderInfoForm form);

    /**
     * 分页查询通关前审核
     * @param page
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findGoCustomsAuditByPage(Page page, @Param("form") QueryOrderInfoForm form);

    /**
     * 获取主订单信息
     * @param idValue
     * @return
     */
    InputMainOrderVO getMainOrderById(@Param("idValue") Long idValue);

    /**
     * 统计订单数据
     * @return
     * @param legalIds
     */
    OrderDataCountVO countOrderData(@Param("legalIds") List<Long> legalIds);

    /**
     * 二期优化1：通关前审核，通关前复核
     * @param form
     * @return
     */
    InitGoCustomsAuditVO initGoCustomsAudit1(@Param("form") InitGoCustomsAuditForm form);

    /**
     * 二期优化1：通关前审核，通关前复核
     * @param form
     * @return
     */
    InitGoCustomsAuditVO initGoCustomsAudit2(@Param("form") InitGoCustomsAuditForm form);

    /**
     * 分页查询订单
     * @param page
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findOrderInfoByPage(Page<OrderInfoVO> page, @Param("form")QueryOrderInfoForm form, @Param("legalIds")List<Long> legalIds);

    /**
     * 分页查询通关前审核
     * @param page
     * @param form
     * @return
     */
    IPage<OrderInfoVO> findGoCustomsAuditByPage(Page<OrderInfoVO> page, @Param("form")QueryOrderInfoForm form,@Param("legalIds") List<Long> legalIds);
}
