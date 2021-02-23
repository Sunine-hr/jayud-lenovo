package com.jayud.oms.service;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddCostTypeForm;
import com.jayud.oms.model.bo.AddDictTypeForm;
import com.jayud.oms.model.bo.QueryDictTypeForm;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.po.Dict;
import com.jayud.oms.model.po.DictType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 字典类型 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
public interface IDictTypeService extends IService<DictType> {


    public IPage<DictType> findByPage(QueryDictTypeForm form);

    /**
     * 校验唯一性
     * @return
     */
    public boolean checkUnique(DictType dictType);



}
