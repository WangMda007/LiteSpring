package com.mini.springframework.core.convert.converter;

import java.util.Set;
import cn.hutool.core.lang.Assert;
//通用转换器
public interface GenericConverter {
    //存放可转化的类型
    Set<ConvertiblePair> getConvertibleTypes();

    Object convert(Object source, Class sourceType, Class targetType);

    //ConvertiblePair存放要转型的源Class和目标Class，以及判断Class
    final class ConvertiblePair {
        private final Class<?> targetType;
        private final Class<?> sourceType;


        public ConvertiblePair(Class<?> targetType, Class<?> sourceType) {
            Assert.notNull(targetType,"targetType must not null");
            Assert.notNull(sourceType,"sourceType must not null");
            this.targetType = targetType;
            this.sourceType = sourceType;
        }

        public Class<?> getTargetType() {
            return targetType;
        }
        public Class<?> getSourceType() {
            return sourceType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ConvertiblePair other = (ConvertiblePair) o;
            return this.sourceType.equals(other.sourceType) && this.targetType.equals(other.targetType);
        }

        @Override
        public int hashCode() {
            return this.sourceType.hashCode()* 31 + this.targetType.hashCode();
        }
    }
}
