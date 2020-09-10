package com.jayud.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.enums.FormIDEnum;
import com.jayud.po.K3Entity;
import com.jayud.service.BaseService;
import com.jayud.service.KingdeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * k3基本数据查询 通用服务接口实现
 * @author bocong.zheng
 */
@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    KingdeeService kingdeeService;

    @Override
    public <T> List<T> query(String name,Class<T> clz) {
        K3Entity k3Entity = clz.getAnnotation(K3Entity.class);
        if(!Optional.of(k3Entity).isPresent()){
            Asserts.fail(ResultEnum.PARAM_ERROR,"k3基本数据字段未配置");
        }
        CommonResult<List<T>> resultVO = null;
        if (k3Entity.formId().equals(FormIDEnum.RECEIVABLE) || k3Entity.formId().equals(FormIDEnum.PAYABLE)) {
            resultVO = kingdeeService.query(likeSqlAll(k3Entity, name), clz, k3Entity);
            if (resultVO.getCode() == 0) {
                return resultVO.getData();
            }
        } else {
            resultVO = kingdeeService.query(likeSql(k3Entity, name), clz, k3Entity);
            if (resultVO.getCode() == 0) {
                return resultVO.getData();

            }
        }
        return Collections.emptyList();
    }

    @Override
    public <T> Optional<T> get(String name,Class<T> clz) {
        K3Entity k3Entity = clz.getAnnotation(K3Entity.class);
        if(!Optional.of(k3Entity).isPresent()){
            Asserts.fail(ResultEnum.PARAM_ERROR,"k3基本数据字段未配置");
        }
        T data = null;
        CommonResult<List<T>> resultVO = kingdeeService.query(equalsSql(k3Entity,name),clz,k3Entity );
        if (resultVO.getCode() == 0) {
            if (CollectionUtil.isNotEmpty(resultVO.getData())) {
                data = resultVO.getData().get(0);
            }
        }
        return Optional.ofNullable(data);
    }

   /**
     * 拼接查询条件语句，支持精确查询
     * @param k3Entity
     * @param name 查询用的key
     * @return
     */
    private String equalsSql(K3Entity k3Entity,String name){
        StringBuilder sb = new StringBuilder();
        sb.append(k3Entity.fName());
        sb.append(" = '");
        sb.append(name);
        //默认查询启用状态数据
        sb.append("' and FForbidStatus='A'");
        return sb.toString();
    }

    /**
     * 拼接查询条件语句,支持模糊查询
     * @param k3Entity
     * @param name
     * @return
     */
    private String likeSql(K3Entity k3Entity,String name){
        StringBuilder sb = new StringBuilder();
        sb.append(k3Entity.fName());
        sb.append(" like '%");
        sb.append(name);
        //默认查询启用状态数据
        sb.append("%' and FForbidStatus='A'");
        return sb.toString();
    }


    /**
     * 应收应付单带禁用状态的拼接查询条件语句,支持模糊查询
     *
     * @param k3Entity
     * @param name
     * @return
     */
    private String likeSqlAll(K3Entity k3Entity, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(k3Entity.fName());
        sb.append(" like '%");
        sb.append(name);
        //默认查询启用状态数据
        sb.append("%' and FCancelStatus='A'");
        return sb.toString();
    }
}
