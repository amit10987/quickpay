package minibank.quickpay.handler;

import minibank.quickpay.dto.ErrorResponse;
import minibank.quickpay.util.JsonUtil;
import spark.Request;
import spark.Response;

public class QuickPayExceptionHandler {

    private QuickPayExceptionHandler() {
    }

    public static String notFound(Request req, Response res) {
        res.type("application/json");
        return JsonUtil.serialize(new ErrorResponse("Requested resource is not found."));
    }

    public static String internalServerError(Request req, Response res) {
        res.type("application/json");
        return JsonUtil.serialize(new ErrorResponse("Internal server error."));
    }

    public static void process(Exception ex, Request req, Response res) {
        QuickPayExceptionHandler.prepareErrorResponse(ex, res);
    }

    public static void prepareErrorResponse(Exception ex, Response res) {
        res.status(200);
        res.type("application/json");
        res.body(JsonUtil.serialize(new ErrorResponse(ex.getMessage())));
    }
}
