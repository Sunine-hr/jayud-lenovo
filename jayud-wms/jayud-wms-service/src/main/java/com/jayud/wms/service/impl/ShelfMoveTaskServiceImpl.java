package com.jayud.wms.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.BaseResult;
import com.jayud.common.dto.HikAGVFrom;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.wms.mapper.ShelfMoveTaskMapper;
import com.jayud.wms.mapper.WorkbenchMapper;
import com.jayud.wms.model.bo.CreateShelfMoveTaskForm;
import com.jayud.wms.model.constant.CodeConStants;
import com.jayud.wms.model.enums.ShelfMoveTaskOrderSourceEnum;
import com.jayud.wms.model.enums.ShelfMoveTaskOrderStatusEnum;
import com.jayud.wms.model.po.ShelfMoveTask;
import com.jayud.wms.model.po.WarehouseShelf;
import com.jayud.wms.model.po.Workbench;
import com.jayud.wms.service.IShelfMoveTaskService;
import com.jayud.wms.service.IWarehouseShelfService;
import com.jayud.wms.utils.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 货架移动任务 服务实现类
 *
 * @author jyd
 * @since 2022-03-05
 */
@Service
public class ShelfMoveTaskServiceImpl extends ServiceImpl<ShelfMoveTaskMapper, ShelfMoveTask> implements IShelfMoveTaskService {


    @Autowired
    private ShelfMoveTaskMapper shelfMoveTaskMapper;
    @Autowired
    private CodeUtils codeUtils;
    @Autowired
    private WorkbenchMapper workbenchMapper;
    @Autowired
    private IWarehouseShelfService warehouseShelfService;

    @Override
    public IPage<ShelfMoveTask> selectPage(ShelfMoveTask shelfMoveTask,
                                        Integer pageNo,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<ShelfMoveTask> page=new Page<ShelfMoveTask>(pageNo, pageSize);
        IPage<ShelfMoveTask> pageList= shelfMoveTaskMapper.pageList(page, shelfMoveTask);
        return pageList;
    }

