package io.dworkin.property.impl;

import com.github.pgasync.ConnectionPool;
import io.dworkin.property.api.Property;
import io.dworkin.property.api.PropertyValue;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Property repository using pgasynk
 * Created by yakov on 17.03.2017.
 */
public class PropertyRepository {

    private final ConnectionPool connectionPool;

    public PropertyRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CompletionStage<Optional<Property>> getByName(String name) {

        final CompletableFuture<Optional<Property>> future = new CompletableFuture<>();

        String query = "select * from property where name=$1";

        connectionPool.query(query, Arrays.asList(name), result -> {
            if (result.size() > 0)
                future.complete(Optional.of(
                        new Property(result.row(0).getString("name"), result.row(0).getString("displayName"))));
            else
                future.complete(Optional.empty());
        }, future::completeExceptionally);

        return future;
    }

    public CompletionStage<Long> create(Property property) {

        final CompletableFuture<Long> future = new CompletableFuture<>();

        String query = "INSERT INTO property(id, displayname, name) VALUES (nextval('property_id_seq'), $1, $2)";
        connectionPool.query(query, Arrays.asList(property.getDisplayName(), property.getName()),
                res -> future.complete(1L), future::completeExceptionally);

        return future;
    }

    public CompletionStage<Long> update(Property property) {

        final CompletableFuture<Long> future = new CompletableFuture<>();

        String query = "UPDATE property SET displayname=$1 WHERE name=$2";
        connectionPool.query(query, Arrays.asList(property.getDisplayName(), property.getName()),
                res -> future.complete(1L), future::completeExceptionally);

        return future;
    }

    public CompletionStage<Optional<PropertyValue>>getPropertyValueByName(String name) {
        final CompletableFuture<Optional<PropertyValue>> future = new CompletableFuture<>();
        final String query = "select * from property_value where name=$1";

        connectionPool.query(query, Arrays.asList(name), result -> {
            if (result.size() > 0)
                future.complete(Optional.of(
                        new PropertyValue(result.row(0).getString("name"), result.row(0).getString("displayName"))));
            else
                future.complete(Optional.empty());
        }, future::completeExceptionally);

        return future;
    }
}
