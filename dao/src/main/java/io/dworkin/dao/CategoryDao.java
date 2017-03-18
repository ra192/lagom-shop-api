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

    CompletableFuture<List<CategoryEntity>> listRoots();

    CompletableFuture<List<CategoryEntity>> listByParentName(String name);

    CompletableFuture<Long> create(CategoryEntity category, String parentName, Set<String> propertyNames);

    CompletableFuture<Boolean> update(CategoryEntity category, String parentName, Set<String> propertyNames);
}
