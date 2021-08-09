package com.jayud.oauth.scheduling;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.RedisUtils;
import com.jayud.common.entity.EntWechatSysConf;
import com.jayud.common.enums.MsgChannelTypeEnum;
import com.jayud.common.enums.SystemConfTypeEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oauth.feign.OmsClient;
import com.jayud.oauth.model.po.MsgUserChannel;
import com.jayud.oauth.model.po.SystemUser;
import com.jayud.oauth.model.vo.TrainingManagementVO;
import com.jayud.oauth.service.IMsgUserChannelService;
import com.jayud.oauth.service.ISystemUserService;
import com.jayud.oauth.service.ITrainingManagementService;
import com.jayud.oauth.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private IMsgUserChannelService msgUserChannelService;

    /**
     * 定时推送培训信息
     */
    @Scheduled(cron = "0/59 * * * * ?")
    public void pushTrainingInfo() {
        log.info("*********   定时推送培训信息任务执行 在线人数(" + webSocket.getOnlineCount() + ")  **************");

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
                if (webSocket.AppointSending(user.getName(), JSONObject.toJSONStringWithDateFormat(msg, "yyyy-MM-dd HH:mm"))) {
                    redisUtils.set("TrainingMsg:" + user.getId() + "-" + msg.getId(), "1", 86400);
                }
            }
        }

        log.info("*********   定时推送培训信息任务结束 在线人数(" + webSocket.getOnlineCount() + ")   **************");
    }

    /**
     * 同步微信账号
     */
    @Scheduled(cron = "0/59 * * * * ?")
    public void synEnterpriseWechatUserInfo() {
        QueryWrapper<SystemUser> condition = new QueryWrapper<>();
        condition.lambda().isNotNull(SystemUser::getPhone).eq(SystemUser::getStatus, true);
        List<SystemUser> systemUsers = this.systemUserService.getBaseMapper().selectList(condition);
        if (CollectionUtil.isEmpty(systemUsers)) {
            return;
        }
        Object sysConfObj = this.omsClient.getSystemConf(SystemConfTypeEnum.TWO.getCode()).getData();

        if (sysConfObj == null) {
            return;
        }

        cn.hutool.json.JSONObject sysConf = new cn.hutool.json.JSONObject(sysConfObj);
        cn.hutool.json.JSONObject confData = sysConf.getJSONObject("confData");
        if (confData == null) {
            return;
        }

        String corpid = confData.getStr("corpid");
        String corpsecret = confData.getStr("corpsecret");

        Object tokenObj = this.omsClient.getEnterpriseToken(corpid, corpsecret).getData();
        if (tokenObj == null) {
            return;
        }
        String token = tokenObj.toString();
        Object depObj = this.omsClient.getEnterpriseDep(null, corpid, corpsecret, token).getData();
        JSONArray depArray = new JSONArray(depObj);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < depArray.size(); i++) {
            cn.hutool.json.JSONObject jsonObject = depArray.getJSONObject(i);
            Long depId = jsonObject.getLong("id");
            Object tmpObj = this.omsClient.getEnterpriseDepStaff(depId, true, corpid, corpsecret, token).getData();
            if (tmpObj == null) {
                continue;
            }
            JSONArray tmpArray = new JSONArray(tmpObj);
            for (int i1 = 0; i1 < tmpArray.size(); i1++) {
                cn.hutool.json.JSONObject tmp = tmpArray.getJSONObject(i1);
                if (tmp == null) {
                    continue;
                }
                map.put(tmp.getStr("mobile"), tmp.getStr("userid"));
            }
        }

        List<MsgUserChannel> channels = this.msgUserChannelService.getByCondition(new MsgUserChannel().setType(MsgChannelTypeEnum.ENT_WECHAT.getCode()));
        Map<Long, MsgUserChannel> channelMap = channels.stream().collect(Collectors.toMap(e -> e.getUserId(), e -> e));

        for (SystemUser systemUser : systemUsers) {
            String tmp = map.get(systemUser.getPhone());
            if (StringUtils.isEmpty(tmp)) {
                continue;
            }
            MsgUserChannel msgUserChannel = channelMap.get(systemUser.getId());

            if (msgUserChannel != null) {
                if (!tmp.equals(msgUserChannel.getAccount())) {
                    this.msgUserChannelService.updateById(new MsgUserChannel().setId(msgUserChannel.getId()).setAccount(tmp));
                }
            } else {
                MsgUserChannel channel = new MsgUserChannel().setName(MsgChannelTypeEnum.ENT_WECHAT.getDesc()).setType(MsgChannelTypeEnum.ENT_WECHAT.getCode())
                        .setUserId(systemUser.getId()).setAccount(tmp).setIsSelect(false);
                this.msgUserChannelService.save(channel);
            }
        }
    }
}
