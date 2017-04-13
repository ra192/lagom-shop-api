package io.dworkin.category.impl;


import com.github.pgasync.ConnectionPool;
import io.dworkin.category.api.Category;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Category repository using pgasynk
 * Created by yakov on 14.03.2017.
 */
public class CategoryRepository {

    private final ConnectionPool connectionPool;

    public CategoryRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CompletionStage<Optional<Category>> getByName(String name) {

        final CompletableFuture<Optional<Category>> future = new CompletableFuture<>();

        connectionPool.query("select *, exists(select * from category where parent_id=cat.id) from category as cat where name=$1",
                Arrays.asList(name), result -> {
                    if (result.size() > 0) {
                        future.complete(Optional.of(new Category(result.row(0).getString("name"),
                                result.row(0).getString("displayName"), result.row(0).getBoolean("exists"))));
                    } else {
                        future.complete(Optional.empty());
                    }
                }, future::completeExceptionally);

        return future;
    }

    public CompletionStage<PSequence<Category>> listRoots() {
        final CompletableFuture<PSequence<Category>> future = new CompletableFuture<>();

        final String query = "select *, exists(select * from category where parent_id=cat.id) from category as cat where parent_id is null";

        connectionPool.query(query,
                result -> future.complete(TreePVector.from(StreamSupport.stream(result.spliterator(), false)
                        .map(row -> new Category(row.getString("name"), row.getString("displayname"),
                                result.row(0).getBoolean("exists")))
                        .collect(Collectors.toList()))),
                future::completeExceptionally);

        return future;
    }

    public CompletionStage<PSequence<Category>> listByParentName(String name) {

        final CompletableFuture<PSequence<Category>> future = new CompletableFuture<>();

        final String query = "select *, exists(select * from category where parent_id=cat.id) from category as cat where parent_id = (select id from category where name=$1)";

        connectionPool.query(query, Arrays.asList(name),
                result -> future.complete(TreePVector.from(StreamSupport.stream(result.spliterator(), false)
                        .map(row -> new Category(row.getString("name"), row.getString("displayname"),
                                result.row(0).getBoolean("exists")))
                        .collect(Collectors.toList()))),
                future::completeExceptionally);

        return future;
    }

    public CompletionStage<Long> create(Category category, String parentName, Set<String> propertyNames) {

        final CompletableFuture<Long> future = new CompletableFuture<>();


        String query = "INSERT INTO category(id, displayname, name, parent_id) VALUES (nextval('category_id_seq'), $1, $2, (select id from category where name = $3));";

        connectionPool.query(query, Arrays.asList(category.displayName, category.name, parentName),
                result -> connectionPool.query(updatePropertiesQuery(category.name, propertyNames),
                        res -> future.complete(1L), future::completeExceptionally), future::completeExceptionally);

        return future;
    }

    public CompletionStage<Boolean> update(Category category, String parentName, Set<String> propertyNames) {

        final CompletableFuture<Boolean> future = new CompletableFuture<>();

        String query = "UPDATE category SET displayname=$2, parent_id=(select id from category where name=$3) WHERE name=$1;";

        connectionPool.query(query, Arrays.asList(category.name, category.displayName, parentName),
                result -> connectionPool.query(updatePropertiesQuery(category.name, propertyNames),
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
