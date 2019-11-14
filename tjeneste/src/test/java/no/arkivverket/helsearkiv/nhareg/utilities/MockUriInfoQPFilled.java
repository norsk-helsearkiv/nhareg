package no.arkivverket.helsearkiv.nhareg.utilities;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class MockUriInfoQPFilled extends MockUriInfo {
    @Override
    public MultivaluedMap<String, String> getQueryParameters() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<String, String>();
        map.add("side", "2");
        map.add("antall", "1");
        return map;
    }
}
