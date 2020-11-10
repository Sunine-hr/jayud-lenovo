package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.SupplierServeMapper;
import com.jayud.mall.model.bo.QuerySupplierServeForm;
import com.jayud.mall.model.po.SupplierCost;
import com.jayud.mall.model.po.SupplierServe;
import com.jayud.mall.model.vo.SupplierCostVO;
import com.jayud.mall.model.vo.SupplierServeVO;
import com.jayud.mall.service.ISupplierCostService;
import com.jayud.mall.service.ISupplierServeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 供应商服务 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-10
 */
@Service
public class SupplierServeServiceImpl extends ServiceImpl<SupplierServeMapper, SupplierServe> implements ISupplierServeService {

    @Autowired
    SupplierServeMapper supplierServeMapper;

    @Autowired
    ISupplierCostService supplierCostService;

    @Override
    public IPage<SupplierServeVO> findSupplierServeByPage(QuerySupplierServeForm form) {
        //定义分页参数
        Page<SupplierServeVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<SupplierServeVO> pageInfo = supplierServeMapper.findSupplierServeByPage(page, form);

        List<SupplierServeVO> list = pageInfo.getRecords();
        list.forEach(supplierServeVO -> {
            String supplierCode = supplierServeVO.getSupplierCode();
            String serveCode = supplierServeVO.getServeCode();
            QueryWrapper<SupplierCost> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("supplier_code", supplierCode);
            queryWrapper.eq("serve_code", serveCode);
            List<SupplierCost> supplierCostList = supplierCostService.list(queryWrapper);
            List<SupplierCostVO> supplierCostVOList = ConvertUtil.convertList(supplierCostList, SupplierCostVO.class);
            supplierServeVO.setSupplierCostVOList(supplierCostVOList);
        });
        return pageInfo;
    }
}
