package com.wusisu.learn.annotation;

public class MainTest {
    public static void main(String[] args) {
        // 加载配置文件并获取上下文
        ApplicationContext ctx = new ApplicationContext(BeansXML.class);
        // 获取bean
        Holiday holiday = ctx.getBean("holiday");
//        holiday.book();
        holiday.haveFun();
    }
}