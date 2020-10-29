package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.mapper.CostTypeMapper;
import com.jayud.oms.model.bo.AddCostTypeForm;
import com.jayud.oms.model.bo.QueryCostTypeForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.vo.CostTypeVO;
import com.jayud.oms.service.ICostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RedisUtils redisUtils;

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
    public List<CostTypeVO> findCostTypeByIds(List<Long> ids) {
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
        String loginUser = redisUtils.get("loginUser", 100);
        CostType costType = ConvertUtil.convert(form, CostType.class);

        //判断是否代收代垫， 是-费用类别后面加-Y，否则-N
        String codeName = form.getIsPayCollection() ? form.getCodeName() + "-Y" : form.getCodeName() + "-N";
        costType.setCodeName(codeName);

        if (Objects.isNull(costType.getId())) {
            costType.setCreateTime(LocalDateTime.now());
            costType.setCreateUser(loginUser);
            return this.save(costType);
        } else {
            costType.setUpdateTime(LocalDateTime.now());
            costType.setUpdateUser(loginUser);
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
     * 更新为无效状态
     * @param ids
     * @return
     */
    @Override
    public boolean deleteByIds(List<Long> ids) {
        String loginUser = redisUtils.get("loginUser", 100);
        List<CostType> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        ids.stream().forEach(id -> {
            list.add(new CostType().setId(id).setStatus(StatusEnum.INVALID.getCode())
                    .setUpdateTime(now).setUpdateUser(loginUser));
        });
        return this.updateBatchById(list);
    }


}
