package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryServiceGroupForm;
import com.jayud.mall.model.bo.ServiceGroupForm;
import com.jayud.mall.model.po.ServiceGroup;
import com.jayud.mall.model.vo.ServiceGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 报价服务组 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface ServiceGroupMapper extends BaseMapper<ServiceGroup> {

    /**
     * 查询报价服务组List
     * @param form
     * @return
     */
    List<ServiceGroupVO> findServiceGroup(@Param("form") ServiceGroupForm form);

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<ServiceGroupVO> findServiceGroupByPage(Page<ServiceGroupVO> page, @Param("form") QueryServiceGroupForm form);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ServiceGroupVO findServiceGroupById(@Param("id") Long id);
}
