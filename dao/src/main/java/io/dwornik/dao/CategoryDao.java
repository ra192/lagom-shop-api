package io.dwornik.dao;

import io.dwornik.model.CategoryEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by yakov on 14.03.2017.
 */
public interface CategoryDao {
    CompletableFuture<CategoryEntity> getByName(String name);
    CompletableFuture<List<CategoryEntity>> listByParentId(Long parentId);
}
