package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.mapper.LogisticsTrackMapper;
import com.jayud.oms.model.bo.QueryLogisticsTrackForm;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.po.OrderStatus;
import com.jayud.oms.model.vo.LogisticsTrackVO;
import com.jayud.oms.service.ILogisticsTrackService;
import com.jayud.oms.service.IOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 物流轨迹跟踪表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-22
 */
@Service
public class LogisticsTrackServiceImpl extends ServiceImpl<LogisticsTrackMapper, LogisticsTrack> implements ILogisticsTrackService {

    @Autowired
    IOrderStatusService statusService;

    @Autowired
    FileClient fileClient;

    @Override
    public List<LogisticsTrackVO> findReplyStatus(QueryLogisticsTrackForm form) {
        String prePath = fileClient.getBaseUrl().getData().toString();
        //根据业务类型获取该业务类型的所有流程节点 业务类型加有没有父节点获取，这里是获取子订单流程节点
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("class_code",form.getClassCode());
        queryWrapper.ne("f_id",0);
        queryWrapper.eq("status",1);
        queryWrapper.orderByAsc("sub_sorts");
        List<OrderStatus> allOrderStatus = statusService.list(queryWrapper);//所有流程
        List<LogisticsTrackVO> logisticsTrackVOS = new ArrayList<>();//构建所有流程返回给前端显示
        for (OrderStatus orderStatus : allOrderStatus) {
            LogisticsTrackVO logisticsTrackVO = new LogisticsTrackVO();
            if(OrderStatusEnum.PASS.getCode().equals(orderStatus.getContainState())){//特殊处理需要反馈确认的状态节点
                logisticsTrackVO.setFlag(false);
            }else {
                logisticsTrackVO.setFlag(true);
            }
            logisticsTrackVO.setStatus(orderStatus.getContainState());
            logisticsTrackVO.setStatusName(orderStatus.getName());
            logisticsTrackVOS.add(logisticsTrackVO);
        }
        for (int i = 0; i < logisticsTrackVOS.size(); i++) {
            //获取子订单下的某状态的操作记录
            String containState = logisticsTrackVOS.get(i).getStatus();//在子节点上原则上只有一个
            form.setStatus(containState);
            List<LogisticsTrackVO> oprProcess = baseMapper.findReplyStatus(form);//已操作流程
            if(oprProcess == null || oprProcess.size() == 0){
                break;
            }
            //取该状态下最新的一条操作记录,并且该记录比上一节点的最新流程创建时间大
            LogisticsTrackVO nowOprProcess = oprProcess.get(0);
            boolean flag = false;
            if(i != 0){//第一个流程节点除外
                form.setStatus(logisticsTrackVOS.get(i-1).getStatus());
                List<LogisticsTrackVO> preOprStatus = baseMapper.findReplyStatus(form);//已操作流程
                LogisticsTrackVO nowPreOprProcess = preOprStatus.get(0);
                if(nowOprProcess.getCreatedTime().compareTo(nowPreOprProcess.getCreatedTime()) >= 0){
                    flag = true;
                }
            }else {
                flag = true;
            }
            if(flag) {
                logisticsTrackVOS.get(i).setId(nowOprProcess.getId());
                logisticsTrackVOS.get(i).setDescription(nowOprProcess.getDescription());
                logisticsTrackVOS.get(i).setOperatorUser(nowOprProcess.getOperatorUser());
                logisticsTrackVOS.get(i).setOperatorTime(nowOprProcess.getOperatorTime());
                String statusPic = nowOprProcess.getStatusPic();
                String statusPicNme = nowOprProcess.getStatusPicName();
                logisticsTrackVOS.get(i).setFileViewList(StringUtils.getFileViews(statusPic,statusPicNme,prePath));
            }

        }
        return logisticsTrackVOS;
    }
}
