package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.JDKUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.service.ILogisticsTrackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * <p>
 * 文件管理 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-03-04
 */
@RestController
@Api(tags = "文件管理")
@RequestMapping("/file")
public class FileManagementController {

    @Autowired
    private ILogisticsTrackService logisticsTrackService;
    @Autowired
    private FileClient fileClient;


    /**
     * 查询文件
     */
    @ApiOperation(value = "查询文件")
    @PostMapping("/list")
    public CommonResult<List<Map<String, Object>>> list(@RequestBody Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject(map);
        if (jsonObject.isNull("data")) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        JSONArray datas = jsonObject.getJSONArray("data");
        Map<Long, String> orderNo = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        Integer type = 0;
        for (int i = 0; i < datas.size(); i++) {
            JSONObject object = datas.getJSONObject(i);
            ids.add(object.getLong("id"));
            type = object.getInt("type");
            orderNo.put(object.getLong("id"), object.getStr("orderNo"));
        }
        List<LogisticsTrack> list = logisticsTrackService.getLogisticsTrackByType(ids, type);
        list = JDKUtils.getGroupByLastData(list, LogisticsTrack::getCreatedTime, LogisticsTrack::getStatus);
        Object url = fileClient.getBaseUrl().getData();

        List<Map<String, Object>> responses = new ArrayList<>();
        list.forEach(e -> {
            if (!StringUtils.isEmpty(e.getStatusPic())) {
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("type", e.getStatusName());
                tmp.put("fileViews", StringUtils.getFileViews(e.getStatusPic(), e.getStatusPicName(), url.toString()));
                tmp.put("createdUser", e.getCreatedUser());
                tmp.put("createdTime", e.getCreatedTime());
                tmp.put("orderNo", orderNo.get(e.getOrderId()));
                responses.add(tmp);
            }
        });

        return CommonResult.success(responses);
    }
}

