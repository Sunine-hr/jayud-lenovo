package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.QueryDictForm;
import com.jayud.oms.model.bo.QueryDictTypeForm;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.po.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.po.DictType;
import com.jayud.oms.model.vo.DictVO;

import java.util.List;

/**
 * <p>
 * 字典 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
public interface IDictService extends IService<Dict> {


    public IPage<DictVO> findByPage(QueryDictForm form);

    /**
     * 校验唯一性
     *
     * @return
     */
    public boolean checkUnique(Dict dict);

    boolean enableOrDisable(Integer id);

    /**
     * 根据字典类型code查询
     */
    public List<Dict> getByDictTypeCode(String dictTypeCode);
}
