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
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.mapper.WorkOrderMapper;
import com.jayud.mall.model.bo.QueryWorkOrderForm;
import com.jayud.mall.model.bo.WorkOrderAddForm;
import com.jayud.mall.model.bo.WorkOrderEvaluateForm;
import com.jayud.mall.model.bo.WorkOrderReplyForm;
import com.jayud.mall.model.po.WorkOrder;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.model.vo.TemplateUrlVO;
import com.jayud.mall.model.vo.WorkOrderVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IWorkOrderService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单工单表 服务实现类
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
    OrderInfoMapper orderInfoMapper;
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
        String evaluation = form.getEvaluation();
        WorkOrderVO workOrderVO = workOrderMapper.findWorkOrderById(id);
        if(ObjectUtil.isEmpty(workOrderVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        Integer status = workOrderVO.getStatus();//状态(1进行中 2已结单 3待评价 4已关闭)
        if(status != 3){
            return CommonResult.error(-1, "当前工单状态不正确，不能评价");
        }
        WorkOrder workOrder = ConvertUtil.convert(workOrderVO, WorkOrder.class);
        workOrder.setEvaluation(evaluation);
        this.saveOrUpdate(workOrder);
        return CommonResult.success("客户评价，操作成功");
    }

    @Override
    public CommonResult<WorkOrderVO> addWorkOrder(WorkOrderAddForm form) {
        WorkOrder workOrder = ConvertUtil.convert(form, WorkOrder.class);

        Integer businessType = form.getBusinessType();//工单业务类型(1订单工单 2提单工单)
        Long businessId = workOrder.getBusinessId();
        if(businessType == 1){
            //1订单工单
            OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(businessId);
            if(ObjectUtil.isEmpty(orderInfoVO)){
                return CommonResult.error(-1, "当前订单不存在,无法创建工单");
            }
            String orderNo = orderInfoVO.getOrderNo();
            Long orderInfoVOId = orderInfoVO.getId();
            workOrder.setBusinessNo(orderNo);
            workOrder.setBusinessId(orderInfoVOId);

            //生成工单编号
            String workNo = NumberGeneratedUtils.getOrderNoByCode2("work_order_no");
            workOrder.setWorkNo(workNo);
            CustomerUser customerUser = baseService.getCustomerUser();
            workOrder.setCreatorType(1);//创建人类型(1客户 2后台用户)
            workOrder.setCreator(customerUser.getId());//创建人(客户id customer id, 用户id system_user id)
            workOrder.setCreatorName(customerUser.getCompany());//创建人名称(客户名称 customer company, 用户名称 system_user name)
        } else {
            //2提单工单
            //TODO ...
        }
        List<TemplateUrlVO> fileUrls = form.getFileUrls();
        if(CollUtil.isNotEmpty(fileUrls)){
            String s = JSONObject.toJSONString(fileUrls);
            workOrder.setFileUrl(s);
        }
        workOrder.setWorkType(1);//工单类型(1普通工单)
        workOrder.setStatus(1);//状态(1进行中 2已结单 3待评价 4已关闭)

        this.saveOrUpdate(workOrder);
        WorkOrderVO workOrderVO = ConvertUtil.convert(workOrder, WorkOrderVO.class);
        return CommonResult.success(workOrderVO);
    }

    @Override
    public CommonResult statementWorkOrder(Long id) {
        WorkOrderVO workOrderVO = workOrderMapper.findWorkOrderById(id);
        if(ObjectUtil.isEmpty(workOrderVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        Integer status = workOrderVO.getStatus();
        if(status != 1){
            return CommonResult.error(-1, "只能对进行中的工单进行结单");
        }
        WorkOrder workOrder = ConvertUtil.convert(workOrderVO, WorkOrder.class);
        workOrder.setStatus(2);//1进行中 2已结单 3待评价 4已关闭
        AuthUser user = baseService.getUser();
        workOrder.setOperator(user.getId());
        workOrder.setOperationTime(LocalDateTime.now());
        this.saveOrUpdate(workOrder);
        return CommonResult.success("结单成功");
    }

    @Override
    public CommonResult replyWorkOrder(WorkOrderReplyForm form) {
        Long id = form.getId();
        String revert = form.getRevert();
        WorkOrderVO workOrderVO = workOrderMapper.findWorkOrderById(id);
        if(ObjectUtil.isEmpty(workOrderVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        WorkOrder workOrder = ConvertUtil.convert(workOrderVO, WorkOrder.class);
        workOrder.setRevert(revert);
        AuthUser user = baseService.getUser();
        workOrder.setOperator(user.getId());
        workOrder.setOperationTime(LocalDateTime.now());
        this.saveOrUpdate(workOrder);
        return CommonResult.success("回复成功");
    }

    @Override
    public CommonResult closeWorkOrder(Long id) {
        WorkOrderVO workOrderVO = workOrderMapper.findWorkOrderById(id);
        if(ObjectUtil.isEmpty(workOrderVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        WorkOrder workOrder = ConvertUtil.convert(workOrderVO, WorkOrder.class);
        //4已关闭
        workOrder.setStatus(4);//状态(1进行中 2已结单 3待评价 4已关闭)
        this.saveOrUpdate(workOrder);
        return CommonResult.success("关闭工单成功");
    }
}