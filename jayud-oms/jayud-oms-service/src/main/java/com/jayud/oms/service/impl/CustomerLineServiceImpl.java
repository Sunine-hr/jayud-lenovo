package com.jayud.oms.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.AddCustomerLineForm;
import com.jayud.oms.model.bo.QueryCustomerLineForm;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.po.CustomerLine;
import com.jayud.oms.mapper.CustomerLineMapper;
import com.jayud.oms.model.po.CustomerLineRelation;
import com.jayud.oms.model.vo.CustomerLineDetailsVO;
import com.jayud.oms.model.vo.CustomerLineVO;
import com.jayud.oms.service.ICustomerLineRelationService;
import com.jayud.oms.service.ICustomerLineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 客户线路管理 服务实现类
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
@Service
public class CustomerLineServiceImpl extends ServiceImpl<CustomerLineMapper, CustomerLine> implements ICustomerLineService {

    @Autowired
    private ICustomerLineRelationService customerLineRelationService;

    /**
     * 分页查询客户线路
     * @param form
     * @return
     */
    @Override
    public IPage<CustomerLineVO> findCustomerLineByPage(QueryCustomerLineForm form) {
        Page page = new Page(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findCustomerLineByPage(page, form);
    }

    /**
     * 检查是否已存在
     * @param id
     * @return
     */
    @Override
    public boolean checkExists(Long id) {
        return this.count(new QueryWrapper<CustomerLine>().lambda().eq(CustomerLine::getId, id)) > 0;
    }

    /**
     * 删除客户线路
     * @param id
     */
    @Override
    public void delLine(Long id) {
        this.remove(new QueryWrapper<CustomerLine>().lambda().eq(CustomerLine::getId, id));
    }

    /**
     * 查看客户线路详情
     * @param id
     * @return
     */
    @Override
    public CustomerLineDetailsVO getCustomerLineDetails(Long id) {
        return this.baseMapper.getCustomerLineDetails(id);
    }

    /**
     * 新增编辑客户线路
     * @param form
     * @return
     */
    @Override
    @Transactional
    public boolean saveOrUpdateCustomerLine(AddCustomerLineForm form) {

        CustomerLine customerLine = ConvertUtil.convert(form, CustomerLine.class);

        boolean result = false;
        boolean isSave = false;
        if (Objects.isNull(customerLine.getId())) {
            customerLine.setCreateTime(LocalDateTime.now())
                    .setCreatedUser(UserOperator.getToken());
            result = this.save(customerLine);
            isSave = true;
        } else {
            customerLine
                    .setUpTime(LocalDateTime.now())
                    .setUpUser(UserOperator.getToken());
            result = this.updateById(customerLine);
        }
        if (!result) {
            return result;
        }
        if (isSave) {
            if (CollUtil.isNotEmpty(form.getCustomerLineRelations())) {
                for (CustomerLineRelation customerLineRelation : form.getCustomerLineRelations()) {
                    customerLineRelation.setId(null);
                    customerLineRelation.setCustomerLineId(customerLine.getId());
                }
                customerLineRelationService.saveBatch(form.getCustomerLineRelations());
            }
        } else {
            // 先清除列表
            customerLineRelationService.deleteByCustomerLineId(form.getLineId());
            if (CollUtil.isNotEmpty(form.getCustomerLineRelations())) {
                for (CustomerLineRelation customerLineRelation : form.getCustomerLineRelations()) {
                    customerLineRelation.setId(null);
                    customerLineRelation.setCustomerLineId(customerLine.getId());
                }
                customerLineRelationService.saveBatch(form.getCustomerLineRelations());
            }
        }

        return result;
    }

    /**
     * 客户线路唯一性检查
     * @param customerLineTemp
     */
    @Override
    public void checkUnique(CustomerLine customerLineTemp) {
        boolean exists = exitName(customerLineTemp.getId(), customerLineTemp.getCustomerLineName());
        if (exists) {
            throw new JayudBizException(400, "客户线路名称已存在");
        }
        exists = exitCode(customerLineTemp.getId(), customerLineTemp.getCustomerLineCode());
        if (exists) {
            throw new JayudBizException(400, "客户线路编号已存在");
        }
    }

    /**
     * 检查客户线路名称是否存储
     * @param id
     * @param customerLineName
     * @return
     */
    @Override
    public boolean exitName(Long id, String customerLineName) {
        QueryWrapper<CustomerLine> condition = new QueryWrapper<>();
        condition.lambda().eq(CustomerLine::getCustomerLineName, customerLineName);
        if (id != null) {
            condition.lambda().ne(CustomerLine::getId, id);
        }
        return this.count(condition) > 0;
    }

    /**
     * 检查客户线路编号是否存储
     * @param id
     * @param customerLineCode
     * @return
     */
    @Override
    public boolean exitCode(Long id, String customerLineCode) {
        QueryWrapper<CustomerLine> condition = new QueryWrapper<>();
        condition.lambda().eq(CustomerLine::getCustomerLineName, customerLineCode);
        if (id != null) {
            condition.lambda().ne(CustomerLine::getId, id);
        }
        return this.count(condition) > 0;
    }
}
