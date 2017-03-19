package io.dworkin;

import com.lightbend.lagom.javadsl.api.transport.Forbidden;
import io.dworkin.dao.UserDao;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by yakov on 19.03.2017.
 */
public abstract class SecuredServiceImpl {

    private final UserDao userDao;

    protected SecuredServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    protected <Response> CompletableFuture<Response> authenticated(String token, Function<String, CompletableFuture<Response>> authCall) {
        final CompletableFuture<Optional<String>> usernameFuture = CompletableFuture.completedFuture((token.equalsIgnoreCase("secured")) ? Optional.of("ra") : Optional.empty());
        return usernameFuture.thenComposeAsync(usrNameOpt -> {
            if (usrNameOpt.isPresent()) return userDao.getByName(usrNameOpt.get()).thenComposeAsync(usrOpts -> {
                if (usrOpts.isPresent()) return authCall.apply(usrOpts.get().getUsername());
                else throw new Forbidden("User not found");
            });
            else throw new Forbidden("Wrong token");
        });
    }

    protected <Response> CompletableFuture<Response> authorized(String token, List<String> allowedRoles, Supplier<CompletableFuture<Response>> authCall) {
        return authenticated(token,username-> userDao.getRoles(username).thenComposeAsync(roles->{
            if(roles.containsAll(allowedRoles))
                return authCall.get();
            else throw new Forbidden("Permissions denied");
        }));
    }
}
