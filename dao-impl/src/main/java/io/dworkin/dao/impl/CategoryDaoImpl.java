package io.dworkin.dao.impl;


import io.dworkin.dao.CategoryDao;
import io.dworkin.db.MyConnectionPool;
import io.dworkin.model.CategoryEntity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * {@link CategoryDao} implementation using pgasynk
 * Created by yakov on 14.03.2017.
 */
public class CategoryDaoImpl implements CategoryDao {

    private final MyConnectionPool connectionPool;

    public CategoryDaoImpl(MyConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public CompletableFuture<Optional<CategoryEntity>> getByName(String name) {

        final CompletableFuture<Optional<CategoryEntity>> future = new CompletableFuture<>();

        connectionPool.getDb().query("select * from category where name=$1", Arrays.asList(name), result -> {
            if (result.size() > 0) {
                final CategoryEntity category = new CategoryEntity(result.row(0).getLong("id"), result.row(0).getString("name"),
                        result.row(0).getString("displayName"), result.row(0).getLong("parent_id"));

                future.complete(Optional.of(category));
            } else {
                future.complete(Optional.empty());
            }
        }, future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<List<CategoryEntity>> listByParentId(Long parentId) {

        final CompletableFuture<List<CategoryEntity>> future = new CompletableFuture<>();

        final String query = (parentId==null) ? "select * from category where parent_id is null" :
                "select * from category where parent_id = ".concat(parentId.toString());

        connectionPool.getDb().query(query,
                result -> {
                    final List<CategoryEntity> categories = StreamSupport.stream(result.spliterator(), false)
                            .map(row -> new CategoryEntity(row.getLong("id"), row.getString("name"),
                                    row.getString("displayname"), row.getLong("parent_id"))).collect(Collectors.toList());

                    future.complete(categories);
                },
                future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Long> create(CategoryEntity category) {

        final CompletableFuture<Long> future = new CompletableFuture<>();

        connectionPool.getDb().query("select nextval('category_id_seq')", idResult -> {
            final Long id = idResult.row(0).getLong(0);

            String query = "INSERT INTO category(id, displayname, name, parent_id) VALUES ($1, $2, $3, $4);";

            connectionPool.getDb().query(query, Arrays.asList(id, category.getDisplayName(), category.getName(), category.getParentId()),
                    result -> future.complete(id), future::completeExceptionally);
        }, future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Boolean> update(CategoryEntity category) {

        final CompletableFuture<Boolean> future = new CompletableFuture<>();

        String query = "UPDATE category SET displayname=$2, name=$3, parent_id=$4 WHERE id=$1";

        connectionPool.getDb().query(query, Arrays.asList(category.getId(), category.getDisplayName(), category.getName(), category.getParentId()),
                result -> future.complete(true), future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Boolean> updateProperties(Long id, Set<Long> propertyIds) {

        final CompletableFuture<Boolean> future = new CompletableFuture<>();

        final StringBuilder queryBuilder = new StringBuilder("delete from category_property where category_id=").append(id).append(";");

        if (!propertyIds.isEmpty()) {
            queryBuilder.append("INSERT INTO category_property(category_id, properties_id) VALUES");
            final Iterator<Long> iterator = propertyIds.iterator();
            while (iterator.hasNext()) {
                final Long propertyId = iterator.next();
                queryBuilder.append(" (").append(id).append(", ").append(propertyId).append(")");
                if (iterator.hasNext())
                    queryBuilder.append(", ");
                else
                    queryBuilder.append(";");
            }
        }

        connectionPool.getDb().query(queryBuilder.toString(), result -> future.complete(true), future::completeExceptionally);

        return future;
    }
}
