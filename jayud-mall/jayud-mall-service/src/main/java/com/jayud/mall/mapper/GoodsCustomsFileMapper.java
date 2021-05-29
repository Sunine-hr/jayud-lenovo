package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.GoodsCustomsFile;
import com.jayud.mall.model.vo.GoodsCustomsFileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 商品报关文件列表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-29
 */
@Mapper
@Component
public interface GoodsCustomsFileMapper extends BaseMapper<GoodsCustomsFile> {

    /**
     * 根据商品id，查询
     * @param goodId
     * @return
     */
    List<GoodsCustomsFileVO> findGoodsCustomsFileByGoodId(@Param("goodId") Integer goodId);
}
