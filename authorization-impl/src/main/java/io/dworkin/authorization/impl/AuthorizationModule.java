package io.dworkin.authorization.impl;

import com.github.pgasync.ConnectionPool;
import com.github.pgasync.ConnectionPoolBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.dworkin.authorization.api.AuthorizationService;
import io.dworkin.security.impl.UserRepository;

/**
 * Created by yakov on 21.03.2017.
 */
public class AuthorizationModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(
                serviceBinding(AuthorizationService.class,AuthorizationServiceImpl.class));
    }

    @Provides
    Config config() {
        return ConfigFactory.load();
    }

    @Provides
    ConnectionPool connectionPool(Config config) {
        return new ConnectionPoolBuilder()
                .hostname(config.getString("db.host"))
                .port(config.getInt("db.port"))
                .database(config.getString("db.name"))
                .username(config.getString("db.password"))
                .password(config.getString("db.password"))
                .poolSize(config.getInt("db.poolSize"))
                .build();
    }

    @Provides
    UserRepository userRepository(ConnectionPool connectionPool) {
        return new UserRepository(connectionPool);
    }
}
