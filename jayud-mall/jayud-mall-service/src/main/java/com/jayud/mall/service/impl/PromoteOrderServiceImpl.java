package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.PromoteCompanyMapper;
import com.jayud.mall.mapper.PromoteOrderMapper;
import com.jayud.mall.model.bo.QueryPromoteOrderForm;
import com.jayud.mall.model.bo.SavePromoteOrderForm;
import com.jayud.mall.model.po.PromoteOrder;
import com.jayud.mall.model.vo.PromoteCompanyVO;
import com.jayud.mall.model.vo.PromoteOrderVO;
import com.jayud.mall.service.IPromoteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 推广订单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-20
 */
@Service
public class PromoteOrderServiceImpl extends ServiceImpl<PromoteOrderMapper, PromoteOrder> implements IPromoteOrderService {

    @Autowired
    PromoteOrderMapper promoteOrderMapper;
    @Autowired
    PromoteCompanyMapper promoteCompanyMapper;

    @Override
    public IPage<PromoteOrderVO> findPromoteOrderByPage(QueryPromoteOrderForm form) {
        //定义分页参数
        Page<PromoteOrderVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.asc("t.id"));
        IPage<PromoteOrderVO> pageInfo = promoteOrderMapper.findPromoteOrderByPage(page, form);
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePromoteOrder(SavePromoteOrderForm form) {
        Integer id = form.getId();
        if(ObjectUtil.isEmpty(id)){
            //新增
            if(ObjectUtil.isEmpty(form.getCompanyId())){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "推广订单的渠道/公司不存在");
            }
            Integer companyId = form.getCompanyId();
            PromoteCompanyVO promoteCompanyVO = promoteCompanyMapper.findPromoteCompanyByCompanyId(companyId);
            if(ObjectUtil.isEmpty(promoteCompanyVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "推广订单的渠道/公司不存在");
            }
            PromoteOrder promoteOrder = ConvertUtil.convert(form, PromoteOrder.class);
            promoteOrder.setCompanyName(promoteCompanyVO.getCompanyName());

            this.saveOrUpdate(promoteOrder);
        }else{
            PromoteOrderVO promoteOrderVO = promoteOrderMapper.findPromoteOrderById(id);
            if(ObjectUtil.isEmpty(promoteOrderVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "推广订单不存在");
            }
            //编辑
            PromoteOrder promoteOrder = ConvertUtil.convert(promoteOrderVO, PromoteOrder.class);
            promoteOrder.setClientCompanyName(form.getClientCompanyName());
            promoteOrder.setClientContacts(form.getClientContacts());
            promoteOrder.setClientPhone(form.getClientPhone());
            promoteOrder.setClientCompanyAddress(form.getClientCompanyAddress());
            promoteOrder.setClientManagePlatform(form.getClientManagePlatform());
            this.saveOrUpdate(promoteOrder);
        }

    }

    @Override
    public PromoteOrderVO findPromoteOrderById(Integer id) {
        return promoteOrderMapper.findPromoteOrderById(id);
    }
}
