package com.mini.springframework.beans.factory.support;

import com.mini.springframework.beans.BeansException;
import com.mini.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 *   
 * <p>
 * Bean 实例化策略
 */
public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException;

}
