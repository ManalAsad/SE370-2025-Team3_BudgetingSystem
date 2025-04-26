package org.project.repositories;

import org.project.models.User;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Integer> {
    Optional<User> findByEmail(String email) throws Exception;
    Optional<User> findByUsername(String username) throws Exception;
    boolean existsByEmail(String email) throws Exception;
}