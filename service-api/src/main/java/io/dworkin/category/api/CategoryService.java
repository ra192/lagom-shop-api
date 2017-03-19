package io.dworkin.category.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.*;

import static com.lightbend.lagom.javadsl.api.Service.*;

import java.util.List;
import java.util.Optional;

/**
 * Category service interface
 * Created by yakov on 14.03.2017.
 */
public interface CategoryService extends Service {

    ServiceCall<NotUsed, Optional<Category>> getByName(String name);

    ServiceCall<NotUsed, List<Category>> listRoots();

    ServiceCall<NotUsed, List<Category>> listByParent(String name);

    /**
     * Create category
     * Example: curl -X POST -H "Content-Type:application/json" -d '{"name":"test1", "displayName":"test1", "parent":"pc_parts", "properties":["manufacturer","socket"]}' http://localhost:9000/api/category/create
     *
     * @return ok if success
     */
    ServiceCall<CategoryRequest, String> create();

    ServiceCall<CategoryRequest, String> update();

    @Override
    default Descriptor descriptor() {
        return named("category").withCalls(
                pathCall("/api/category/get/:name", this::getByName),
                pathCall("/api/category/list", this::listRoots),
                pathCall("/api/category/list/:parentName", this::listByParent),
                pathCall("/api/category/create", this::create),
                pathCall("/api/category/update", this::update)
        ).withAutoAcl(true);
    }
}