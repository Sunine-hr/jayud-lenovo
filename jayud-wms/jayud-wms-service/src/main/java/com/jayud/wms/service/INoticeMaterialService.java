package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.NoticeMaterial;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通知单物料信息 服务类
 *
 * @author jyd
 * @since 2021-12-16
 */
public interface INoticeMaterialService extends IService<NoticeMaterial> {

    /**
     * 分页查询
     *
     * @param noticeMaterial
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    IPage<NoticeMaterial> selectPage(NoticeMaterial noticeMaterial,
                                     Integer pageNo,
                                     Integer pageSize,
                                     HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param noticeMaterial
     * @return
     */
    List<NoticeMaterial> selectList(NoticeMaterial noticeMaterial);

    List<NoticeMaterial> getByCondition(NoticeMaterial condition);
}
