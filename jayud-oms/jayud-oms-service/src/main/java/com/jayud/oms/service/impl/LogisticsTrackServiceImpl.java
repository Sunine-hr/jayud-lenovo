package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.mapper.LogisticsTrackMapper;
import com.jayud.oms.model.bo.QueryLogisticsTrackForm;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.po.OrderStatus;
import com.jayud.oms.model.vo.FileView;
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
        queryWrapper.eq("biz_code",form.getBizCode());
        queryWrapper.ne("f_id",0);
        queryWrapper.eq("status",1);
        queryWrapper.orderByAsc("sorts");
        List<OrderStatus> allOrderStatus = statusService.list(queryWrapper);//所有流程
        List<LogisticsTrackVO> logisticsTrackVOS = new ArrayList<>();//构建所有流程返回给前端显示
        for (OrderStatus orderStatus : allOrderStatus) {
            LogisticsTrackVO logisticsTrackVO = new LogisticsTrackVO();
            logisticsTrackVO.setStatus(orderStatus.getIdCode());
            logisticsTrackVO.setStatusName(orderStatus.getName());
            logisticsTrackVOS.add(logisticsTrackVO);
        }
        for (int i = 0; i < logisticsTrackVOS.size(); i++) {
            //获取子订单下的某状态的操作记录
            form.setStatus(logisticsTrackVOS.get(i).getStatus());
            List<LogisticsTrackVO> oprProcess = baseMapper.findReplyStatus(form);//已操作流程
            if(oprProcess == null || oprProcess.size() == 0){
                break;
            }
            //取该状态下最新的一条操作记录,并且该记录比上一节点的最新流程创建时间大
            LogisticsTrackVO nowOprProcess = oprProcess.get(0);
            if(i != 0){//第一个流程节点除外
                form.setStatus(logisticsTrackVOS.get(i-1).getStatus());
                List<LogisticsTrackVO> preOprStatus = baseMapper.findReplyStatus(form);//已操作流程
                LogisticsTrackVO nowPreOprProcess = preOprStatus.get(0);
                if(nowOprProcess.getCreatedTime().compareTo(nowPreOprProcess.getCreatedTime()) >= 0){
                    logisticsTrackVOS.get(i).setId(nowOprProcess.getId());
                    logisticsTrackVOS.get(i).setDescription(nowOprProcess.getDescription());
                    logisticsTrackVOS.get(i).setOperatorUser(nowOprProcess.getOperatorUser());
                    logisticsTrackVOS.get(i).setOperatorTime(nowOprProcess.getOperatorTime());
                    String statusPic = nowOprProcess.getStatusPic();
                    List<FileView> fileViews = new ArrayList<>();
                    if(statusPic != null && "".equals(statusPic)){
                        String[] fileList = statusPic.split(",");
                        for(String str : fileList){
                            int index = str.lastIndexOf("/");
                            FileView fileView = new FileView();
                            fileView.setRelativePath(str);
                            fileView.setFileName(str.substring(index + 1, str.length()));
                            fileView.setAbsolutePath(prePath + str);
                            fileViews.add(fileView);
                        }
                    }
                    logisticsTrackVOS.get(i).setFileViewList(fileViews);

                }
            }

        }
        return logisticsTrackVOS;
    }
}
