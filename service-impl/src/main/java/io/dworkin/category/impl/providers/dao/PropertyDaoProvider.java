package io.dworkin.category.impl.providers.dao;

import io.dworkin.dao.PropertyDao;
import io.dworkin.dao.impl.PropertyDaoImpl;
import io.dworkin.db.MyConnectionPool;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by yakov on 18.03.2017.
 */
public class PropertyDaoProvider implements Provider<PropertyDao> {

    private final MyConnectionPool connectionPool;

    @Inject
    public PropertyDaoProvider(MyConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public PropertyDao get() {
        return new PropertyDaoImpl(connectionPool);
    }
}
