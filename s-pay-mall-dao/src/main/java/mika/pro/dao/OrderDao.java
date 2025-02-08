package mika.pro.dao;

import mika.pro.domain.po.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ErenMikasa
 * Date 2025/2/7
 * 订单操作
 */
@Mapper
public interface OrderDao {

    /**
     * Description  插入订单
     * @param: orderDetail
     */
    void insertOrder(OrderDetail orderDetail);

    /**
     * Description  根据用户id和商品id 查询两种订单：当前订单等待支付状态 或 还未创建支付单
     * status=pay_wait、create
     * @param: orderDetail 使用其中的用户id和商品id
     */
    OrderDetail queryOrderUnPay(OrderDetail orderDetail);

    /**
     * Description  改变订单
     * @param: orderDetail
     * return void
     */
    void updateOrder(OrderDetail orderDetail);

    /**
     * Description  查询掉单
     * return java.util.List<java.lang.String>
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * Description  查询超时订单
     * return java.util.List<java.lang.String>
     */
    List<String> queryTimeOutOrder();
}
