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
        return notUsed -> {
            if (!name.isPresent())
                return categoryDao.listByParentId(Optional.empty()).thenApply(cats -> cats.stream().map(cat -> new Category(cat.getName(), cat.getDisplayName())).collect(Collectors.toList()));
            else
                return categoryDao.getByName(name.get()).thenComposeAsync(catOpt -> categoryDao.listByParentId(catOpt.map(CategoryEntity::getId))
                        .thenApply(cats -> cats.stream().map(itm -> new Category(itm.getName(), itm.getDisplayName()))
                                .collect(Collectors.toList())));
        };
    }
}
