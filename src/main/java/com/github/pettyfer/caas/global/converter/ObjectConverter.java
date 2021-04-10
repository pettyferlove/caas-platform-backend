package com.github.pettyfer.caas.global.converter;

/**
 * 对象转换器接口
 *
 * @author Petty
 * @date 2018/5/10
 */
public interface ObjectConverter<T, F> {

    /**
     * 转换至Dto
     *
     * @param source 原对象
     * @param target 目标对象
     */
    void convertToDto(T source, F target);

    /**
     * 将Dto对象转换至目标对象
     *
     * @param source 原对象
     * @param target 目标对象
     */
    void convertFromDto(T source, F target);

    /**
     * 复制Dto对象
     *
     * @param source 原对象
     * @param target 目标对象
     */
    void copyDto(T source, T target);
}
