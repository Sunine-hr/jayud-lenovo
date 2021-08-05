package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.beetl.BeetlUtils;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.enums.MsgTempTriggerStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.AddMessagePushTemplateForm;
import com.jayud.oms.model.bo.QueryMessagePushTemplateForm;
import com.jayud.oms.model.po.MessagePushTemplate;
import com.jayud.oms.model.vo.CostGenreVO;
import com.jayud.oms.model.vo.MessagePushTemplateVO;
import com.jayud.oms.service.IMessagePushTemplateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 消息推送模板 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-08-02
 */
@RestController
@RequestMapping("/messagePushTemplate")
public class MessagePushTemplateController {

    @Autowired
    private IMessagePushTemplateService messagePushTemplateService;

    @ApiOperation(value = "创建编辑模板")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(@RequestBody @Valid AddMessagePushTemplateForm form) throws Exception {
        form.checkSql();
        //操作状态,一个状态只能一个模板
        if (form.getType() == 1 && form.getId() == null) {
            int count = this.messagePushTemplateService.getByCondition(new MessagePushTemplate()
                    .setTriggerStatus(form.getTriggerStatus()).setType(form.getType())).size();
            if (count > 0) return CommonResult.error(400, "一个提醒状态只能绑定一个模板");
        }

        if (!this.messagePushTemplateService.checkUnique(form)) {
            return CommonResult.error(400, "消息名称重复");
        }

//        Map<String, Object> queryParam = this.messagePushTemplateService.executeTemplateSQL(form.getSqlSelect());
        String content = BeetlUtils.strTemplate(form.getTemplateContent(), null);
        String title = BeetlUtils.strTemplate(form.getTemplateTitle(), null);
        form.setContent(content).setTitle(title);
        this.messagePushTemplateService.saveOrUpdate(form);
        return CommonResult.success();
    }

    @ApiOperation(value = "分页查询消息模板")
    @PostMapping("/findByPage")
    public CommonResult<CommonPageResult<MessagePushTemplateVO>> findByPage(@RequestBody QueryMessagePushTemplateForm form) {
        IPage<MessagePushTemplateVO> pageList = this.messagePushTemplateService.findByPage(form);
        CommonPageResult<MessagePushTemplateVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询推送模板详情")
    @PostMapping("/getDetails")
    public CommonResult<MessagePushTemplateVO> getDetails(@RequestBody Map<String, String> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        MessagePushTemplate tmp = this.messagePushTemplateService.getById(id);
        MessagePushTemplateVO convert = ConvertUtil.convert(tmp, MessagePushTemplateVO.class);
        return CommonResult.success(convert);
    }

    @ApiOperation(value = "启用/禁用,id是主键")
    @PostMapping(value = "/enableOrDisable")
    public CommonResult enableOrDisable(@RequestBody Map<String, String> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        if (this.messagePushTemplateService.enableOrDisable(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
    }


    @ApiOperation(value = "查询模块提醒状态")
    @PostMapping("/getReminderStatus")
    public CommonResult<List<InitComboxStrVO>> getReminderStatus(@RequestBody Map<String, Object> map) {
        String mark = MapUtil.getStr(map, "code");
        if (StringUtils.isEmpty(mark)) {
            return CommonResult.error(400, "请选择模块");
        }
        return CommonResult.success(MsgTempTriggerStatusEnum.initComboxStrVO(mark));
    }

    @ApiOperation(value = "查询模块")
    @PostMapping("/getModules")
    public CommonResult<List<InitComboxStrVO>> getModules() {
        return CommonResult.success(MsgTempTriggerStatusEnum.getModules());
    }


}

