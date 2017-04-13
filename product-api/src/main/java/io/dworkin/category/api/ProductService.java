package io.dworkin.category.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.*;
import com.lightbend.lagom.javadsl.api.transport.Method;
import io.dworkin.security.filters.CorsFilter;
import org.pcollections.PSequence;

import java.util.concurrent.CompletableFuture;

import static com.lightbend.lagom.javadsl.api.Service.*;

/**
 * Product service interface
 * Created by yakov on 19.03.2017.
 */
public interface ProductService extends Service {

    /**
     * Get products list by category and product properties
     * Example: curl -X POST -H "Content-Type:application/json" -d '{"category":"cpu", "properties":[{"propertyValues":["intel"]},{"propertyValues":["socket-1150","socket-2011"]}]}' http://localhost:9000/api/product/list
     *
     * @return products list
     */
    ServiceCall<ListFilteredRequest, PSequence<Product>> listFiltered();

    /**
     * Get products list by category and product properties
     * Example: curl -X POST -H "Content-Type:application/json" -d '{"category":"cpu", "properties":[{"name":"manufacturer","propertyValues":["intel"]}]}' http://localhost:9000/api/product/countPropertyValues
     *
     * @return products list
     */
    ServiceCall<CountPropertyValuesRequest, CountPropertyValuesResponse> countPropertyValues();

    ServiceCall<ManageProductRequest, String> create();

    ServiceCall<ManageProductRequest, String> update();

    @Override
    default Descriptor descriptor() {
        return named("product").withCalls(
                restCall(Method.POST, "/api/product/list", this::listFiltered),
                restCall(Method.OPTIONS, "/api/product/list", this::optionsRequest),
                restCall(Method.POST,"/api/product/countPropertyValues", this::countPropertyValues),
                restCall(Method.OPTIONS,"/api/product/countPropertyValues", this::optionsRequest),
                pathCall("/api/product/create", this::create),
                pathCall("/api/product/update", this::update)
        ).withHeaderFilter(new CorsFilter()).withAutoAcl(true);
    }

    default ServiceCall<NotUsed, Done> optionsRequest() {
        return notUsed -> CompletableFuture.completedFuture(Done.getInstance());
    }
}
