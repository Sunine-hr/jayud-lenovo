package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.QueryCountryForm;
import com.jayud.scm.model.po.BCountry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.HsCodeFormVO;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * <p>
 * 国家表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Mapper
public interface BCountryMapper extends BaseMapper<BCountry> {


    IPage<BCountryVO> findCountryList(@Param("form") QueryCountryForm form, @Param("page") Page<BCountryVO> page);
}
