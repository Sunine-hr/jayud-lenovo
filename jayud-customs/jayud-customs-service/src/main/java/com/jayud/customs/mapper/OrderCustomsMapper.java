package com.jayud.customs.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.customs.model.bo.QueryCustomsOrderInfoForm;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.CustomsOrderInfoVO;
import com.jayud.customs.model.vo.OrderCustomsVO;
import com.jayud.customs.model.vo.StatisticsDataNumberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报关业务订单表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Mapper
public interface OrderCustomsMapper extends BaseMapper<OrderCustoms> {


    List<OrderCustomsVO> findOrderCustomsByCondition(Map<String, Object> param);

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<CustomsOrderInfoVO> findCustomsOrderByPage(Page page, @Param("form") QueryCustomsOrderInfoForm form);

    /**
     * 报关各个菜单列表数据量统计
     * @return
     */
    StatisticsDataNumberVO statisticsDataNumber();
}
