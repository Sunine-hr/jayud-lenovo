package com.jayud.oms.model.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商业伙伴开发评估表
 * </p>
 *
 * @author LDR
 * @since 2021-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "BusinessDevEvaluation对象", description = "商业伙伴开发评估表")
public class BusinessDevEvaluation extends Model<BusinessDevEvaluation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "商业伙伴全称(客户code)")
    private String customerCode;

    @ApiModelProperty(value = "商业伙伴全称(客户名称)")
    private String customerName;

    @ApiModelProperty(value = "法人代表")
    private String legal;

    @ApiModelProperty(value = "成立年份")
    private LocalDateTime setYear;

    @ApiModelProperty(value = "社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "经营范围")
    private String business;

    @ApiModelProperty(value = "注册资金")
    private BigDecimal registeredCapital;

    @ApiModelProperty(value = "通过何种认证")
    private String authentication;

    @ApiModelProperty(value = "注册地址")
    private String registeredAddress;

    @ApiModelProperty(value = "企业类型")
    private Integer enterpriseType;

    @ApiModelProperty(value = "通讯地址")
    private String correspondenceAddress;

    @ApiModelProperty(value = "商业伙伴来源")
    private String source;

    @ApiModelProperty(value = "年报关量")
    private Integer number;

    @ApiModelProperty(value = "海关信用等级")
    private Integer level;

    @ApiModelProperty(value = "海关编码")
    private String customsCode;

    @ApiModelProperty(value = "进出口口岸")
    private String port;

    @ApiModelProperty(value = "关务负责人")
    private String principal;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "联系邮箱")
    private String email;

    @ApiModelProperty(value = "我司洽谈人")
    private String discussPeople;

    @ApiModelProperty(value = "洽谈日期")
    private LocalDateTime discussDate;

    @ApiModelProperty(value = "信息整理人")
    private String informationOrganizer;

    @ApiModelProperty(value = "国家企业信用(0:异常,1:无异常)")
    private Integer stateCredit;

    @ApiModelProperty(value = "海关征信(0:异常,1:无异常)")
    private Integer customsCredit;

    @ApiModelProperty(value = "承诺付款账期(单位:天)")
    private Integer payment;

    @ApiModelProperty(value = "是否为上市公司")
    private String isListed;

    @ApiModelProperty(value = "法人是否被列为失信被执行人或限制消费人员")
    private String faith;

    @ApiModelProperty(value = "评估方式")
    private String way;

    @ApiModelProperty(value = "大门和传达室是否配各人员驻守")
    private String isGarrison;

    @ApiModelProperty(value = "对离职人员是否有要求工作交接")
    private String isTransfer;

    @ApiModelProperty(value = "企业经营场所配备照明装置是否充足")
    private String isLighting;

    @ApiModelProperty(value = "是否对商业伙伴进行全面评估并定期予以检查")
    private String isTerms;

    @ApiModelProperty(value = "是否装有监控或警报系统")
    private String isWarning;

    @ApiModelProperty(value = "对于装运和接收的货物,是否核对货物与单证信息的致性")
    private String isCheckgoods;

    @ApiModelProperty(value = "内外窗户、大门和围栏是否设有锁闭装置?")
    private String isClosetool;

    @ApiModelProperty(value = "是否有对出入的运输工具和集装箱进行检查的制度与程序?")
    private String isCheckbox;

    @ApiModelProperty(value = "公司是否设置敏感/受控区(货物作业区域和存放区城等),并对于人员进入进行曾理")
    private String isSensitive;

    @ApiModelProperty(value = "是否有封条使用管理制度")
    private String isSeal;

    @ApiModelProperty(value = "是否具备员工身份识别系统?是否检查访客带有照片的身份证件并登记、发放访客证")
    private String isIdentify;

    @ApiModelProperty(value = "在货物被接收或发送前是否能提供运输车辆及司机息?")
    private String isCheckdriver;

    @ApiModelProperty(value = "是否有制定程序确保盘问及阻止非授权或身份不明的人员进入经营场所")
    private String isInspection;

    @ApiModelProperty(value = "是否具备对灾害、紧急安全事故等异常情况的应急机制?是否定期对员工进行安全意识培训")
    private String isEmergency;

    @ApiModelProperty(value = "是否有员工聊前核的制度?是否进行聘前背景调查?是否对员工进行供应链安全意识的培训")
    private String isCheckemployees;

    @ApiModelProperty(value = "贸易安全状况评估统计")
    private String tradeSecurity;

    @ApiModelProperty(value = "评估人")
    private String createUser;

    @ApiModelProperty(value = "评估时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expiresTime;

    @ApiModelProperty(value = "状态(0:待经理审核,1:待总经理审核,2:审核通过,3.审核拒绝)")
    private Integer status;

    @ApiModelProperty(value = "类型(0:客户,1:供应商)")
    private Integer type;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "审批意见")
    private String auditOpinion;

    @ApiModelProperty(value = "评估意见")
    private String evaluationOpinion;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
