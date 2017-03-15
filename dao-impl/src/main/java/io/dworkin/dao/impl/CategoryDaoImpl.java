package io.dworkin.dao.impl;


import io.dworkin.dao.CategoryDao;
import io.dworkin.db.DBConnection;
import io.dworkin.model.CategoryEntity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by yakov on 14.03.2017.
 */
public class CategoryDaoImpl implements CategoryDao {

    @Override
    public CompletableFuture<CategoryEntity> getByName(String name) {

        final CompletableFuture<CategoryEntity> future = new CompletableFuture<>();

        DBConnection.db.query("select * from category where name=$1", Arrays.asList(name), result -> {
            if (result.size() > 0) {
                final CategoryEntity category = new CategoryEntity(result.row(0).getLong("id"), result.row(0).getString("name"),
                        result.row(0).getString("displayName"), result.row(0).getLong("parent_id"));

                future.complete(category);
            } else {
                future.completeExceptionally(new Exception("CategoryEntity with specified name doesn't exist"));
            }
        }, future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<List<CategoryEntity>> listByParentId(Long parentId) {

        final CompletableFuture<List<CategoryEntity>> future = new CompletableFuture<>();

        final String query = (parentId == 0) ? "select * from category where parent_id is null" :
                "select * from category where parent_id = ".concat(parentId.toString());

        DBConnection.db.query(query,
                result -> {
                    final List<CategoryEntity> categories = StreamSupport.stream(result.spliterator(), false)
                            .map(row -> new CategoryEntity(row.getLong("id"), row.getString("name"),
                                    row.getString("displayname"), row.getLong("parent_id"))).collect(Collectors.toList());

                    future.complete(categories);
                },
                future::completeExceptionally);

        return future;
    }
}
