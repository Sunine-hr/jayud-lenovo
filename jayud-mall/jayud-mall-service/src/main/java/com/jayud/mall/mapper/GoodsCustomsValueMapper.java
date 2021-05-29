package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.GoodsCustomsValue;
import com.jayud.mall.model.vo.GoodsCustomsValueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 商品报关申报价值 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-29
 */
@Mapper
@Component
public interface GoodsCustomsValueMapper extends BaseMapper<GoodsCustomsValue> {

    /**
     * 根据商品id，查询
     * @param goodId
     * @return
     */
    List<GoodsCustomsValueVO> findGoodsCustomsValueByGoodId(@Param("goodId") Integer goodId);
}
