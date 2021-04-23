package com.jayud.storage.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.storage.model.bo.QueryGoodForm;
import com.jayud.storage.model.po.Good;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.storage.model.vo.GoodVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 * 商品信息维护表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-04-22
 */
@Mapper
public interface GoodMapper extends BaseMapper<Good> {

    IPage<GoodVO> findGoodsByPage(@Param("form") QueryGoodForm form);
}
