package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.BillClearanceInfoQueryForm;
import com.jayud.mall.model.po.ClearanceInfoCase;
import com.jayud.mall.model.vo.BillCaseVO;
import com.jayud.mall.model.vo.ClearanceInfoCaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 清关文件箱号 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Mapper
@Component
public interface ClearanceInfoCaseMapper extends BaseMapper<ClearanceInfoCase> {

    /**
     * 根据bid，查询
     * @param bId 提单对应清关信息id(bill_clearance_info id)
     * @return
     */
    List<ClearanceInfoCaseVO> findClearanceInfoCaseByBid(@Param("bId") Long bId);

    /**
     * 清关箱子-查询提单下未生成的订单箱子(分类型)
     * @param form
     * @return
     */
    List<BillCaseVO> findUnselectedBillCaseByClearance(@Param("form") BillClearanceInfoQueryForm form);

    /**
     * 清关箱子-查询提单下已生成的订单箱子
     * @param form
     * @return
     */
    List<BillCaseVO> findSelectedBillCaseByClearance(@Param("form") BillClearanceInfoQueryForm form);
}
