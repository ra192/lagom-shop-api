package io.dwornik.dao.impl;


import io.dwornik.db.DBConnection;
import io.dwornik.model.Category;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * Created by yakov on 14.03.2017.
 */
public class CategoryDaoImpl implements io.dwornik.dao.CategoryDao {

    @Override
    public CompletableFuture<Category> getByName(String name) {

        final CompletableFuture<Category> future=new CompletableFuture<>();

        DBConnection.db.query("select * from category where name=$1", Arrays.asList(name),result->{
            if (result.size() > 0) {
                final Category category = new Category(result.row(0).getLong("id"), result.row(0).getString("name"),
                        result.row(0).getString("displayName"), result.row(0).getLong("parent_id"));

                future.complete(category);
            } else {
                future.completeExceptionally(new Exception("Category with specified name doesn't exist"));
            }
        }, future::completeExceptionally);

        return future;
    }
}
