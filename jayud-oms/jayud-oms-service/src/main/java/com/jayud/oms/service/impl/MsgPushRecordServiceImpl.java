package com.jayud.oms.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.beetl.BeetlUtils;
import com.jayud.common.entity.Email;
import com.jayud.common.entity.EmailSysConf;
import com.jayud.common.entity.EntWechatSysConf;
import com.jayud.common.enums.*;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.EmailClient;
import com.jayud.oms.feign.MsgClient;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.QueryMsgPushListCondition;
import com.jayud.oms.model.bo.QueryMsgPushRecordForm;
import com.jayud.oms.model.po.*;
import com.jayud.oms.mapper.MsgPushRecordMapper;
import com.jayud.oms.model.vo.MsgPushRecordVO;
import com.jayud.oms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
@Slf4j
public class MsgPushRecordServiceImpl extends ServiceImpl<MsgPushRecordMapper, MsgPushRecord> implements IMsgPushRecordService {

    @Autowired
    private IMessagePushTemplateService messagePushTemplateService;
    @Autowired
    private IMsgPushListService msgPushListService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private EmailClient emailClient;
    @Autowired
    private ISystemConfService systemConfService;
    @Autowired
    private WechatMsgService wechatMsgService;
    @Autowired
    private MsgClient msgClient;

