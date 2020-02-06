package no.arkivverket.helsearkiv.nhareg.domene.xml;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.dataformat.xml.ser.XmlBeanSerializerModifier;
import no.arkivverket.helsearkiv.nhareg.domene.additionalinfo.*;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.CV;
import no.arkivverket.helsearkiv.nhareg.domene.transfer.DateOrYear;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class EmptyPropertySerializerModifier extends XmlBeanSerializerModifier {

    private static final List<Class> CLASSES = Arrays.asList(
        AdditionalInfo.class,
        CV.class,
        Episode.class,
        Initiative.class,
        Procedure.class,
        Unit.class
    );

    private static final List<Class> EMPTY_CLASSES = Arrays.asList(
        Boolean.class,
        DateOrYear.class,
        Integer.class,
        LocalDate.class,
        LocalDateTime.class,
        String.class
    );
    
    /**
     * Assigns NullSerializer for different classes. Lets us handle null values when marshalling XML based on the 
     * type of class that is being marshalled.
     */
    @Override
    public List<BeanPropertyWriter> changeProperties(final SerializationConfig config,
                                                     final BeanDescription beanDesc,
                                                     final List<BeanPropertyWriter> beanProperties) {
        // Fixes XML attributes
        super.changeProperties(config, beanDesc, beanProperties);
        
        for (BeanPropertyWriter propertyWriter : beanProperties) {
            final JavaType javaType  = propertyWriter.getType();

            // All the classes in EMPTY_CLASSES are assigned a serializer which prints an empty string.
            for (Class c: EMPTY_CLASSES) {
                if (javaType.isTypeOrSubTypeOf(c)) {
                    if ("sikkermors".equals(propertyWriter.getName())) {
                        // Special case where null should write 1.
                        propertyWriter.assignNullSerializer(DeathDateSerializer.INSTANCE);
                        break;
                    } else {
                        propertyWriter.assignNullSerializer(NullStringSerializer.INSTANCE);
                        break;
                    }
                }
            }

            // All the classes in CLASSES, or subclasses of them, are assigned a generic serializer which calls an empty constructor on the class.
            for (Class c : CLASSES) {
                if (javaType.isTypeOrSubTypeOf(c)) {
                    final ObjectSerializer serializer = new ObjectSerializer();
                    serializer.settClass(c);
                    propertyWriter.assignNullSerializer(serializer);
                    break;
                }
            }
        }

        return beanProperties;
    }

}