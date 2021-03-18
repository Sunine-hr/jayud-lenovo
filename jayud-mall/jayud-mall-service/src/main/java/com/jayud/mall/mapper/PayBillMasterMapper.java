package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryPayBillMasterForm;
import com.jayud.mall.model.po.PayBillMaster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.PayBillMasterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 应付账单主单 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Mapper
@Component
public interface PayBillMasterMapper extends BaseMapper<PayBillMaster> {

    /**
     * 应付账单分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<PayBillMasterVO> findPayBillMasterByPage(Page<PayBillMasterVO> page, @Param("form") QueryPayBillMasterForm form);

    /**
     * 根据主单id，查询应付费用明细
     * @param id
     * @return
     */
    PayBillMasterVO findPayBillMasterById(@Param("id") Long id);

    /**
     * 根据应收对账单，查询应付账单
     * @param accountPayableId 应付对账单id
     * @return
     */
    List<PayBillMasterVO> findPayBillMasterByAccountPayableId(@Param("accountPayableId") Long accountPayableId);

    /**
     * 验证，账单期间的应付单据是否月结过
     * @param firstday
     * @param lastDay
     * @return
     */
    List<PayBillMaster> verifyPayBillMasterByCreateTime(@Param("firstday") LocalDateTime firstday, @Param("lastDay") LocalDateTime lastDay);

    /**
     * 查询，账单期间的应付账单，准备生成应付对账单
     * @param firstday
     * @param lastDay
     * @return
     */
    List<PayBillMaster> findPayBillMasterByCreateTime(@Param("firstday") LocalDateTime firstday, @Param("lastDay") LocalDateTime lastDay);


}
