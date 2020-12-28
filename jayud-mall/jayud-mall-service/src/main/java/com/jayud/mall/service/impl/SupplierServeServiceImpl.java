package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.admin.security.domain.AuthUser;
import com.jayud.mall.admin.security.service.BaseService;
import com.jayud.mall.mapper.SupplierServeMapper;
import com.jayud.mall.model.bo.QuerySupplierServeForm;
import com.jayud.mall.model.bo.SupplierServeForm;
import com.jayud.mall.model.po.SupplierCost;
import com.jayud.mall.model.po.SupplierServe;
import com.jayud.mall.model.vo.SupplierCostVO;
import com.jayud.mall.model.vo.SupplierServeVO;
import com.jayud.mall.service.INumberGeneratedService;
import com.jayud.mall.service.ISupplierCostService;
import com.jayud.mall.service.ISupplierServeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    BaseService baseService;

    @Autowired
    INumberGeneratedService numberGeneratedService;

    @Override
    public IPage<SupplierServeVO> findSupplierServeByPage(QuerySupplierServeForm form) {
        //定义分页参数
        Page<SupplierServeVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<SupplierServeVO> pageInfo = supplierServeMapper.findSupplierServeByPage(page, form);

        List<SupplierServeVO> list = pageInfo.getRecords();
        list.forEach(supplierServeVO -> {
//            String supplierCode = supplierServeVO.getSupplierCode();
            String serveCode = supplierServeVO.getServeCode();
            QueryWrapper<SupplierCost> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("supplier_code", supplierCode);
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
        AuthUser user = baseService.getUser();
        SupplierServe supplierServe = ConvertUtil.convert(form, SupplierServe.class);
        Long id = supplierServe.getId();
        if(id == null){
            //mysql-生成单号，有规则
            String serveCode = numberGeneratedService.getOrderNoByCode("serve_code");
            supplierServe.setServeCode(serveCode);
            supplierServe.setStatus("1");
        }
        supplierServe.setUserId(user.getId().intValue());
        supplierServe.setUserName(user.getName());
        supplierServe.setCreateTime(LocalDateTime.now());
        //1.保存-供应商服务
        this.saveOrUpdate(supplierServe);

        Long supplierInfoId = supplierServe.getSupplierInfoId();//供应商id(supplier_info id)
        Long serviceId = supplierServe.getId();//服务id(supplier_serve id)
        QueryWrapper<SupplierCost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("supplier_info_id", supplierInfoId);
        queryWrapper.eq("service_id", serviceId);
        //2.删除-供应商服务费用
        supplierCostService.remove(queryWrapper);

        List<SupplierCostVO> supplierCostVOList = form.getSupplierCostVOList();
        supplierCostVOList.forEach(supplierCostVO -> {
            supplierCostVO.setSupplierInfoId(supplierInfoId);
            supplierCostVO.setServiceId(serviceId);
            supplierCostVO.setUserId(user.getId().intValue());
            supplierCostVO.setUserName(user.getUserName());
            supplierCostVO.setCreateTime(LocalDateTime.now());
        });
        List<SupplierCost> supplierCosts = ConvertUtil.convertList(supplierCostVOList, SupplierCost.class);
        //3.保存-供应商服务费用
        supplierCostService.saveOrUpdateBatch(supplierCosts);

        return CommonResult.success("保存供应商服务，成功！");
    }
}
