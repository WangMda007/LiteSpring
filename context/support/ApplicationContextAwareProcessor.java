package com.mini.springframework.context.support;

import com.mini.springframework.beans.BeansException;
import com.mini.springframework.beans.factory.config.BeanPostProcessor;
import com.mini.springframework.context.ApplicationContext;
import com.mini.springframework.context.ApplicationContextAware;

 
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
