package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddCostGenreForm;
import com.jayud.oms.model.bo.QueryCostGenreForm;
import com.jayud.oms.model.po.CostGenre;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.CostGenreVO;
import com.jayud.oms.model.vo.CostTypeVO;

/**
 * <p>
 * 基础数据费用类型 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-30
 */
public interface ICostGenreService extends IService<CostGenre> {

    /**
     * 分页查询费用类型
     */
    IPage<CostGenreVO> findCostGenreByPage(QueryCostGenreForm form);

    /**
     * 新增编辑费用类型
     */
    boolean saveOrUpdateCostGenre(AddCostGenreForm form);

    /**
     * 更改启用/禁用费用类型状态
     * @param id
     * @return
     */
    public boolean enableOrDisableCostGenre(Long id);

    /**
     * 根据id查询费用类型
     */
    CostGenreVO getById(Long id);
}
