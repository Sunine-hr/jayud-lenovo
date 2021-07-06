package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryInlandFeeCostForm;
import com.jayud.mall.model.po.InlandFeeCost;
import com.jayud.mall.model.vo.InlandFeeCostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 内陆费费用表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-08
 */
@Mapper
@Component
public interface InlandFeeCostMapper extends BaseMapper<InlandFeeCost> {

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<InlandFeeCostVO> findInlandFeeCostByPage(Page<InlandFeeCostVO> page, @Param("form") QueryInlandFeeCostForm form);

    /**
     * 根据id，查询内陆费费用
     * @param id
     * @return
     */
    InlandFeeCostVO findInlandFeeCostById(@Param("id") Long id);

    /**
     * 查询路线的内陆费
     * @param paraMap
     * @return
     */
    InlandFeeCostVO findInlandFeeCostByPara(@Param("paraMap") Map<String, Object> paraMap);
}
