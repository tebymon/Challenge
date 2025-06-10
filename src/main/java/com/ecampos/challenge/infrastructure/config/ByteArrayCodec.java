package com.ecampos.challenge.infrastructure.config;

import io.lettuce.core.codec.RedisCodec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
public class ByteArrayCodec implements RedisCodec<String, byte[]> {

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return StandardCharsets.UTF_8.decode(bytes).toString();
    }

    @Override
    public byte[] decodeValue(ByteBuffer bytes) {
        byte[] b = new byte[bytes.remaining()];
        bytes.get(b);
        return b;
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return StandardCharsets.UTF_8.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(byte[] value) {
        return ByteBuffer.wrap(value);
    }

}
