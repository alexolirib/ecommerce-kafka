package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFuncion<T> {
    void consume(ConsumerRecord<String, T> record) throws Exception;
}
