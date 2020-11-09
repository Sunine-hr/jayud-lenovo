package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryCustomsDataForm;
import com.jayud.mall.model.po.CustomsData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CustomsDataVO;

/**
 * <p>
 * 报关资料表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
public interface ICustomsDataService extends IService<CustomsData> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<CustomsDataVO> findCustomsDataByPage(QueryCustomsDataForm form);
}
