package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOrderCaseWmsForm;
import com.jayud.mall.model.po.OrderCaseWms;
import com.jayud.mall.model.vo.OrderCaseWmsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单装箱信息(仓库测量) Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-09
 */
@Mapper
@Component
public interface OrderCaseWmsMapper extends BaseMapper<OrderCaseWms> {

    /**
     * 根据箱号查询
     * @param cartonNo
     * @return
     */
    OrderCaseWmsVO findOrderCaseWmsByCartonNo(@Param("cartonNo") String cartonNo);

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<OrderCaseWmsVO> findOrderCaseWmsPage(Page<OrderCaseWmsVO> page, @Param("form") QueryOrderCaseWmsForm form);
}
