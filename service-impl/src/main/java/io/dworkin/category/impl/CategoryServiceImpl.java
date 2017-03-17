package io.dworkin.category.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.typesafe.conductr.lib.java.Tuple;
import io.dworkin.category.api.Category;
import io.dworkin.category.api.CategoryService;
import io.dworkin.category.api.CreateCategory;
import io.dworkin.dao.CategoryDao;
import io.dworkin.dao.PropertyDao;
import io.dworkin.model.CategoryEntity;
import io.dworkin.model.PropertyEntity;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * {@link CategoryService} implementation
 * Created by yakov on 14.03.2017.
 */
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;
    private final PropertyDao propertyDao;

    @Inject
    public CategoryServiceImpl(CategoryDao categoryDao, PropertyDao propertyDao) {
        this.categoryDao = categoryDao;
        this.propertyDao = propertyDao;
    }

    @Override
    public ServiceCall<NotUsed, Optional<Category>> getByName(String name) {
        return notUsed -> categoryDao.getByName(name).thenApply(catOpt -> catOpt.map(cat ->
                new Category(cat.getName(), cat.getDisplayName())));
    }

    @Override
    public ServiceCall<NotUsed, List<Category>> listByParent(String name) {
        return notUsed -> categoryDao.getByName(name).thenApply(catOpt -> catOpt.map(CategoryEntity::getId).orElse(null))
                .thenComposeAsync(categoryDao::listByParentId)
                .thenApply(cats -> cats.stream().map(itm -> new Category(itm.getName(), itm.getDisplayName()))
                        .collect(toList()));
    }

    @Override
    public ServiceCall<CreateCategory, String> create() {
        return createRequest -> {
            final CompletableFuture<Long> createFuture = categoryDao.getByName(createRequest.parent)
                    .thenApply(catOpt -> catOpt.map(CategoryEntity::getId).orElse(null))
                    .thenComposeAsync(parentId -> categoryDao.create(new CategoryEntity(null, createRequest.name,
                            createRequest.displayName, parentId)));

            final List<CompletableFuture<Optional<Long>>> propertyFuturesList = createRequest.properties.stream()
                    .map(nm -> propertyDao.getByName(nm).thenApply(propOpt -> propOpt.map(PropertyEntity::getId))).collect(toList());
            CompletableFuture<Set<Optional<Long>>> propertyIdsFuture = allOf(propertyFuturesList.toArray(
                    new CompletableFuture[propertyFuturesList.size()]))
                    .thenApply(v -> propertyFuturesList.stream().map(CompletableFuture::join).collect(toSet()));

            return createFuture.thenCombine(propertyIdsFuture, (id, propOptIds) -> {
                final Set<Long> popIds = propOptIds.stream().filter(Optional::isPresent).map(Optional::get).collect(toSet());
                return new Tuple<>(id, popIds);
            }).thenComposeAsync(pair -> categoryDao.updateProperties(pair._1, pair._2)).thenApply(res -> "ok");
        };
    }
}

