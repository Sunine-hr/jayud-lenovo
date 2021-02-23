package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.mapper.DictTypeMapper;
import com.jayud.oms.model.bo.QueryDictTypeForm;
import com.jayud.oms.model.po.DictType;
import com.jayud.oms.service.IDictTypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典类型 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements IDictTypeService {

    @Override
    public IPage<DictType> findByPage(QueryDictTypeForm form) {
        Page<DictType> page = new Page<>(form.getPageNum(), form.getPageSize());
        QueryWrapper<DictType> condition = new QueryWrapper<>();
        if (!StringUtils.isEmpty(form.getName())) {
            condition.lambda().eq(DictType::getName, form.getName());
        }
        return this.page(page, condition);
    }


    /**
     * 校验唯一性
     *
     * @return
     */
    @Override
    public boolean checkUnique(DictType dictType) {
        QueryWrapper<DictType> condition = new QueryWrapper<>();
        if (dictType.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(DictType::getId, dictType.getId())
                    .eq(DictType::getName, dictType.getName()));
            int count = this.count(condition);
            //匹配到自己名称,不进行唯一校验
            if (count == 0) {
                condition = new QueryWrapper<>();
                condition.lambda().eq(DictType::getName, dictType.getName());
                return this.count(condition) > 0;
            } else {
                return false;
            }
        } else {
            condition.lambda().eq(DictType::getCode, dictType.getCode())
                    .or().eq(DictType::getName, dictType.getName());
            return this.count(condition) > 0;
        }

    }


}
