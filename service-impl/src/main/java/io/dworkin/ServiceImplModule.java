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
import io.dworkin.dao.CategoryDao;
import io.dworkin.dao.ProductDao;
import io.dworkin.dao.PropertyDao;
import io.dworkin.dao.UserDao;
import io.dworkin.dao.impl.CategoryDaoImpl;
import io.dworkin.dao.impl.ProductDaoImpl;
import io.dworkin.dao.impl.PropertyDaoImpl;
import io.dworkin.dao.impl.UserDaoImpl;
import io.dworkin.db.MyConnectionPool;
import io.dworkin.product.api.ProductService;
import io.dworkin.product.impl.ProductServiceImpl;

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
    PropertyDao providePropertyDao(MyConnectionPool connectionPool) {
        return new PropertyDaoImpl(connectionPool);
    }

    @Provides
    CategoryDao provideCategoryDao(MyConnectionPool connectionPool) {
        return new CategoryDaoImpl(connectionPool);
    }

    @Provides
    ProductDao provideProductDao(MyConnectionPool connectionPool) {
        return new ProductDaoImpl(connectionPool);
    }

    @Provides
    UserDao provideUserDao(MyConnectionPool connectionPool) {
        return new UserDaoImpl(connectionPool);
    }
}
