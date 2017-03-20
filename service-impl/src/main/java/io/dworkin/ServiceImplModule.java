/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package io.dworkin;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.dworkin.category.api.CategoryService;
import io.dworkin.category.impl.CategoryServiceImpl;
import io.dworkin.category.impl.CategoryRepository;
import io.dworkin.security.impl.UserRepository;
import io.dworkin.db.MyConnectionPool;
import io.dworkin.product.api.ProductService;
import io.dworkin.product.impl.ProductRepository;
import io.dworkin.product.impl.ProductServiceImpl;
import io.dworkin.property.impl.PropertyRepository;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class ServiceImplModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(
                serviceBinding(CategoryService.class, CategoryServiceImpl.class),
                serviceBinding(ProductService.class,ProductServiceImpl.class)
        );
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    @Provides
    MyConnectionPool provideMyConnectionPool(Config config) {
        return new MyConnectionPool(config.getString("db.host"), config.getInt("db.port"), config.getString("db.name"),
                config.getString("db.user"), config.getString("db.password"), config.getInt("db.poolSize"));
    }

    @Provides
    PropertyRepository providePropertyRepository(MyConnectionPool connectionPool) {
        return new PropertyRepository(connectionPool);
    }

    @Provides
    CategoryRepository provideCategoryRepository(MyConnectionPool connectionPool) {
        return new CategoryRepository(connectionPool);
    }

    @Provides
    ProductRepository provideProductRepository(MyConnectionPool connectionPool) {
        return new ProductRepository(connectionPool);
    }

    @Provides
    UserRepository provideUserRepository(MyConnectionPool connectionPool) {
        return new UserRepository(connectionPool);
    }
}
