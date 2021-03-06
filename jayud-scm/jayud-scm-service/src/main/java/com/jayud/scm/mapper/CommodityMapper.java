package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.DeleteForm;
import com.jayud.scm.model.bo.QueryCommodityForm;
import com.jayud.scm.model.po.Commodity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.CommodityFormVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-07-27
 */
@Mapper
public interface CommodityMapper extends BaseMapper<Commodity> {

    /**
     * 分页查询商品信息
     * @param page
     * @param form
     * @return
     */
    IPage<CommodityFormVO> findByPage(@Param("page") Page<CommodityFormVO> page, @Param("form")QueryCommodityForm form);

    void getOrderNo(Map<String,Object> map);

    boolean commonDelete(@Param("form") DeleteForm form);
}
