package com.mini.springframework.core.convert;

import org.jetbrains.annotations.Nullable;

//判断与转换
public interface ConversionService {

    boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);

    <T> T convert(Object source, Class<T> targetType);
}
