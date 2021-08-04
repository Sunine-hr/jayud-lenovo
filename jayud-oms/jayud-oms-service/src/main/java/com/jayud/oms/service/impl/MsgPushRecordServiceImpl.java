package com.jayud.oms.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.beetl.BeetlUtils;
import com.jayud.common.enums.SendStatusTypeEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.QueryMsgPushListCondition;
import com.jayud.oms.model.po.BindingMsgTemplateInfoVO;
import com.jayud.oms.model.po.MessagePushTemplate;
import com.jayud.oms.model.po.MsgPushListInfoVO;
import com.jayud.oms.model.po.MsgPushRecord;
import com.jayud.oms.mapper.MsgPushRecordMapper;
import com.jayud.oms.service.IMessagePushTemplateService;
import com.jayud.oms.service.IMsgPushListService;
import com.jayud.oms.service.IMsgPushRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.beetl.core.misc.BeetlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息推送记录 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-03
 */
@Service
public class MsgPushRecordServiceImpl extends ServiceImpl<MsgPushRecordMapper, MsgPushRecord> implements IMsgPushRecordService {

    @Autowired
    private IMessagePushTemplateService messagePushTemplateService;
    @Autowired
    private IMsgPushListService msgPushListService;
    @Autowired
    private OauthClient oauthClient;

    /**
     * 推送任务
     */
    @Override
    public void createPushTask(String triggerStatus, Map<String, Object> sqlParam,
                               LocalDateTime now, Map<String, Object> otherParam) {
        Map<Integer, List<MessagePushTemplate>> templates = messagePushTemplateService.getByCondition(new MessagePushTemplate().setStatus(StatusEnum.ENABLE.getCode())
                .setTriggerStatus(triggerStatus)).stream().collect(Collectors.groupingBy(MessagePushTemplate::getType));
        templates.forEach((k, v) -> {
            switch (k) {
                case 1:
                    //处理操作人信息
                    this.processOptInfo(v, sqlParam, now, otherParam);
                    break;
                case 2:
                    break;
            }
        });

    }

    private void processOptInfo(List<MessagePushTemplate> list, Map<String, Object> map,
                                LocalDateTime date, Map<String, Object> otherParam) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Object associatedUserIdObj = otherParam.get("associatedUserId");
        //获取模板id
        Map<Long, MessagePushTemplate> templateMap = list.stream().collect(Collectors.toMap(MessagePushTemplate::getId, e -> e));
        QueryMsgPushListCondition condition = new QueryMsgPushListCondition();
        condition.setTemplateIds(new ArrayList<>(templateMap.keySet()));
        List<MsgPushListInfoVO> detailsList = msgPushListService.getDetailsList(condition);
        if (CollectionUtils.isEmpty(detailsList)) {
            return;
        }
        //获取用户id
        List<Long> userIds = detailsList.stream().map(MsgPushListInfoVO::getRecipientId).collect(Collectors.toList());
        //获取用户渠道
        List<Object> msgChannelResult = this.oauthClient.getMsgUserChannelByUserIds(userIds).getData();
        if (CollectionUtils.isEmpty(msgChannelResult)) {
            return;
        }
        Map<Long, List<Object>> msgChannelMap = msgChannelResult.stream().collect(Collectors.groupingBy(e -> Long.valueOf(((Map<String, Object>) e).get("userId").toString())));
        List<MsgPushRecord> msgPushRecords = new ArrayList<>();
        for (MsgPushListInfoVO msgPushListInfoVO : detailsList) {
            JSONArray msgChannelJsonArr = new JSONArray(msgChannelMap.get(msgPushListInfoVO.getRecipientId()));
            for (BindingMsgTemplateInfoVO bindingMsgTemplate : msgPushListInfoVO.getBindingMsgTemplates()) {
                MessagePushTemplate template = templateMap.get(bindingMsgTemplate.getTemplateId());
                if (bindingMsgTemplate.getSelfRegarding() &&
                        associatedUserIdObj != null && !((List<Long>) associatedUserIdObj).contains(msgPushListInfoVO.getRecipientId())) {
                    continue;
                }
                //消息渠道,一个消息对应多个渠道
                //组装消息
                msgPushRecords.addAll(this.assemblyMsgRecord(map, msgChannelJsonArr, template, msgPushListInfoVO, bindingMsgTemplate, date));
            }
        }
        this.saveBatch(msgPushRecords);
    }

    private List<MsgPushRecord> assemblyMsgRecord(Map<String, Object> paramMap, JSONArray msgChannelJsonArr,
                                                  MessagePushTemplate template, MsgPushListInfoVO msgPushListInfoVO, BindingMsgTemplateInfoVO bindingMsgTemplate, LocalDateTime date) {

        if (msgChannelJsonArr == null) {
            return new ArrayList<>();
        }
        String errMsg = "";
        try {
            template.setSqlSelect(BeetlUtils.strTemplate(template.getSqlSelect(), paramMap));
        } catch (Exception e) {
            errMsg = "sql模板生成失败 msg:" + e.getMessage();
            log.warn(errMsg);
        }
        List<MsgPushRecord> list = new ArrayList<>();
        for (int i = 0; i < msgChannelJsonArr.size(); i++) {
            JSONObject jsonObject = msgChannelJsonArr.getJSONObject(i);
            String name = jsonObject.getStr("name");
            String account = jsonObject.getStr("account");
            Integer type = jsonObject.getInt("type");
            LocalDateTime doDelayTime = date;
            if (template.getSendTimeType() == 2) {
                //时间组装
                doDelayTime = this.doDelayTime(date, template.getSendTime(), template.getTimeUnit());
            }

            MsgPushRecord msgPushRecord = new MsgPushRecord().setPost(bindingMsgTemplate.getPost())
                    .setRecipientName(msgPushListInfoVO.getRecipientName())
                    .setRecipientId(msgPushListInfoVO.getRecipientId())
                    .setType(template.getType()).setReceivingStatus(template.getTriggerStatus())
                    .setReceivingMode(name).setReceivingAccount(account)
                    .setNum(5).setInitialTime(doDelayTime).setSendTimeType(template.getSendTimeType())
                    .setDelayTime(template.getSendTime()).setTimeUnit(template.getTimeUnit())
                    .setSendTime(doDelayTime).setStatus(SendStatusTypeEnum.WAIT.getCode())
                    .setErrMsg(errMsg).setMsgChannelType(type)
                    .setTemplateContent(template.getTemplateContent())
                    .setSqlSelect(template.getSqlSelect())
                    .setTemplateTitle(template.getTemplateTitle())
                    .setTitle(template.getTitle());
            list.add(msgPushRecord);
        }
        return list;
    }

    private LocalDateTime doDelayTime(LocalDateTime date, Integer sendTime, String timeUnit) {
        LocalDateTime tmp = null;
        switch (timeUnit) {
            case "天":
                tmp = date.plusDays(sendTime);
                break;
            case "时":
                tmp = date.plusHours(sendTime);
                break;
            case "分":
                tmp = date.plusMinutes(sendTime);
                break;
        }
        return tmp;
    }
}
