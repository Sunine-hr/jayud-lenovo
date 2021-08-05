package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.beetl.BeetlUtils;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.mapper.MessagePushTemplateMapper;
import com.jayud.oms.model.bo.AddMessagePushTemplateForm;
import com.jayud.oms.model.bo.QueryMessagePushTemplateForm;
import com.jayud.oms.model.po.BindingMsgTemplate;
import com.jayud.oms.model.po.MessagePushTemplate;
import com.jayud.oms.model.po.MsgPushList;
import com.jayud.oms.model.po.MsgPushRecord;
import com.jayud.oms.model.vo.MessagePushTemplateVO;
import com.jayud.oms.model.vo.SystemUserVO;
import com.jayud.oms.service.IBindingMsgTemplateService;
import com.jayud.oms.service.IMessagePushTemplateService;
import com.jayud.oms.service.IMsgPushListService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息推送模板 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
@Service
public class MessagePushTemplateServiceImpl extends ServiceImpl<MessagePushTemplateMapper, MessagePushTemplate> implements IMessagePushTemplateService {

    @Autowired
    private IMsgPushListService msgPushListService;
    @Autowired
    private IBindingMsgTemplateService bindingMsgTemplateService;
    @Autowired
    private OauthClient oauthClient;


    @Override
    @Transactional
    public void saveOrUpdate(AddMessagePushTemplateForm form) {
        MessagePushTemplate convert = ConvertUtil.convert(form, MessagePushTemplate.class);
        convert.setNum(null);
        if (convert.getId() == null) {
            String prefix = form.getType() == 2 ? "CMT" : "OMT";
            //订单规则
            Integer count = this.baseMapper.selectCount(new QueryWrapper<>(new MessagePushTemplate().setType(form.getType())));
            convert.setNum(prefix + StringUtils.zeroComplement(4, count + 1));
            convert.setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
        } else {
            convert.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(convert);

        //操作人类型,根据岗位获取所有人,并且创建消息
        if (form.getType() == 1 && form.getId() == null && !StringUtils.isEmpty(form.getPost())) {
            Map<String, MsgPushList> msgPushLists = this.msgPushListService.getByCondition(new MsgPushList().setType(form.getType()))
                    .stream().collect(Collectors.toMap(e -> e.getRecipientId() + "~" + e.getPost(), e -> e));

            //根据岗位查询消息列表,根据岗位追加模板
            List<MsgPushList> addOpt = new ArrayList<>();
            List<BindingMsgTemplate> update = new ArrayList<>();
            for (String post : form.getPosts()) {
                List<SystemUserVO> users = this.oauthClient.getEnableUserByWorkName(post).getData();
                if (CollectionUtils.isEmpty(users)) {
                    continue;
                }
                //根据岗位+用户id查询消息列表消息
                users.forEach(e -> {
                    MsgPushList msgPushList = msgPushLists.get(e.getId() + "~" + post);
                    if (msgPushList == null) {
                        MsgPushList tmp = new MsgPushList().setCreateTime(LocalDateTime.now())
                                .setCreateUser(UserOperator.getToken()).setPost(post).setRecipientName(e.getUserName())
                                .setRecipientId(e.getId()).setType(form.getType());
                        addOpt.add(tmp);
                    } else {
                        //存在消息列表追加,在绑定模板追加
                        BindingMsgTemplate bindingMsgTemplate = new BindingMsgTemplate().setMsgListId(msgPushList.getId())
                                .setTemplateId(convert.getId()).setPost(post).setType(form.getType())
                                .setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
                        update.add(bindingMsgTemplate);
                    }
                });
            }
            if (CollectionUtils.isNotEmpty(addOpt)) {
                this.msgPushListService.saveBatch(addOpt);
                addOpt.forEach(e -> {
                    BindingMsgTemplate bindingMsgTemplate = new BindingMsgTemplate().setMsgListId(e.getId())
                            .setTemplateId(convert.getId()).setPost(e.getPost()).setType(form.getType())
                            .setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
                    update.add(bindingMsgTemplate);
                });
            }
            this.bindingMsgTemplateService.saveBatch(update);
        }
    }

    @Override
    public Map<String, Object> executeTemplateSQL(String sqlSelect) {
        try {
            return this.baseMapper.executeTemplateSQL(sqlSelect);
        } catch (Exception e) {
            throw new JayudBizException("sql语法错误 错误信息:" + e.getMessage());
        }
    }

    @Override
    public List<MessagePushTemplate> getByCondition(MessagePushTemplate messagePushTemplate) {
        return this.baseMapper.selectList(new QueryWrapper<>(messagePushTemplate));
    }

    @Override
    public boolean checkUnique(AddMessagePushTemplateForm form) {
        List<MessagePushTemplate> list = this.getByCondition(new MessagePushTemplate()
                .setMsgName(form.getMsgName()).setType(form.getType()));
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        MessagePushTemplate messagePushTemplate = list.get(0);
        return messagePushTemplate.getId().equals(form.getId());
    }

    @Override
    public IPage<MessagePushTemplateVO> findByPage(QueryMessagePushTemplateForm form) {
        //定义分页参数
        Page<MessagePushTemplateVO> page = new Page(form.getPageNum(), form.getPageSize());
        IPage<MessagePushTemplateVO> iPage = this.baseMapper.findByPage(page, form);
        return iPage;
    }

    /**
     * 更改启用/禁用状态
     *
     * @param id
     * @return
     */
    @Override
    public boolean enableOrDisable(Long id) {
        //查询当前状态
        QueryWrapper<MessagePushTemplate> condition = new QueryWrapper<>();
        condition.lambda().select(MessagePushTemplate::getStatus).eq(MessagePushTemplate::getId, id);
        MessagePushTemplate tmp = this.baseMapper.selectOne(condition);

        Integer status = 1 == tmp.getStatus() ? StatusEnum.DISABLE.getCode() : StatusEnum.ENABLE.getCode();

        MessagePushTemplate update = new MessagePushTemplate().setId(id).setStatus(status)
                .setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());

        return this.updateById(update);
    }

    @Override
    public void fillTemplate(MsgPushRecord msgPushRecord) throws Exception {
        Map<String, Object> queryParam = this.executeTemplateSQL(msgPushRecord.getSqlSelect());
        String content = BeetlUtils.strTemplate(msgPushRecord.getTemplateContent(), queryParam);
        String title = BeetlUtils.strTemplate(msgPushRecord.getTemplateTitle(), queryParam);
        msgPushRecord.setReceiveContent(content).setTitle(title);
    }
}
