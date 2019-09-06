package com.ggiovannini.moneytransfers.utils;

import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

public class LoggerUtils {

    /**
     * Build a convenient string from the Request to be logged
     *
     * @param request
     * @return
     */
    public static String requestInfoToString(Request request) {
        StringBuilder sb = new StringBuilder();
        sb.append("REQUEST: ");
        sb.append(request.requestMethod());
        sb.append(" " + request.url());
        sb.append(StringUtils.isBlank(request.body()) ? "" : " " + request.body());
        return sb.toString();
    }

    /**
     * Build a convenient string from the Response to be logged
     *
     * @param response
     * @return
     */
    public static String responseInfoToString(Response response) {
        StringBuilder sb = new StringBuilder();
        sb.append("RESPONSE: ");
        sb.append(response.status());
        sb.append(StringUtils.isBlank(response.body()) ? "" : " " + response.body());
        return sb.toString();
    }

    /**
     * Build a convenient string from an error status code and an error message to be logged
     *
     * @param status
     * @param message
     * @return
     */
    public static String responseErrorToString(int status, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("RESPONSE: ");
        sb.append(status);
        sb.append(StringUtils.isBlank(message) ? "" : " " + message);
        return sb.toString();
    }
}
