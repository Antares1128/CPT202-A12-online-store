package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.entity.Order;
import com.cy.store.entity.OrderItem;
import com.cy.store.mapper.OrderMapper;
import com.cy.store.service.IAddressService;
import com.cy.store.service.ICartService;
import com.cy.store.service.IOrderService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/** Business layer implementation classes for processing orders and order data */
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IAddressService addressService;
    @Autowired
    private ICartService cartService;

    @Transactional
    @Override
    public Order create(Integer aid, Integer[] cids, Integer uid, String username) {
        // Create current time object
        Date now = new Date();

        // Query the data in the selected shopping cart list according to cids
        List<CartVO> carts = cartService.getVOByCids(uid, cids);

        // Calculate the total price of these items
        long totalPrice = 0;
        for (CartVO cart : carts) {
            totalPrice += cart.getRealPrice() * cart.getNum();
        }

        // Create order data object
        Order order = new Order();
        // Complete data: uid
        order.setUid(uid);
        // Query delivery address data
        Address address = addressService.getByAid(aid, uid);
        // Complete data: 6 items related to the delivery address
        order.setRecvName(address.getName());
        order.setRecvPhone(address.getPhone());
        order.setRecvProvince(address.getProvinceName());
        order.setRecvCity(address.getCityName());
        order.setRecvArea(address.getAreaName());
        order.setRecvAddress(address.getAddress());
        // Complete data：totalPrice
        order.setTotalPrice(totalPrice);
        // Complete data：status
        order.setStatus(0);
        // Complete data：order time
        order.setOrderTime(now);
        // Complete data：log
        order.setCreatedUser(username);
        order.setCreatedTime(now);
        order.setModifiedUser(username);
        order.setModifiedTime(now);
        // Insert order data
        Integer rows1 = orderMapper.insertOrder(order);
        if (rows1 != 1) {
            throw new InsertException("An unknown error occurred while inserting order data, please contact your system administrator.");
        }

        // Traverse carts and insert order item data in a loop
        for (CartVO cart : carts) {
            // Create order item data
            OrderItem item = new OrderItem();
            // Complete data：setOid(order.getOid())
            item.setOid(order.getOid());
            // Complete data：pid, title, image, price, num
            item.setPid(cart.getPid());
            item.setTitle(cart.getTitle());
            item.setImage(cart.getImage());
            item.setPrice(cart.getRealPrice());
            item.setNum(cart.getNum());
            // Complete data：4 logs
            item.setCreatedUser(username);
            item.setCreatedTime(now);
            item.setModifiedUser(username);
            item.setModifiedTime(now);
            // Insert order item data
            Integer rows2 = orderMapper.insertOrderItem(item);
            if (rows2 != 1) {
                throw new InsertException("An unknown error occurred while inserting order item data, please contact your system administrator.");
            }
        }

        // return
        return order;
    }
}
