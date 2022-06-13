package com.ossprj.fileset.task.process.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public abstract class AbstractFilesetProcessor<T> implements FilesetProcessor {

    protected final String name;
    protected final Map<String, Object> configMap;
    protected final T configuration;

    protected AbstractFilesetProcessor(final String name, final Map<String, Object> configMap) {
        try {
            this.name = name;
            this.configMap = configMap;
            final ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
            final Type actualType = parameterizedType.getActualTypeArguments()[0];
            this.configuration = (T) new ObjectMapper().convertValue(configMap, Class.forName(actualType.getTypeName()));
        } catch (IllegalArgumentException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void init() {
        doInit();
    }

    protected void doInit() {
    }

    @Override
    public String toString() {
        return getName() + "{" +
                "configuration=" + configuration +
                '}';
    }

    @Override
    public void close() {

    }
}
