package io.dworkin.product.impl;

import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.typesafe.config.Config;
import io.dworkin.product.api.*;
import io.dworkin.product.api.CountPropertyValueResponse.CountPropertyItem;
import io.dworkin.product.api.CountPropertyValueResponse.CountPropertyValueItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.Futures;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

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

    @Override
    public ServiceCall<CountPropertyValueRequest, CountPropertyValueResponse> countPropertyValues() {
        return request -> {
            log.info("Product count property values method was invoked with: {}", request);

            final List<Pair<String, List<String>>> propertyValues = request.properties.stream()
                    .map(itm -> new Pair<>(itm.property, itm.propertyValues)).collect(toList());

            final CompletionStage<List<ProductRepository.PropertyWithCount>> propertyValuesStage =
                    productRepository.countPropertyValuesByCategoryIdAndFilter(request.category, null, propertyValues);

            final CompletionStage<List<List<ProductRepository.PropertyWithCount>>> additionalPropertyValuesStages =
                    Futures.sequence(propertyValues.stream().map(pair -> productRepository
                            .countPropertyValuesByCategoryIdAndFilter(request.category, pair.first(), propertyValues)).collect(toList()));

            return propertyValuesStage.thenCombine(additionalPropertyValuesStages, Pair::new).thenApply(pair ->
                    new CountPropertyValueResponse(pair.first().stream().map(propItm ->
                            new CountPropertyItem(propItm.name, propItm.diaplayName, propItm.propertyValues.stream().map(propValItm ->
                                    new CountPropertyValueItem(propValItm.name, propValItm.diaplayName, propValItm.count))
                                    .collect(toList()))).collect(toList()),
                            pair.second().stream().map(addPropItms -> addPropItms.stream().map(addPropItm ->
                                    new CountPropertyItem(addPropItm.name, addPropItm.diaplayName,
                                            addPropItm.propertyValues.stream().map(addPropValItm ->
                                                    new CountPropertyValueItem(addPropValItm.name, addPropValItm.diaplayName, addPropValItm.count))
                                                    .collect(toList()))).collect(toList())).collect(toList())));
        };
    }
}
