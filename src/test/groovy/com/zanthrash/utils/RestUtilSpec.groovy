package com.zanthrash.utils

import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll

class RestUtilSpec extends Specification {


    @Unroll("#httpStatus is error == #isError ")
    def "check know potential HttpStatus for if error"() {
        expect:
           RestUtil.hasError(httpStatus) == isError

        where:
            httpStatus                      | isError
            HttpStatus.OK                   | false
            HttpStatus.NOT_FOUND            | true
            HttpStatus.UNAUTHORIZED         | true
            HttpStatus.BAD_REQUEST          | true
            HttpStatus.FORBIDDEN            | true
            HttpStatus.UNPROCESSABLE_ENTITY | true

    }
}
