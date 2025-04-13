package com.mini.springframework.context.support;

import com.mini.springframework.beans.factory.FactoryBean;
import com.mini.springframework.beans.factory.InitializingBean;
import com.mini.springframework.core.convert.ConversionService;
import com.mini.springframework.core.convert.converter.Converter;
import com.mini.springframework.core.convert.converter.ConverterFactory;
import com.mini.springframework.core.convert.converter.ConverterRegistry;
import com.mini.springframework.core.convert.converter.GenericConverter;
import com.mini.springframework.core.convert.support.DefaultConversionService;
import com.mini.springframework.core.convert.support.GenericConversionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

//包装GenericConversionService
//提供获取Object，class，注册转换器等方法
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {
    @Nullable
    private Set<?> converters;

    @NotNull
    private GenericConversionService conversionService;


    @Override
    public ConversionService getObject() throws Exception {
        return conversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return conversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    // 用了注册转换操作的类。最终这个类会被配置到 spring.xml 中在启动的过程加载。
    @Override
    public void afterPropertiesSet() throws Exception {
        this.conversionService = new DefaultConversionService();
        registerConverters(converters, conversionService);
    }

    private void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter<?, ?>) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory<?, ?>) {
                    registry.addConverterFactory((ConverterFactory<?, ?>) converter);
                } else {
                    throw new IllegalArgumentException("Each converter object must implement one of the " +
                            "Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }

}
