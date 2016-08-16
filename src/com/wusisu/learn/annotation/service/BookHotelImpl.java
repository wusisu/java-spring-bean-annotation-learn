package com.wusisu.learn.annotation.service;

import com.wusisu.learn.annotation.service.dao.IBook;

public class BookHotelImpl implements IBook {
    @Override
    public void book() {
        System.out.println("After dinner the Goddess go hotel!");
    }
}