package com.jayud.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.tools.model.bo.AddFbaOrderForm;
import com.jayud.tools.model.bo.QueryFbaOrderForm;
import com.jayud.tools.model.po.FbaOrder;
import com.jayud.tools.mapper.FbaOrderMapper;
import com.jayud.tools.model.po.FbaOrderTrack;
import com.jayud.tools.model.vo.FbaOrderTrackVO;
import com.jayud.tools.model.vo.FbaOrderVO;
import com.jayud.tools.service.IFbaOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.tools.service.IFbaOrderTrackService;
import com.jayud.tools.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * FBA订单 服务实现类
 * </p>
 *
 * @author llj
 * @since 2021-12-22
 */
@Service
public class FbaOrderServiceImpl extends ServiceImpl<FbaOrderMapper, FbaOrder> implements IFbaOrderService {


    @Autowired
    private IFbaOrderTrackService fbaOrderTrackService;

    @Override
    public IPage selectPage(QueryFbaOrderForm queryFbaOrderForm) {
        Page<FbaOrderVO> page = new Page<>(queryFbaOrderForm.getPageNum(), queryFbaOrderForm.getPageSize());
        return this.baseMapper.findByPage(queryFbaOrderForm,page);
    }

    @Override
    public List<FbaOrderVO> selectList(QueryFbaOrderForm queryFbaOrderForm) {
        return this.baseMapper.findList(queryFbaOrderForm);
    }

    @Override
    public void saveOrUpdateFbaOrder(AddFbaOrderForm addFbaOrderForm) {
        FbaOrder fbaOrder = ConvertUtil.convert(addFbaOrderForm, FbaOrder.class);
        if(fbaOrder.getId() != null){
            fbaOrder.setUpdateTime(LocalDateTime.now());
//            fbaOrder.setUpdateUser(UserOperator.getToken());
            fbaOrder.setUpdateUser(addFbaOrderForm.getLoginUserName());

        }else{
            fbaOrder.setCreateTime(LocalDateTime.now());
            fbaOrder.setCreateUser(addFbaOrderForm.getLoginUserName());
        }
        boolean result = this.saveOrUpdate(fbaOrder);
        if(result){
            log.warn("新增或修改订单成功");
        }
    }

    @Override
    public void deleteById(List<Long> ids) {
        List<FbaOrder> fbaOrders = new ArrayList<>();
        for (Long id : ids) {
            FbaOrder fbaOrder = new FbaOrder();
            fbaOrder.setId(id.intValue());
            fbaOrder.setIsDelete(1);
            fbaOrder.setUpdateTime(LocalDateTime.now());
            fbaOrder.setUpdateUser(UserOperator.getToken());
            fbaOrders.add(fbaOrder);
        }
        boolean result = this.updateBatchById(fbaOrders);
        if(result){
            log.warn("删除订单成功");
        }
    }

    @Override
    public FbaOrderVO getFbaOrderById(int id) {
        FbaOrder fbaOrder = this.getById(id);
        FbaOrderVO fbaOrderVO = ConvertUtil.convert(fbaOrder, FbaOrderVO.class);
        List<FbaOrderTrack> fbaOrderTracks = fbaOrderTrackService.getFbaOrderTrackByOrderId(fbaOrderVO.getId());
        List<FbaOrderTrackVO> fbaOrderTrackVOS = ConvertUtil.convertList(fbaOrderTracks, FbaOrderTrackVO.class);
        fbaOrderVO.setFbaOrderTrackVOS(fbaOrderTrackVOS);
        return fbaOrderVO;
    }

    @Override
    public FbaOrder getFbaOrderByOrderNo(String orderNo) {
        QueryWrapper<FbaOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FbaOrder::getOrderNo,orderNo);
        queryWrapper.lambda().eq(FbaOrder::getIsDelete,0);
        FbaOrder fbaOrder = this.getOne(queryWrapper);
        return fbaOrder;
    }

    @Override
    public FbaOrderVO getFbaOrderVOByOrderNo(String orderNo) {

        FbaOrderVO fbaOrderVO = this.baseMapper.getFbaOrderVOByOrderNo(orderNo);

        List<FbaOrderTrack> fbaOrderTrackByOrderId = this.fbaOrderTrackService.getFbaOrderTrackByOrderId(fbaOrderVO.getId());
        List<FbaOrderTrackVO> fbaOrderTrackVOS = ConvertUtil.convertList(fbaOrderTrackByOrderId, FbaOrderTrackVO.class);
        for (FbaOrderTrackVO fbaOrderTrackVO : fbaOrderTrackVOS) {
            fbaOrderTrackVO.setDayWeek(DateUtil.getWeekDay(fbaOrderTrackVO.getTrajectoryTime()));
        }
        fbaOrderVO.setFbaOrderTrackVOS(fbaOrderTrackVOS);
        return fbaOrderVO;
    }

    @Override
    public FbaOrder getFbaOrderByCustomerNo(String customerNo) {
        QueryWrapper<FbaOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FbaOrder::getCustomerNo,customerNo);
        queryWrapper.lambda().eq(FbaOrder::getIsDelete,0);
        FbaOrder fbaOrder = this.getOne(queryWrapper);
        return fbaOrder;
    }

    @Override
    public FbaOrder getFbaOrderByTransshipmentNo(String transshipmentNo) {
        QueryWrapper<FbaOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FbaOrder::getTransshipmentNo,transshipmentNo);
        queryWrapper.lambda().eq(FbaOrder::getIsDelete,0);
        FbaOrder fbaOrder = this.getOne(queryWrapper);
        return fbaOrder;
    }

}
