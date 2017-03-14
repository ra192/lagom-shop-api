package io.dwornik.db;

import com.github.pgasync.ConnectionPoolBuilder;
import com.github.pgasync.Db;

/**
 * Created by yakov on 14.03.2017.
 */
public final class DBConnection {
    public static final Db db = new ConnectionPoolBuilder()
            .hostname("localhost")
            .port(5432)
            .database("myshop")
            .username("myshop")
            .password("myshop")
            .poolSize(20)
            .build();
}
