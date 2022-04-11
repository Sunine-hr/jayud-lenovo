package com.jayud.auth.mapper;

import com.jayud.auth.model.po.VehicleSizeInfo;
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
 * 车辆尺寸信息 Mapper 接口
 *
 * @author jayud
 * @since 2022-04-11
 */
@Mapper
public interface VehicleSizeInfoMapper extends BaseMapper<VehicleSizeInfo> {

    /**
     * @description 分页查询
     * @author  jayud
     * @date   2022-04-11
     * @param: page
     * @param: vehicleSizeInfo
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jayud.auth.model.po.VehicleSizeInfo>
     **/
    IPage<VehicleSizeInfo> pageList(@Param("page") Page<VehicleSizeInfo> page, @Param("vehicleSizeInfo") VehicleSizeInfo vehicleSizeInfo);

    /**
     * @description 列表查询数据
     * @author  jayud
     * @date   2022-04-11
     * @param: vehicleSizeInfo
     * @return: java.util.List<com.jayud.auth.model.po.VehicleSizeInfo>
     **/
    List<VehicleSizeInfo> list(@Param("vehicleSizeInfo") VehicleSizeInfo vehicleSizeInfo);


    /**
     * @description 根据id物理删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @return: int
     **/
    int phyDelById(@Param("id") Long id);

    /**
     * @description 根据id逻辑删除
     * @author  jayud
     * @date   2022-04-11
     * @param: id
     * @param: username
     * @return: int
     **/
    int logicDel(@Param("id") Long id,@Param("username") String username);


    /**
     * @description 查询导出
     * @author  jayud
     * @date   2022-04-11
     * @param: paramMap
     * @return: java.util.List<java.util.LinkedHashMap<java.lang.String,java.lang.Object>>
     **/
    List<LinkedHashMap<String, Object>> queryVehicleSizeInfoForExcel(Map<String, Object> paramMap);

    /**
     * 获取车型
     * @return
     */
    List<VehicleSizeInfo> findVehicleSize();
}
