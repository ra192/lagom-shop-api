package io.dwornik.category.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dwornik.category.api.Category;
import io.dwornik.category.api.CategoryService;
import io.dwornik.dao.CategoryDao;

import javax.inject.Inject;
import java.util.List;
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
    public ServiceCall<NotUsed, Category> getByName(String name) {
        return notUsed -> categoryDao.getByName(name).thenApply(cat -> new Category(cat.getName(), cat.getDisplayName()));
    }

    @Override
    public ServiceCall<NotUsed, List<Category>> listByParent(String name) {
        return notUsed -> {
            if (name.equalsIgnoreCase("root"))
                return categoryDao.listByParentId(0L).thenApply(cats -> cats.stream().map(cat -> new Category(cat.getName(), cat.getDisplayName())).collect(Collectors.toList()));
            else
                return categoryDao.getByName(name).thenComposeAsync(cat -> categoryDao.listByParentId(cat.getId())
                        .thenApply(cats -> cats.stream().map(itm -> new Category(itm.getName(), itm.getDisplayName()))
                                .collect(Collectors.toList())));
        };
    }
}
