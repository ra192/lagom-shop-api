package io.dworkin.product.impl;

import com.github.pgasync.ConnectionPool;
import com.github.pgasync.ConnectionPoolBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.dworkin.product.api.ProductService;

/**
 * Created by yakov on 20.03.2017.
 */
public class ProductModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(
                serviceBinding(ProductService.class,ProductServiceImpl.class)
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
    ProductRepository productRepository(ConnectionPool connectionPool) {
        return new ProductRepository(connectionPool);
    }
}