package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.bo.ContainerForm;
import com.jayud.wms.model.po.Container;
import com.jayud.wms.model.vo.ContainerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * <p>
 * 客户信息Mapper 接口
 * </p>
 */
@Mapper
public interface ContainerMapper extends BaseMapper<Container> {

    /**
     * 分页查询
     */
    IPage<ContainerVO> pageList(@Param("page") Page<ContainerForm> page, @Param("container") ContainerForm container);

//    int updateContainerId(Container form);

    /**
     * 列表查询
     */
    List<ContainerVO> list(@Param("container") ContainerForm container);

    /**
     * 根据需求查询单个容器信息
     * @param container
     * @return
     */
    ContainerForm getContainerOne(@Param("container") Container container);

    // 导出
    List<LinkedHashMap<String, Object>> queryContainerForExcel(@Param("container") ContainerForm container);

}
