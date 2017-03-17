package io.dworkin.dao;

import io.dworkin.model.PropertyEntity;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Property DAO
 * Created by yakov on 17.03.2017.
 */
public interface PropertyDao {
    CompletableFuture<Optional<PropertyEntity>> getByName(String name);

    CompletableFuture<Long> create(PropertyEntity property);

    CompletableFuture<Long> update(PropertyEntity property);
}
