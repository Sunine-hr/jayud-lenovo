package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryBusinessLogForm;
import com.jayud.mall.model.po.BusinessLog;
import com.jayud.mall.model.vo.BusinessLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 业务日志表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-24
 */
@Mapper
@Component
public interface BusinessLogMapper extends BaseMapper<BusinessLog> {

    /**
     * 分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<BusinessLogVO> findBusinessLogByPage(Page<BusinessLogVO> page, @Param("form") QueryBusinessLogForm form);
}
