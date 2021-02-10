package com.jayud.oceanship.service;

import com.jayud.oceanship.po.SeaBookship;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 海运订船表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
public interface ISeaBookshipService extends IService<SeaBookship> {

    /**
     * 根据订单id获取订船信息
     * @param id
     * @return
     */
    SeaBookship getEnableBySeaOrderId(Long id);

    /**
     * 添加/修改订船操作
     * @param seaBookship
     */
    void saveOrUpdateBookShip(SeaBookship seaBookship);

    /**
     * 根据订单id修改订船信息
     * @param id
     * @param seaBookship
     * @return
     */
    boolean updateBySeaOrderId(Long id, SeaBookship seaBookship);
}
