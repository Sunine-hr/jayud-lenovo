package com.jayud.scm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonPageResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.scm.mapper.SystemSqlConfigMapper;
import com.jayud.scm.model.bo.QueryCommonConfigForm;
import com.jayud.scm.model.bo.QuerySystemSqlConfigForm;
import com.jayud.scm.model.bo.SystemSqlConfigForm;
import com.jayud.scm.model.enums.VoidedEnum;
import com.jayud.scm.model.po.SystemSqlConfig;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.SystemSqlConfigVO;
import com.jayud.scm.model.vo.TableColumnVO;
import com.jayud.scm.service.ISystemRoleActionDataService;
import com.jayud.scm.service.ISystemSqlConfigService;
import com.jayud.scm.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SQL数据源配置 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Service
public class SystemSqlConfigServiceImpl extends ServiceImpl<SystemSqlConfigMapper, SystemSqlConfig> implements ISystemSqlConfigService {

    @Autowired
    private SystemSqlConfigMapper systemSqlConfigMapper;

    @Autowired
    private ISystemUserService systemUserService;

    @Autowired
    private ISystemRoleActionDataService systemRoleActionDataService;

    @Override
    public IPage<SystemSqlConfigVO> findByPage(QuerySystemSqlConfigForm form) {
        //定义分页参数
        Page<SystemSqlConfigVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<SystemSqlConfigVO> pageInfo = systemSqlConfigMapper.findByPage(page, form);
        return pageInfo;
    }

