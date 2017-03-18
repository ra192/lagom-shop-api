/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package io.dworkin.category.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import io.dworkin.category.api.CategoryService;
import io.dworkin.dao.CategoryDao;
import io.dworkin.dao.PropertyDao;
import io.dworkin.dao.impl.CategoryDaoImpl;
import io.dworkin.dao.impl.PropertyDaoImpl;
import io.dworkin.db.MyConnectionPool;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class ShopApiModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(CategoryService.class, CategoryServiceImpl.class));
    }

    @Provides
    MyConnectionPool provideMyConnectionPool() {
        return new MyConnectionPool("localhost",5432,"myshop","myshop","myshop",20);
    }

    @Provides
    PropertyDao providePropertyDao(MyConnectionPool connectionPool) {
        return new PropertyDaoImpl(connectionPool);
    }

    @Provides
    CategoryDao provideCategoryDao(MyConnectionPool connectionPool) {
        return new CategoryDaoImpl(connectionPool);
    }
}
