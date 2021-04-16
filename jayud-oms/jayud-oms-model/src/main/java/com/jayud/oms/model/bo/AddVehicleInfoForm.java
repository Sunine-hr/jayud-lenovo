package com.jayud.oms.model.bo;

import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.po.DriverInfo;
import com.jayud.oms.model.vo.DriverInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 供应商对应车辆信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
@Data
public class AddVehicleInfoForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Long id;

    @ApiModelProperty(value = "大陆车牌")
    @NotEmpty(message = "plateNumber is required")
    private String plateNumber;

    @ApiModelProperty(value = "香港车牌")
    private String hkNumber;

    @ApiModelProperty(value = "供应商id")
    @NotNull(message = "supplierId is required")
    private Long supplierId;

    @ApiModelProperty(value = "牌头公司")
    private String ptCompany;

    @ApiModelProperty(value = "牌头传真")
    private String ptFax;

    @ApiModelProperty(value = "车辆类型(1吨车 2柜车)")
    @NotNull(message = "carType is required")
//    @Pattern(regexp = "1|2",message = "carType requires '1' or '2' only")
    private Integer carType;

    @ApiModelProperty(value = "牌头电话")
    private String ptPhone;

    @ApiModelProperty(value = "海关编码")
    private String customsCode;

    @ApiModelProperty(value = "通关卡号")
    private String cardNumber;

    @ApiModelProperty(value = "吉车重量")
    private String weight;

//    @ApiModelProperty(value = "附件,多个时用逗号分隔")
//    private String files;

    @ApiModelProperty(value = "附件地址集合")
    private List<FileView> fileViews = new ArrayList<>();

    @ApiModelProperty(value = "寮步密码")
    private String steppingCode;

    @ApiModelProperty(value = "企业代码")
    private String enterpriseCode;

    @ApiModelProperty(value = "车型尺寸")
    private String vehicleTonnage;

    @ApiModelProperty(value = "司机对象")
    @NotNull(message = "需要关联司机")
    private List<AddDriverInfoForm> driverInfos;

    @ApiModelProperty(value = "主司机id")
    @NotNull(message = "请选择主司机")
    private Long mainDriverId;

    @ApiModelProperty(value = "车辆类型(0:中港车,1:内陆车)")
    private Integer type;

    public void checkCreateOrUpdate() {
        String msg = "必填";
        switch (type) {
            case 0:
                if (this.carType == null) {
                    throw new JayudBizException("请选择车辆类型");
                }
            case 1:
                if (StringUtils.isEmpty(this.plateNumber)) {
                    throw new JayudBizException("大陆车牌" + msg);
                }
                if (this.supplierId == null) {
                    throw new JayudBizException("运输供应商" + msg);
                }
                if (StringUtils.isEmpty(this.vehicleTonnage)) {
                    throw new JayudBizException("请选择车型尺寸");
                }
                if (CollectionUtils.isEmpty(this.driverInfos)) {
                    throw new JayudBizException("请关联司机");
                }
                if (this.mainDriverId == null) {
                    throw new JayudBizException("请选择司机");
                }
                break;
            default:
                throw new JayudBizException("不存在该车辆类型");
        }
    }

}
