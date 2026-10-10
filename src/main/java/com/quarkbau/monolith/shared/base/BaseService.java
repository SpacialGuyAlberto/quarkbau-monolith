package com.quarkbau.monolith.shared.base;

import java.util.List;
import java.util.Optional;

public interface BaseService<D, ID> {
    List<D> findAll();
    Optional<D> findById(ID id);
    D create(D dto);
    Optional<D> update(ID id, D dto);
    boolean delete(ID id);
}
