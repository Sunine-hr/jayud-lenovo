package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.NoticeSnMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知单物料sn信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-18
 */
@Mapper
public interface NoticeSnMaterialMapper extends BaseMapper<NoticeSnMaterial> {
    /**
    *   分页查询
    */
    IPage<NoticeSnMaterial> pageList(@Param("page") Page<NoticeSnMaterial> page, @Param("noticeSnMaterial") NoticeSnMaterial noticeSnMaterial);

    /**
    *   列表查询
    */
    List<NoticeSnMaterial> list(@Param("noticeSnMaterial") NoticeSnMaterial noticeSnMaterial);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryNoticeSnMaterialForExcel(Map<String, Object> paramMap);
}
