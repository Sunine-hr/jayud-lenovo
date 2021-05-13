package com.jayud.customs.schedule;

import com.jayud.common.utils.DateUtils;
import com.jayud.customs.model.enums.BGOrderStatusEnum;
import com.jayud.customs.model.po.OrderCustoms;
import com.jayud.customs.model.vo.DclarationProcessStepVO;
import com.jayud.customs.model.vo.DeclarationOPDetailVO;
import com.jayud.customs.service.ICustomsApiService;
import com.jayud.customs.service.IOrderCustomsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 定时任务
 */
@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    ICustomsApiService service;

    @Autowired
    IOrderCustomsService orderCustomsService;

    /**
     * corn表达式格式：秒 分 时 日 月 星期 年（可选）
     * 0/7 * * * * ?        代表每7秒执行一次
     * 0 0 4 1 * ?          每月1号凌晨4点触发
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void scheduledTaskV1() {
        log.info("*********   定时同步云报关报关单进程信息到OMS任务执行   **************");
        // 获取OMS填写了委托单号的报关单数据
        List<OrderCustoms> taskData = orderCustomsService.getOrderCustomsTaskData();
        Map<String, DeclarationOPDetailVO> stepMap;
        // 通过委托单号查询云报关进程信息
        for (OrderCustoms orderCustoms : taskData) {
            String entrustNo = orderCustoms.getEntrustNo();

            // 获得报关进程列表信息
            DclarationProcessStepVO processStep = service.getDeclarationProcessStep(entrustNo);
            if (!Optional.ofNullable(processStep).isPresent()) {
                log.info("未查询到委托单号为{}的进程信息", entrustNo);
                continue;
            }
            List<DeclarationOPDetailVO> processStepDtl = processStep.getDtl();
            stepMap = processStepDtl.stream().collect(Collectors.toMap(DeclarationOPDetailVO::getCname, e -> e, (k1, k2) -> k2));

            // 获得OMS报关单状态
            BGOrderStatusEnum bgOrderStatusEnum = BGOrderStatusEnum.getEnum(orderCustoms.getStatus());
            while (bgOrderStatusEnum.hasMoreElements()) {
                bgOrderStatusEnum = bgOrderStatusEnum.nextElement();
                DeclarationOPDetailVO declarationOPDetailVO = stepMap.get(bgOrderStatusEnum.getStatus());

                if (!Optional.ofNullable(declarationOPDetailVO).isPresent()) {
                    continue;
                }

                if (Objects.equals(BGOrderStatusEnum.CUSTOMS_C_10.getCode(), bgOrderStatusEnum.getCode())) {
                    orderCustoms.setYunCustomsNo(processStep.getHead().getCustom_apply_no());
                }

                LocalDateTime processTime = DateUtils.str2LocalDateTime(declarationOPDetailVO.getProcess_dt(), "");
                bgOrderStatusEnum.updateProcessStatus(orderCustoms,
                        e -> orderCustomsService.updateProcessStatus(orderCustoms, declarationOPDetailVO.getP_name(), processTime));
            }
        }
        log.info("*********   定时同步云报关报关单进程信息到OMS任务执行结束   **************");
    }
}
