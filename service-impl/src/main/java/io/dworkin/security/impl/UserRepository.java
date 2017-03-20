package io.dworkin.security.impl;

import com.github.pgasync.ConnectionPool;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Created by yakov on 19.03.2017.
 */
public class UserRepository {
    private final ConnectionPool connectionPool;

    public UserRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CompletionStage<Optional<UserEntity>> getByName(String username) {
        final CompletableFuture<Optional<UserEntity>> future = new CompletableFuture<>();

        final String query = "select * from \"user\" where username=$1";

        connectionPool.query(query, Arrays.asList(username), result -> {
            if (result.size() > 0)
                future.complete(Optional.of(new UserEntity(result.row(0).getString("username"), result.row(0).getString("password"))));
            else future.complete(Optional.empty());
        }, future::completeExceptionally);

        return future;
    }

    public CompletionStage<List<String>> getRoles(String username) {
        CompletableFuture<List<String>> future = new CompletableFuture<>();

        final String query = "select role from user_role where user_id=(select id from \"user\" where username=$1)";
        connectionPool.query(query, Arrays.asList(username), result ->
                future.complete(StreamSupport.stream(result.spliterator(), false)
                        .map(row -> row.getString("role")).collect(toList())), future::completeExceptionally);

        return future;
    }
}
