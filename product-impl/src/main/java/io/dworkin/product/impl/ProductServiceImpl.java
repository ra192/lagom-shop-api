package io.dworkin.product.impl;

import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.typesafe.config.Config;
import io.dworkin.product.api.ListFilteredRequest;
import io.dworkin.product.api.Product;
import io.dworkin.product.api.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * {@link ProductService} implementation
 * Created by yakov on 19.03.2017.
 */
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Config config;

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Inject
    public ProductServiceImpl(ProductRepository productRepository, Config config) {
        this.productRepository = productRepository;
        this.config = config;
    }

    @Override
    public ServiceCall<ListFilteredRequest, List<Product>> listFiltered() {
        return request -> {
            log.info("Product list filtered method was invoked with: {}", request);

            final Integer first = (request.first != null) ? request.first : 0;
            final Integer max = (request.max != null) ? request.max : first + config.getInt("product.list.max.default");
            final String orderBy = (request.orderBy != null) ? request.orderBy : "displayName";
            final Boolean isAsk = (request.isAsc != null) ? request.isAsc : true;

            return productRepository.listByCategoryNameAndPropertyValues(request.category, request.properties, first, max, orderBy, isAsk)
                    .thenApply(products -> products.stream().map(itm ->
                            new Product(itm.getCode(), itm.getDisplayName(), itm.getPrice(), itm.getDescription(), itm.getImageUrl()))
                            .collect(toList()));
        };
    }
}
