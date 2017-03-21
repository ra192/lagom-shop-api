package io.dworkin.authorization.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.pathCall;

/**
 * Created by yakov on 21.03.2017.
 */
public interface AuthorizationService extends Service {

    /**
     * curl -H "Authorization: Basic QWxhZGRpbjpPcGVuU2VzYW1l" http://localhost:9000/api/authorization/token
     * @return
     */
    ServiceCall<NotUsed,String>authorize();

    @Override
    default Descriptor descriptor() {
        return Service.named("authorization").withCalls(
                pathCall("/api/authorization/token",this::authorize)
        ).withAutoAcl(true);
    }
}
