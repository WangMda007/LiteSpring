package com.mini.springframework.core.convert.support;

import com.mini.springframework.core.convert.converter.ConverterFactory;
import com.mini.springframework.core.convert.converter.ConverterRegistry;

//具体转化服务实现接口，简化操作
public class DefaultConversionService extends GenericConversionService{
    public DefaultConversionService() {
        addDefaultConverters(this);
    }

    public static void addDefaultConverters(ConverterRegistry converterRegistry) {
        converterRegistry.addConverterFactory(new StringtoNumberConverterFactory());
    }

}
