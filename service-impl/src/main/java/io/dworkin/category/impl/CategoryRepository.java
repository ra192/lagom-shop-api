package io.dworkin.category.impl;


import io.dworkin.db.MyConnectionPool;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Category repository using pgasynk
 * Created by yakov on 14.03.2017.
 */
public class CategoryRepository {

    private final MyConnectionPool connectionPool;

    public CategoryRepository(MyConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CompletionStage<Optional<CategoryEntity>> getByName(String name) {

        final CompletableFuture<Optional<CategoryEntity>> future = new CompletableFuture<>();

        connectionPool.getDb().query("select * from category where name=$1", Arrays.asList(name), result -> {
            if (result.size() > 0) {
                final CategoryEntity category = new CategoryEntity(result.row(0).getString("name"),
                        result.row(0).getString("displayName"));

                future.complete(Optional.of(category));
            } else {
                future.complete(Optional.empty());
            }
        }, future::completeExceptionally);

        return future;
    }

    public CompletionStage<List<CategoryEntity>> listRoots() {
        final CompletableFuture<List<CategoryEntity>> future = new CompletableFuture<>();

        final String query = "select * from category where parent_id is null";

        connectionPool.getDb().query(query,
                result -> {
                    final List<CategoryEntity> categories = StreamSupport.stream(result.spliterator(), false)
                            .map(row -> new CategoryEntity(row.getString("name"), row.getString("displayname")))
                            .collect(Collectors.toList());

                    future.complete(categories);
                },
                future::completeExceptionally);

        return future;
    }

    public CompletionStage<List<CategoryEntity>> listByParentName(String name) {

        final CompletableFuture<List<CategoryEntity>> future = new CompletableFuture<>();

        final String query = "select * from category where parent_id = (select id from category where name=$1)";

        connectionPool.getDb().query(query, Arrays.asList(name),
                result -> {
                    final List<CategoryEntity> categories = StreamSupport.stream(result.spliterator(), false)
                            .map(row -> new CategoryEntity(row.getString("name"), row.getString("displayname")))
                            .collect(Collectors.toList());

                    future.complete(categories);
                },
                future::completeExceptionally);

        return future;
    }

    public CompletionStage<Long> create(CategoryEntity category, String parentName, Set<String> propertyNames) {

        final CompletableFuture<Long> future = new CompletableFuture<>();


        String query = "INSERT INTO category(id, displayname, name, parent_id) VALUES (nextval('category_id_seq'), $1, $2, (select id from category where name = $3));";

        connectionPool.getDb().query(query, Arrays.asList(category.getDisplayName(), category.getName(), parentName),
                result -> connectionPool.getDb().query(updatePropertiesQuery(category.getName(), propertyNames),
                        res -> future.complete(1L), future::completeExceptionally), future::completeExceptionally);

        return future;
    }

    public CompletionStage<Boolean> update(CategoryEntity category, String parentName, Set<String> propertyNames) {

        final CompletableFuture<Boolean> future = new CompletableFuture<>();

        String query = "UPDATE category SET displayname=$2, parent_id=(select id from category where name=$3) WHERE name=$1;";

        connectionPool.getDb().query(query, Arrays.asList(category.getName(), category.getDisplayName(), parentName),
                result -> connectionPool.getDb().query(updatePropertiesQuery(category.getName(), propertyNames),
                        res -> future.complete(true), future::completeExceptionally), future::completeExceptionally);

        return future;
    }

    private String updatePropertiesQuery(String name, Set<String> propertyNames) {


        final StringBuilder queryBuilder = new StringBuilder(
                "delete from category_property where category_id=(select id from category where name='")
                .append(name).append("');");

        if (!propertyNames.isEmpty()) {
            queryBuilder.append("INSERT INTO category_property(category_id, properties_id) VALUES");
            final Iterator<String> iterator = propertyNames.iterator();
            while (iterator.hasNext()) {
                final String propertyName = iterator.next();
                queryBuilder.append(" ((select id from category where name='").append(name)
                        .append("'), (select id from property where name='").append(propertyName).append("'))");
                if (iterator.hasNext())
                    queryBuilder.append(", ");
                else
                    queryBuilder.append(";");
            }
        }

        return queryBuilder.toString();
    }
}
