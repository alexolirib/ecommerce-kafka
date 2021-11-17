package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerFuncion {
    void consume(ConsumerRecord<String, String> record);
}
