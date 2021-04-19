package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryOperationTeamForm;
import com.jayud.mall.model.po.OperationTeam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.OperationTeamVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 运营(服务)小组 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Mapper
@Component
public interface OperationTeamMapper extends BaseMapper<OperationTeam> {

    /**
     * 分页查询运营服务小组
     * @param page
     * @param form
     * @return
     */
    IPage<OperationTeamVO> findOperationTeamByPage(Page<OperationTeamVO> page, @Param("form") QueryOperationTeamForm form);

    /**
     * 根据id，查找运营组
     * @param id
     * @return
     */
    OperationTeamVO findOperationTeamById(@Param("id") Long id);
}
