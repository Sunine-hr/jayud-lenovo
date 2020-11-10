package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.SupplierServeMapper;
import com.jayud.mall.model.bo.QuerySupplierServeForm;
import com.jayud.mall.model.bo.SupplierServeForm;
import com.jayud.mall.model.po.SupplierCost;
import com.jayud.mall.model.po.SupplierServe;
import com.jayud.mall.model.vo.SupplierCostVO;
import com.jayud.mall.model.vo.SupplierServeVO;
import com.jayud.mall.service.ISupplierCostService;
import com.jayud.mall.service.ISupplierServeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult saveSupplierServe(SupplierServeForm form) {
        //保存供应商服务
        SupplierServe supplierServe = ConvertUtil.convert(form, SupplierServe.class);
        this.saveOrUpdate(supplierServe);

        String supplierCode = supplierServe.getSupplierCode();
        String serveCode = supplierServe.getServeCode();

        List<SupplierCostVO> supplierCostVOList = form.getSupplierCostVOList();
        //先删除-供应商服务费用
        QueryWrapper<SupplierCost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("supplier_code", supplierCode);
        queryWrapper.eq("serve_code", serveCode);
        supplierCostService.remove(queryWrapper);
        //再保存-供应商服务费用
        List<SupplierCost> supplierCostList = new ArrayList<>();
        supplierCostVOList.forEach(supplierCostVO -> {
            supplierCostVO.setSupplierCode(supplierCode);
            supplierCostVO.setServeCode(serveCode);
            SupplierCost supplierCost = ConvertUtil.convert(supplierCostVO, SupplierCost.class);
            supplierCostList.add(supplierCost);
        });
        supplierCostService.saveOrUpdateBatch(supplierCostList);
        return CommonResult.success("保存供应商服务，成功！");
    }
}
