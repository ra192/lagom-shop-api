package io.dworkin.dao.impl;

import io.dworkin.dao.UserDao;
import io.dworkin.db.MyConnectionPool;
import io.dworkin.model.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Created by yakov on 19.03.2017.
 */
public class UserDaoImpl implements UserDao {
    private final MyConnectionPool connectionPool;

    public UserDaoImpl(MyConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public CompletableFuture<Optional<UserEntity>> getByName(String username) {
        final CompletableFuture<Optional<UserEntity>> future = new CompletableFuture<>();

        final String query = "select * from user where username=$1";

        connectionPool.getDb().query(query, Arrays.asList(username), result -> {
            if (result.size() > 0)
                future.complete(Optional.of(new UserEntity(result.row(0).getString("username"), result.row(0).getString("password"))));
            else future.complete(Optional.empty());
        }, future::completeExceptionally);

        return future;
    }

    @Override
    public CompletableFuture<List<String>> getRoles(String username) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();

        final String query = "select role from user_role where user_id=(select id from user where name=$1)";
        connectionPool.getDb().query(query, Arrays.asList(username), result ->
                future.complete(StreamSupport.stream(result.spliterator(), false)
                        .map(row -> row.getString("role")).collect(toList())), future::completeExceptionally);

        return future;
    }
}
