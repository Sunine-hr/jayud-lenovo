package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.BillClearanceInfoForm;
import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.model.bo.QueryAccountReceivableForm;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.*;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 应收对账单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Service
public class BillClearanceInfoServiceImpl extends ServiceImpl<BillClearanceInfoMapper, BillClearanceInfo> implements IBillClearanceInfoService {


    @Autowired
    IBillClearanceInfoService billClearanceInfoService;
    /**
     * 集合数据
     * @param
     * @return
     */
    public IPage<BillClearanceInfoVO> findBillClearanceInfoByPage(BillClearanceInfoForm form){
    //定义分页参数
        Page<BillClearanceInfoVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<BillClearanceInfoVO> pageInfo = billClearanceInfoService.findBillClearanceInfoByPage(form);
        return  pageInfo;
    }


    /**
     * 增加
     * @param billClearanceInfo
     */
    public void insertBillClearanceInfo(BillClearanceInfoForm billClearanceInfo){
        billClearanceInfoService.insertBillClearanceInfo(billClearanceInfo);
    }

    /**
     * 修改
     * @param billClearanceInfo
     */
   public void updateBillClearanceInfo(BillClearanceInfoForm billClearanceInfo){
       billClearanceInfoService.updateBillClearanceInfo(billClearanceInfo);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteBillClearanceInfo(Long id){
        billClearanceInfoService.deleteBillClearanceInfo(id);
    }


}
