package com.jayud.oceanship.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 海运节点操作流程
 */
@Data
@Slf4j
public class AddSeaBookShipForm extends Model<AddSeaBookShipForm> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "状态(0:确认,1:待确认,2:删除)")
    private Integer status;

    @ApiModelProperty(value = "海运订单编号")
    private String seaOrderNo;

    @ApiModelProperty(value = "海运订单id")
    private Long seaOrderId;

    @ApiModelProperty(value = "代理供应商id")
    private Long agentSupplierId;

    @ApiModelProperty(value = "入仓号")
    private String warehousingNo;

    @ApiModelProperty(value = "船公司")
    private String shipCompany;

    @ApiModelProperty(value = "船名字")
    private String shipName;

    @ApiModelProperty(value = "船次")
    private String shipNumber;

    @ApiModelProperty(value = "预计离港时间")
    private String etd;

    @ApiModelProperty(value = "实际离岗时间")
    private String atd;

    @ApiModelProperty(value = "预计到港时间")
    private String eta;

    @ApiModelProperty(value = "实际到港时间")
    private String ata;

    @ApiModelProperty(value = "截关时间")
    private String closingTime;

    @ApiModelProperty(value = "交仓码头")
    private String deliveryWharf;

    @ApiModelProperty(value = "提单文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "提单文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> fileViewList = new ArrayList<>();

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public boolean checkBookShipParam() {
        String title = "订船操作";
        if (StringUtils.isEmpty(this.seaOrderNo)) {
            log.warn(title + " 海运订单编号必填");
            return false;
        }
        if (this.seaOrderId == null) {
            log.warn(title + " 海运订单id必填");
            return false;
        }
        if (this.agentSupplierId == null) {
            log.warn(title + " 供应商id必填");
            return false;
        }
        if (this.closingTime == null) {
            log.warn(title + " 截关时间必填");
            return false;
        }
//        if (StringUtils.isEmpty(this.shipCompany)) {
//            log.warn(title + " 船公司必填");
//            return false;
//        }
//        if (StringUtils.isEmpty(this.shipName)) {
//            log.warn(title + " 船名必填");
//            return false;
//        }
//        if (StringUtils.isEmpty(this.shipNumber)) {
//            log.warn(title + " 船次必填");
//            return false;
//        }
        if (StringUtils.isEmpty(this.deliveryWharf)) {
            log.warn(title + " 交仓码头必填");
            return false;
        }
        return true;
    }

    public boolean checkBookShipOptParam() {
        String title = "确认装船操作";
        if (StringUtils.isEmpty(this.seaOrderNo)) {
            log.warn(title + " 海运订单编号必填");
            return false;
        }
        if (this.seaOrderId == null) {
            log.warn(title + " 海运订单id必填");
            return false;
        }
        if (this.agentSupplierId == null) {
            log.warn(title + " 供应商id必填");
            return false;
        }
        if (StringUtils.isEmpty(this.shipCompany)) {
            log.warn(title + " 船公司必填");
            return false;
        }
        if (StringUtils.isEmpty(this.shipName)) {
            log.warn(title + " 船名必填");
            return false;
        }
        if (StringUtils.isEmpty(this.shipNumber)) {
            log.warn(title + " 船次必填");
            return false;
        }
        if (StringUtils.isEmpty(this.deliveryWharf)) {
            log.warn(title + " 交仓码头必填");
            return false;
        }
        return true;
    }

//    public boolean checkConfirmLadingBillOptParam() {
//        String title = "放单确认操作";
//        if (StringUtils.isEmpty(this.mainNo)) {
//            log.warn(title + " 主单号必填");
//            return false;
//        }
//        if (this.billLadingWeight == null) {
//            log.warn(title + " 提单重量必填");
//            return false;
//        }
//        return true;
//    }

    public boolean checkConfirmATAOptParam() {
        String title = "确认到港操作";
//        if (this.id == null) {
//            log.warn(title + " 订舱id必填");
//            return false;
//        }
        if (StringUtils.isEmpty(this.ata)) {
            log.warn(title + " 实际到港时间必填");
            return false;
        }
        return true;
    }

    //获取文件名和文件地址
    public void getFile(){
        this.fileName = com.jayud.common.utils.StringUtils.getFileNameStr(fileViewList);
        this.filePath = com.jayud.common.utils.StringUtils.getFileStr(fileViewList);
    }

    public void toUp(){
        if(this.shipCompany != null){
            this.shipCompany = this.shipCompany.toUpperCase();
        }
        if(this.shipName != null){
            this.shipName = this.shipName.toUpperCase();
        }
        if(this.shipNumber != null){
            this.shipNumber = this.shipNumber.toUpperCase();
        }
    }
}
