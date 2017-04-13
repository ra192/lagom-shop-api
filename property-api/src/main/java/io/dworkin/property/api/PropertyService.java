package io.dworkin.property.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dworkin.security.filters.CorsFilter;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.*;

/**
 * Created by yakov on 13.04.2017.
 */
public interface PropertyService extends Service {

    ServiceCall<NotUsed, Optional<PropertyValue>> getPropertyValueByName(String name);

    @Override
    default Descriptor descriptor() {
        return named("property").withCalls(
                pathCall("/api/property/getValue/:name", this::getPropertyValueByName)
        ).withHeaderFilter(new CorsFilter()).withAutoAcl(true);
    }
}
