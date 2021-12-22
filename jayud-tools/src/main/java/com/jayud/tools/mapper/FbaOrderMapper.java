package com.jayud.tools.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.tools.model.bo.QueryFbaOrderForm;
import com.jayud.tools.model.po.FbaOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.tools.model.vo.FbaOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.jmx.export.annotation.ManagedNotifications;

import java.util.List;

/**
 * <p>
 * FBA订单 Mapper 接口
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Mapper
public interface FbaOrderMapper extends BaseMapper<FbaOrder> {

    List<FbaOrderVO> findList(@Param("form") QueryFbaOrderForm queryFbaOrderForm);

    IPage findByPage(@Param("form")QueryFbaOrderForm queryFbaOrderForm, @Param("page")Page<FbaOrderVO> page);
}
