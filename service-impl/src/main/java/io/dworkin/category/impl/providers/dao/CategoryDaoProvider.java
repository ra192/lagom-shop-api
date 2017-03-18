package io.dworkin.category.impl.providers.dao;

import io.dworkin.dao.CategoryDao;
import io.dworkin.dao.impl.CategoryDaoImpl;
import io.dworkin.db.MyConnectionPool;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by yakov on 18.03.2017.
 */
public class CategoryDaoProvider implements Provider<CategoryDao> {

    private final MyConnectionPool connectionPool;

    @Inject
    public CategoryDaoProvider(MyConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public CategoryDao get() {
        return new CategoryDaoImpl(connectionPool);
    }
}
