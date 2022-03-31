package com.jayud.wms.mapper;

import com.jayud.wms.model.po.WmsReceiptAppend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货单附加服务表 Mapper 接口
 *
 * @author jayud
 * @since 2022-03-31
 */
@Mapper
public interface WmsReceiptAppendMapper extends BaseMapper<WmsReceiptAppend> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-03-31
     * @param: page
     * @param: wmsReceiptAppend
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.wms.model.po.WmsReceiptAppend>
     **/
    IPage<WmsReceiptAppend> pageList(@Param("page") Page<WmsReceiptAppend> page, @Param("wmsReceiptAppend") WmsReceiptAppend wmsReceiptAppend);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-03-31
     * @param: wmsReceiptAppend
     * @return: java.util.List<com.jayud.wms.model.po.WmsReceiptAppend>
     **/
    List<WmsReceiptAppend> list(@Param("wmsReceiptAppend") WmsReceiptAppend wmsReceiptAppend);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-03-31
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-03-31
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-03-31
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryWmsReceiptAppendForExcel(Map<String, Object> paramMap);
}
