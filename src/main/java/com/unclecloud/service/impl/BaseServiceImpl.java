package com.unclecloud.service.impl;

import com.unclecloud.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * Generic Service Implement
 *
 * @param <T>
 * @param <PK>
 */
public class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

    @Autowired
    protected JpaRepository<T, PK> baseRepository;

    public T findById(PK id) {
        return baseRepository.getOne(id);
    }

    public List<T> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    public Page<T> findAll(Example example, Pageable pageable) {
        return baseRepository.findAll(example, pageable);
    }

    @Transactional
    public T save(T t) {
        return baseRepository.save(t);
    }

}
