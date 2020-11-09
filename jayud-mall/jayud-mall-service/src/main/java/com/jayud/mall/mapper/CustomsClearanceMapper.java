package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryCustomsClearanceForm;
import com.jayud.mall.model.po.CustomsClearance;
import com.jayud.mall.model.vo.CustomsClearanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 清关资料表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Mapper
@Component
public interface CustomsClearanceMapper extends BaseMapper<CustomsClearance> {

    /**
     * 分页
     * @param page
     * @param form
     * @return
     */
    IPage<CustomsClearanceVO> findCustomsClearanceByPage(Page<CustomsClearanceVO> page, QueryCustomsClearanceForm form);
}
