package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.WorkOrderMapper;
import com.jayud.mall.model.bo.QueryWorkOrderForm;
import com.jayud.mall.model.bo.WorkOrderAddForm;
import com.jayud.mall.model.bo.WorkOrderEvaluateForm;
import com.jayud.mall.model.po.WorkOrder;
import com.jayud.mall.model.vo.TemplateUrlVO;
import com.jayud.mall.model.vo.WorkOrderVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IWorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 工单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-26
 */
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements IWorkOrderService {

    @Autowired
    WorkOrderMapper workOrderMapper;
    @Autowired
    BaseService baseService;

    @Override
    public IPage<WorkOrderVO> findWorkOrderByPage(QueryWorkOrderForm form) {
        //定义分页参数
        Page<WorkOrderVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<WorkOrderVO> pageInfo = workOrderMapper.findWorkOrderByPage(page, form);

        //设置文件格式
        List<WorkOrderVO> records = pageInfo.getRecords();
        records.forEach(workOrderVO -> {
            String fileUrl = workOrderVO.getFileUrl();
            if(StrUtil.isNotEmpty(fileUrl)){
                String json = fileUrl;
                try {
                    List<TemplateUrlVO> fileUrls = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                    });
                    workOrderVO.setFileUrls(fileUrls);
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("json格式错误");
                    workOrderVO.setFileUrls(new ArrayList<>());
                }
            }else{
                workOrderVO.setFileUrls(new ArrayList<>());
            }
        });
        return pageInfo;
    }

    @Override
    public CommonResult<WorkOrderVO> findWorkOrderById(Long id) {
        WorkOrderVO workOrderVO = workOrderMapper.findWorkOrderById(id);
        if(ObjectUtil.isEmpty(workOrderVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        //文件
        String fileUrl = workOrderVO.getFileUrl();
        if(StrUtil.isNotEmpty(fileUrl)){
            String json = fileUrl;
            try {
                List<TemplateUrlVO> fileUrls = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                });
                workOrderVO.setFileUrls(fileUrls);
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("json格式错误");
                workOrderVO.setFileUrls(new ArrayList<>());
            }
        }else{
            workOrderVO.setFileUrls(new ArrayList<>());
        }
        return CommonResult.success(workOrderVO);
    }

    @Override
    public CommonResult delWorkOrderById(Long id) {
        WorkOrderVO workOrderVO = workOrderMapper.findWorkOrderById(id);
        if(ObjectUtil.isEmpty(workOrderVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        Integer status = workOrderVO.getStatus();//状态(1进行中 2已结单 3待评价 4已关闭)
        if(status != 4){
            return CommonResult.error(-1, "当前工单状态不正确，不能删除");
        }
        this.removeById(id);
        return CommonResult.success("工单删除成功");
    }

    @Override
    public CommonResult evaluateWorkOrderById(WorkOrderEvaluateForm form) {
        Long id = form.getId();
        String customerEvaluation = form.getCustomerEvaluation();
        WorkOrderVO workOrderVO = workOrderMapper.findWorkOrderById(id);
        if(ObjectUtil.isEmpty(workOrderVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        Integer status = workOrderVO.getStatus();//状态(1进行中 2已结单 3待评价 4已关闭)
        if(status != 3){
            return CommonResult.error(-1, "当前工单状态不正确，不能评价");
        }
        WorkOrder workOrder = ConvertUtil.convert(workOrderVO, WorkOrder.class);
        workOrder.setCustomerEvaluation(customerEvaluation);
        this.saveOrUpdate(workOrder);
        return CommonResult.success("客户评价，操作成功");
    }

    @Override
    public CommonResult<WorkOrderVO> addWorkOrder(WorkOrderAddForm form) {
        WorkOrder workOrder = ConvertUtil.convert(form, WorkOrder.class);
        List<TemplateUrlVO> fileUrls = form.getFileUrls();
        if(CollUtil.isNotEmpty(fileUrls)){
            String s = JSONObject.toJSONString(fileUrls);
            workOrder.setFileUrl(s);
        }
        workOrder.setStatus(1);//状态(1进行中 2已结单 3待评价 4已关闭)
        workOrder.setType(1);//工单类型(1普通工单)
        CustomerUser customerUser = baseService.getCustomerUser();
        workOrder.setCustomerId(customerUser.getId());//创建人(客户用户、客户id)、提交账号(customer id)

        this.saveOrUpdate(workOrder);
        WorkOrderVO workOrderVO = ConvertUtil.convert(workOrder, WorkOrderVO.class);
        return CommonResult.success(workOrderVO);
    }
}
