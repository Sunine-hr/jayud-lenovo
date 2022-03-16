package com.jayud.crm.model.bo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import com.jayud.crm.model.constant.CrmDictCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * CrmCreditVisit 实体类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="基本档案_客户_客户走访记录对象", description="基本档案_客户_客户走访记录")
public class CrmCreditVisitForm extends SysBaseEntity {


    @ApiModelProperty(value = "客户ID")
    private Long custId;

    @NotBlank(message = "客户名称不能为空")
    @ApiModelProperty(value = "客户名称")
    private String custName;

    @ApiModelProperty(value = "走访人、公司")
    private String visitName;

    @ApiModelProperty(value = "客户对接人")
    private String custRelation;

//    @NotBlank(message = "走访日期不能为空")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "走访日期")
    private  LocalDateTime  visitDate;

//    @NotBlank(message = "走访结束日期不能为空")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "走访结束日期")
    private  LocalDateTime  endDate;

    @NotBlank(message = "走访地址不能为空")
    @ApiModelProperty(value = "走访地址")
    private String vistAddress;

    @ApiModelProperty(value = "走访事项")
    private String vistItem;

    @NotBlank(message = "客户需求不能为空")
    @ApiModelProperty(value = "客户需求")
    private String custReq;

    @NotBlank(message = "解决方案不能为空")
    @ApiModelProperty(value = "解决方案")
    private String custAnswer;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "拜访人员集合s")
    private List<Long> visitNameList;

    @ApiModelProperty(value = "创建时间保存")
    private List<String> creationVisitTime;

    @ApiModelProperty(value = "创建时间")
    private List<String> creationTime;


    @ApiModelProperty(value = "创建时间前")
    private String creationTimeOne;

    @ApiModelProperty(value = "创建时间后")
    private String creationTimeTwo;

    //去掉前后空格
    public void setCustName(String custName) {
        this.custName = custName.trim();
    }

    public void setCreationVisitTime(List<String> creationVisitTime) {
        this.creationVisitTime = creationVisitTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDate = LocalDateTime.parse(creationVisitTime.get(0), formatter);
        LocalDateTime parsedDate2 = LocalDateTime.parse(creationVisitTime.get(1), formatter);
        this.visitDate = parsedDate ;
        this.endDate = parsedDate2 ;
    }
    public void setCreationTime(List<String> creationTime) {
        this.creationTime = creationTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String format = sdf.format(date);
        // 只传第一个 计算设置时间到 当前时间
        if(creationTime.get(0)!=null&&creationTime.get(1)==null){
            this.creationTimeOne = creationTime.get(0);
//            if(creationTime.get(1)!=null){
//                this.creationTimeTwo = creationTime.get(1);
//            }else {
                this.creationTimeTwo = format ;
//            }
        }
        // 只传第二个 计算 到第二个时间之前的所有的
        if(creationTime.get(1)!=null&&creationTime.get(0)==null){
            this.creationTimeOne = CrmDictCode.CRM_FILE_TYPE ;
            this.creationTimeTwo = creationTime.get(1);
        }
        if(creationTime.get(0)!=null&&creationTime.get(1)!=null){
            this.creationTimeOne = creationTime.get(0);
            this.creationTimeTwo = creationTime.get(1);
        }
    }



    public static void main(String[] args) {
        String dateStr = "2021-09-03 21:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDate = LocalDateTime.parse(dateStr, formatter);
//        System.out.println(parsedDate);

        String s="名字 ";
        System.out.println("之前："+s.length());
        String trim = s.trim();
        System.out.println(trim);
        System.out.println("之后："+trim.length());
    }
}
