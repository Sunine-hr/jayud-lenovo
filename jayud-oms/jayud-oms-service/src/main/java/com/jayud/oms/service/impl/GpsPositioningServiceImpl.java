package com.jayud.oms.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.GPSUtil;
import com.jayud.oms.feign.TmsClient;
import com.jayud.oms.model.bo.QueryGPSRecord;
import com.jayud.oms.model.po.GpsPositioning;
import com.jayud.oms.mapper.GpsPositioningMapper;
import com.jayud.oms.model.po.LogisticsTrack;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.po.VehicleInfo;
import com.jayud.oms.model.vo.GpsPositioningVO;
import com.jayud.oms.model.vo.TrackPlaybackVO;
import com.jayud.oms.model.vo.gps.GPSOrderInfoVO;
import com.jayud.oms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-08-30
 */
@Service
public class GpsPositioningServiceImpl extends ServiceImpl<GpsPositioningMapper, GpsPositioning> implements IGpsPositioningService {

    @Autowired
    private ISupplierInfoService supplierInfoService;
    @Autowired
    private GPSPositioningApiService gpsPositioningApiService;
    @Autowired
    private IVehicleInfoService vehicleInfoService;
    @Autowired
    private TmsClient tmsClient;
    @Autowired
    private ILogisticsTrackService logisticsTrackService;

