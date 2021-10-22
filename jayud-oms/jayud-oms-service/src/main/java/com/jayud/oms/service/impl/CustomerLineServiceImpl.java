package com.jayud.oms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.AddCustomerLineForm;
import com.jayud.oms.model.bo.QueryCustomerLineForm;
import com.jayud.oms.model.po.*;
import com.jayud.oms.mapper.CustomerLineMapper;
import com.jayud.oms.model.vo.CustomerLineDetailsVO;
import com.jayud.oms.model.vo.CustomerLineVO;
import com.jayud.oms.service.ICustomerLineRelationService;
import com.jayud.oms.service.ICustomerLineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.service.IDriverInfoService;
import com.jayud.oms.service.ILineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private IDriverInfoService driverInfoService;

    @Autowired
    private ILineService lineService;

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
    @Transactional
    public void delLine(Long id) {
        customerLineRelationService.deleteByCustomerLineId(id);
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

        // 司机信息
        DriverInfo driverInfo = driverInfoService.getOne(new QueryWrapper<DriverInfo>().lambda()
                .select(DriverInfo::getName)
                .eq(DriverInfo::getId, form.getDriverInfoId()));
        if (driverInfo == null) {
            throw new JayudBizException(400, "查询不到司机信息");
        }
        // 线路信息
        Line line = lineService.getOne(new QueryWrapper<Line>().lambda()
                .select(Line::getLineName)
                .eq(Line::getId, form.getLineId()));
        if (line == null) {
            throw new JayudBizException(400, "查询不到线路信息");
        }
        customerLine.setDriverName(driverInfo.getName());
        customerLine.setLineName(line.getLineName());

        // 线路规则
        if (form.getLineRules() != null && form.getLineRules().size() > 0) {
            customerLine.setLineRule(CollUtil.join(form.getLineRules(), ","));
        }

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
            customerLineRelationService.deleteByCustomerLineId(form.getId());
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
        condition.lambda().eq(CustomerLine::getCustomerLineCode, customerLineCode);
        if (id != null) {
            condition.lambda().ne(CustomerLine::getId, id);
        }
        return this.count(condition) > 0;
    }

    /**
     * 该线路存在客户线路关联数据
     * @param id
     * @return
     */
    @Override
    public boolean isExistLineRelation(Long id) {
        QueryWrapper<CustomerLine> condition = new QueryWrapper<>();
        if (id != null) {
            condition.lambda().eq(CustomerLine::getLineId, id);
        }
        return this.count(condition) > 0;
    }

    /**
     * 获取客户线路编号
     * @return
     */
    @Override
    public String autoGenerateNum() {
        StringBuilder orderNo = new StringBuilder();
        String curDate = DateUtils.LocalDateTime2Str(LocalDateTime.now(), "yyyyMMdd");
        Map<String, Object> customerLine = this.baseMapper.getLastCodeByCreateTime(curDate);
        int index = 1;
        if (customerLine != null) {
            String sn = MapUtil.getStr(customerLine, "code").substring(10);
            index = Integer.parseInt(sn) + 1;
        }
        orderNo.append("KH").append(curDate).append(StringUtils.zeroComplement(4, index));
        return orderNo.toString();
    }
}
