package com.jayud.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.wms.model.po.WmsMaterialBasicInfo;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 物料基本信息 Mapper 接口
 *
 * @author jyd
 * @since 2021-12-16
 */
@Mapper
public interface WmsMaterialBasicInfoMapper extends BaseMapper<WmsMaterialBasicInfo> {
    /**
    *   分页查询
    */
    IPage<WmsMaterialBasicInfoVO> pageList(@Param("page") Page<WmsMaterialBasicInfoVO> page, @Param("wmsMaterialBasicInfoVO") WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

    /**
    *   列表查询
    */
    List<WmsMaterialBasicInfoVO> list(@Param("wmsMaterialBasicInfoVO") WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);
}
