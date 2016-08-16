package com.wusisu.learn.annotation;

import com.wusisu.learn.annotation.annotation.Configuration;
import com.wusisu.learn.annotation.annotation.Resource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.wusisu.learn.annotation.annotation.Bean;

public class ApplicationContext {
    // 配置文件类
    private Class<?> configClass;
    // 配置文件类中Bean,只处理使用@Bean标注的方法,没有注解的方法将被忽略
    // Map<注解名称,注解方法>
    private Map<String, Method> mapMethods;
    // 配置文件类的实例
    private Object configObj;
    // 构造函数
    public ApplicationContext(Class<?> configClass) {
        this.configClass = configClass;
        // 检查配置文件类
        if (checkConfigClass()) {
            // 读取配置文件类的方法
            mapMethods = getDeclaredMethods(configClass);
        }
    }
    // 判断配置文件类是否使用了@Configuration注解
    private boolean checkConfigClass() {
        Boolean useConfiguration = false;
        try {
            useConfiguration = configClass.isAnnotationPresent(Configuration.class);
            if (!useConfiguration) {
                System.out.println("[配置文件类:" + configClass.getName() + " 缺少@Configuration注解]");
            }
        } catch (Exception e) {
            System.out.println("[读取配置文件类失败]" + e);
        }
        return useConfiguration;
    }
    // 获取一个类中所有使用了@Bean注解的方法
    private Map<String, Method> getDeclaredMethods(Class<?> clazz) {
        Map<String, Method> resultMap = new HashMap<>();
        try {
            for (Method m : clazz.getDeclaredMethods()) {
                // 获取每个方法的@bean注解
                Bean beanMethod = m.getAnnotation(Bean.class);
                if (null != beanMethod) {
                    // @Bean没有自定义value,使用方法名称作为value
                    String annoName = beanMethod.value();
                    annoName = ("".equals(annoName)) ? m.getName() : annoName;
                    resultMap.put(annoName, m);
                }
            }
        } catch (Exception e) {
            System.out.println("[读取配置文件类Bean定义出错]" + e);
        }
        return resultMap;
    }
    // 获取类实例,只能调用无参的构造函数
    private <T> T getInstance(Class<T> clazz) {
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            System.out.println("[实例化配置文件类失败,类名称: " + clazz.getName() + "]" + e);
        }
        return instance;
    }
    // 字符串的第一个字母转换为大写
    private String getMethodName(String fildeName) {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
    /**
     * 根据bean名称获取bean
     * @param beanName 注册的Bean名称
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName) {
        if (null == configObj) {
             configObj = getInstance(configClass);
        }
        // Bean实例
        T instance = null;
        Method m = mapMethods.get(beanName);
        if (null != m) {
            try {
                // 获取实例
                instance = (T) m.invoke(configObj);
                // 实例对象对应的Class类型
                Class<?> instanceClass = instance.getClass();
                // 注入依赖(实例化@Resource引用的Bean)
                for (Field fs : instanceClass.getDeclaredFields()) {
                    // 读取属性上的@Resource注解信息
                    Resource resField = fs.getAnnotation(Resource.class);
                    if (null != resField) {
                        // 获取注解值
                        String ResValue = resField.value();
                        // 判断是否已在配置文件类中已注册
                        Method refM = mapMethods.get(ResValue);
                        if (null != refM) {
                            try {
                                // 获取属性对应的标准setter方法名称
                                String setterName = "set"+ getMethodName(fs.getName());
                                // 根据名称和入参类型获取setter方法
                                Method setter = instanceClass.getMethod(setterName,fs.getType());
                                // 执行setter方法注入属性值
                                // refM.invoke(configObj) 将返回一个配置文件类中的实例
                                setter.invoke(instance,refM.invoke(configObj));
                            } catch (Exception e) {
                                System.out.println("[类:"+ instance.getClass().getName()+ ",属性:" + fs.getName()+ " 缺少标准的setter()方法]" + e);
                            }
                        } else {
                            System.out.println("[配置文件类中未找到对应的Bean:"+ ResValue + "]");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("[实例化bean失败,名称:" + beanName + "]" + e);
            }
        }
        return instance;
    }
}