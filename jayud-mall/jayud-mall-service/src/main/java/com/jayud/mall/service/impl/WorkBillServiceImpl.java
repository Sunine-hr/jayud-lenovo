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
import com.jayud.mall.mapper.OceanBillMapper;
import com.jayud.mall.mapper.WorkBillMapper;
import com.jayud.mall.model.bo.QueryWorkBillForm;
import com.jayud.mall.model.bo.WorkBillAddForm;
import com.jayud.mall.model.bo.WorkBillEvaluateForm;
import com.jayud.mall.model.bo.WorkBillReplyForm;
import com.jayud.mall.model.po.WorkBill;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.model.vo.TemplateUrlVO;
import com.jayud.mall.model.vo.WorkBillVO;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IWorkBillService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 提单工单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-30
 */
@Service
public class WorkBillServiceImpl extends ServiceImpl<WorkBillMapper, WorkBill> implements IWorkBillService {

    @Autowired
    WorkBillMapper workBillMapper;
    @Autowired
    OceanBillMapper oceanBillMapper;
    @Autowired
    BaseService baseService;

    @Override
    public IPage<WorkBillVO> findWorkBillByPage(QueryWorkBillForm form) {
        //定义分页参数
        Page<WorkBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<WorkBillVO> pageInfo = workBillMapper.findWorkBillByPage(page, form);

        //设置文件格式
        List<WorkBillVO> records = pageInfo.getRecords();
        records.forEach(workBillVO -> {
            String fileUrl = workBillVO.getFileUrl();
            if(StrUtil.isNotEmpty(fileUrl)){
                String json = fileUrl;
                try {
                    List<TemplateUrlVO> fileUrls = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                    });
                    workBillVO.setFileUrls(fileUrls);
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("json格式错误");
                    workBillVO.setFileUrls(new ArrayList<>());
                }
            }else{
                workBillVO.setFileUrls(new ArrayList<>());
            }
        });
        return pageInfo;
    }

    @Override
    public CommonResult statementWorkBill(Long id) {
        WorkBillVO workBillVO = workBillMapper.findWorkBillById(id);
        if(ObjectUtil.isEmpty(workBillVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        Integer status = workBillVO.getStatus();
        if(status != 1){
            return CommonResult.error(-1, "只能对进行中的工单进行结单");
        }
        WorkBill workOrder = ConvertUtil.convert(workBillVO, WorkBill.class);
        workOrder.setStatus(2);//1进行中 2已结单 3待评价 4已关闭
        AuthUser user = baseService.getUser();
        workOrder.setOperator(user.getId());
        workOrder.setOperationTime(LocalDateTime.now());
        this.saveOrUpdate(workOrder);
        return CommonResult.success("结单成功");
    }

    @Override
    public CommonResult replyWorkBill(WorkBillReplyForm form) {
        Long id = form.getId();
        String revert = form.getRevert();
        WorkBillVO workBillVO = workBillMapper.findWorkBillById(id);
        if(ObjectUtil.isEmpty(workBillVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        WorkBill workBill = ConvertUtil.convert(workBillVO, WorkBill.class);
        workBill.setRevert(revert);
        AuthUser user = baseService.getUser();
        workBill.setOperator(user.getId());
        workBill.setOperationTime(LocalDateTime.now());
        this.saveOrUpdate(workBill);
        return CommonResult.success("回复成功");
    }

    @Override
    public CommonResult<WorkBillVO> findWorkBillById(Long id) {
        WorkBillVO workBillVO = workBillMapper.findWorkBillById(id);
        if(ObjectUtil.isEmpty(workBillVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        //文件,设置文件
        String fileUrl = workBillVO.getFileUrl();
        if(StrUtil.isNotEmpty(fileUrl)){
            String json = fileUrl;
            try {
                List<TemplateUrlVO> fileUrls = JSON.parseObject(json, new TypeReference<List<TemplateUrlVO>>() {
                });
                workBillVO.setFileUrls(fileUrls);
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("json格式错误");
                workBillVO.setFileUrls(new ArrayList<>());
            }
        }else{
            workBillVO.setFileUrls(new ArrayList<>());
        }
        return CommonResult.success(workBillVO);
    }

    @Override
    public CommonResult delWorkBillById(Long id) {
        WorkBillVO workBillVO = workBillMapper.findWorkBillById(id);
        if(ObjectUtil.isEmpty(workBillVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        Integer status = workBillVO.getStatus();//状态(1进行中 2已结单 3待评价 4已关闭)
        if(status != 4){
            return CommonResult.error(-1, "当前工单状态不正确，不能删除");
        }
        this.removeById(id);
        return CommonResult.success("工单删除成功");
    }

    @Override
    public CommonResult evaluateWorkBillById(WorkBillEvaluateForm form) {
        Long id = form.getId();
        String evaluation = form.getEvaluation();
        WorkBillVO workBillVO = workBillMapper.findWorkBillById(id);
        if(ObjectUtil.isEmpty(workBillVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        Integer status = workBillVO.getStatus();//状态(1进行中 2已结单 3待评价 4已关闭)
        if(status != 3){
            return CommonResult.error(-1, "当前工单状态不正确，不能评价");
        }
        WorkBill workBill = ConvertUtil.convert(workBillVO, WorkBill.class);
        workBill.setEvaluation(evaluation);
        this.saveOrUpdate(workBill);
        return CommonResult.success("客户评价，操作成功");
    }

    @Override
    public CommonResult<WorkBillVO> addWorkBill(WorkBillAddForm form) {
        WorkBill workBill = ConvertUtil.convert(form, WorkBill.class);
        Long billId = workBill.getBillId();
        OceanBillVO oceanBillVO = oceanBillMapper.findOceanBillById(billId);
        if(ObjectUtil.isEmpty(oceanBillVO)){
            return CommonResult.error(-1, "当前提单不存在,无法创建工单");
        }
        String billNo = oceanBillVO.getOrderId();//order_id '提单号(供应商提供)',
        workBill.setBillNo(billNo);
        //生成工单编号
        String workNo = NumberGeneratedUtils.getOrderNoByCode2("work_bill_no");
        workBill.setWorkNo(workNo);
        List<TemplateUrlVO> fileUrls = form.getFileUrls();
        if(CollUtil.isNotEmpty(fileUrls)){
            String s = JSONObject.toJSONString(fileUrls);
            workBill.setFileUrl(s);
        }
        workBill.setStatus(1);//状态(1进行中 2已结单 3待评价 4已关闭)
        workBill.setType(1);//工单类型(1普通工单)
        AuthUser user = baseService.getUser();
        workBill.setCreateId(user.getId().intValue());//创建人(后台用户id)(system_user id)

        this.saveOrUpdate(workBill);
        WorkBillVO workBillVO = ConvertUtil.convert(workBill, WorkBillVO.class);
        return CommonResult.success(workBillVO);
    }

    @Override
    public CommonResult closeWorkBill(Long id) {
        WorkBillVO workBillVO = workBillMapper.findWorkBillById(id);
        if(ObjectUtil.isEmpty(workBillVO)){
            return CommonResult.error(-1, "没有找到工单");
        }
        WorkBill workBill = ConvertUtil.convert(workBillVO, WorkBill.class);
        //4已关闭
        workBill.setStatus(4);//状态(1进行中 2已结单 3待评价 4已关闭)
        this.saveOrUpdate(workBill);
        return CommonResult.success("关闭工单成功");
    }
}
