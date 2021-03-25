package com.jayud.finance.service;

import com.alibaba.fastjson.JSONObject;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.PayableHeaderForm;
import com.jayud.finance.bo.ReceivableHeaderForm;
import com.jayud.finance.po.K3Entity;
import com.jayud.finance.po.PushProperties;

import java.util.List;
import java.util.Map;

/**
 * 金蝶接口服务
 */
public interface KingdeeService {

    /**
     * 金蝶登录接口
     *
     * @param url     接口地址
     * @param content 查询参数
     */
    CommonResult login(String url, String content);


    /**
     * 金蝶查询接口
     * <br/>
     * 请求参数最终会被拼装成
     * <br/>{
     * <br/>    "formid":"业务表单ID",
     * <br/>    "data":"查询参数json格式数据"
     * <br/>}
     * <br/>的形式
     * <br/>createdby ziran_ye
     * <br/>modifiedby william 20200528
     *
     * @param formId 业务表单ID
     * @param param  查询参数
     */
    CommonResult view(String formId, Map<String, Object> param);

    /**
     * 根据接口请求向金蝶推送应收单
     *
     * @param formId  表单头（必录，预设）
     * @param reqForm 请求数据
     * @return
     */
    CommonResult saveReceivableBill(String formId, ReceivableHeaderForm reqForm);

    /**
     * 根据接口请求向金蝶推送应付单
     *
     * @param formId  表单头（必录，预设）
     * @param reqForm 请求数据
     * @return
     */
    CommonResult savePayableBill(String formId, PayableHeaderForm reqForm);

    /**
     * @param formId 业务对象ID
     * @param basic  保存参数
     */
    CommonResult save(String formId, JSONObject basic);

    /**
     * 列表查询数据
     *
     * @param filterString 过滤条件
     * @param clz
     * @param k3Entity
     * @return
     */
    <T> CommonResult<List<T>> query(String filterString, Class<T> clz, K3Entity k3Entity);

    /**
     * 根据作业单号查询并返回对应的应收或应付单数据
     * by william 20200528
     *
     * @param orderId
     * @param clz
     * @param <T>
     * @return
     */
    <T> CommonResult<T> getInvoice(String orderId, Class<T> clz);

    /**
     * @param formId 业务对象ID
     */
    CommonResult save(String formId, String content);


    /**
     * 删除金蝶已存在应收/应付订单
     *
     * @param businessNo 业务单号
     * @param type    0应收,1应付
     */
    void deleteOrder(String businessNo, int type);


    /**
     * 金蝶删除接口
     * @param properties
     */
    public PushProperties removeSpecifiedInvoice(PushProperties properties);
}
