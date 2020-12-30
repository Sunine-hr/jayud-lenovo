package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CustomsClearanceForm;
import com.jayud.mall.model.bo.QueryCustomsClearanceForm;
import com.jayud.mall.model.po.CustomsClearance;
import com.jayud.mall.model.vo.CustomsClearanceVO;

import java.util.List;

/**
 * <p>
 * 清关资料表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
public interface ICustomsClearanceService extends IService<CustomsClearance> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<CustomsClearanceVO> findCustomsClearanceByPage(QueryCustomsClearanceForm form);

    /**
     * 保存清关资料
     * @param form
     * @return
     */
    CommonResult saveCustomsData(CustomsClearanceForm form);

    /**
     * 查询清关商品资料list
     * @return
     */
    List<CustomsClearanceVO> findCustomsClearance();
}
