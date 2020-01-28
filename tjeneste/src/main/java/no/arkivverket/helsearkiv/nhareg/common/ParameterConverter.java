package no.arkivverket.helsearkiv.nhareg.common;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

public class ParameterConverter {

    /**
     * Filter out empty entries and convert to map.
     * @param queryParameters HTTP query parameters passed to the endpoint.
     * @return HashMap all empty params removed. Empty map if queryParameters is null.
     */
    public static Map<String, String> multivaluedToMap(@NotNull final MultivaluedMap<String, String> queryParameters) {
        final Map<String, String> mappedQueries = new HashMap<>();
        
        // Convert to map
        queryParameters.forEach((key, value) -> {
            if (value != null && value.size() > 0) {
                mappedQueries.put(key, value.get(0));
            }
        });
        
        // Remove all empty entries
        mappedQueries.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().isEmpty());
        
        return mappedQueries;
    }
}