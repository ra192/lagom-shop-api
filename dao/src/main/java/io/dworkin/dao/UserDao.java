package io.dworkin.dao;

import io.dworkin.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by yakov on 19.03.2017.
 */
public interface UserDao {
    CompletableFuture<Optional<UserEntity>> getByName(String username);

    CompletableFuture<List<String>> getRoles(String username);
}