    @Override
    public List<GpsPositioning> getByPlateNumbers(Set<String> licensePlateSet, Integer status) {
        QueryWrapper<GpsPositioning> condition = new QueryWrapper<>();
        condition.lambda().in(GpsPositioning::getPlateNumber, licensePlateSet);
        if (status != null) {
            condition.lambda().eq(GpsPositioning::getStatus, status);
        }
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<GpsPositioning> getByOrderNo(List<String> orderNos, Integer status) {
        QueryWrapper<GpsPositioning> condition = new QueryWrapper<>();
        condition.lambda().in(GpsPositioning::getOrderNo, orderNos);
        if (status != null) {
            condition.lambda().eq(GpsPositioning::getStatus, status);
        }
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<GpsPositioning> getGroupByOrderNo(List<String> orderNos, Integer status) {
        QueryWrapper<GpsPositioning> condition = new QueryWrapper<>();
        condition.lambda().in(GpsPositioning::getOrderNo, orderNos);
        if (status != null) {
            condition.lambda().eq(GpsPositioning::getStatus, status);
        }
        condition.lambda().groupBy(GpsPositioning::getOrderNo);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 查询车辆历史轨迹详情
     *
     * @param form
     * @return
     */
    @Override
    public TrackPlaybackVO getVehicleHistoryTrackInfo(QueryGPSRecord form) {
        GPSOrderInfoVO gpsOrderInfoVO = new GPSOrderInfoVO();
        List<GpsPositioning> gpsPositionings = this.getVehicleHistoryTrack(form, gpsOrderInfoVO);
        TrackPlaybackVO trackPlaybackVO = new TrackPlaybackVO();
        List<List<Double>> pointPositions = new ArrayList<>();
        List<GpsPositioningVO> tmps = new ArrayList<>();
        if (CollectionUtils.isEmpty(gpsPositionings)) {
            return trackPlaybackVO;
        }
        for (GpsPositioning gpsPositioning : gpsPositionings) {
            GpsPositioningVO gpsPositioningVO = ConvertUtil.convert(gpsPositioning, GpsPositioningVO.class);
            double[] doubles = GPSUtil.gps84_To_Gcj02(Double.parseDouble(gpsPositioningVO.getLatitude()),
                    Double.parseDouble(gpsPositioningVO.getLongitude()));
            List<Double> pointPosition = new ArrayList<>();
            pointPosition.add(doubles[1]);
            pointPosition.add(doubles[0]);
            pointPositions.add(pointPosition);
            gpsPositioningVO.setGLatitude(doubles[0] + "");
            gpsPositioningVO.setGLongitude(doubles[1] + "");
            tmps.add(gpsPositioningVO);
        }
        trackPlaybackVO.setPointPositions(pointPositions);
        trackPlaybackVO.setGpsPositioningVOs(tmps);
        trackPlaybackVO.assemblyBasicData(gpsOrderInfoVO);
        return trackPlaybackVO;
    }


    /**
     * 获取车辆历史轨迹
     *
     * @return
     */
    @Override
    public List<GpsPositioning> getVehicleHistoryTrack(QueryGPSRecord form, GPSOrderInfoVO gpsOrderInfoVO) {
        GPSOrderInfoVO orderGPSInfo = this.getOrderGPSInfo(form);

        List<VehicleInfo> vehicleInfos = this.vehicleInfoService.getByCondition(new VehicleInfo().setPlateNumber(form.getLicensePlate()));
        if (CollectionUtils.isEmpty(vehicleInfos)) {
            throw new JayudBizException(400, "该车辆不存在");
        }
        //查询供应商
        SupplierInfo supplierInfo = this.supplierInfoService.getById(vehicleInfos.get(0).getSupplierId());
        String gpsReqParam = supplierInfo.getGpsReqParam();
        Map<String, Object> paraMap = JSONUtil.toBean(gpsReqParam, Map.class);
        paraMap.put("gpsAddress", supplierInfo.getGpsAddress());
        // gps厂商
        if (supplierInfo.getGpsType() == null) {
            throw new JayudBizException(400, "供应商没有配置gps厂商");
        }
        //车辆维度
        switch (form.getType()) {
            case 1: //车辆维度
                Object obj = this.gpsPositioningApiService.getHistory(form.getLicensePlate(),
                        DateUtils.str2LocalDateTime(form.getStartTime(), null),
                        DateUtils.str2LocalDateTime(form.getEndTime(), null), supplierInfo.getGpsType(), paraMap);
                return this.gpsPositioningApiService.convertDatas(obj);
            case 2: //订单维度
                obj = this.gpsPositioningApiService.getHistory(form.getLicensePlate(),
                        orderGPSInfo.getStartTime(), orderGPSInfo.getEndTime(), supplierInfo.getGpsType(), paraMap);

                gpsOrderInfoVO.setGoodsInfo(orderGPSInfo.getGoodsInfo());
                gpsOrderInfoVO.setCustomerName(orderGPSInfo.getCustomerName());
                gpsOrderInfoVO.setDriverName(orderGPSInfo.getDriverName());
                gpsOrderInfoVO.setPlateNumber(orderGPSInfo.getPlateNumber());
//                gpsOrderInfoVO = ConvertUtil.convert(orderGPSInfo, GPSOrderInfoVO.class);
                return this.gpsPositioningApiService.convertDatas(obj);

        }

        return new ArrayList<>();


//        if (paramMap == null) {
//            return ApiResult.ok();
//        }
//        //车牌
//        List<String> licensePlateList = new ArrayList<>();
//        List<String> orderNos = new ArrayList<>();
//        paramMap.forEach((k, v) -> {
//            licensePlateList.add(k);
//            v.forEach(e -> {
//                String orderNo = MapUtil.getStr(e, "orderNo");
//                orderNos.add(orderNo) ;
//            });
//        });
//        //获取供应商
//        List<VehicleDetailsVO> list = vehicleInfoService.getDetailsByPlateNum(licensePlateList);
//
//        Map<String, List<VehicleDetailsVO>> tmp = list.stream().filter(e -> e.getSupplierInfoVO().getGpsType() != null).collect(Collectors.groupingBy(e -> e.getSupplierInfoVO().getGpsType() + "~" + e.getSupplierInfoVO().getSupplierCode()));
//        if (tmp == null) {
//            log.info("供应商没有配置gps厂商");
//            return ApiResult.ok();
//        }
//
//        //根据车牌获取gps信息
////        List<GpsPositioning> oldPositioning = this.gpsPositioningService.getGroupByOrderNo(orderNos, 2);
////        Map<String, GpsPositioning> oldMap = oldPositioning.stream().collect(Collectors.toMap(e -> e.getPlateNumber() + "~" + e.getOrderNo(), e -> e));
//        tmp.forEach((k, v) -> {
//            String[] split = k.split("~");
//            SupplierInfoVO supplierInfoVO = v.get(0).getSupplierInfoVO();
//            String gpsReqParam = supplierInfoVO.getGpsReqParam();
//            Map<String, Object> paraMap = JSONUtil.toBean(gpsReqParam, Map.class);
//            paraMap.put("gpsAddress", supplierInfoVO.getGpsAddress());
//
//            for (VehicleDetailsVO vehicleDetailsVO : v) {
//                //获取车牌下订单集合
//                List<Map<String, Object>> orderInfos = paramMap.get(vehicleDetailsVO.getPlateNumber());
//                orderInfos.forEach(e -> {
//                    String orderNo = MapUtil.getStr(e, "orderNo");
//                    //过滤已经同步的订单
////                    if (oldMap.get(vehicleDetailsVO.getPlateNumber() + "~" + orderNo) == null) {
//                    if (!this.redisUtils.hasKey("GPS_" + orderNo)) {
//                        try {
//                            LocalDateTime startTime = MapUtil.get(e, "startTime", LocalDateTime.class);
//                            LocalDateTime endTime = MapUtil.get(e, "endTime", LocalDateTime.class);
//                            Object obj = this.gpsPositioningApiService.getHistory(vehicleDetailsVO.getPlateNumber(), startTime, endTime, Integer.valueOf(split[0]), paraMap);
//
//                            List<GpsPositioning> gpsPositioning = this.gpsPositioningApiService.convertDatas(obj);
//                            if (gpsPositioning != null) {
//                                List<List<String>> positionList = new ArrayList<>();
//                                gpsPositioning.forEach(g -> {
//                                    g.setOrderNo(orderNo).setCreateTime(LocalDateTime.now()).setStatus(2).setStartTime(startTime).setEndTime(endTime);
//                                    List<String> position = new ArrayList<>();
//                                    position.add(g.getLatitude());
//                                    position.add(g.getLongitude());
//                                    positionList.add(position);
//                                });
//                                this.redisUtils.set("GPS_" + orderNo, positionList);
////                            this.gpsPositioningService.saveBatch(gpsPositioning);
//                            }
//
//                        } catch (JayudBizException exception) {
//                            log.error("获取历史轨迹错误,gps厂商={},错误信息={}", GPSTypeEnum.getDesc(Integer.valueOf(split[0])), exception.getMessage(), exception);
//                        }
//                    }
//                });
//            }
//        });
    }

    private GPSOrderInfoVO getOrderGPSInfo(QueryGPSRecord form) {
        if (form.getType() != 2) {
            return new GPSOrderInfoVO();
        }
        SubOrderSignEnum subOrderEnum = SubOrderSignEnum.getEnum(form.getSubType());
        GPSOrderInfoVO gpsOrderInfoVO = new GPSOrderInfoVO();
        switch (subOrderEnum) {
            case ZGYS:
                //获取车辆信息
                Object data = this.tmsClient.getTmsInfoBySubOrderNo(form.getOrderNo()).getData();
                gpsOrderInfoVO.assemblyTmsOrder(data);
                LogisticsTrack lt = this.logisticsTrackService.getLogisticsTrackByOrderIdAndStatusAndType(gpsOrderInfoVO.getSubOrderId(),
                        OrderStatusEnum.TMS_T_15.getCode(),
                        BusinessTypeEnum.ZGYS.getCode());
                gpsOrderInfoVO.setEndTime(lt.getOperatorTime());
                form.setLicensePlate(gpsOrderInfoVO.getPlateNumber());
                break;
            default:
                throw new JayudBizException("暂时不支持该单据");
        }

        return gpsOrderInfoVO;

    }


    @Override
    public List<GpsPositioning> getByPlateNumber(String plateNumber, Integer status) {
        QueryWrapper<GpsPositioning> condition = new QueryWrapper<>();
        condition.lambda().eq(GpsPositioning::getPlateNumber, plateNumber);
        if (status != null) {
            condition.lambda().eq(GpsPositioning::getStatus, status);
        }
        condition.lambda().orderByDesc(GpsPositioning::getGpsTime);
        return this.baseMapper.selectList(condition);
    }

}
