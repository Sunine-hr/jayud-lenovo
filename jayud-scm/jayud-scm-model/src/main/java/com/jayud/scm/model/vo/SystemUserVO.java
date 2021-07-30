package com.jayud.scm.model.vo;

import com.jayud.scm.model.po.LegalEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bocong.zheng
 */
@Data
public class SystemUserVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "审核状态")
    private String auditStatusDesc;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "联系方式")
    private String phone;

    @ApiModelProperty(value = "部门ID")
    private Long departmentId;

    @ApiModelProperty(value = "岗位ID")
    private Long workId;

    @ApiModelProperty(value = "岗位描述")
    private String workName;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "所属公司ID")
    private Long companyId;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "所属上级ID")
    private Long superiorId;

    @ApiModelProperty(value = "所属上级名称")
    private String superiorName;

    @ApiModelProperty(value = "备注信息")
    private String note;

    @ApiModelProperty(value = "最后登录时间")
    private String loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->Off；1->On")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private String createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "用户token")
    private String token;

    @ApiModelProperty(value = "登录错误")
    private Boolean isError;

    @ApiModelProperty(value = "法人主体id集合")
    private List<Long> legalEntityIds = new ArrayList<>();

    @ApiModelProperty(value = "法人主体名称拼接字符串")
    private String legalEntityIdStr;

    @ApiModelProperty(value = "法人主体集合")
    private List<LegalEntity> legalEntities;

    @ApiModelProperty(value = "是否强制修改密码")
    private Boolean isForcedPasswordChange;

    @ApiModelProperty(value = "修改密码时间")
    private LocalDateTime updatePassWordDate;

//    public List<Long> getLegalEntityIds() {
//        if(!StringUtil.isNullOrEmpty(this.legalEntityIdStr)){
//            String[] strs = this.legalEntityIdStr.split(",");
//            for (String str : strs) {
//                legalEntityIds.add(Long.valueOf(str));
//            }
//        }
//        return legalEntityIds;
//    }

    public String getLegalEntityIdStr() {
        legalEntityIdStr = "";
        if(this.legalEntities!=null){
            for (int i = 0; i < legalEntities.size(); i++) {
                if(i==legalEntities.size()-1){
                    legalEntityIdStr = legalEntityIdStr+legalEntities.get(i).getLegalName();
                }else{
                    legalEntityIdStr = legalEntityIdStr+legalEntities.get(i).getLegalName()+",";
                }
            }
        }
        return legalEntityIdStr;
    }

    public List<Long> getLegalEntityIds() {
        if (this.legalEntities != null) {
            for (LegalEntity legalEntity : legalEntities) {
                legalEntityIds.add(legalEntity.getId());
            }
        }
        return legalEntityIds;
    }
}
