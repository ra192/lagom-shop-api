package io.dworkin.authorization.impl;

import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.transport.Forbidden;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import io.dworkin.authorization.api.AuthorizationService;
import io.dworkin.security.impl.TokenEntity;
import io.dworkin.security.impl.TokenRepository;
import io.dworkin.security.impl.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.Date;

/**
 * Created by yakov on 21.03.2017.
 */
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Inject
    public AuthorizationServiceImpl(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public HeaderServiceCall<NotUsed, String> authorize() {

        return (requestHeader, request) -> {
            ResponseHeader responseHeader = ResponseHeader.OK
                    .withHeader("Server", "Hello service");

            return requestHeader.getHeader("Authorization").map(auth -> {
                String[] usernameAndPass = new String(Base64.decodeBase64(auth.replaceFirst("Basic", "").trim())).split(":");
                if (usernameAndPass.length < 2 || StringUtils.isBlank(usernameAndPass[0]) || StringUtils.isBlank(usernameAndPass[1]))
                    throw new Forbidden("Authorization header is not valid");
                return userRepository.getByName(usernameAndPass[0]).thenComposeAsync(userOpt -> userOpt.map(user -> {
                    String hashedPass = DigestUtils.sha256Hex(usernameAndPass[1]);
                    if (hashedPass.equalsIgnoreCase(user.getPassword())) {
                        final String token = RandomStringUtils.randomAlphanumeric(32);
                        return tokenRepository.createToken(new TokenEntity(user.getUsername(), token, new Date()))
                                .thenApply(res -> Pair.create(responseHeader, token));
                    } else
                        throw new Forbidden("wrong password");
                }).orElseThrow(() -> new Forbidden("user not found")));
            }).orElseThrow(() -> new Forbidden("Authorization header is not specified"));
        };
    }
}
