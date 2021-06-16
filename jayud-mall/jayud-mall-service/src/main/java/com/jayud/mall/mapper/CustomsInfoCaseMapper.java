package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.bo.BillCustomsInfoQueryForm;
import com.jayud.mall.model.po.CustomsInfoCase;
import com.jayud.mall.model.vo.BillCaseVO;
import com.jayud.mall.model.vo.CustomsInfoCaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 报关文件箱号 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Mapper
@Component
public interface CustomsInfoCaseMapper extends BaseMapper<CustomsInfoCase> {

    /**
     * 根据bid，查询
     * @param bId 提单对应报关信息id(bill_customs_info id)
     * @return
     */
    List<CustomsInfoCaseVO> findCustomsInfoCaseByBid(@Param("bId") Long bId);

    /**
     * 报关箱子-查询提单下未生成的订单箱子(分类型)
     * @param form
     * @return
     */
    List<BillCaseVO> findUnselectedBillCaseByCustoms(@Param("form") BillCustomsInfoQueryForm form);

    /**
     * 报关箱子-查询提单下已生成的订单箱子
     * @param form
     * @return
     */
    List<BillCaseVO> findSelectedBillCaseByCustoms(@Param("form") BillCustomsInfoQueryForm form);
}
