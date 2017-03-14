package io.dwornik.category.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dwornik.category.api.CategoryService;
import io.dwornik.dao.CategoryDao;

import javax.inject.Inject;

/**
 * Created by yakov on 14.03.2017.
 */
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    @Inject
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public ServiceCall<NotUsed, String> getByName(String name) {
        return notUsed -> categoryDao.getByName(name).thenApply(cat->cat.getDisplayName());
    }
}
