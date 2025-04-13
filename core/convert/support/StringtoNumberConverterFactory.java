package com.mini.springframework.core.convert.support;

import cn.hutool.core.util.NumberUtil;
import com.mini.springframework.core.convert.converter.Converter;
import com.mini.springframework.core.convert.converter.ConverterFactory;
import com.mini.springframework.util.NumberUtils;
import org.jetbrains.annotations.NotNull;

//提供获取转换器接口和实现StringToNumber
public class StringtoNumberConverterFactory implements ConverterFactory<String, Number> {
    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }
    public static class StringToNumber<T extends Number> implements Converter<String, T> {
        private final Class<T> targetType;
        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }
        @Override
        @NotNull
        public T convert(String source) {
            if(source == null) {
                return null;
            }
            return NumberUtils.parseNumber(source, this.targetType);
        }
    }
}
