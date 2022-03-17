package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.LockReLocation;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 推荐库位锁定 服务类
 *
 * @author jyd
 * @since 2022-01-17
 */
public interface ILockReLocationService extends IService<LockReLocation> {

    /**
    *  分页查询
    * @param lockReLocation
    * @param req
    * @return
    */
    IPage<LockReLocation> selectPage(LockReLocation lockReLocation,
                                    Integer pageNo,
                                    Integer pageSize,
                                    HttpServletRequest req);

    /**
    *  查询列表
    * @param lockReLocation
    * @return
    */
    List<LockReLocation> selectList(LockReLocation lockReLocation);

    /**
     * 保存(新增+编辑)
     * @param lockReLocation
     */
    LockReLocation saveOrUpdateLockReLocation(LockReLocation lockReLocation);

    /**
     * 逻辑删除
     * @param id
     */
    void delLockReLocation(int id);

    /**
     * 查询导出
     * @param paramMap
     * @return
     */
    List<LinkedHashMap<String, Object>> queryLockReLocationForExcel(Map<String, Object> paramMap);


}
