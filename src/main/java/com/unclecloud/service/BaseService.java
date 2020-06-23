package com.unclecloud.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<T> findAll(Example example, Pageable pageable);

    T save(T t);

}
