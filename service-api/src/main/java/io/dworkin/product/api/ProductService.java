package io.dworkin.product.api;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.*;

import java.util.List;

import static com.lightbend.lagom.javadsl.api.Service.*;

/**
 * Product service interface
 * Created by yakov on 19.03.2017.
 */
public interface ProductService extends Service {

    /**
     * Get products list by category and product properties
     * Example: curl -X POST -H "Content-Type:application/json" -d '{"category":"cpu", "properties":[["intel"],["socket-1150","socket-2011"]]}' http://localhost:9000/api/product/list
     * @return products list
     */
    ServiceCall<ListFilteredRequest, List<Product>> listFiltered();

    @Override
    default Descriptor descriptor() {
        return named("product").withCalls(
                pathCall("/api/product/list", this::listFiltered)
        ).withAutoAcl(true);
    }
}
