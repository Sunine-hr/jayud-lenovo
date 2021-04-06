package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryTransportForm;
import com.jayud.mall.model.po.Transport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.TransportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 运输管理表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-06
 */
@Mapper
@Component
public interface TransportMapper extends BaseMapper<Transport> {

    /**
     * 运输管理，分页查询
     * @param page
     * @param form
     * @return
     */
    IPage<TransportVO> findTransportByPage(Page<TransportVO> page, @Param("form") QueryTransportForm form);
}
