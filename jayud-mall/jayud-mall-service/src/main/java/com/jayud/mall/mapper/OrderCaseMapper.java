package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOrderCaseForm;
import com.jayud.mall.model.po.OrderCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.OrderCaseVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单对应箱号信息 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Mapper
@Component
public interface OrderCaseMapper extends BaseMapper<OrderCase> {

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<OrderCaseVO> findOrderCaseByPage(Page<OrderCaseVO> page, QueryOrderCaseForm form);

}
