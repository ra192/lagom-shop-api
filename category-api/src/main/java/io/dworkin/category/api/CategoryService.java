package io.dworkin.category.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dworkin.security.filters.CorsFilter;
import org.pcollections.PSequence;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * Category service interface
 * Created by yakov on 14.03.2017.
 */
public interface CategoryService extends Service {

    ServiceCall<NotUsed, Optional<Category>> getByName(String name);

    ServiceCall<NotUsed, PSequence<Category>> listRoots();

    ServiceCall<NotUsed, PSequence<Category>> listByParent(String name);

    /**
     * Create category
     * Example: curl -X POST -H "Content-Type:application/json" -H "token:lsL3mfpulYOHdYVXz3lhQwso2aDzCAjV" -d '{"name":"test1", "displayName":"test1", "parent":"pc_parts", "properties":["manufacturer","socket"]}' http://localhost:9000/api/category/create
     *
     * @return ok if success
     */
    ServiceCall<ManageCategoryRequest, String> create();

    ServiceCall<ManageCategoryRequest, String> update();

    @Override
    default Descriptor descriptor() {
        return named("category").withCalls(
                pathCall("/api/category/get/:name", this::getByName),
                pathCall("/api/category/list", this::listRoots),
                pathCall("/api/category/list/:parentName", this::listByParent),
                pathCall("/api/category/create", this::create),
                pathCall("/api/category/update", this::update)
        ).withHeaderFilter(new CorsFilter()).withAutoAcl(true);
    }
}