package io.dwornik.dao;

import io.dwornik.model.Category;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by yakov on 14.03.2017.
 */
public interface CategoryDao {
    CompletableFuture<Category> getByName(String name);
}
