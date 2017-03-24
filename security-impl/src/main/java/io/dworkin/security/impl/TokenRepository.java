package io.dworkin.security.impl;

import com.github.pgasync.ConnectionPool;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

/**
 * Created by yakov on 21.03.2017.
 */
public class TokenRepository {
    private final ConnectionPool connectionPool;

    public TokenRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CompletionStage<Optional<TokenEntity>> getUsernameByToken(String token) {
        final CompletableFuture<Optional<TokenEntity>> future = new CompletableFuture<>();

        final String query = "select * from \"user\" where id=(select user_id from token where token=$1)";

        connectionPool.query(query, asList(token), result -> {
            if (result.size() > 0)
                future.complete(Optional.of(new TokenEntity(result.row(0).getString("username"),
                        result.row(0).getString("token"), result.row(0).getTimestamp("valid_to").toInstant())));
            else
                future.complete(Optional.empty());
        }, future::completeExceptionally);

        return future;
    }

    public CompletionStage<Boolean> createToken(TokenEntity tokenEntity) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();

        final String query = "insert into token(user_id,token,valid_to) values ((select id from \"user\" where username=$1),$2,$3)";

        connectionPool.query(query, asList(tokenEntity.getUsername(), tokenEntity.getToken(),
                new Timestamp(tokenEntity.getValidTo().toEpochMilli())), result -> future.complete(true), future::completeExceptionally);

        return future;
    }
}
