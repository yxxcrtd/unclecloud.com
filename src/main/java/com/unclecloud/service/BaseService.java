package com.unclecloud.service;

import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * Generic Service
 *
 * @param <T>
 * @param <PK>
 */
public interface BaseService<T, PK extends Serializable> {

    T findById(PK id);

    List<T> findAll(Sort sort);

    T save(T t);

}
