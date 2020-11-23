package com.jayud.tools.thread;

import com.jayud.tools.model.po.CargoName;
import com.jayud.tools.service.ICargoNameService;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * CargoNameThread
 */
public class CargoNameThread extends Thread {

    private ICargoNameService cargoNameService;
    private List<CargoName> list;
    private CountDownLatch countDownLatch;

    public CargoNameThread(ICargoNameService cargoNameService, List<CargoName> list, CountDownLatch countDownLatch){
        this.cargoNameService = cargoNameService;
        this.list = list;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        if (!CollectionUtils.isEmpty(list)) {
            cargoNameService.saveBatch(list);
        }
        countDownLatch.countDown();
    }
}
