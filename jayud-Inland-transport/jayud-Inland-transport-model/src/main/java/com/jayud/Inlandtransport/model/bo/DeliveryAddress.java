//package com.jayud.Inlandtransport.model.bo;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.jayud.common.utils.FileView;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import org.apache.commons.lang.StringUtils;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * <p>
// * 提货/送货地址
// * </p>
// *
// * @author LDR
// * @since 2020-11-30
// */
//@Data
//public class DeliveryAddress {
//
//    private static final long serialVersionUID = 1L;
//
//    @ApiModelProperty(value = "主键")
//    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;
//
//    @ApiModelProperty(value = "联系人")
//    private String contacts;
//
//    @ApiModelProperty(value = "电话")
//    private String phone;
//
//    @ApiModelProperty(value = "详细地址")
//    private String address;
//
//    @ApiModelProperty(value = "交货日期(提货日期/送货日期)")
//    private LocalDateTime deliveryDate;
//
//    @ApiModelProperty(value = "地址类型(3:提货,4:送货)")
//    private Integer addressType;
//
//    @ApiModelProperty(value = "货品描述")
//    private String name;
//
//    @ApiModelProperty(value = "板数")
//    private Integer plateAmount;
//
//    @ApiModelProperty(value = "板数单位")
//    private String plateUnit;
//
//    @ApiModelProperty(value = "散货件数")
//    private Integer bulkCargoAmount;
//
//    @ApiModelProperty(value = "散货单位")
//    private String bulkCargoUnit;
//
//    @ApiModelProperty(value = "尺寸(长宽高)")
//    private String size;
//
//    @ApiModelProperty(value = "总重量(kg)")
//    private Double totalWeight;
//
//    @ApiModelProperty(value = "体积")
//    private Double volume;
//
//    @ApiModelProperty(value = "业务类型(业务类型(0:空运,1:纯报关,2:中港运输...)参考BusinessTypeEnum)")
//    private Integer businessType;
//
//    @ApiModelProperty(value = "附件集合")
//    private List<FileView> fileViewList = new ArrayList<>();
//
//    public boolean checkCreateAirOrder() {
//        if (StringUtils.isEmpty(this.name) || plateAmount == null
//                || StringUtils.isEmpty(this.plateUnit)
//                || this.bulkCargoAmount == null || StringUtils.isEmpty(this.bulkCargoUnit)
//                || this.size == null || this.totalWeight == null || this.volume == null) {
//            return false;
//        }
//        return true;
//    }
//
//}
