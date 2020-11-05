package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryWarehouseInfoForm;
import com.jayud.oms.model.po.WarehouseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.WarehouseInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 仓库信息表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-10-16
 */
@Mapper
public interface WarehouseInfoMapper extends BaseMapper<WarehouseInfo> {

    IPage<WarehouseInfoVO> findWarehouseInfoByPage(Page page, @Param("form") QueryWarehouseInfoForm form);
}
