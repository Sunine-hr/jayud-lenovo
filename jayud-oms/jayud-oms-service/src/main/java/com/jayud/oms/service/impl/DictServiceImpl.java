package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.oms.model.bo.QueryDictForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostGenre;
import com.jayud.oms.model.po.Dict;
import com.jayud.oms.mapper.DictMapper;
import com.jayud.oms.model.vo.DictVO;
import com.jayud.oms.service.IDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 字典 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-02-23
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Override
    public IPage<DictVO> findByPage(QueryDictForm form) {
        Page<Dict> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }


    /**
     * 校验唯一性
     *
     * @return
     */
    @Override
    public boolean checkUnique(Dict dict) {
        QueryWrapper<Dict> condition = new QueryWrapper<>();
        if (dict.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(Dict::getId, dict.getId())
                    .eq(Dict::getValue, dict.getValue()));
            int count = this.count(condition);
            //匹配到自己名称,不进行唯一校验
            if (count == 0) {
                condition = new QueryWrapper<>();
                condition.lambda().eq(Dict::getValue, dict.getValue());
                return this.count(condition) > 0;
            } else {
                return false;
            }
        } else {
            condition.lambda().eq(Dict::getCode, dict.getCode())
                    .or().eq(Dict::getValue, dict.getValue());
            return this.count(condition) > 0;
        }

    }

    /**
     * 更改启用/禁用费用类型状态
     *
     * @param id
     * @return
     */
    @Override
    public boolean enableOrDisable(Integer id) {
        //查询当前状态
        QueryWrapper<Dict> condition = new QueryWrapper<>();
        condition.lambda().select(Dict::getStatus).eq(Dict::getId, id);
        Dict tmp = this.baseMapper.selectOne(condition);

        String status = "1".equals(tmp.getStatus()) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();

        Dict dict = new Dict().setId(id).setStatus(status);

        return this.updateById(dict);
    }

    /**
     * 根据字典类型code查询
     */
    @Override
    public List<Dict> getByDictTypeCode(String dictTypeCode) {
        QueryWrapper<Dict> condition = new QueryWrapper<>();
        condition.lambda().eq(Dict::getDictTypeCode, dictTypeCode);
        return this.baseMapper.selectList(condition);
    }
}
