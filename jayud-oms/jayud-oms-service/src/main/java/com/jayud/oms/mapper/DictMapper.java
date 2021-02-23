package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryDictForm;
import com.jayud.oms.model.po.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.DictVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 字典 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {



    IPage<DictVO> findByPage(Page<Dict> page, QueryDictForm form);
}
