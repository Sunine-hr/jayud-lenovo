package com.jayud.tools.service;

import com.jayud.tools.model.po.SensitiveCommodity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 敏感品名表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
public interface ISensitiveCommodityService extends IService<SensitiveCommodity> {

    /**
     * 查询敏感品名list
     * @return
     */
    List<SensitiveCommodity> getSensitiveCommodityList();
}
