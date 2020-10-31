package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.RedisUtils;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddCostGenreForm;
import com.jayud.oms.model.bo.QueryCostGenreForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostGenre;
import com.jayud.oms.mapper.CostGenreMapper;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.po.ProductBiz;
import com.jayud.oms.model.vo.CostGenreVO;
import com.jayud.oms.model.vo.CostTypeVO;
import com.jayud.oms.service.ICostGenreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
