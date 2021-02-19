package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.mapper.CostTypeMapper;
import com.jayud.oms.model.bo.AddCostTypeForm;
import com.jayud.oms.model.bo.QueryCostTypeForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.po.WarehouseInfo;
import com.jayud.oms.model.vo.CostTypeVO;
import com.jayud.oms.service.ICostTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 费用类型，暂时废弃 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@Service
public class CostTypeServiceImpl extends ServiceImpl<CostTypeMapper, CostType> implements ICostTypeService {

    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    @Override
    public IPage<CostTypeVO> findCostTypeByPage(QueryCostTypeForm form) {
        Page<CostType> page = new Page(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findCostTypeByPage(page, form);
    }

    /**
     * 根据id集合查询费用类型
     */
    @Override
    public List<CostTypeVO> getCostTypeByIds(List<Long> ids) {
        List<CostTypeVO> list = new ArrayList<>();
        List<CostType> costTypes = this.baseMapper.selectBatchIds(ids);
        for (CostType costType : costTypes) {
            CostTypeVO costTypeVO = ConvertUtil.convert(costType, CostTypeVO.class);
            list.add(costTypeVO);
        }

        return list;
    }

    /**
     * 新增编辑费用类别
     *
     * @param form
     * @return
     */
    @Override
    public boolean saveOrUpdateCostType(AddCostTypeForm form) {
        CostType costType = ConvertUtil.convert(form, CostType.class);

        //判断是否代收代垫， 是-费用类别后面加-Y，否则-N
//        String sign = form.getIsPayCollection() ? "-Y" : "-N";

        if (Objects.isNull(costType.getId())) {
            costType
//                    .setCodeName(form.getCodeName() + sign)
                    .setCodeName(form.getCodeName())
                    .setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());
            return this.save(costType);
        } else {
//            String str = form.getCodeName().substring(0, form.getCodeName().indexOf("-"));
            costType.setCodeName(form.getCodeName())
//                    .setCodeName(str + sign)
                    .setCode(null)
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            return this.updateById(costType);
        }
    }

    /**
     * 根据id查询费用类型
     */
    @Override
    public CostTypeVO getById(Long id) {
        CostType costType = this.baseMapper.selectById(id);
        return ConvertUtil.convert(costType, CostTypeVO.class);
    }

    /**
     * 更改启用/禁用状态
     *
     * @param id
     * @return
     */
    @Override
    public boolean enableOrDisableCostType(Long id) {
        //查询当前状态
        QueryWrapper<CostType> condition = new QueryWrapper<>();
        condition.lambda().select(CostType::getStatus).eq(CostType::getId, id);
        CostType tmp = this.baseMapper.selectOne(condition);

        String status = "1".equals(tmp.getStatus()) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();

        CostType costType = new CostType().setId(id).setStatus(status)
                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());

        return this.updateById(costType);
    }


    /**
     * 获取启用费用类别
     */
    @Override
    public List<CostType> getEnableCostType() {
        QueryWrapper<CostType> condition = new QueryWrapper<>();
        condition.lambda().eq(CostType::getStatus, StatusEnum.ENABLE.getCode());
        return this.baseMapper.selectList(condition);
    }

    /**
     * 校验唯一性
     *
     * @return
     */
    @Override
    public boolean checkUnique(CostType costType) {
        QueryWrapper<CostType> condition = new QueryWrapper<>();
        if (costType.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(CostType::getId, costType.getId())
                    .eq(CostType::getCodeName, costType.getCodeName()));
            int count = this.count(condition);
            //匹配到自己名称,不进行唯一校验
            if (count == 0) {
                condition = new QueryWrapper<>();
                condition.lambda().eq(CostType::getCodeName, costType.getCodeName());
                return this.count(condition) > 0;
            } else {
                return false;
            }
        } else {
            condition.lambda().eq(CostType::getCode, costType.getCode())
                    .or().eq(CostType::getCodeName, costType.getCodeName());
            return this.count(condition) > 0;
        }

    }

}
