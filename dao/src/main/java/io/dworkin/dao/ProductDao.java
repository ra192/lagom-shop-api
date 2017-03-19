package io.dworkin.dao;

import io.dworkin.model.ProductEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by yakov on 19.03.2017.
 */
public interface ProductDao {
    CompletableFuture<Optional<ProductEntity>> getByCode(String code);

    CompletableFuture<List<ProductEntity>> listByCategoryNameAndPropertyValues(String category, List<List<String>> propertyValues,
                                                                               Integer first, Integer max, String orderProperty, Boolean isAsc);
}
