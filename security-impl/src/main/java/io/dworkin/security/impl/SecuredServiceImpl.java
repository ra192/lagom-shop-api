package io.dworkin.security.impl;

import com.lightbend.lagom.javadsl.api.transport.Forbidden;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Created by yakov on 19.03.2017.
 */
public abstract class SecuredServiceImpl {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    protected SecuredServiceImpl(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    protected <Request, Response> ServerServiceCall<Request, Response> authenticated(Function<String, ServerServiceCall<Request, Response>> authCall) {
        return HeaderServiceCall.composeAsync(requestHeader -> {
            final CompletionStage<Optional<TokenEntity>> tokenFuture = requestHeader.getHeader("token")
                    .map(tokenRepository::getUsernameByToken).orElseThrow(() -> new Forbidden("Token header is not specified"));

            return tokenFuture.thenApply(tokenOpt -> {
                if (tokenOpt.isPresent() && tokenOpt.get().getValidTo().after(new Date()))
                    return authCall.apply(tokenOpt.get().getUsername());
                else throw new Forbidden("Wrong token");
            });
        });
    }

    protected <Request, Response> ServerServiceCall<Request, Response> authorized(List<String> allowedRoles, ServerServiceCall<Request, Response> authCall) {
        return authenticated(username -> request -> userRepository.getRoles(username).thenCompose(roles -> {
            if (roles.containsAll(allowedRoles))
                return authCall.invoke(request);
            else throw new Forbidden("Permissions denied");
        }));
    }
}
