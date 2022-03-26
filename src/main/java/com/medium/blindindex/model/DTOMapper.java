package com.medium.blindindex.model;

public interface DTOMapper<T> {

    void mapFrom(T t);

    T mapToDomain();

}
