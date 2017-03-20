package io.dworkin.property.impl;

import io.dworkin.db.MyConnectionPool;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Property repository using pgasynk
 * Created by yakov on 17.03.2017.
 */
public class PropertyRepository {

    private final MyConnectionPool connectionPool;

    public PropertyRepository(MyConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CompletionStage<Optional<PropertyEntity>> getByName(String name) {

        final CompletableFuture<Optional<PropertyEntity>> future = new CompletableFuture<>();

        String query = "select * from property where name=$1";

        connectionPool.getDb().query(query, Arrays.asList(name), result -> {
            if (result.size() > 0)
                future.complete(Optional.of(
                        new PropertyEntity(result.row(0).getString("name"), result.row(0).getString("displayName"))));
            else
                future.complete(Optional.empty());
        }, future::completeExceptionally);

        return future;
    }

    public CompletionStage<Long> create(PropertyEntity property) {

        final CompletableFuture<Long> future = new CompletableFuture<>();

        String query = "INSERT INTO property(id, displayname, name) VALUES (nextval('property_id_seq'), $1, $2)";
        connectionPool.getDb().query(query, Arrays.asList(property.getDisplayName(), property.getName()),
                res -> future.complete(1L), future::completeExceptionally);

        return future;
    }

    public CompletionStage<Long> update(PropertyEntity property) {

        final CompletableFuture<Long> future = new CompletableFuture<>();

        String query = "UPDATE property SET displayname=$1 WHERE name=$2";
        connectionPool.getDb().query(query, Arrays.asList(property.getDisplayName(), property.getName()),
                res -> future.complete(1L), future::completeExceptionally);

        return future;
    }
}
