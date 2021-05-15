package com.jayud.oauth.scheduling;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.jayud.common.RedisUtils;
import com.jayud.oauth.model.po.SystemUser;
import com.jayud.oauth.model.vo.TrainingManagementVO;
import com.jayud.oauth.service.ISystemUserService;
import com.jayud.oauth.service.ITrainingManagementService;
import com.jayud.oauth.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@EnableScheduling
@Slf4j
public class TimeTask {
    @Autowired
    private WebSocket webSocket;
    @Autowired
    private ITrainingManagementService trainingManagementService;
    @Autowired
    private ISystemUserService systemUserService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 定时推送培训信息
     */
    @Scheduled(cron = "0/59 * * * * ?")
    public void pushTrainingInfo() {
        log.info("*********   定时推送培训信息任务执行 在线人数("+webSocket.getOnlineCount()+")  **************");

        //查询最新一周培训资料
        List<TrainingManagementVO> list = this.trainingManagementService.getInfoLastWeek();
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        //根据部门id查询所有员工
        Set<String> departmentIds = new HashSet<>();
        Map<String, List<TrainingManagementVO>> tmp = new HashMap<>();
        for (TrainingManagementVO trainingManagement : list) {
            String[] split = trainingManagement.getTraineesDesc().split(",");
            for (String departmentId : split) {
                List<TrainingManagementVO> trainingManagements = tmp.get(departmentId);
                if (CollectionUtil.isEmpty(trainingManagements)) {
                    trainingManagements = new ArrayList<>();
                    trainingManagements.add(trainingManagement);
                    tmp.put(departmentId, trainingManagements);
                } else {
                    trainingManagements.add(trainingManagement);
                    tmp.put(departmentId, trainingManagements);
                }
                departmentIds.add(departmentId);
            }
        }
        //查询所有部门员工
        List<SystemUser> users = systemUserService.getByDepartmentIds(departmentIds);

        //根据员工名字推送
        for (SystemUser user : users) {
            List<TrainingManagementVO> msgs = tmp.get(user.getDepartmentId().toString());
            for (TrainingManagementVO msg : msgs) {
                //过滤已经推送过消息
                if (redisUtils.hasKey("TrainingMsg:" + user.getId() + "-" + msg.getId())) {
                    continue;
                }
                if (webSocket.AppointSending(user.getName(), JSONObject.toJSONStringWithDateFormat(msg, "yyyy-MM-dd HH:mm"))){
                    redisUtils.set("TrainingMsg:" + user.getId() + "-" + msg.getId(), "1", 86400);
                }
            }
        }

        log.info("*********   定时推送培训信息任务结束 在线人数("+webSocket.getOnlineCount()+")   **************");
    }
}
