package com.jayud.mall.controller;

import com.jayud.mall.service.IOceanCounterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oceancounter")
@Api(tags = "A039-admin-提单对应货柜信息接口")
@ApiSort(value = 39)
public class OceanCounterController {

    @Autowired
    IOceanCounterService oceanCounterService;

}
