package com.jayud.auth.service.impl;

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
import com.jayud.auth.feign.CrmClient;
import com.jayud.auth.mapper.SystemSqlConfigMapper;
import com.jayud.auth.model.bo.QueryCommonConfigForm;
import com.jayud.auth.model.bo.QuerySystemSqlConfigForm;
import com.jayud.auth.model.bo.SystemSqlConfigForm;
import com.jayud.auth.model.po.SysDepart;
import com.jayud.auth.model.po.SysTenantRole;
import com.jayud.auth.model.po.SysUser;
import com.jayud.auth.model.po.SystemSqlConfig;
import com.jayud.auth.model.vo.SysUserVO;
import com.jayud.auth.model.vo.SystemSqlConfigVO;
import com.jayud.auth.model.vo.TableColumnVO;
import com.jayud.auth.service.ISysDepartService;
import com.jayud.auth.service.ISysTenantRoleService;
import com.jayud.auth.service.ISysUserService;
import com.jayud.auth.service.ISystemSqlConfigService;
import com.jayud.common.BaseResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlCodeConstant;
import com.jayud.common.dto.AuthUserDetail;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.CurrentUserUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * SQL??????????????? ???????????????
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
    private ISysUserService sysUserService;
    @Autowired
    private ISysTenantRoleService sysTenantRoleService;

    @Autowired
    private ISysDepartService sysDepartService;


    @Autowired
    private CrmClient crmClient;

    @Override
    public IPage<SystemSqlConfigVO> findByPage(QuerySystemSqlConfigForm form) {
        //??????????????????
        Page<SystemSqlConfigVO> page = new Page(form.getPageNum(),form.getPageSize());
        //??????????????????
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
        //??????????????????
        SysUserVO systemUser = sysUserService.getSystemUserByName(CurrentUserUtil.getUsername());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "??????????????????????????????!");
        }
        if(ObjectUtil.isEmpty(id)){
            //id ?????????????????????
            QueryWrapper<SystemSqlConfig> systemSqlConfigQueryWrapper = new QueryWrapper<>();
            systemSqlConfigQueryWrapper.eq("sql_code", sqlCode);
            List<SystemSqlConfig> list = this.list(systemSqlConfigQueryWrapper);
            if(ObjectUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL?????????????????????????????????");
            }
            //?????????ID(system_user id)
            systemSqlConfig.setCrtBy(systemUser.getId().intValue());
            //???????????????(system_user user_name)
            systemSqlConfig.setCrtByName(systemUser.getName());
            //????????????
            systemSqlConfig.setCrtByDtm(LocalDateTime.now());
        }else{
            //id ????????????????????????
            QueryWrapper<SystemSqlConfig> systemSqlConfigQueryWrapper = new QueryWrapper<>();
            systemSqlConfigQueryWrapper.ne("id", id);
            systemSqlConfigQueryWrapper.eq("sql_code", sqlCode);
            List<SystemSqlConfig> list = this.list(systemSqlConfigQueryWrapper);
            if(ObjectUtil.isNotEmpty(list)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL?????????????????????????????????");
            }
            //???????????????ID
            systemSqlConfig.setMdyBy(systemUser.getId().intValue());
            //?????????????????????
            systemSqlConfig.setMdyByName(systemUser.getName());
            //??????????????????
            systemSqlConfig.setMdyByDtm(LocalDateTime.now());
            systemSqlConfig.setVersion(systemSqlConfig.getVersion()+1);

        }

        this.saveOrUpdate(systemSqlConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delSystemSqlConfig(Integer id) {
        SystemSqlConfigVO systemSqlConfigVO = systemSqlConfigMapper.getSystemSqlConfigById(id);
        if(ObjectUtil.isEmpty(systemSqlConfigVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "??????????????????????????????");
        }
        //??????????????????
        SysUserVO systemUser = sysUserService.getSystemUserByName(CurrentUserUtil.getUsername());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "??????????????????????????????!");
        }
        SystemSqlConfig systemSqlConfig = ConvertUtil.convert(systemSqlConfigVO, SystemSqlConfig.class);
        //???????????? ??????1
        systemSqlConfig.setVoided(1);
        //?????????ID
        systemSqlConfig.setVoidedBy(systemUser.getId().intValue());
        //???????????????
        systemSqlConfig.setVoidedByName(systemUser.getUserName());
        //????????????
        systemSqlConfig.setVoidedByDtm(LocalDateTime.now());
        this.saveOrUpdate(systemSqlConfig);
    }

    @Override
    public List<TableColumnVO> getTableColumn(String sqlCode) {

//        //JSON??????
//        //list -> json
//        String json = JSONObject.toJSONString(tableColumnList);
//        //json -> list
//        List<TableColumnVO> lsit = JSON.parseObject(json, new TypeReference<List<TableColumnVO>>() {});

        SystemSqlConfigVO systemSqlConfigVO = systemSqlConfigMapper.getSystemSqlConfigBySqlCode(sqlCode);
        if(ObjectUtil.isEmpty(systemSqlConfigVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL?????????????????????????????????");
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
        String paraIdentifier = "@";//?????????????????????
        //??????????????????
        SysUserVO systemUser = sysUserService.getSystemUserByName(CurrentUserUtil.getUsername());
        if(ObjectUtil.isEmpty(systemUser)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "??????????????????????????????!");
        }
        String sqlCode = form.getSqlCode();//SQL??????
        Map<String, Object> condPara = form.getCondPara();//????????????{k-v},?????????
        if(ObjectUtil.isEmpty(condPara)){
            condPara = new HashMap<>();
        }
        String sqlWhereCondition = form.getSqlWhereCondition();//where??????(and ...)
        SystemSqlConfigVO systemSqlConfigVO = systemSqlConfigMapper.getSystemSqlConfigBySqlCode(sqlCode);
        if(ObjectUtil.isEmpty(systemSqlConfigVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL??????????????????????????????SQL??????");
        }
        //1.???SQL??????
        String sqlStr = systemSqlConfigVO.getSqlStr();//SQL??????
        String sqlParams = systemSqlConfigVO.getSqlParams();//WHERE??????
        String sqlOrder = systemSqlConfigVO.getSqlOrder();//ORDER??????

        if(StrUtil.isEmpty(sqlStr)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "SQL?????????????????????SQL?????????");
        }
        //2.WHERE????????????
        String where = " ";//WHERE??????
        if(StrUtil.isNotEmpty(sqlParams)){
            String[] split = sqlParams.split("[|]");
            StringBuffer stringBuffer = new StringBuffer();
            for (int k=0; k<split.length; k++){
                String s = split[k];
                if(k== 0){
                    stringBuffer.append(s);//?????????where????????????????????????????????????
                }else{
                    //condPara ????????????
                    for (Map.Entry<String, Object> m : condPara.entrySet()) {
                        //System.out.println("key:" + m.getKey() + " value:" + m.getValue());
                        String key = m.getKey();//???
                        Object value = m.getValue();//???

                        String matchesParam  = paraIdentifier+key;//?????????@sqlCode

                        //??????`?????????s`???????????????`?????????matchesParam`
                        if(s.contains(matchesParam)){
                            //??????
                            String replace = s.replace(matchesParam, value.toString());
                            stringBuffer.append(replace);
                            break;
                        }
                    }
                }
            }
            where = stringBuffer.toString();
        }


//        String sqlDataStr = systemSqlConfigVO.getSqlDataStr();//??????????????????
//        if(StrUtil.isNotEmpty(sqlDataStr) && !systemUser.getUserName().equals("admin")){
//            List<Integer> roleData = systemRoleActionDataService.getRoleData(form.getActionCode());
//            if(CollectionUtil.isNotEmpty(roleData)){
//                StringBuffer stringBuffer = new StringBuffer();
//                for (Integer roleDatum : roleData) {
//                    stringBuffer.append(roleDatum).append(",");
//                }
//                where = where + " " + sqlDataStr.replaceAll("@userId", stringBuffer.substring(0,stringBuffer.length()-1));
//            }
//
//        }
        if(StrUtil.isNotEmpty(sqlWhereCondition)){
            //where??????(and ...)
            where = where + " " + sqlWhereCondition;
        }

        //3.ORDER BY???????????????
        String order = "";
        if(StrUtil.isNotEmpty(sqlOrder)){
            order = order + " " + sqlOrder;
        }

        Map<String, Object> paraMap = new HashMap<>();
        //SQL??????
        paraMap.put("sqlStr", sqlStr);
        //WHERE??????
        paraMap.put("where", where);
        //ORDER BY??????
        paraMap.put("order", order);


        List<Long> custIdList = new ArrayList<>();
        if (systemSqlConfigVO.getSqlCode().equals(SqlCodeConstant.CRM_CUST_SQL_CODE)){
            AuthUserDetail authUserDetail = CurrentUserUtil.getUserDetail();
            BaseResult<SysTenantRole> tenantRoleBaseResult = sysTenantRoleService.selectByTenatCode(CurrentUserUtil.getUserTenantCode());
            boolean isShow = true;
            if (tenantRoleBaseResult.isSuccess()){
                SysTenantRole sysTenantRole = tenantRoleBaseResult.getResult();
                if (!sysTenantRole.getIsShowCrmPublic()){
                    isShow = false;
                }
            }else {
                isShow = false;
            }
            if (!isShow){
                where += " AND c.is_public = 0 ";
            }
            if (authUserDetail.getIsDepartCharge()){
                List<SysDepart> departList = sysDepartService.slectChildrenById(authUserDetail.getDepartId());
                List<Long> departIdList = departList.stream().map(x->x.getId()).collect(Collectors.toList());
                SysUser sysUser = new SysUser();
                sysUser.setDepartIdList(departIdList);
                List<SysUserVO> userList = sysUserService.selectList(sysUser);
                List<Long> userIdList = userList.stream().map(x->x.getId()).collect(Collectors.toList());
                custIdList = crmClient.selectCustIdListByUserIds(userIdList).getResult();
            }
            String finalSqlStr = "";
            finalSqlStr = "  SELECT DISTINCT alls.cids allId,alls.* FROM ("+sqlStr+" " +where +"";
//                    " AND ccm.manage_user_id = "+authUserDetail.getId();
            if (CollUtil.isNotEmpty(custIdList)){
                finalSqlStr = finalSqlStr+"  UNION ALL "+sqlStr +" " + where + " AND c.id IN ("+ StringUtils.join(custIdList,StrUtil.C_COMMA)+")";
            }
            finalSqlStr+=" ) alls ";
            System.out.println(sqlStr);
            //SQL??????
            paraMap.put("sqlStr", finalSqlStr);
            //WHERE??????
            paraMap.put("where", "");
        }

        //??????????????????
        Page<Map<String, Object>> page = new Page(form.getPageNum(),form.getPageSize());
        //??????????????????        ??????????????????????????????????????????sql????????????
        //page.addOrder(OrderItem.desc("t.id"));
        //????????????????????????
        IPage<Map<String, Object>> pageInfo = systemSqlConfigMapper.findCommonByPage(page, paraMap);

        Map<String, Object> extendedData = new HashMap<>();
        extendedData.put("version",systemSqlConfigVO.getVersion());
        List<Map<String, Object>> records = pageInfo.getRecords();
        if(CollUtil.isEmpty(records)){
            //?????????????????????
            extendedData.put("pageMsg", systemSqlConfigVO.getMsgStr());
        }else{
            //?????????????????????
            extendedData.put("pageMsg", "??????????????????");
        }
        //??????????????? countMap
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
            //??????sql
            String countSql = stringBuffer.toString();
            if(StrUtil.isNotEmpty(countSql)){
                //??????sql
                paraMap.put("countSql", countSql);
                //????????????Mapc
                Map<String, Object> countMap = systemSqlConfigMapper.findCommonCount(paraMap);
                extendedData.put("countMap", countMap);
            }
        }
        //???????????????????????????????????????????????????????????????extendedData?????????????????????
        CommonPageResult<Map<String, Object>> pageVO = new CommonPageResult(pageInfo, extendedData);
        return pageVO;
    }


}
