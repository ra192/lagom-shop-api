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

    ServiceCall<NotUsed, List<Category>> listByParent(String name);

    ServiceCall<CreateCategory, String> create();

    @Override
    default Descriptor descriptor() {
        return named("category").withCalls(
                pathCall("/api/category/get/:name",this::getByName),
                pathCall("/api/category/list/:parentName",this::listByParent)
        ).withAutoAcl(true);
    }
}