package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.QueryLineForm;
import com.jayud.oms.model.bo.QueryPlatformForm;
import com.jayud.oms.model.po.Platform;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.PlatformDetailsVO;
import com.jayud.oms.model.vo.PlatformVO;

/**
 * <p>
 * 月台管理 服务类
 * </p>
 *
 * @author CYC
 * @since 2021-10-23
 */
public interface IPlatformService extends IService<Platform> {

    /**
     * 分页查询月台
     * @param form
     * @return
     */
    IPage<PlatformVO> findPlatformByPage(QueryPlatformForm form);

    /**
     * 校验唯一性
     * @param platform
     * @return
     */
    boolean checkUniqueByName(Platform platform);

    /**
     * 校验唯一性
     * @param platformTemp
     * @return
     */
    boolean checkUniqueByCode(Platform platformTemp);

    /**
     * 获取月台编号
     * @return
     */
    String autoGenerateNum();

    /**
     * 新增编辑月台
     * @param platform
     * @return
     */
    boolean saveOrUpdatePlatform(Platform platform);

    /**
     * 检查月台是否存在
     * @param id
     * @return
     */
    boolean checkExists(Long id);

    /**
     * 删除月台
     * @param id
     */
    void delPlatform(Long id);

    /**
     * 查看月台详情
     * @param id
     * @return
     */
    PlatformDetailsVO getPlatformDetails(Long id);
}
