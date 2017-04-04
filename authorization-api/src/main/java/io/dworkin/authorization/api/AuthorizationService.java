package io.dworkin.authorization.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dworkin.security.filters.CorsFilter;

import java.util.concurrent.CompletableFuture;

import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.GET;
import static com.lightbend.lagom.javadsl.api.transport.Method.OPTIONS;

/**
 * Created by yakov on 21.03.2017.
 */
public interface AuthorizationService extends Service {

    /**
     * curl -H "Authorization: Basic QWxhZGRpbjpPcGVuU2VzYW1l" http://localhost:9000/api/authorization/token
     * for Aladdin:OpenSesame
     * @return
     */
    ServiceCall<NotUsed,String>authorize();

    @Override
    default Descriptor descriptor() {
        return Service.named("authorization").withCalls(
                restCall(GET,"/api/authorization/token",this::authorize),
                restCall(OPTIONS,"/api/authorization/token",this::optionsRequest)
        ).withHeaderFilter(new CorsFilter()).withAutoAcl(true);
    }

    default ServiceCall<NotUsed, Done> optionsRequest() {
        return notUsed -> CompletableFuture.completedFuture(Done.getInstance());
    }
}
