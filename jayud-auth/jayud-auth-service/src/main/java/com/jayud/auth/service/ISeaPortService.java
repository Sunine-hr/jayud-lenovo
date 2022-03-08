//package com.jayud.auth.service;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.jayud.common.entity.InitComboxStrVO;
//import com.jayud.oceanship.bo.AddSeaPortForm;
//import com.jayud.oceanship.bo.QuerySeaPortForm;
//import com.jayud.oceanship.po.SeaPort;
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.jayud.oceanship.vo.SeaOrderFormVO;
//import com.jayud.oceanship.vo.SeaPortVO;
//
//import java.util.List;
//
///**
// * <p>
// * 船港口地址表 服务类
// * </p>
// *
// * @author LLJ
// * @since 2021-01-29
// */
//public interface ISeaPortService extends IService<SeaPort> {
//
//    /**
//     * 获取港口列表
//     * @return
//     */
//    List<InitComboxStrVO> initSeaPort();
//
//    String getPortName(String portDepartureCode);
//
//    IPage<SeaPortVO> findByPage(QuerySeaPortForm form);
//
//    SeaPort isCodeExistence(String code);
//
//    SeaPort isNameExistence(String name);
//
//    boolean saveOrUpdateSeaPort(AddSeaPortForm form);
//
//    boolean deleteSeaPort(Long id);
//
//    boolean saveOrUpdateBatchSeaPort(List<AddSeaPortForm> list);
//}
