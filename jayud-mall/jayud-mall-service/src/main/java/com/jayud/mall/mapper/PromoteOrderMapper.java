package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryPromoteOrderForm;
import com.jayud.mall.model.po.PromoteOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.PromoteOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 推广订单表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-20
 */
@Mapper
public interface PromoteOrderMapper extends BaseMapper<PromoteOrder> {

    IPage<PromoteOrderVO> findPromoteOrderByPage(Page<PromoteOrderVO> page, @Param("form") QueryPromoteOrderForm form);

    PromoteOrderVO findPromoteOrderById(@Param("id") Integer id);
}
