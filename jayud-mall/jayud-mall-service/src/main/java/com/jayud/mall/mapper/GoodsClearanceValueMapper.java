package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.GoodsClearanceValue;
import com.jayud.mall.model.vo.GoodsClearanceValueVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 商品清关申报价值 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-29
 */
@Mapper
@Component
public interface GoodsClearanceValueMapper extends BaseMapper<GoodsClearanceValue> {

    /**
     * 根据商品id，查询
     * @param goodId
     * @return
     */
    List<GoodsClearanceValueVO> findGoodsClearanceValueByGoodId(@Param("goodId") Integer goodId);
}
