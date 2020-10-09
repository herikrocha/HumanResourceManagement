package br.com.hrm.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParametrizedTypeUtil implements ParameterizedType {

    private ParameterizedType delegate;
    private Type[] actualTypeArguments;

    ParametrizedTypeUtil(ParameterizedType delegate, Type[] actualTypeArguments) {
        this.delegate = delegate;
        this.actualTypeArguments = actualTypeArguments;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public Type getRawType() {
        return delegate.getRawType();
    }

    @Override
    public Type getOwnerType() {
        return delegate.getOwnerType();
    }


}