    @Override
    public List<ShelfMoveTask> selectList(ShelfMoveTask shelfMoveTask){
        return shelfMoveTaskMapper.list(shelfMoveTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShelfMoveTask saveOrUpdateShelfMoveTask(ShelfMoveTask shelfMoveTask) {
        Long id = shelfMoveTask.getId();
        String movementTypeCode = shelfMoveTask.getMovementTypeCode();
        if(StrUtil.isEmpty(movementTypeCode)){
            throw new IllegalArgumentException("移库类型代码不能为空");
        }
        String movementTypeName = shelfMoveTask.getMovementTypeName();
        if(StrUtil.isEmpty(movementTypeName)){
            throw new IllegalArgumentException("移库类型名称不能为空");
        }

        if(ObjectUtil.isEmpty(id)){
            //新增 --> add 创建人、创建时间
            shelfMoveTask.setCreateBy(CurrentUserUtil.getUsername());
            shelfMoveTask.setCreateTime(new Date());

//            String mainCode = codeUtils.getCodeByRule(CodeConStants.SHELF_MOVE_TASK_MAIN_CODE);//货架移动任务号
//            String mxCode = codeUtils.getCodeByRule(CodeConStants.SHELF_MOVE_TASK_MX_CODE);//货架移动任务明细号
//
//            shelfMoveTask.setMainCode(mainCode);
//            shelfMoveTask.setMxCode(mxCode);


        }else{
            //修改 --> update 更新人、更新时间
            shelfMoveTask.setUpdateBy(CurrentUserUtil.getUsername());
            shelfMoveTask.setUpdateTime(new Date());
        }
        this.saveOrUpdate(shelfMoveTask);
        return shelfMoveTask;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delShelfMoveTask(int id) {
        ShelfMoveTask shelfMoveTask = this.baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(shelfMoveTask)){
            throw new IllegalArgumentException("货架移动任务不存在，无法删除");
        }
        //逻辑删除 -->update 修改人、修改时间、是否删除
        shelfMoveTask.setUpdateBy(CurrentUserUtil.getUsername());
        shelfMoveTask.setUpdateTime(new Date());
        shelfMoveTask.setIsDeleted(true);
        this.saveOrUpdate(shelfMoveTask);
    }

    @Override
    public List<LinkedHashMap<String, Object>> queryShelfMoveTaskForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryShelfMoveTaskForExcel(paramMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createShelfMoveTask(CreateShelfMoveTaskForm form) {
        //验证，货架移动任务
        verificationShelfMove(form);

        String movementTypeCode = form.getMovementTypeCode();//移库类型代码
        String movementTypeName = form.getMovementTypeName();//移库类型名称
        Long workbenchId = form.getWorkbenchId();//工作台id
        String workbenchCode = form.getWorkbenchCode();//工作台编号
        List<WarehouseShelf> warehouseShelfList = form.getWarehouseShelfList();//货架信息List

        List<ShelfMoveTask> tasks = new ArrayList<>();

        String mainCode = codeUtils.getCodeByRule(CodeConStants.SHELF_MOVE_TASK_MAIN_CODE);//货架移动任务号
        warehouseShelfList.forEach(warehouseShelf -> {
            ShelfMoveTask task = new ShelfMoveTask();
            task.setMovementTypeCode(movementTypeCode);
            task.setMovementTypeName(movementTypeName);

            String mxCode = codeUtils.getCodeByRule(CodeConStants.SHELF_MOVE_TASK_MX_CODE);//货架移动任务明细号
            task.setMainCode(mainCode);
            task.setMxCode(mxCode);

            //工作台
            task.setWorkbenchId(workbenchId);
            task.setWorkbenchCode(workbenchCode);
            //货架(仓库+库区+货架)
            Long shelfId = warehouseShelf.getId();
            String shelfCode = warehouseShelf.getCode();
            task.setWarehouseId(warehouseShelf.getWarehouseId());
            task.setWarehouseCode(warehouseShelf.getWarehouseCode());
            task.setWarehouseName(warehouseShelf.getWarehouseName());
            task.setWarehouseAreaId(warehouseShelf.getWarehouseAreaId());
            task.setWarehouseAreaCode(warehouseShelf.getWarehouseAreaCode());
            task.setWarehouseAreaName(warehouseShelf.getWarehouseAreaName());
            task.setShelfId(shelfId);
            task.setShelfCode(shelfCode);

            //订单来源(1系统创建 2人工创建)
            task.setOrderSource(ShelfMoveTaskOrderSourceEnum.TWO.getCode());
            //订单状态(1待移动 2移动中 3已完成)
            task.setOrderStatus(ShelfMoveTaskOrderStatusEnum.ONE.getCode());

            //创建人、创建时间
            task.setCreateBy(CurrentUserUtil.getUsername());
            task.setCreateTime(new Date());

            tasks.add(task);
        });
        if(CollUtil.isNotEmpty(tasks)){
            this.saveOrUpdateBatch(tasks);
        }
    }

    @Override
    public BaseResult updateShelfMoveTaskByMxCode(HikAGVFrom from) {
        String mxCode = from.getMxCode();
        if(StrUtil.isEmpty(mxCode)){
            return BaseResult.error("货架移动任务明细号，不能为空");
        }
        QueryWrapper<ShelfMoveTask> shelfMoveTaskQueryWrapper = new QueryWrapper<>();
        shelfMoveTaskQueryWrapper.lambda().eq(ShelfMoveTask::getIsDeleted, 0);
        shelfMoveTaskQueryWrapper.lambda().eq(ShelfMoveTask::getMxCode, mxCode);
        shelfMoveTaskQueryWrapper.lambda().groupBy(ShelfMoveTask::getMxCode);
        ShelfMoveTask task = shelfMoveTaskMapper.selectOne(shelfMoveTaskQueryWrapper);
        if(ObjectUtil.isEmpty(task)){
            return BaseResult.error("货架移动任务不存在");
        }
        Integer orderStatus = from.getOrderStatus();
        List<Integer> orderStatusList = Arrays.asList(1,2,3);//订单状态(1待移动 2移动中 3已完成)
        boolean exit = orderStatusList.stream().filter(m -> m.equals(orderStatus)).findAny().isPresent();
        if(!exit){
            return BaseResult.error("订单状态有误");
        }

        task.setOrderStatus(orderStatus);

        this.saveOrUpdate(task);

        return BaseResult.ok("操作成功");
    }

    /**
     * 验证，货架移动任务
     * @param form
     */
    private void verificationShelfMove(CreateShelfMoveTaskForm form) {
        String movementTypeCode = form.getMovementTypeCode();
        if(StrUtil.isEmpty(movementTypeCode)){
            throw new IllegalArgumentException("移库类型代码不能为空");
        }
        String movementTypeName = form.getMovementTypeName();
        if(StrUtil.isEmpty(movementTypeName)){
            throw new IllegalArgumentException("移库类型名称不能为空");
        }
        Long workbenchId = form.getWorkbenchId();
        if(ObjectUtil.isEmpty(workbenchId)){
            throw new IllegalArgumentException("工作台id不能为空");
        }
        String workbenchCode = form.getWorkbenchCode();
        if(StrUtil.isEmpty(workbenchCode)){
            throw new IllegalArgumentException("工作台编号不能为空");
        }
        List<WarehouseShelf> warehouseShelfList = form.getWarehouseShelfList();
        if(CollUtil.isEmpty(warehouseShelfList)){
            throw new IllegalArgumentException("货架不能为空");
        }
        Workbench workbench = workbenchMapper.selectById(workbenchId);
        Long workbench_warehouseId = workbench.getWarehouseId();

        List<Long> warehouseIdList = warehouseShelfList.stream().map(warehouseShelf -> warehouseShelf.getWarehouseId()).collect(Collectors.toList());
        Set<Long> longSet = new HashSet<>(warehouseIdList);
        if(longSet.size() > 1){
            throw new IllegalArgumentException("所选的货架，必须属于同一个仓库");
        }
        List<Long> list = new ArrayList(longSet);
        Long shelf_warehouseId = list.get(0);
        if(!workbench_warehouseId.equals(shelf_warehouseId)){
            throw new IllegalArgumentException("所选的工作台和货架，必须属于同一个仓库");
        }

        //检查选中货架的 货架任务移动状态
        //`shelf_move_task`.`order_status` int(11) NOT NULL COMMENT '订单状态(1待移动 2移动中 3已完成)',
        for (int i=0; i<warehouseShelfList.size(); i++){
            WarehouseShelf warehouseShelf = warehouseShelfList.get(i);
            Long warehouseId = warehouseShelf.getWarehouseId();
            Long warehouseAreaId = warehouseShelf.getWarehouseAreaId();
            Long shelfId = warehouseShelf.getId();

            QueryWrapper<ShelfMoveTask> taskQueryWrapper = new QueryWrapper<>();
            //taskQueryWrapper.lambda().eq(ShelfMoveTask::getWorkbenchId, workbenchId);
            taskQueryWrapper.lambda().eq(ShelfMoveTask::getWarehouseId, warehouseId);
            taskQueryWrapper.lambda().eq(ShelfMoveTask::getWarehouseAreaId, warehouseAreaId);
            taskQueryWrapper.lambda().eq(ShelfMoveTask::getShelfId, shelfId);
            taskQueryWrapper.lambda().eq(ShelfMoveTask::getIsDeleted, 0);
            List<ShelfMoveTask> tasks = shelfMoveTaskMapper.selectList(taskQueryWrapper);
            if(CollUtil.isNotEmpty(tasks)){
                // 订单状态(1待移动 2移动中 3已完成)
                // 只有订单状态为 已完成的货架，才能重新创建 移动货架任务
                List<Integer> orderStatusList = tasks.stream().map(task -> task.getOrderStatus()).collect(Collectors.toList());
                Set<Integer> orderStatusSet = new HashSet<>(orderStatusList);
                if(orderStatusSet.size() > 1){
                    throw new IllegalArgumentException("所选的货架，已经有移动任务了，并且移动任务正在进行中");
                }
                List<Integer> orderStatus = new ArrayList(orderStatusSet);
                Integer shelfMoveTaskOrderStatus = orderStatus.get(0);
                //货架存在移动任务，并且移动任务状态 不等于 已完成，不能移动此货架
                if(!shelfMoveTaskOrderStatus.equals(ShelfMoveTaskOrderStatusEnum.THREE.getCode())){
                    throw new IllegalArgumentException("所选的货架存在移动任务，并且移动任务状态 不等于 已完成，不能创建货架移动任务");
                }
            }
        }
    }

}
