package com.mini.springframework.core.convert.converter;

//转换器接口
public interface Converter<S,T> {
    T convert(S source);
}
