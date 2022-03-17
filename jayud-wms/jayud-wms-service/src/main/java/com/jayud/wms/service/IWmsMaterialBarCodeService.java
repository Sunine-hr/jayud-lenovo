package com.jayud.wms.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.wms.model.po.WmsMaterialBarCode;
import com.jayud.wms.model.vo.WmsMaterialBasicInfoVO;
import com.jayud.common.BaseResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 物料-条码 服务类
 *
 * @author jyd
 * @since 2021-12-16
 */
public interface IWmsMaterialBarCodeService extends IService<WmsMaterialBarCode> {

        /**
         *  分页查询
         * @param wmsMaterialBarCode
         * @param req
         * @return
         */
        IPage<WmsMaterialBarCode> selectPage(WmsMaterialBarCode wmsMaterialBarCode,
                                    HttpServletRequest req);

        /**
         *  查询列表
         * @param wmsMaterialBarCode
         * @return
         */
        List<WmsMaterialBarCode> selectList(WmsMaterialBarCode wmsMaterialBarCode);

        /**
         * @description 根据物料id查询条形编码
         * @author  ciro
         * @date   2021/12/16 16:12
         * @param: wmsMaterialBasicInfoVO
         * @return: com.jayud.model.vo.WmsMaterialBasicInfoVO
         **/
        WmsMaterialBasicInfoVO selectByMaterialId(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

        /**
         * @description 新增
         * @author  ciro
         * @date   2021/12/17 9:10
         * @param: wmsMaterialBasicInfoVO
         * @return: com.jyd.component.commons.result.Result
         **/
        BaseResult add(WmsMaterialBasicInfoVO wmsMaterialBasicInfoVO);

        /**
         * @description 根据物料id删除
         * @author  ciro
         * @date   2021/12/17 10:03
         * @param: materialId
         * @return: void
         **/
        void delByMaterialId(long materialId);

}
