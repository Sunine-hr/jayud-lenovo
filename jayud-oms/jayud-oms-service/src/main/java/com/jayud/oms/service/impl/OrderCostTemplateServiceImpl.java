package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.model.bo.OrderCostTemplateDTO;
import com.jayud.oms.model.bo.OrderCostTemplateInfoDTO;
import com.jayud.oms.model.bo.QueryCostTemplateForm;
import com.jayud.oms.model.enums.StatusEnum;
import com.jayud.oms.model.po.CostInfo;
import com.jayud.oms.model.po.CurrencyInfo;
import com.jayud.oms.model.po.OrderCostTemplate;
import com.jayud.oms.mapper.OrderCostTemplateMapper;
import com.jayud.oms.model.po.OrderCostTemplateInfo;
import com.jayud.oms.model.vo.InitComboxStrVO;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.service.IOrderCostTemplateInfoService;
import com.jayud.oms.service.IOrderCostTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 费用模板 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-04-08
 */
@Service
public class OrderCostTemplateServiceImpl extends ServiceImpl<OrderCostTemplateMapper, OrderCostTemplate> implements IOrderCostTemplateService {

    @Autowired
    private IOrderCostTemplateInfoService orderCostTemplateInfoMapper;

    /**
     * 添加/编辑
     *
     * @param orderCostTemplateDTO
     */
    @Override
    @Transactional
    public void saveOrUpdateInfo(OrderCostTemplateDTO orderCostTemplateDTO) {
        OrderCostTemplate costTemplate = ConvertUtil.convert(orderCostTemplateDTO, OrderCostTemplate.class);

        //清除之前数据
        if (costTemplate.getId() != null) {
            orderCostTemplateInfoMapper.deleteByTemplateId(costTemplate.getId());
        }

        if (costTemplate.getId() == null) {
            costTemplate.setCreateTime(LocalDateTime.now())
                    .setCreateUser(UserOperator.getToken())
                    .setStatus(Integer.valueOf(StatusEnum.ENABLE.getCode()));
        }

        this.saveOrUpdate(costTemplate);

        //批量新增/修改
        List<OrderCostTemplateInfoDTO> costTemplateInfo = orderCostTemplateDTO.getCostTemplateInfo();
        List<OrderCostTemplateInfo> costTemplateInfos = new ArrayList<>();
        for (OrderCostTemplateInfoDTO orderCostTemplateInfoDTO : costTemplateInfo) {
            OrderCostTemplateInfo templateInfo = ConvertUtil.convert(orderCostTemplateInfoDTO, OrderCostTemplateInfo.class);
            if (templateInfo.getId() == null) {
                templateInfo.setCreateTime(LocalDateTime.now());
            }
            templateInfo.setOrderCostTemplateId(costTemplate.getId());
            costTemplateInfos.add(templateInfo);
        }

        this.orderCostTemplateInfoMapper.saveOrUpdateBatch(costTemplateInfos);
    }


    @Override
    public IPage<OrderCostTemplateDTO> findByPage(QueryCostTemplateForm form) {
        Page<OrderCostTemplateDTO> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    /**
     * 更改启用/禁用状态
     *
     * @param id
     * @return
     */
    @Override
    public boolean enableOrDisableCostInfo(Long id) {
        //查询当前状态
        QueryWrapper<OrderCostTemplate> condition = new QueryWrapper<>();
        condition.lambda().select(OrderCostTemplate::getStatus).eq(OrderCostTemplate::getId, id);
        OrderCostTemplate tmp = this.baseMapper.selectOne(condition);

        String status = Integer.valueOf(StatusEnum.ENABLE.getCode()).equals(tmp.getStatus()) ? StatusEnum.INVALID.getCode() : StatusEnum.ENABLE.getCode();

        tmp = new OrderCostTemplate().setId(id).setStatus(Integer.valueOf(status));

        return this.updateById(tmp);
    }


    /**
     * 校验唯一
     *
     * @return
     */
    @Override
    public boolean checkUnique(OrderCostTemplate orderCostTemplate) {
        QueryWrapper<OrderCostTemplate> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderCostTemplate::getName, orderCostTemplate.getName());

        List<OrderCostTemplate> orderCostTemplates = this.baseMapper.selectList(condition);
        if (CollectionUtils.isEmpty(orderCostTemplates)) {
            return false;
        }

        OrderCostTemplate costTemplate = orderCostTemplates.get(0);
        if (costTemplate.getId().equals(orderCostTemplate.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public OrderCostTemplateDTO getCostTemplateInfo(Long id) {
        return this.baseMapper.getCostTemplateInfo(id);
    }

    @Override
    public List<InitComboxStrVO> initCostTemplate() {
        QueryWrapper<OrderCostTemplate> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderCostTemplate::getStatus, StatusEnum.ENABLE.getCode())
                .eq(OrderCostTemplate::getCreateUser, UserOperator.getToken());
        List<OrderCostTemplate> tmps = this.list(queryWrapper);
        List<InitComboxStrVO> initComboxVOS = new ArrayList<>();
        for (OrderCostTemplate tmp : tmps) {
            InitComboxStrVO initComboxVO = new InitComboxStrVO();
            initComboxVO.setName(tmp.getName());
            initComboxVO.setNote(String.valueOf(tmp.getType())); //设置费用类型
            initComboxVO.setId(tmp.getId());
            initComboxVOS.add(initComboxVO);
        }
        return initComboxVOS;
    }
}
