package io.dworkin.dao;

import io.dworkin.model.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Category DAO
 * Created by yakov on 14.03.2017.
 */
public interface CategoryDao {

    CompletableFuture<Optional<CategoryEntity>> getByName(String name);

    CompletableFuture<List<CategoryEntity>> listByParentId(Long parentId);

    CompletableFuture<Long> create(CategoryEntity category);

    CompletableFuture<Boolean> update(CategoryEntity category);

    CompletableFuture<Boolean> updateProperties(Long id, Set<Long> propertyIds);
}
