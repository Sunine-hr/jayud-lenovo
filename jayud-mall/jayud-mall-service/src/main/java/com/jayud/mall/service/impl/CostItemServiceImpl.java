package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CostItemMapper;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.CostItem;
import com.jayud.mall.model.vo.CostItemVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ICostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 费用项目 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-28
 */
@Service
public class CostItemServiceImpl extends ServiceImpl<CostItemMapper, CostItem> implements ICostItemService {

    @Autowired
    CostItemMapper costItemMapper;

    @Autowired
    BaseService baseService;

    @Override
    public List<CostItemVO> findCostItem(CostItemForm form) {
        QueryWrapper<CostItem> queryWrapper = new QueryWrapper<>();
        Long id = form.getId();
        if(id != null){
            queryWrapper.eq("id", id);
        }
        String identifying = form.getIdentifying();
        if(identifying != null && identifying != ""){
            queryWrapper.eq("identifying", identifying);
        }
        String costCode = form.getCostCode();
        if(costCode != null && costCode != ""){
            queryWrapper.like("cost_code", costCode);
        }
        String costName = form.getCostName();
        if(costName != null && costName != ""){
            queryWrapper.like("cost_name", costName);
        }
        String status = form.getStatus();
        if(status != null && status != ""){
            queryWrapper.eq("status", status);
        }
        List<String> notCostCodes = form.getNotCostCodes();
        if(notCostCodes != null && notCostCodes.size()>0){
            queryWrapper.notIn("cost_code", notCostCodes);
        }
        List<CostItem> costItems = costItemMapper.selectList(queryWrapper);
        List<CostItemVO> costItemVOS = ConvertUtil.convertList(costItems, CostItemVO.class);
        return costItemVOS;
    }

    @Override
    public List<CostItemVO> findCostItemBySupId(CostItemSupForm form) {
        List<CostItemVO> list = costItemMapper.findCostItemBySupId(form);
        return list;
    }

    @Override
    public IPage<CostItemVO> findCostItemByPage(QueryCostItemForm form) {
        //定义分页参数
        Page<CostItemVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<CostItemVO> pageInfo = costItemMapper.findCostItemByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CostItemVO saveCostItem(CostItemForm form) {
        AuthUser user = baseService.getUser();
        if(ObjectUtil.isEmpty(user)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录。");
        }
        Long id = form.getId();
        CostItem costItem = ConvertUtil.convert(form, CostItem.class);
        String costCode = costItem.getCostCode();
        if(ObjectUtil.isEmpty(id)){
            //新增
            QueryWrapper<CostItem> qw = new QueryWrapper<>();
            qw.eq("cost_code", costCode);
            List<CostItem> list = this.list(qw);
            if(CollUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "代码已存在，请重新输入。");
            }
        }else{
            //修改
            CostItemVO costItemVO = costItemMapper.findCostItemById(id);
            if(ObjectUtil.isEmpty(costItemVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "费用不存在");
            }

            QueryWrapper<CostItem> qw = new QueryWrapper<>();
            qw.ne("id", id);
            qw.eq("cost_code", costCode);
            List<CostItem> list = this.list(qw);
            if(CollUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "代码已存在，请重新输入。");
            }
        }
        costItem.setUserId(user.getId().intValue());
        costItem.setUserName(user.getName());
        costItem.setCreateTime(LocalDateTime.now());
        this.saveOrUpdate(costItem);
        CostItemVO costItemVO = ConvertUtil.convert(costItem, CostItemVO.class);
        return costItemVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopOrEnabled(CostItemStatusForm form) {
        Long id = form.getId();
        CostItemVO costItemVO = costItemMapper.findCostItemById(id);
        if(ObjectUtil.isEmpty(costItemVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "费用不存在");
        }
        String status = form.getStatus();
        CostItem costItem = ConvertUtil.convert(costItemVO, CostItem.class);
        costItem.setStatus(status);
        this.saveOrUpdate(costItem);
    }

    @Override
    public List<CostItemVO> findCostItemByServiceId(SupplierCostServerIdForm form) {
        Long serviceId = form.getServiceId();
        return costItemMapper.findCostItemByServiceId(serviceId);
    }

}
