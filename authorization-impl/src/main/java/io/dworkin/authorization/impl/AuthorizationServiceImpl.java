package io.dworkin.authorization.impl;

import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import io.dworkin.authorization.api.AuthorizationService;
import io.dworkin.security.impl.UserRepository;
import org.apache.commons.codec.binary.Base64;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Created by yakov on 21.03.2017.
 */
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;

    @Inject
    public AuthorizationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public HeaderServiceCall<NotUsed, String> authorize() {

        return (requestHeader, request) -> {
            requestHeader.getHeader("Authorization").map(auth -> {
                String[] usernameAndPass = new String(Base64.decodeBase64(auth)).split(":");

                return "ok";
            });
            ResponseHeader responseHeader = ResponseHeader.OK
                    .withHeader("Server", "Hello service");
            return CompletableFuture.completedFuture(Pair.create(responseHeader, "secured"));
        };
    }
}
