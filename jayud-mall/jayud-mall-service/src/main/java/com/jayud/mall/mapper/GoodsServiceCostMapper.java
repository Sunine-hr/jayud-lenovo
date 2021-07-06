package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.GoodsServiceCost;
import com.jayud.mall.model.vo.GoodsServiceCostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 商品服务费用表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-08
 */
@Mapper
@Component
public interface GoodsServiceCostMapper extends BaseMapper<GoodsServiceCost> {

    /**
     * 根据商品id，查询 商品服务费用
     * @param goodId
     * @return
     */
    List<GoodsServiceCostVO> findGoodsServiceCostByGoodId(@Param("goodId") Long goodId);
}
