package io.dworkin.category.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dworkin.product.api.Category;
import io.dworkin.product.api.ManageCategoryRequest;
import io.dworkin.product.api.CategoryService;
import io.dworkin.security.impl.SecuredServiceImpl;
import io.dworkin.security.impl.TokenRepository;
import io.dworkin.security.impl.UserRepository;
import org.pcollections.PSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

import static java.util.Collections.singletonList;

/**
 * {@link CategoryService} implementation
 * Created by yakov on 14.03.2017.
 */
public class CategoryServiceImpl extends SecuredServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TokenRepository tokenRepository;

    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Inject
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository, TokenRepository tokenRepository) {
        super(userRepository, tokenRepository);
        this.categoryRepository = categoryRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public ServiceCall<NotUsed, Optional<Category>> getByName(String name) {
        return notUsed -> {
            log.info("Category get by name method was invoked with: {}",name);

            return categoryRepository.getByName(name);
        };
    }

    @Override
    public ServiceCall<NotUsed, PSequence<Category>> listRoots() {
        return notUsed -> {
            log.info("Category list roots method was invoked");

            return categoryRepository.listRoots();
        };
    }

    @Override
    public ServiceCall<NotUsed, PSequence<Category>> listByParent(String name) {
        return notUsed -> {
            log.info("Category list by parent method was invoked with parent name {}", name);

            return categoryRepository.listByParentName(name);
        };
    }

    @Override
    public ServiceCall<ManageCategoryRequest, String> create() {
        return authorized(singletonList("category-management"), createRequest -> {
            log.info("Create category method was invoked with params: {}", createRequest);

            return categoryRepository.create(new Category(createRequest.name, createRequest.displayName), createRequest.parent,
                    createRequest.properties).thenApply(res -> "ok");
        });
    }

    @Override
    public ServiceCall<ManageCategoryRequest, String> update() {
        return authorized(singletonList("category-management"), updateRequest -> {
            log.info("Update category method was invoked with params: {}", updateRequest);

            return categoryRepository.update(new Category(updateRequest.name, updateRequest.displayName), updateRequest.parent,
                    updateRequest.properties).thenApply(res -> "ok");
        });
    }
}

