package io.dworkin.security.impl;

import com.github.pgasync.ConnectionPool;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by yakov on 21.03.2017.
 */
public class TokenRepository {
    private final ConnectionPool connectionPool;

    public TokenRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public CompletionStage<Boolean> createToken(String username, String token, Date validTo) {
        final CompletableFuture<Boolean> future = new CompletableFuture<>();

        String query = "insert into token(user_id,token,valid_to) values ((select id from \"user\" where username=$1),$2,$3)";

        connectionPool.query(query, Arrays.asList(username, token, new Timestamp(validTo.getTime())), result -> future.complete(true), future::completeExceptionally);

        return future;
    }
}
