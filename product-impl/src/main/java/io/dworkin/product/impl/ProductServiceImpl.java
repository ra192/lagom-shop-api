package io.dworkin.product.impl;

import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.typesafe.config.Config;
import io.dworkin.category.api.*;
import io.dworkin.property.api.Property;
import io.dworkin.property.api.PropertyService;
import io.dworkin.property.api.PropertyValue;
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
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;

/**
 * {@link ProductService} implementation
 * Created by yakov on 19.03.2017.
 */
public class ProductServiceImpl extends SecuredServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final PropertyService propertyService;
    private final Config config;

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Inject
    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, TokenRepository tokenRepository, PropertyService propertyService, Config config) {
        super(userRepository, tokenRepository);
        this.productRepository = productRepository;
        this.propertyService = propertyService;
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

            final CompletionStage<List<Pair<Optional<Property>, List<Optional<PropertyValue>>>>> selectedPropertiesStages =
                    Futures.sequence(request.properties.stream().map(prop -> propertyService.getByName(prop.name).invoke()
                            .thenCombine(Futures.sequence(prop.propertyValues.stream().map(valName ->
                                    propertyService.getPropertyValueByName(valName).invoke()).collect(toList())), Pair::new))
                            .collect(toList()));

            return propertyValuesStage.thenCombine(additionalPropertyValuesStages, Pair::new)
                    .thenCombine(selectedPropertiesStages, Pair::new).thenApply(result ->
                            new CountPropertyValuesResponse(result.first().first(), TreePVector.from(result.first().second().stream()
                                    .map(res -> res.get(0)).collect(toList())), TreePVector.from(result.second().stream()
                                    .map(res -> new PropertyWithCount(res.first().get().getName(), res.first().get().getDisplayName(),
                                            TreePVector.from(res.second().stream().map(itm ->
                                                    new PropertyWithCount.PropertyValueWithCount(itm.get().getName(),
                                                            itm.get().getDisplayName(), 0L)).collect(toList()))))
                                    .collect(toList()))));
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
