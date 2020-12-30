package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomsDataForm;
import com.jayud.mall.model.bo.QueryCustomsDataForm;
import com.jayud.mall.model.po.CustomsData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.CustomsDataVO;

import java.util.List;

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

    /**
     * 保存报关资料
     * @param form
     * @return
     */
    CommonResult saveCustomsData(CustomsDataForm form);

    /**
     * 查询报关商品资料list
     * @return
     */
    List<CustomsDataVO> findCustomsData();
}
