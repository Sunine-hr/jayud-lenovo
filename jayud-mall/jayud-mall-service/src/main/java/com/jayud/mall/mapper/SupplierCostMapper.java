package com.jayud.mall.mapper;

import com.jayud.mall.model.bo.SupplierCostForm;
import com.jayud.mall.model.po.SupplierCost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.SupplierCostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 供应商服务费用 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Mapper
@Component
public interface SupplierCostMapper extends BaseMapper<SupplierCost> {

    /**
     * 查询-供应商服务费用-list
     * @param form
     * @return
     */
    List<SupplierCostVO> findSupplierCost(@Param("form") SupplierCostForm form);
}