    @Override
    public SystemSqlConfigVO getSystemSqlConfigById(Integer id) {
        return systemSqlConfigMapper.getSystemSqlConfigById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSystemSqlConfig(SystemSqlConfigForm form) {
        SystemSqlConfig systemSqlConfig = ConvertUtil.convert(form, SystemSqlConfig.class);
        Integer id = systemSqlConfig.getId();
        String sqlCode = systemSqlConfig.getSqlCode();
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录!");
        }
        if(ObjectUtil.isEmpty(id)){
            //id 为空，代表新增
            QueryWrapper<SystemSqlConfig> systemSqlConfigQueryWrapper = new QueryWrapper<>();
            systemSqlConfigQueryWrapper.eq("sql_code", sqlCode);
            List<SystemSqlConfig> list = this.list(systemSqlConfigQueryWrapper);
            if(ObjectUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL代码，已存在，不能新增");
            }
            systemSqlConfig.setCrtBy(systemUser.getId().intValue());//创建人ID(system_user id)
            systemSqlConfig.setCrtByName(systemUser.getUserName());//创建人名称(system_user user_name)
            systemSqlConfig.setCrtByDtm(LocalDateTime.now());//创建时间
        }else{
            //id 不为空，代表修改
            QueryWrapper<SystemSqlConfig> systemSqlConfigQueryWrapper = new QueryWrapper<>();
            systemSqlConfigQueryWrapper.ne("id", id);
            systemSqlConfigQueryWrapper.eq("sql_code", sqlCode);
            List<SystemSqlConfig> list = this.list(systemSqlConfigQueryWrapper);
            if(ObjectUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL代码，已存在，不能修改");
            }
            systemSqlConfig.setMdyBy(systemUser.getId().intValue());//最后修改人ID
            systemSqlConfig.setMdyByName(systemUser.getUserName());//最后修改人名称
            systemSqlConfig.setMdyByDtm(LocalDateTime.now());//最后修改时间

        }
        this.saveOrUpdate(systemSqlConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delSystemSqlConfig(Integer id) {
        SystemSqlConfigVO systemSqlConfigVO = systemSqlConfigMapper.getSystemSqlConfigById(id);
        if(ObjectUtil.isEmpty(systemSqlConfigVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "记录不存在，不能删除");
        }
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录!");
        }
        SystemSqlConfig systemSqlConfig = ConvertUtil.convert(systemSqlConfigVO, SystemSqlConfig.class);
        systemSqlConfig.setVoided(VoidedEnum.ONE.getCode());//删除标记 设为1
        systemSqlConfig.setVoidedBy(systemUser.getId().intValue());//删除人ID
        systemSqlConfig.setVoidedByName(systemUser.getUserName());//删除人名称
        systemSqlConfig.setVoidedByDtm(LocalDateTime.now());//删除时间
        this.saveOrUpdate(systemSqlConfig);
    }

    @Override
    public List<TableColumnVO> getTableColumn(String sqlCode) {
        /*
        //JSON数据
        //list -> json
        String json = JSONObject.toJSONString(tableColumnList);
        //json -> list
        List<TableColumnVO> lsit = JSON.parseObject(json, new TypeReference<List<TableColumnVO>>() {});
        */
        SystemSqlConfigVO systemSqlConfigVO = systemSqlConfigMapper.getSystemSqlConfigBySqlCode(sqlCode);
        if(ObjectUtil.isEmpty(systemSqlConfigVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL代码，没有匹配的表格列");
        }
        String columnName = systemSqlConfigVO.getColumnName();
        List<TableColumnVO> lsit = new ArrayList<>();
        if(ObjectUtil.isNotEmpty(columnName)){
            try {
                lsit = JSON.parseObject(columnName, new TypeReference<List<TableColumnVO>>() {});
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return lsit;
    }

    @Override
    public CommonPageResult<Map<String, Object>>  findCommonByPage(QueryCommonConfigForm form) {
        String paraIdentifier = "@";//查询参数标识符
        //登录用户信息
        SystemUser systemUser = systemUserService.getSystemUserBySystemName(UserOperator.getToken());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "用户失效，请重新登录!");
        }
        String sqlCode = form.getSqlCode();//SQL代码
        Map<String, Object> condPara = form.getCondPara();//条件参数{k-v},键值对
        if(ObjectUtil.isEmpty(condPara)){
            condPara = new HashMap<>();
        }
        String sqlWhereCondition = form.getSqlWhereCondition();//where条件(and ...)
        SystemSqlConfigVO systemSqlConfigVO = systemSqlConfigMapper.getSystemSqlConfigBySqlCode(sqlCode);
        if(ObjectUtil.isEmpty(systemSqlConfigVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL代码，没有找到对应的SQL配置");
        }
        //1.主SQL语句
        String sqlStr = systemSqlConfigVO.getSqlStr();//SQL语句
        String sqlParams = systemSqlConfigVO.getSqlParams();//WHERE语句
        String sqlOrder = systemSqlConfigVO.getSqlOrder();//ORDER语句

        if(StrUtil.isEmpty(sqlStr)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL代码，没有配置SQL语句。");
        }
        //2.WHERE条件语句
        String where = "";//WHERE语句
        if(StrUtil.isNotEmpty(sqlParams)){
            String[] split = sqlParams.split("[|]");
            StringBuffer stringBuffer = new StringBuffer();
            for (int k=0; k<split.length; k++){
                String s = split[k];
                if(k== 0){
                    stringBuffer.append(s);//第一个where条件，直接放入，不用匹配
                }else{
                    //condPara 循环匹配
                    for (Map.Entry<String, Object> m : condPara.entrySet()) {
                        //System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                        String key = m.getKey();//键
                        Object value = m.getValue();//值

                        String matchesParam  = paraIdentifier+key;//例子：@sqlCode

                        //匹配`字符串s`中是否含有`字符串matchesParam`
                        if(s.contains(matchesParam)){
                            //替换
                            String replace = s.replace(matchesParam, value.toString());
                            stringBuffer.append(replace);
                            break;
                        }
                    }
                }
            }
            where = stringBuffer.toString();
        }

        String sqlDataStr = systemSqlConfigVO.getSqlDataStr();//数据权限查询
        if(StrUtil.isNotEmpty(sqlDataStr) && !systemUser.getUserName().equals("admin")){
            List<Integer> roleData = systemRoleActionDataService.getRoleData(form.getActionCode());
            if(CollectionUtil.isNotEmpty(roleData)){
                StringBuffer stringBuffer = new StringBuffer();
                for (Integer roleDatum : roleData) {
                    stringBuffer.append(roleDatum).append(",");
                }
                where = where + " " + sqlDataStr.replaceAll("@userId", stringBuffer.substring(0,stringBuffer.length()-1));
            }

        }
        if(StrUtil.isNotEmpty(sqlWhereCondition)){
            //where条件(and ...)
            where = where + " " + sqlWhereCondition;
        }

        //3.ORDER BY语句，排序
        String order = "";
        if(StrUtil.isNotEmpty(sqlOrder)){
            order = order + " " + sqlOrder;
        }

        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("sqlStr", sqlStr);//SQL语句
        paraMap.put("where", where);//WHERE语句
        paraMap.put("order", order);//ORDER BY语句
        //定义分页参数
        Page<Map<String, Object>> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则        注释掉，不在这里排序，后台用sql拼装排序
        //page.addOrder(OrderItem.desc("t.id"));
        IPage<Map<String, Object>> pageInfo = systemSqlConfigMapper.findCommonByPage(page, paraMap);//这是分页的结果集

        Map<String, Object> extendedData = new HashMap<>();
        List<Map<String, Object>> records = pageInfo.getRecords();
        if(CollUtil.isEmpty(records)){
            extendedData.put("pageMsg", systemSqlConfigVO.getMsgStr());//无数据消息提醒
        }else{
            extendedData.put("pageMsg", "分页查询成功");//无数据消息提醒
        }
        //数据汇总列 countMap
        String sqlSumColumn = systemSqlConfigVO.getSqlSumColumn();
        if(StrUtil.isNotEmpty(sqlSumColumn)){

            String[] sqlSumColumnSplist = sqlSumColumn.split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for (int n=0; n<sqlSumColumnSplist.length; n++){
                String field = sqlSumColumnSplist[n];
                if(n==0){
                    stringBuffer.append(" IFNULL(SUM("+field+"), 0) " + field + " ");
                }else{
                    stringBuffer.append(",").append(" IFNULL(SUM("+field+"), 0) " + field + " ");
                }
            }
            String countSql = stringBuffer.toString();//汇总sql
            if(StrUtil.isNotEmpty(countSql)){
                paraMap.put("countSql", countSql);//汇总sql
                Map<String, Object> countMap = systemSqlConfigMapper.findCommonCount(paraMap);//汇总数据Map
                extendedData.put("countMap", countMap);
            }
        }
        CommonPageResult<Map<String, Object>> pageVO = new CommonPageResult(pageInfo, extendedData);//这个是整个的结果结果集（后面汇总的结果放在extendedData扩展数据里面）
        return pageVO;
    }


}
