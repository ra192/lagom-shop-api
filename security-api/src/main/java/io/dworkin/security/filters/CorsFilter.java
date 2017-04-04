package io.dworkin.security.filters;

import com.lightbend.lagom.javadsl.api.transport.HeaderFilter;
import com.lightbend.lagom.javadsl.api.transport.RequestHeader;
import com.lightbend.lagom.javadsl.api.transport.ResponseHeader;

/**
 * Created by yakov on 29.03.2017.
 */
public class CorsFilter implements HeaderFilter {
    @Override
    public RequestHeader transformClientRequest(RequestHeader requestHeader) {
        return requestHeader;
    }

    @Override
    public RequestHeader transformServerRequest(RequestHeader requestHeader) {
        return requestHeader;
    }

    @Override
    public ResponseHeader transformServerResponse(ResponseHeader responseHeader, RequestHeader requestHeader) {
        return responseHeader.
                withHeader("Access-Control-Allow-Origin", "*")
                .withHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    }

    @Override
    public ResponseHeader transformClientResponse(ResponseHeader responseHeader, RequestHeader requestHeader) {
        return responseHeader;
    }
}
