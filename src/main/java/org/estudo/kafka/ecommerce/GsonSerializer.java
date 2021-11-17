package org.estudo.kafka.ecommerce;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;
//necessário implementar essa interface para criar o serializador para o kafka (Assim é possível serializar json a partir de uma classe
public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new GsonBuilder().create();

    @Override
    public byte[] serialize(String s, T o) {
        return gson.toJson(o).getBytes();
    }
}
