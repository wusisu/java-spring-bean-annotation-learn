package com.wusisu.learn.annotation;
import com.wusisu.learn.annotation.annotation.Resource;
import com.wusisu.learn.annotation.service.dao.IBook;
public class Holiday {
    // 注入bean
    @Resource("bookHotel")
    private IBook book;
    // 必须,标准的setter方法
    public void setBook(IBook book) {
        this.book = book;
    }
    // 使用注入的bean
    public void haveFun(){
        book.book();
    }
}