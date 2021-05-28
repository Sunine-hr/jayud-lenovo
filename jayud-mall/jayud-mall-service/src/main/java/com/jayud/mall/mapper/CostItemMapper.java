package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.CostItemSupForm;
import com.jayud.mall.model.bo.QueryCostItemForm;
import com.jayud.mall.model.po.CostItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.CostItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 费用项目 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-28
 */
@Mapper
@Component
public interface CostItemMapper extends BaseMapper<CostItem> {

    /**
     * 根据供应商id，获取供应商费用（应付费用信息）
     * @param form
     * @return
     */
    List<CostItemVO> findCostItemBySupId(@Param("form") CostItemSupForm form);

    /**
     * 根据费用代码，查询费用
     * @param costCode
     * @return
     */
    CostItemVO findCostItemByCostCode(@Param("costCode") String costCode);

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<CostItemVO> findCostItemByPage(Page<CostItemVO> page, @Param("form") QueryCostItemForm form);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CostItemVO findCostItemById(@Param("id") Long id);
}
