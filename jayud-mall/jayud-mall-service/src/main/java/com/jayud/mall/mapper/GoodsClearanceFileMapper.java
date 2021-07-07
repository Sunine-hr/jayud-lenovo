package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.GoodsClearanceFile;
import com.jayud.mall.model.vo.GoodsClearanceFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-29
 */
@Mapper
@Component
public interface GoodsClearanceFileMapper extends BaseMapper<GoodsClearanceFile> {

    /**
     * 根据商品id，查询
     * @param goodId
     * @return
     */
    List<GoodsClearanceFileVO> findGoodsClearanceFileByGoodId(@Param("goodId") Integer goodId);
}
