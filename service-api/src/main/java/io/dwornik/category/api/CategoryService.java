package io.dwornik.category.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.List;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * Category service interface
 * Created by yakov on 14.03.2017.
 */
public interface CategoryService extends Service {

    ServiceCall<NotUsed, Category> getByName(String name);

    ServiceCall<NotUsed, List<Category>> listByParent(String name);

    @Override
    default Descriptor descriptor(){
        // @formatter:off
        return named("category").withCalls(
            pathCall("/api/category/get/:name",this::getByName),
            pathCall("/api/category/listByParent/:name",this::listByParent)
        ).withAutoAcl(true);
        // @formatter:on
    }
}
