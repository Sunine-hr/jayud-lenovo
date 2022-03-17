package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.bo.ContainerForm;
import com.jayud.wms.model.po.Container;
import com.jayud.wms.model.vo.ContainerVO;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 容器信息 服务类
 *
 * @author jyd
 * @since 2021-12-13
 */
public interface IContainerService extends IService<Container> {

    /**
     * 分页查询
     *
     * @param containerForm
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    IPage<ContainerVO> selectPage(ContainerForm containerForm,
                                  Integer pageNo,
                                  Integer pageSize,
                                  HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param container
     * @return
     */
    List<ContainerVO> selectList(ContainerForm container);

    /**
     * \
     * 新增或者修改
     *
     * @param containerForm
     * @return
     */
    boolean saveOrUpdateContainerForm(ContainerForm containerForm);


    /**
     * 根据需求查询单个容器信息
     */
    ContainerForm getContainerOne(@Param("code") String code, @Param("id") Long id, @Param("createBy") String createBy, @Param("warehouseId") Long warehouseId);

    // 导出
    List<LinkedHashMap<String, Object>> queryContainerForExcel(ContainerForm containerForm,
                                                               Integer pageNo,
                                                               Integer pageSize,
                                                               HttpServletRequest req);

    List<Container> getEnableByWarehouseId(Long warehouseId);
}
