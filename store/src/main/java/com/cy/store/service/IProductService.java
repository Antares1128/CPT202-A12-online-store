package com.cy.store.service;

import com.cy.store.entity.Product;
import com.cy.store.service.ex.ProductNotFoundException;

import java.util.List;

public interface IProductService {
    List<Product> findHotList();

    Product findById(Integer id);
}
