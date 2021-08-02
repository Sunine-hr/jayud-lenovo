package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CommodityFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.CommodityFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品操作日志表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Mapper
public interface CommodityFollowMapper extends BaseMapper<CommodityFollow> {

    IPage<CommodityFollowVO> findListByCommodityId(@Param("form") QueryCommonForm form, @Param("page")Page<BCountryVO> page);
}
