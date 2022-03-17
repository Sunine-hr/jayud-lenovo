package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.NoticeMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 通知单物料信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-16
 */
@Mapper
public interface NoticeMaterialMapper extends BaseMapper<NoticeMaterial> {
    /**
    *   分页查询
    */
    IPage<NoticeMaterial> pageList(@Param("page") Page<NoticeMaterial> page, @Param("noticeMaterial") NoticeMaterial noticeMaterial);

    /**
    *   列表查询
    */
    List<NoticeMaterial> list(@Param("noticeMaterial") NoticeMaterial noticeMaterial);
}
