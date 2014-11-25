package fr.vsct.quicky.jmx.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.money.Money;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MoneyJacksonModule extends SimpleModule {

    public MoneyJacksonModule() {
        super("money jackson module", Version.unknownVersion());
        this.addSerializer(new JsonMoneySerializer());
        this.addDeserializer(Money.class, new JsonMoneyDeserializer());
    }

    public static class JsonMoneyDeserializer extends StdDeserializer<Money> {

        protected JsonMoneyDeserializer() {
            super(Money.class);
        }

        @Override
        public Money deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return Money.parse(jp.getValueAsString());
        }
    }

    public static class JsonMoneySerializer extends StdSerializer<Money> {

        protected JsonMoneySerializer() {
            super(Money.class);
        }

        @Override
        public void serialize(Money value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            //Write the amount in cents
            jgen.writeString(value.toString());
        }
    }
}
