/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package io.dworkin.category.impl;

import com.github.pgasync.ConnectionPool;
import com.github.pgasync.ConnectionPoolBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.dworkin.product.api.CategoryService;
import io.dworkin.security.impl.TokenRepository;
import io.dworkin.security.impl.UserRepository;

/**
 * The module that binds the CategoryService so that it can be served.
 */
public class CategoryModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(
                serviceBinding(CategoryService.class, CategoryServiceImpl.class)
        );
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    @Provides
    ConnectionPool provideConnectionPool(Config config) {
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
    CategoryRepository provideCategoryRepository(ConnectionPool connectionPool) {
        return new CategoryRepository(connectionPool);
    }

    @Provides
    UserRepository provideUserRepository(ConnectionPool connectionPool) {
        return new UserRepository(connectionPool);
    }

    @Provides
    TokenRepository tokenRepository(ConnectionPool connectionPool) {
        return new TokenRepository(connectionPool);
    }
}
