package minibank.quickpay.handler;

import minibank.quickpay.dto.ErrorResponse;
import minibank.quickpay.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

public class QuickPayExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(QuickPayExceptionHandler.class);

    private static final String APPLICATION_JSON = "application/json";

    private QuickPayExceptionHandler() {
    }

    public static String notFound(Request req, Response res) {
        logger.error("Requested resource not found for request: {}", req.body());
        res.type(APPLICATION_JSON);
        return JsonUtil.serialize(new ErrorResponse("Requested resource is not found."));
    }

    public static String internalServerError(Request req, Response res) {
        logger.error("Internal server error for request: {}", req.body());
        res.type(APPLICATION_JSON);
        return JsonUtil.serialize(new ErrorResponse("Internal server error."));
    }

    public static void process(Exception ex, Request req, Response res) {
        logger.info("Processing exception for request: {}", req.body());
        logger.warn("Exception : {}", ex.getMessage());
        QuickPayExceptionHandler.prepareErrorResponse(ex, res);
    }

    public static void prepareErrorResponse(Exception ex, Response res) {
        res.status(200);
        res.type(APPLICATION_JSON);
        res.body(JsonUtil.serialize(new ErrorResponse(ex.getMessage())));
    }
}
