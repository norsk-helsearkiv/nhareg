package no.arkivverket.helsearkiv.nhareg.domene.xml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ObjectSerializer<T> extends JsonSerializer<T> {

    private Class<T> tClass;

    public void settClass(final Class<T> tClass) {
        this.tClass = tClass;
    }

    /**
     * Initializes null values by calling their empty constructor.
     */
    @Override
    public void serialize(final T value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        try {
            gen.writeObject(tClass.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
}