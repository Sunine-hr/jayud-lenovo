package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddCostGenreForm;
import com.jayud.oms.model.bo.QueryCostGenreForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.*;
import com.jayud.oms.mapper.CostGenreMapper;
import com.jayud.oms.model.vo.CostGenreVO;
import com.jayud.oms.service.ICostGenreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.service.ICostGenreTaxRateService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 基础数据费用类型 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-30
 */
@Service
public class CostGenreServiceImpl extends ServiceImpl<CostGenreMapper, CostGenre> implements ICostGenreService {

    @Autowired
    private ICostGenreTaxRateService costGenreTaxRateService;

    /**
     * 分页查询费用类型
     */
    @Override
    public IPage<CostGenreVO> findCostGenreByPage(QueryCostGenreForm form) {
        IPage<CostGenreVO> iPage = this.baseMapper.findCostGenreByPage(new Page(form.getPageNum(), form.getPageSize()), form);
        return iPage;
    }

    /**
     * 新增编辑费用类型
     */
    @Override
    public boolean saveOrUpdateCostGenre(AddCostGenreForm form) {
        CostGenre costGenre = ConvertUtil.convert(form, CostGenre.class);
        if (Objects.isNull(costGenre.getId())) {
            costGenre.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());
            return this.save(costGenre);
        } else {
            costGenre.setCode(null)
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            return this.updateById(costGenre);
        }
    }

    /**
     * 更改启用/禁用费用类型状态
     *
     * @param id
     * @return
     */
    @Override
    public boolean enableOrDisableCostGenre(Long id) {
        //查询当前状态
        QueryWrapper<CostGenre> condition = new QueryWrapper<>();
        condition.lambda().select(CostGenre::getStatus).eq(CostGenre::getId, id);
        CostGenre tmp = this.baseMapper.selectOne(condition);

        String status = "1".equals(tmp.getStatus()) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();

        CostGenre costGenre = new CostGenre().setId(id).setStatus(status)
                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());

        return this.updateById(costGenre);
    }

    /**
     * 根据id查询费用类型
     */
    @Override
    public CostGenreVO getById(Long id) {
        CostGenre costGenre = this.baseMapper.selectById(id);
        return ConvertUtil.convert(costGenre, CostGenreVO.class);
    }

    /**
     * 根据id集合查询费用类型
     */
    @Override
    public List<CostGenre> getByIds(List<Long> ids) {
        return this.baseMapper.selectBatchIds(ids);
    }

    /**
     * 获取启用费用类型
     */
    @Override
    public List<CostGenre> getEnableCostGenre() {
        QueryWrapper<CostGenre> condition = new QueryWrapper<>();
        condition.lambda().eq(CostGenre::getStatus, StatusEnum.ENABLE.getCode());
        return this.baseMapper.selectList(condition);
    }

    /**
     * 校验费用类型唯一性
     *
     * @return
     */
    @Override
    public boolean checkUnique(CostGenre costGenre) {
        QueryWrapper<CostGenre> condition = new QueryWrapper<>();
        if (costGenre.getId() != null) {
            //修改过滤自身名字
            condition.lambda().and(tmp -> tmp.eq(CostGenre::getId, costGenre.getId())
                    .eq(CostGenre::getName, costGenre.getName()));
            int count = this.count(condition);
            //匹配到自己名称,不进行唯一校验
            if (count == 0) {
                condition = new QueryWrapper<>();
                condition.lambda().eq(CostGenre::getName, costGenre.getName());
                return this.count(condition) > 0;
            } else {
                return false;
            }
        } else {
            condition.lambda().eq(CostGenre::getCode, costGenre.getCode())
                    .or().eq(CostGenre::getName, costGenre.getName());
            return this.count(condition) > 0;
        }
    }

    @Override
    @Transactional
    public boolean saveOrUpdate(AddCostGenreForm form) {
        CostGenre costGenre = ConvertUtil.convert(form, CostGenre.class);
        boolean opt = true;
        if (Objects.isNull(costGenre.getId())) {
            costGenre.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());
            this.save(costGenre);
            List<CostGenreTaxRate> genreTaxRates = form.getCostGenreTaxRates();
            if (!CollectionUtils.isEmpty(genreTaxRates)) {
                genreTaxRates.forEach(e -> e.setCostGenreId(costGenre.getId()));
                opt = costGenreTaxRateService.saveBatch(genreTaxRates);
            }
            return opt;
        } else {
            costGenre.setCode(null)
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            opt = this.updateById(costGenre);
            List<CostGenreTaxRate> genreTaxRates = form.getCostGenreTaxRates();
            this.costGenreTaxRateService.remove(new QueryWrapper<>(new CostGenreTaxRate().setCostGenreId(costGenre.getId())));
            if (!CollectionUtils.isEmpty(genreTaxRates)) {
                genreTaxRates.forEach(e -> e.setCostGenreId(costGenre.getId()));
                opt = costGenreTaxRateService.saveBatch(genreTaxRates);
            }
            return opt;
        }
    }

    @Override
    public Long getIdByName(String name) {
        QueryWrapper<CostGenre> condition = new QueryWrapper<>();
        condition.lambda().eq(CostGenre::getName, name);
        List<CostGenre> costGenres = this.baseMapper.selectList(condition);
        if (CollectionUtils.isEmpty(costGenres)) {
            return null;
        }
        return costGenres.get(0).getId();
    }
}
