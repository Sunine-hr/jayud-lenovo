package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.SeedingWallLayout;
import com.jayud.wms.model.vo.SeedingWallLayoutTwoVo;
import com.jayud.wms.model.vo.SeedingWallLayoutVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 播种位布局 服务类
 *
 * @author jyd
 * @since 2021-12-16
 */
public interface ISeedingWallLayoutService extends IService<SeedingWallLayout> {

    /**
     * 分页查询
     *
     * @param seedingWallLayout
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    IPage<SeedingWallLayout> selectPage(SeedingWallLayout seedingWallLayout,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req);

    /**
     * 查询列表
     *
     * @param seedingWallLayout
     * @return
     */
    List<SeedingWallLayout> selectList(SeedingWallLayout seedingWallLayout);

    /**
     * 根据播种墙id,获取播种墙的播种位布局
     * @param seedingWallId
     * @return
     */
    SeedingWallLayoutVo queryBySeedingWallId(Integer seedingWallId);

    /**
     * 保存播种墙的播种位布局
     * @param bo
     */
    void saveSeedingWallLayout(SeedingWallLayoutVo bo);

    List<SeedingWallLayoutTwoVo> sketchMap(String workbenchCode, Long type);

    SeedingWallLayoutTwoVo queryBySeedingWallId(int seedingWallId, int status);
}
