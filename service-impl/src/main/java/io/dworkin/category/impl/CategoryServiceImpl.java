package io.dworkin.category.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dworkin.SecuredServiceImpl;
import io.dworkin.category.api.Category;
import io.dworkin.category.api.CategoryRequest;
import io.dworkin.category.api.CategoryService;
import io.dworkin.dao.CategoryDao;
import io.dworkin.dao.UserDao;
import io.dworkin.model.CategoryEntity;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * {@link CategoryService} implementation
 * Created by yakov on 14.03.2017.
 */
public class CategoryServiceImpl extends SecuredServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    @Inject
    public CategoryServiceImpl(CategoryDao categoryDao, UserDao userDao) {
        super(userDao);
        this.categoryDao = categoryDao;
    }

    @Override
    public ServiceCall<NotUsed, Optional<Category>> getByName(String name) {
        return notUsed -> categoryDao.getByName(name).thenApply(catOpt -> catOpt.map(cat ->
                new Category(cat.getName(), cat.getDisplayName())));
    }

    @Override
    public ServiceCall<NotUsed, List<Category>> listRoots() {
        return notUsed -> categoryDao.listRoots()
                .thenApply(cats -> cats.stream().map(itm -> new Category(itm.getName(), itm.getDisplayName()))
                        .collect(toList()));
    }

    @Override
    public ServiceCall<NotUsed, List<Category>> listByParent(String name) {
        return notUsed -> categoryDao.listByParentName(name)
                .thenApply(cats -> cats.stream().map(itm -> new Category(itm.getName(), itm.getDisplayName()))
                        .collect(toList()));
    }

    @Override
    public ServiceCall<CategoryRequest, String> create() {
        return authorized(singletonList("category-management"), createRequest ->
                categoryDao.create(new CategoryEntity(createRequest.name, createRequest.displayName), createRequest.parent,
                        createRequest.properties).thenApply(res -> "ok"));
    }

    @Override
    public ServiceCall<CategoryRequest, String> update() {
        return authorized(singletonList("category-management"), createRequest ->
                categoryDao.update(new CategoryEntity(createRequest.name, createRequest.displayName), createRequest.parent,
                        createRequest.properties).thenApply(res -> "ok"));
    }
}

