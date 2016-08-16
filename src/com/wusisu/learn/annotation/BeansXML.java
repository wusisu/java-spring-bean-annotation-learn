package com.wusisu.learn.annotation;

/**
 * 配置文件类
 * 将bean注入上下文容器
 * 相当于beans.xml
 */
import com.wusisu.learn.annotation.annotation.Configuration;
import com.wusisu.learn.annotation.service.BookHotelImpl;
import com.wusisu.learn.annotation.service.BookRestaurantImpl;
import com.wusisu.learn.annotation.service.dao.IBook;
import com.wusisu.learn.annotation.annotation.Bean;

@Configuration
public class BeansXML {

    @Bean("bookHotel")
    public IBook bookHotel() {
        return new BookHotelImpl();
    }

    @Bean
    //注册的Bean名称为方法名
    public IBook bookRestaurant() {
        return new BookRestaurantImpl();
    }

    @Bean("holiday")
    public Holiday funnyHoliday() {
        return new Holiday();
    }
}