    @Override
    public IPage<MsgPushRecordVO> findByPage(QueryMsgPushRecordForm form) {
        Page<MsgPushRecord> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    /**
     * 执行全部完成已读
     */
    @Override
    public int doAllReadOperation() {
        QueryWrapper<MsgPushRecord> condition = new QueryWrapper<>();
        condition.lambda().eq(MsgPushRecord::getOptStatus, 1);
        return this.baseMapper.update(new MsgPushRecord().setOptStatus(2), condition);
    }

    @Override
    public int doMarkRead(List<Long> ids) {
        QueryWrapper<MsgPushRecord> condition = new QueryWrapper<>();
        //操作状态(1:未读,2:已读,3:删除)
        condition.lambda().eq(MsgPushRecord::getOptStatus, 1).in(MsgPushRecord::getId, ids);
        return this.baseMapper.update(new MsgPushRecord().setOptStatus(2), condition);
    }

    @Override
    public int doAllDeleteOperation() {
        QueryWrapper<MsgPushRecord> condition = new QueryWrapper<>();
        condition.lambda().ne(MsgPushRecord::getOptStatus, 3);
        return this.baseMapper.update(new MsgPushRecord().setOptStatus(3), condition);
    }

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

    @Override
    public List<MsgPushRecord> getTasksPerformed(LocalDateTime now) {
        QueryWrapper<MsgPushRecord> condition = new QueryWrapper<>();
        condition.lambda().le(MsgPushRecord::getSendTime, now)
                .eq(MsgPushRecord::getStatus, SendStatusTypeEnum.WAIT.getCode());
        return this.baseMapper.selectList(condition);
    }

    @Override
    public void messagePush() {
        List<MsgPushRecord> list = this.getTasksPerformed(LocalDateTime.now());
        List<Integer> channelIds = new ArrayList<>();
        for (SystemConfTypeEnum value : SystemConfTypeEnum.values()) {
            channelIds.add(value.getCode());
        }

        QueryWrapper<SystemConf> condition = new QueryWrapper<>();
        condition.lambda().in(SystemConf::getType, channelIds);
        Map<Integer, String> sysConfMap = systemConfService.getBaseMapper().selectList(condition).stream().collect(Collectors.toMap(SystemConf::getType, SystemConf::getConfData));

        for (MsgPushRecord msgPushRecord : list) {
            MsgChannelTypeEnum channelTypeEnum = MsgChannelTypeEnum.getEnum(msgPushRecord.getMsgChannelType());
            if (channelTypeEnum == null) {
                this.errMsg(msgPushRecord.getId(), "暂不支持" + msgPushRecord.getReceivingMode() + "消息推送模式", SendStatusTypeEnum.FAIL.getCode());
                continue;
            }
            //填充模板
            try {
                if (StringUtils.isEmpty(msgPushRecord.getReceiveContent())) {
                    this.messagePushTemplateService.fillTemplate(msgPushRecord);
                }
            } catch (Exception e) {
                String msg = "动态生成发送内容失败 msg:" + e.getMessage();
                log.warn(msg);
                this.errMsg(msgPushRecord.getId(), msg, SendStatusTypeEnum.FAIL.getCode());
                continue;
            }

            switch (channelTypeEnum) {
                case MAIL://邮件发送
                    this.sendEmail(msgPushRecord, sysConfMap);
                    break;
                case ENT_WECHAT: //企业微信
                    this.sendEntWechat(msgPushRecord, sysConfMap);
                    break;
                case DING_DING: //钉钉
                    this.sendDingDing(msgPushRecord, sysConfMap);
                    break;
            }
        }
    }


    private void sendEmail(MsgPushRecord msgPushRecord, Map<Integer, String> sysConfMap) {
        String sysConfStr = sysConfMap.get(SystemConfTypeEnum.ONE.getCode());
        if (StringUtils.isEmpty(sysConfStr)) {
            log.warn("请配置消息推送配置");
            this.updateById(new MsgPushRecord().setId(msgPushRecord.getId()).setErrMsg("请配置消息推送配置"));
            return;
        }
        EmailSysConf emailSysConf = JSONUtil.toBean(sysConfStr, EmailSysConf.class);
        Email email = new Email().setHost(emailSysConf.getUrl()).setFrom(emailSysConf.getAccount())
                .setPassword(emailSysConf.getPwd()).setSubject(msgPushRecord.getTitle()).setText(msgPushRecord.getReceiveContent())
                .setTo(msgPushRecord.getReceivingAccount());
        try {
            if (!this.emailClient.sendEmail(email).getCode().equals(0)) {
                this.sendFailProcessing(msgPushRecord, "发送邮件失败");
            } else {
                this.sendSuccess(msgPushRecord);
            }
        } catch (Exception e) {
            log.warn("发送邮件系统错误 msg:" + e.getMessage());
            this.errMsg(msgPushRecord.getId(), "发送邮件失败,系统错误", msgPushRecord.getStatus());
        }
    }

    private void sendEntWechat(MsgPushRecord msgPushRecord, Map<Integer, String> sysConfMap) {
        String sysConfStr = sysConfMap.get(SystemConfTypeEnum.TWO.getCode());
        if (StringUtils.isEmpty(sysConfStr)) {
            log.warn("请配置消息推送配置");
            this.updateById(new MsgPushRecord().setId(msgPushRecord.getId()).setErrMsg("请配置消息推送配置"));
            return;
        }
        EntWechatSysConf entWechatSysConf = JSONUtil.toBean(sysConfStr, EntWechatSysConf.class);
        String token = this.wechatMsgService.getEnterpriseToken(entWechatSysConf.getCorpid(), entWechatSysConf.getCorpsecret(), true).getStr("access_token");
        try {
            JSONObject result = this.wechatMsgService.sendEnterpriseMsg(msgPushRecord.getReceivingAccount(),
                    msgPushRecord.getReceiveContent(), entWechatSysConf.getAgentid(), entWechatSysConf.getCorpid(),
                    entWechatSysConf.getCorpsecret(), token);
            if (!result.getInt("errcode").equals(0)) {
//                log.warn("发送{}失败 用户={} 状态={} 报错信息={}",msgPushRecord.getReceivingMode(),
//                        msgPushRecord.getRecipientName(),msgPushRecord.getReceivingStatus(),result.get("errmsg"));
                this.sendFailProcessing(msgPushRecord, "发送企业微信失败");
            } else {
                this.sendSuccess(msgPushRecord);
            }
        } catch (Exception e) {
            log.warn("发送企业微信失败 msg:" + e.getMessage());
            this.errMsg(msgPushRecord.getId(), "发送企业微信失败,系统错误", msgPushRecord.getStatus());
        }
    }

    private void sendDingDing(MsgPushRecord msgPushRecord, Map<Integer, String> sysConfMap) {
        String sysConfStr = sysConfMap.get(SystemConfTypeEnum.THREE.getCode());
        if (StringUtils.isEmpty(sysConfStr)) {
            log.warn("请配置消息推送配置");
            this.updateById(new MsgPushRecord().setId(msgPushRecord.getId()).setErrMsg("请配置消息推送配置"));
            return;
        }
        Map<String, String> dingDingSysConf = JSONUtil.toBean(sysConfStr, Map.class);
        try {
            dingDingSysConf.put("message", msgPushRecord.getReceiveContent());
            dingDingSysConf.put("mobile", msgPushRecord.getReceivingAccount());
            JSONObject result = this.msgClient.sendMessageByMobile(dingDingSysConf);
            if (!result.getInt("errcode").equals(0)) {
                this.sendFailProcessing(msgPushRecord, "发送钉钉失败");
            } else {
                this.sendSuccess(msgPushRecord);
            }
        } catch (Exception e) {
            log.warn("发送企业微信失败 msg:" + e.getMessage());
            this.errMsg(msgPushRecord.getId(), "发送发送钉钉失败,系统错误", msgPushRecord.getStatus());
        }
    }


    private void sendSuccess(MsgPushRecord msgPushRecord) {
        this.updateById(new MsgPushRecord().setErrMsg(" ").setId(msgPushRecord.getId()).setReceiveContent(msgPushRecord.getReceiveContent())
                .setTitle(msgPushRecord.getTitle()).setStatus(SendStatusTypeEnum.SUCCESS.getCode()));
    }

    private void sendFailProcessing(MsgPushRecord msgPushRecord, String msg) {
        MsgPushRecord tmp = new MsgPushRecord();
        if (msgPushRecord.getNum() - 1 < 0) {
            tmp.setId(msgPushRecord.getId()).setErrMsg("重试次数已用完").setStatus(SendStatusTypeEnum.FAIL.getCode());
            return;
        }
        LocalDateTime dateTime = msgPushRecord.getSendTime().plusMinutes(5);
        this.updateById(new MsgPushRecord().setId(msgPushRecord.getId()).setReceiveContent(msgPushRecord.getReceiveContent())
                .setTitle(msgPushRecord.getTitle())
                .setErrMsg(msg).setSendTime(dateTime).setNum(msgPushRecord.getNum() - 1));
    }

    private boolean errMsg(Long id, String msg, Integer status) {
        this.updateById(new MsgPushRecord().setId(id).setErrMsg(msg)
                .setStatus(status));
        return true;
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
        List<Object> msgChannelResult = this.oauthClient.getEnableMsgUserChannelByUserIds(userIds).getData();
        if (CollectionUtils.isEmpty(msgChannelResult)) {
            return;
        }
        Map<Long, List<Object>> msgChannelMap = msgChannelResult.stream().filter(e -> ((Map<String, Object>) e).get("userId") != null).
                collect(Collectors.groupingBy(e -> Long.valueOf(((Map<String, Object>) e).get("userId").toString())));
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
                    .setTitle(template.getTitle())
                    .setSubType(template.getSubType());
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
