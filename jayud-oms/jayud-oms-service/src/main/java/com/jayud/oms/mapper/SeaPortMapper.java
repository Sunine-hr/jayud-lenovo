package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QuerySeaPortForm;
import com.jayud.oms.model.po.SeaPort;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.SeaPortVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 船港口地址表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-06-30
 */
@Mapper
public interface SeaPortMapper extends BaseMapper<SeaPort> {

    IPage<SeaPortVO> findByPage(@Param("page") Page<SeaPortVO> page, @Param("form") QuerySeaPortForm form);

}
