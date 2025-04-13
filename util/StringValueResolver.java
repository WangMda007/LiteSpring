package com.mini.springframework.util;

/**
 * Simple strategy interface for resolving a String value.
 * Used by {@link com.mini.springframework.beans.factory.config.ConfigurableBeanFactory}.
 * <p>
 *
 *
 *
 *
 *
 *   
 */
public interface StringValueResolver {

    String resolveStringValue(String strVal);

}
