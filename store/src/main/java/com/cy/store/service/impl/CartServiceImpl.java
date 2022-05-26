package com.cy.store.service.impl;

import com.cy.store.entity.Cart;
import com.cy.store.entity.Product;
import com.cy.store.mapper.CartMapper;
import com.cy.store.mapper.ProductMapper;
import com.cy.store.service.ICartService;
import com.cy.store.service.ex.AccessDeniedException;
import com.cy.store.service.ex.CartNotFoundException;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cy.store.vo.CartVO;

import java.util.Iterator;
import java.util.List;
import java.util.Date;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public void addToCart(Integer uid, Integer pid, Integer amount, String username) {
        Cart result = cartMapper.findByUidAndPid(uid,pid);
        Date date = new Date();
        if (result == null) {

            Cart cart = new Cart();

            cart.setUid(uid);
            cart.setPid(pid);
            cart.setNum(amount);

            Product product = productMapper.findById(pid);

            cart.setPrice(product.getPrice());

            cart.setCreatedUser(username);
            cart.setCreatedTime(date);
            cart.setModifiedUser(username);
            cart.setModifiedTime(date);

            Integer rows = cartMapper.insert(cart);
            if (rows != 1) {
                throw new InsertException("unknown error occurred while inserting commodity data. Contact your system administrator");
            }
        } else {

            Integer cid = result.getCid();

            Integer num = result.getNum() + amount;

            Integer rows = cartMapper.updateNumByCid(cid, num, username, date);
            if (rows != 1) {
                throw new UpdateException("An unknown error occurred while updating data. Contact your system administrator");
            }
        }
    }
    @Override
    public List<CartVO> getVOByUid(Integer uid) {
        return cartMapper.findVOByUid(uid);
    }

    @Override
    public Integer addNum(Integer cid, Integer uid, String username) {
        Cart result = cartMapper.findByCid(cid);

        if (result == null) {

            throw new CartNotFoundException("The shopping cart data you tried to access does not exist");
        }

        if (!result.getUid().equals(uid)) {
            // 是：抛出AccessDeniedException
            throw new AccessDeniedException("Unauthorized access");
        }

        Integer num = result.getNum() + 1;

        Integer rows = cartMapper.updateNumByCid(cid, num, username, new Date());

        if (rows != 1) {
            throw new InsertException("Unknown error occurs. Contact your system administrator");
        }

        return num;
    }

    @Override
    public List<CartVO> getVOByCids(Integer uid, Integer[] cids) {
        List<CartVO> list = cartMapper.findVOByCid(cids);
        Iterator<CartVO> it = list.iterator();
        while (it.hasNext()) {
            CartVO cartVO = it.next();
            if (!cartVO.getUid().equals(uid)) {
                list.remove(cartVO);
            }
        }
        return list;
    }
}
