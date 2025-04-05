package org.project.repositories;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID> {
    // Basic CRUD operations
    T save(T entity) throws Exception;
    Optional<T> findById(ID id) throws Exception;
    List<T> findAll() throws Exception;
    void delete(ID id) throws Exception;
    T update(T entity) throws Exception;
}