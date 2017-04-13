package io.dworkin.product.impl;

import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.typesafe.config.Config;
import io.dworkin.category.api.*;
import io.dworkin.security.impl.SecuredServiceImpl;
import io.dworkin.security.impl.TokenRepository;
import io.dworkin.security.impl.UserRepository;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.Futures;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;

/**
 * {@link ProductService} implementation
 * Created by yakov on 19.03.2017.
 */
public class ProductServiceImpl extends SecuredServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Config config;

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Inject
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, TokenRepository tokenRepository, Config config) {
        super(userRepository, tokenRepository);
        this.productRepository = productRepository;
        this.config = config;
    }

    @Override
    public ServiceCall<ListFilteredRequest, PSequence<Product>> listFiltered() {
        return request -> {
            log.info("Product list filtered method was invoked with: {}", request);

            final Integer first = (request.first != null) ? request.first : 0;
            final Integer max = (request.max != null) ? request.max : first + config.getInt("product.list.max.default");
            final String orderBy = (request.orderBy != null) ? request.orderBy : "displayName";
            final Boolean isAsk = (request.isAsc != null) ? request.isAsc : true;

            return productRepository.listByCategoryNameAndPropertyValues(request.category, TreePVector.from(
                    request.properties.stream().map(propItm -> propItm.propertyValues).collect(toList())), first, max, orderBy, isAsk);
        };
    }

    @Override
    public ServiceCall<CountPropertyValuesRequest, CountPropertyValuesResponse> countPropertyValues() {
        return request -> {
            log.info("Product count name values method was invoked with: {}", request);

            final PSequence<Pair<String, PSequence<String>>> propertyValues = TreePVector.from(request.properties.stream()
                    .map(itm -> new Pair<>(itm.name, itm.propertyValues)).collect(toList()));

            final CompletionStage<PSequence<PropertyWithCount>> propertyValuesStage =
                    productRepository.countPropertyValuesByCategoryIdAndFilter(request.category, null, propertyValues);

            final CompletionStage<List<PSequence<PropertyWithCount>>> additionalPropertyValuesStages =
                    Futures.sequence(propertyValues.stream().map(pair -> productRepository.countPropertyValuesByCategoryIdAndFilter(request.category, pair.first(), propertyValues)).collect(toList()));

            return propertyValuesStage.thenCombine(additionalPropertyValuesStages, Pair::new).thenApply(pair ->
                    new CountPropertyValuesResponse(pair.first(), TreePVector.from(pair.second().stream()
                            .map(res -> res.get(0)).collect(toList()))));
        };
    }

    @Override
    public ServiceCall<ManageProductRequest, String> create() {
        return authorized(Arrays.asList("manage-product"), createRequest -> {
            log.info("Create product method was invoked with: {}", createRequest);

            return productRepository.create(new Product(createRequest.code, createRequest.displayName,
                            createRequest.price, createRequest.description, createRequest.imageUrl), createRequest.category,
                    createRequest.propertyValues).thenApply(result -> "ok");
        });
    }

    @Override
    public ServiceCall<ManageProductRequest, String> update() {
        return authorized(Arrays.asList("manage-product"), updateRequest -> {
            log.info("Update product method was invoked with: {}", updateRequest);

            return productRepository.update(new Product(updateRequest.code, updateRequest.displayName,
                            updateRequest.price, updateRequest.description, updateRequest.imageUrl), updateRequest.category,
                    updateRequest.propertyValues).thenApply(result -> "ok");
        });
    }
}
