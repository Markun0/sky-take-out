package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    // 处理超时未支付订单, 每分钟扫描一次
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        List<Orders> orders = orderMapper.getAll();
        if(orders.size()>0){
            for(Orders order:orders){
                if(order.getStatus()==1 && order.getOrderTime().plusMinutes(15).isBefore(LocalDateTime.now())){
                    // 订单已取消
                    order.setStatus(6);
                    // 订单超时
                    orderMapper.update(order);
                }
            }
        }
    }

    // 处理未更新订单状态的订单， 每天凌晨1点扫描一次
    @Scheduled(cron = "0 0 1 * * ?")
    public void processOrder(){
        List<Orders> orders = orderMapper.getAll();
        if(orders.size()>0){
            for(Orders order:orders){
                if(order.getStatus()==1){
                    order.setStatus(5);
                    // 订单已完成
                    orderMapper.update(order);
                }
            }
        }
    }
}
