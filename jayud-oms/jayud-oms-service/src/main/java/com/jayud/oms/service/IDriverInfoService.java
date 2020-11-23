package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.QueryDriverInfoForm;
import com.jayud.oms.model.po.DriverInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.DriverInfoLinkVO;
import com.jayud.oms.model.vo.DriverInfoVO;

/**
 * <p>
 * 供应商对应司机信息 服务类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-04
 */
public interface IDriverInfoService extends IService<DriverInfo> {

    /**
     * 分页查询司机信息
     * @param form
     * @return
     */
    IPage<DriverInfoVO> findDriverInfoByPage(QueryDriverInfoForm form);

    /**
     * 新增编辑司机信息
     * @param driverInfo
     * @return
     */
    boolean saveOrUpdateDriverInfo(DriverInfo driverInfo);

    boolean checkUnique(DriverInfo driverInfo);

    boolean enableOrDisableDriver(Long id);

    /**
     * 司机下拉框联动车辆供应商，大陆车牌，香港车牌，司机电话
     * @param driverId
     * @return
     */
    DriverInfoLinkVO getDriverInfoLink(Long driverId);


    /**
     * 根据司机大陆手机查询用户
     * @param phone
     * @return
     */
    DriverInfo getByPhone(String phone);
}
