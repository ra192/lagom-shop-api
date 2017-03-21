package io.dworkin.category.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dworkin.product.api.Category;
import io.dworkin.product.api.CategoryRequest;
import io.dworkin.product.api.CategoryService;
import io.dworkin.security.impl.SecuredServiceImpl;
import io.dworkin.security.impl.TokenRepository;
import io.dworkin.security.impl.UserRepository;

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

    private final CategoryRepository categoryRepository;
    private final TokenRepository tokenRepository;

    @Inject
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository, TokenRepository tokenRepository) {
        super(userRepository, tokenRepository);
        this.categoryRepository = categoryRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public ServiceCall<NotUsed, Optional<Category>> getByName(String name) {
        return notUsed -> categoryRepository.getByName(name).thenApply(catOpt -> catOpt.map(cat ->
                new Category(cat.getName(), cat.getDisplayName())));
    }

    @Override
    public ServiceCall<NotUsed, List<Category>> listRoots() {
        return notUsed -> categoryRepository.listRoots()
                .thenApply(cats -> cats.stream().map(itm -> new Category(itm.getName(), itm.getDisplayName()))
                        .collect(toList()));
    }

    @Override
    public ServiceCall<NotUsed, List<Category>> listByParent(String name) {
        return notUsed -> categoryRepository.listByParentName(name)
                .thenApply(cats -> cats.stream().map(itm -> new Category(itm.getName(), itm.getDisplayName()))
                        .collect(toList()));
    }

    @Override
    public ServiceCall<CategoryRequest, String> create() {
        return authorized(singletonList("category-management"), createRequest ->
                categoryRepository.create(new CategoryEntity(createRequest.name, createRequest.displayName), createRequest.parent,
                        createRequest.properties).thenApply(res -> "ok"));
    }

    @Override
    public ServiceCall<CategoryRequest, String> update() {
        return authorized(singletonList("category-management"), createRequest ->
                categoryRepository.update(new CategoryEntity(createRequest.name, createRequest.displayName), createRequest.parent,
                        createRequest.properties).thenApply(res -> "ok"));
    }
}

