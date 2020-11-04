package com.jayud.oms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.mapper.ProductBizMapper;
import com.jayud.oms.model.bo.AddProductBizForm;
import com.jayud.oms.model.bo.QueryProductBizForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostGenre;
import com.jayud.oms.model.po.ProductBiz;
import com.jayud.oms.model.vo.CostGenreVO;
import com.jayud.oms.model.vo.ProductBizVO;
import com.jayud.oms.service.ICostGenreService;
import com.jayud.oms.service.IProductBizService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ProductBizServiceImpl extends ServiceImpl<ProductBizMapper, ProductBiz> implements IProductBizService {

    @Autowired
    private ICostGenreService costGenreService;


    @Override
    public List<ProductBiz> findProductBiz() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", "1");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public ProductBiz getProductBizByCode(String idCode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", "1");//有效的
        queryWrapper.eq("id_code", idCode);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 列表分页查询
     *
     * @param form
     * @return
     */
    @Override
    public IPage<ProductBizVO> findProductBizByPage(QueryProductBizForm form) {
        Page<ProductBiz> page = new Page(form.getPageNum(), form.getPageSize());
        IPage<ProductBizVO> iPage = this.baseMapper.findProductBizByPage(page, form);
        //根据费用类型id集合查询费用类型名称

        for (ProductBizVO record : iPage.getRecords()) {
            if (StringUtils.isEmpty(record.getCostGenreIds())) {
                continue;
            }
            String[] tmp = record.getCostGenreIds().split(",");
            List<Long> cids = new ArrayList<>(tmp.length);
            for (String id : tmp) {
                cids.add(Long.parseLong(id));
            }
            List<CostGenre> costGenres = this.costGenreService.getByIds(cids);
            List<CostGenreVO> costGenreVOs = new ArrayList<>();
            for (CostGenre costGenre : costGenres) {
                costGenreVOs.add(ConvertUtil.convert(costGenre, CostGenreVO.class));
            }
            record.setCostGenreVOs(costGenreVOs);
        }

        return iPage;
    }

    /**
     * 根据id查询业务类型
     */
    @Override
    public ProductBizVO getById(Long id) {
        ProductBiz productBiz = this.baseMapper.selectById(id);
        ProductBizVO productBizVO = ConvertUtil.convert(productBiz, ProductBizVO.class);
        if (StringUtils.isNotEmpty(productBizVO.getCostGenreIds())) {
            String[] tmp = productBizVO.getCostGenreIds().split(",");
            List<Long> cids = new ArrayList<>();
            for (String cid : tmp) {
                cids.add(Long.parseLong(cid));
            }
            List<CostGenre> costGenres = this.costGenreService.getByIds(cids);
            List<CostGenreVO> costGenreVOs = new ArrayList<>();
            for (CostGenre costGenre : costGenres) {
                costGenreVOs.add(ConvertUtil.convert(costGenre, CostGenreVO.class));
            }
            productBizVO.setCostGenreVOs(costGenreVOs);
        }

        return productBizVO;
    }

    /**
     * 新增编辑业务类型
     *
     * @param form
     * @return
     */
    @Override
    public boolean saveOrUpdateProductBiz(AddProductBizForm form) {
        //拼接费用类型id
        StringBuilder sb = new StringBuilder();
        for (CostGenreVO costGenreVO : form.getCostGenreVOs()) {
            sb.append(costGenreVO.getId()).append(",");
        }
        ProductBiz productBiz = ConvertUtil.convert(form, ProductBiz.class);
        productBiz.setCostGenreIds(sb.substring(0, sb.length() - 1));

        if (Objects.isNull(productBiz.getId())) {
            productBiz.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken());
            return this.save(productBiz);
        } else {
            productBiz.setIdCode(null)
                    .setUpdateTime(LocalDateTime.now())
                    .setUpdateUser(UserOperator.getToken());
            return this.updateById(productBiz);
        }
    }

    /**
     * 更改启用/禁用业务类型状态
     *
     * @param id
     * @return
     */
    @Override
    public boolean enableOrDisableProductBiz(Long id) {
        //查询当前状态
        QueryWrapper<ProductBiz> condition = new QueryWrapper<>();
        condition.lambda().select(ProductBiz::getStatus).eq(ProductBiz::getId, id);
        ProductBiz tmp = this.baseMapper.selectOne(condition);

        String status = "1".equals(tmp.getStatus()) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();

        ProductBiz productBiz = new ProductBiz().setId(id).setStatus(status)
                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());

        return this.updateById(productBiz);
    }

}
