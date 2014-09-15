package com.zanthrash.utils

import org.springframework.http.HttpStatus

/**
 * Created by zanthrash on 9/14/14.
 */
class RestUtil {

    public static boolean hasError(HttpStatus status) {
        HttpStatus.Series series = status.series()
        return series in [HttpStatus.Series.CLIENT_ERROR, HttpStatus.Series.SERVER_ERROR]
    }
}
