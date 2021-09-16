package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BOtherCostItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.BOtherCostItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 费用名称表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
@Mapper
public interface BOtherCostItemMapper extends BaseMapper<BOtherCostItem> {

    IPage<BOtherCostItemVO> findByPage(@Param("page") Page<BOtherCostItemVO> page,@Param("form") QueryCommonForm form);
}
