package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.SeedingWall;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 播种墙信息 服务类
 *
 * @author jyd
 * @since 2021-12-16
 */
public interface ISeedingWallService extends IService<SeedingWall> {

    /**
     * 分页查询
     *
     * @param seedingWall
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    IPage<SeedingWall> selectPage(SeedingWall seedingWall,
                                  Integer pageNo,
                                  Integer pageSize,
                                  HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param seedingWall
     * @return
     */
    List<SeedingWall> selectList(SeedingWall seedingWall);

    /**
     * 保存
     * @param seedingWall
     */
    SeedingWall saveOrUpdateSeedingWall(SeedingWall seedingWall);

    /**
     * 删除
     * @param id
     */
    void delSeedingWall(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> querySeedingWallForExcel(Map<String, Object> paramMap);
}
