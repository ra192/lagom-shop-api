package io.dworkin.product.impl;

import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.typesafe.config.Config;
import io.dworkin.dao.ProductDao;
import io.dworkin.product.api.ListFilteredRequest;
import io.dworkin.product.api.Product;
import io.dworkin.product.api.ProductService;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * {@link ProductService} implementation
 * Created by yakov on 19.03.2017.
 */
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final Config config;

    @Inject
    public ProductServiceImpl(ProductDao productDao, Config config) {
        this.productDao = productDao;
        this.config = config;
    }

    @Override
    public ServiceCall<ListFilteredRequest, List<Product>> listFiltered() {
        return request -> {

            final Integer first = (request.first != null) ? request.first : 0;
            final Integer max = (request.max != null) ? request.max : first + config.getInt("product.list.max.default");
            final String orderBy = (request.orderBy != null) ? request.orderBy : "displayName";
            final Boolean isAsk = (request.isAsc != null) ? request.isAsc : true;

            return productDao.listByCategoryNameAndPropertyValues(request.category, request.properties, first, max, orderBy, isAsk)
                    .thenApply(products -> products.stream().map(itm ->
                            new Product(itm.getCode(), itm.getDisplayName(), itm.getPrice(), itm.getDescription(), itm.getImageUrl()))
                            .collect(toList()));
        };
    }
}
