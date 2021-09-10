package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.po.BBanks;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.BBanksVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 公司银行账户 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Mapper
public interface BBanksMapper extends BaseMapper<BBanks> {

    IPage<BBanksVO> findByPage(Page<BBanksVO> page);
}
