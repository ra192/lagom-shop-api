package io.dworkin.db;

import com.github.pgasync.ConnectionPoolBuilder;
import com.github.pgasync.Db;

/**
 * Created by yakov on 14.03.2017.
 */
public final class MyConnectionPool {
    private final Db db;

    public MyConnectionPool(String hostname, int port, String database, String username, String password, int poolSize) {
        db = new ConnectionPoolBuilder()
                .hostname(hostname)
                .port(port)
                .database(database)
                .username(username)
                .password(password)
                .poolSize(poolSize)
                .build();
    }

    public Db getDb() {
        return db;
    }
}
