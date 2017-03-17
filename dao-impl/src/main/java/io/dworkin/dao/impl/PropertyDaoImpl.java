package io.dworkin.dao.impl;

import io.dworkin.dao.PropertyDao;
import io.dworkin.db.MyConnectionPool;
import io.dworkin.model.PropertyEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * {@link PropertyDao} implementation using pgasynk
 * Created by yakov on 17.03.2017.
 */
public class PropertyDaoImpl implements PropertyDao {

    @Override
    public CompletableFuture<Optional<PropertyEntity>> getByName(String name) {

        final CompletableFuture<Optional<PropertyEntity>> future = new CompletableFuture<>();

        String query = "select * from property where name=$1";

        MyConnectionPool.db.query(query, Arrays.asList(name), result -> {
            if (result.size() > 0)
                future.complete(Optional.of(new PropertyEntity(result.row(0).getLong("id"),
                        result.row(0).getString("name"), result.row(0).getString("displayName"))));
            else
                future.complete(Optional.empty());
        }, future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Long> create(PropertyEntity property) {

        final CompletableFuture<Long> future = new CompletableFuture<>();

        MyConnectionPool.db.query("select nextval('hibernate_sequence')", idRes -> {
            final Long id = idRes.row(0).getLong(0);
            String query = "INSERT INTO property(id, displayname, name) VALUES ($1, $2, $3)";
            MyConnectionPool.db.query(query, Arrays.asList(id, property.getDisplayName(), property.getName()),
                    res -> future.complete(id), future::completeExceptionally);
        }, future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<Long> update(PropertyEntity property) {

        final CompletableFuture<Long> future = new CompletableFuture<>();

        String query = "UPDATE property SET displayname=$1, name=$2 WHERE id=$1";
        MyConnectionPool.db.query(query, Arrays.asList(property.getId(), property.getDisplayName(), property.getName()),
                res -> future.complete(1L), future::completeExceptionally);

        return future;
    }
}
