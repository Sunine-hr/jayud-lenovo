package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BDataDic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.BDataDicVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 数据字典主表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Mapper
public interface BDataDicMapper extends BaseMapper<BDataDic> {

    IPage<BDataDicVO> findByPage(@Param("page") Page<BDataDicVO> page,@Param("form") QueryForm form);
}
