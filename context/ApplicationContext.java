package com.mini.springframework.context;

import com.mini.springframework.beans.factory.HierarchicalBeanFactory;
import com.mini.springframework.beans.factory.ListableBeanFactory;
import com.mini.springframework.core.io.ResourceLoader;

/**
 * Central interface to provide configuration for an application.
 * This is read-only while the application is running, but may be
 * reloaded if the implementation supports this.
 * <p>
 * 应用上下文
 * <p>
 *
 *
 *
 *
 *
 *   
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
