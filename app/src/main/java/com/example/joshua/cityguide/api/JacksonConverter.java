package com.example.joshua.cityguide.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * A {@link Converter} which uses Jackson for reading and writing entities.
 */
class JacksonConverter implements Converter {
    private static final String MIME_TYPE = "application/json; charset=UTF-8";

    private final ObjectMapper mObjectMapper;

    public JacksonConverter() {
        this(new ObjectMapper());
    }

    public JacksonConverter(ObjectMapper objectMapper) {
        this.mObjectMapper = objectMapper;
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            JavaType javaType = mObjectMapper.getTypeFactory().constructType(type);
            return mObjectMapper.readValue(body.in(), javaType);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public TypedOutput toBody(Object object) {
        try {
            String json = mObjectMapper.writeValueAsString(object);
            return new TypedByteArray(MIME_TYPE, json.getBytes("UTF-8"));
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
