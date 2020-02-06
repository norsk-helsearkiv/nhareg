package no.arkivverket.helsearkiv.nhareg.domene.xml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DeathDateSerializer extends JsonSerializer<Object> {
    
    static final JsonSerializer<Object> INSTANCE = new DeathDateSerializer();
    
    @Override
    public void serialize(final Object value, final JsonGenerator gen, final SerializerProvider serializers)
        throws IOException {
        gen.writeString("1");
    }
    
}