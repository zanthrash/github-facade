package com.zanthrash.config

import com.zanthrash.utils.RestUtil
import groovy.util.logging.Log
import groovy.util.logging.Slf4j
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler


/**
 * This is to used to replace the DefaultResponseErrorHandler used in
 * RestTemplate.
 *
 * DefaultResponseErrorHandler throws an Exception which prevents us form
 * mapping the error to a domain object.
 */
@Component
@Slf4j
class GitHubErrorHandler implements ResponseErrorHandler{

    @Override
    boolean hasError(ClientHttpResponse response) throws IOException {
        RestUtil.hasError(response.statusCode)
    }

    @Override
    void handleError(ClientHttpResponse response) throws IOException {
        log.error("Response Error: {}, {}", response.statusCode, response.statusText)
    }
}
