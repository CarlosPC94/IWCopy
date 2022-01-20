package iw_part2.tienda.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class HttpResponseBuilder {

    public static final Map<String, String> getErrorBodyResponse(String exceptionMessage) {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", exceptionMessage);
        return error;
    }

    public static void writeErrorResponse(HttpServletResponse response, String exceptionMessage, HttpStatus status) throws IOException {
        response.setHeader("error", exceptionMessage);
        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), getErrorBodyResponse(exceptionMessage));
    }
}
