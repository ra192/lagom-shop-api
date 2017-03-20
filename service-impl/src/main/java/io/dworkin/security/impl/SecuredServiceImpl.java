package io.dworkin.security.impl;

import com.lightbend.lagom.javadsl.api.transport.Forbidden;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Created by yakov on 19.03.2017.
 */
public abstract class SecuredServiceImpl {

    private final UserRepository userRepository;

    protected SecuredServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected <Request, Response> ServerServiceCall<Request, Response> authenticated(Function<String, ServerServiceCall<Request, Response>> authCall) {
        return HeaderServiceCall.composeAsync(requestHeader -> {
            final CompletionStage<Optional<String>> usernameFuture = requestHeader.getHeader("token").map(token -> {
                final Optional<String> res;
                if (token.equalsIgnoreCase("secured")) res = Optional.of("ra");
                else res = Optional.empty();
                return CompletableFuture.completedFuture(res);
            }).orElse(CompletableFuture.completedFuture(Optional.empty()));

            return usernameFuture.thenApply(usernameOpt -> {
                if (usernameOpt.isPresent())
                    return authCall.apply(usernameOpt.get());
                else throw new Forbidden("Wrong token");
            });
        });
    }

    protected <Request, Response> ServerServiceCall<Request, Response> authorized(List<String> allowedRoles, ServerServiceCall<Request, Response> authCall) {
        return authenticated(username-> request->userRepository.getRoles(username).thenCompose(roles -> {
            if(roles.containsAll(allowedRoles))
            return authCall.invoke(request);
            else throw new Forbidden("Permissions denied");
        }));
    }
}
