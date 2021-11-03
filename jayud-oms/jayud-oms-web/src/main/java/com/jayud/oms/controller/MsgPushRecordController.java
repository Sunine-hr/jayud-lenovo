package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.feign.OauthClient;
import com.jayud.oms.model.bo.QueryMsgPushRecordForm;
import com.jayud.oms.model.po.MsgPushRecord;
import com.jayud.oms.model.vo.MsgPushRecordVO;
import com.jayud.oms.service.IMsgPushRecordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息推送记录 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-08-03
 */
@RestController
@RequestMapping("/msgPushRecord")
public class MsgPushRecordController {

    @Autowired
    private IMsgPushRecordService msgPushRecordService;
    @Autowired
    private OauthClient oauthClient;

    @ApiOperation(value = "获取未读信息")
    @PostMapping(value = "/getUnreadInfo")
    public CommonResult<Integer> getUnreadInfo() {
        String token = UserOperator.getToken();
        Object userId = oauthClient.getSystemUserBySystemName(token).getData();
        QueryWrapper<MsgPushRecord> condition = new QueryWrapper<>();
        condition.lambda().eq(MsgPushRecord::getRecipientId, userId).eq(MsgPushRecord::getOptStatus, 1).groupBy(MsgPushRecord::getReceivingStatus, MsgPushRecord::getInitialTime);
        List<MsgPushRecord> list = msgPushRecordService.getBaseMapper().selectList(condition);
        return CommonResult.success(list.size());
    }

    @ApiOperation(value = "获取消息记录")
    @PostMapping(value = "/getMsgPushRecord")
    public CommonResult<List<MsgPushRecordVO>> getMsgPushRecord() {
        String token = UserOperator.getToken();
        Object userId = oauthClient.getSystemUserBySystemName(token).getData();
        QueryMsgPushRecordForm form = new QueryMsgPushRecordForm();
        form.setRecipientId(Long.valueOf(userId.toString())).setRecipientName(null).setPageNum(1);
        form.setPageSize(5);
        IPage<MsgPushRecordVO> iPage = msgPushRecordService.findByPage(form);
        return CommonResult.success(iPage.getRecords());
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/findByPage")
    public CommonResult<CommonPageResult<MsgPushRecordVO>> findByPage(@RequestBody QueryMsgPushRecordForm form) {
        String token = UserOperator.getToken();
        Object userId = oauthClient.getSystemUserBySystemName(token).getData();
        form.setRecipientId(Long.valueOf(userId.toString())).setRecipientName(null);
        IPage<MsgPushRecordVO> iPage = msgPushRecordService.findByPage(form);
        return CommonResult.success(new CommonPageResult(iPage));
    }

    @ApiOperation(value = "查询详情")
    @PostMapping(value = "/getDetails")
    public CommonResult<MsgPushRecordVO> getDetails(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        MsgPushRecord tmp = msgPushRecordService.getById(id);
        return CommonResult.success(ConvertUtil.convert(tmp, MsgPushRecordVO.class));
    }


    @ApiOperation(value = "执行已读操作")
    @PostMapping(value = "/doReadOperation")
    public CommonResult doReadOperation(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, "id");
        if (id == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        MsgPushRecord msgPushRecord = this.msgPushRecordService.getById(id);
        //操作状态(1:未读,2:已读,3:删除)
        if (msgPushRecord.getOptStatus() == 1) {
            this.msgPushRecordService.update(new MsgPushRecord().setOptStatus(2),
                    new QueryWrapper<>(new MsgPushRecord().setId(id).setOptStatus(msgPushRecord.getOptStatus()).setInitialTime(msgPushRecord.getInitialTime())));
//            this.msgPushRecordService.updateById(new MsgPushRecord().setId(id).setOptStatus(2));
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "标记已读操作")
    @PostMapping(value = "/doMarkRead")
    public CommonResult doMarkRead(@RequestBody List<MsgPushRecordVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        Set<Long> ids = list.stream().map(MsgPushRecordVO::getId).collect(Collectors.toSet());

        for (Long id : ids) {
            MsgPushRecord msgPushRecord = this.msgPushRecordService.getById(id);
            //操作状态(1:未读,2:已读,3:删除)
            if (msgPushRecord.getOptStatus() == 1) {
                this.msgPushRecordService.update(new MsgPushRecord().setOptStatus(2),
                        new QueryWrapper<>(new MsgPushRecord().setId(id).setOptStatus(msgPushRecord.getOptStatus()).setInitialTime(msgPushRecord.getInitialTime())));
            }
        }

//        this.msgPushRecordService.doMarkRead(new ArrayList<>(ids));
        return CommonResult.success();
    }

    @ApiOperation(value = "执行全部已读操作")
    @PostMapping(value = "/doAllReadOperation")
    public CommonResult doAllReadOperation(@RequestBody Map<String, Object> map) {
        this.msgPushRecordService.doAllReadOperation();
        return CommonResult.success();
    }

    @ApiOperation(value = "执行删除操作")
    @PostMapping(value = "/doDeleteOperation")
    public CommonResult doDeleteOperation(@RequestBody List<MsgPushRecordVO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
//        List<MsgPushRecord> deletes = new ArrayList<>();
        for (MsgPushRecordVO msgPushRecordVO : list) {
            this.msgPushRecordService.update(new MsgPushRecord().setOptStatus(3),
                    new QueryWrapper<>(new MsgPushRecord().setId(msgPushRecordVO.getId()).setOptStatus(msgPushRecordVO.getOptStatus()).setInitialTime(msgPushRecordVO.getInitialTime())));
            MsgPushRecord tmp = new MsgPushRecord();
            //操作状态(1:未读,2:已读,3:删除)
//            tmp.setId(msgPushRecordVO.getId()).setOptStatus(3);
//            deletes.add(tmp);
        }
//        this.msgPushRecordService.updateBatchById(deletes);
        return CommonResult.success();
    }


    @ApiOperation(value = "执行全部删除操作")
    @PostMapping(value = "/doAllDeleteOperation")
    public CommonResult doAllDeleteOperation() {
        this.msgPushRecordService.doAllDeleteOperation();
        return CommonResult.success();
    }

}

