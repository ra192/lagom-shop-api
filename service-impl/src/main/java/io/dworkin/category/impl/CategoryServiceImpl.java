package io.dworkin.category.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dworkin.category.api.Category;
import io.dworkin.category.api.CategoryService;
import io.dworkin.dao.CategoryDao;
import io.dworkin.model.CategoryEntity;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    public ServiceCall<NotUsed, Optional<Category>> getByName(String name) {
        return notUsed -> categoryDao.getByName(name).thenApply(catOpt -> catOpt.map(cat ->
                new Category(cat.getName(), cat.getDisplayName())));
    }

    @Override
    public ServiceCall<NotUsed, List<Category>> listByParent(Optional<String> name) {
        return notUsed -> name.map(nm -> categoryDao.getByName(nm).thenApply(itm -> itm.map(CategoryEntity::getId)))
                .orElse(CompletableFuture.completedFuture(Optional.empty()))
                .thenComposeAsync(categoryDao::listByParentId)
                .thenApply(cats -> cats.stream().map(itm -> new Category(itm.getName(), itm.getDisplayName()))
                        .collect(Collectors.toList()));
    }
}
