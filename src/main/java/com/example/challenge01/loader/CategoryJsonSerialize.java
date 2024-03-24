package com.example.challenge01.loader;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CategoryJsonSerialize extends StdSerializer<Category> {
    private static final long serialVersionUID = 1L;


    public CategoryJsonSerialize() {
        this(null);
    }

    protected CategoryJsonSerialize(Class<Category> t) {
        super(t);
    }


    @Override
    public void serialize(Category category, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(category.getName());
    }
}