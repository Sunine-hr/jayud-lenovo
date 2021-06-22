package com.jayud.finance.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.finance.bo.QueryVoidBillingForm;
import com.jayud.finance.po.VoidBillingRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.finance.vo.VoidBillingRecordsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 作废账单表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2021-06-21
 */
public interface VoidBillingRecordsMapper extends BaseMapper<VoidBillingRecords> {

    /**
     * 根据制单时间查询订单数量
     *
     * @param makeTime
     * @param costType
     * @return
     */
    public Integer getCountByMakeTime(@Param("makeTime") String makeTime,
                                      @Param("format") String format, @Param("costType") Integer costType);

    List<VoidBillingRecordsVO> findVoidBillByPage(Page<VoidBillingRecordsVO> page, QueryVoidBillingForm form);
}
