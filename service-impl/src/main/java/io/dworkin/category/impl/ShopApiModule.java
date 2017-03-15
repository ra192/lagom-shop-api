/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package io.dworkin.category.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import io.dworkin.category.api.CategoryService;
import io.dworkin.dao.CategoryDao;
import io.dworkin.dao.impl.CategoryDaoImpl;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class ShopApiModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(CategoryService.class, CategoryServiceImpl.class));
        bind(CategoryDao.class).to(CategoryDaoImpl.class);
    }
}
