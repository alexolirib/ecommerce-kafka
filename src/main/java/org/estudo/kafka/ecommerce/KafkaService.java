package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class KafkaService implements Closeable {
    private final KafkaConsumer<String, String> consumer;
    private final ConsumerFuncion parse;

    public KafkaService(String groupId, String topic, ConsumerFuncion parse) {
        this.consumer = new KafkaConsumer<>(properties(groupId));
        //informar qual é o tópico que desejo ser o consumidor
        consumer.subscribe(Collections.singleton(topic));
        this.parse = parse;
    }

    public void run(){
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if(!records.isEmpty()){
                System.out.println("Nova mensagem:  "+ records.count()+ " registros");
            }
            for (var record: records){
                this.parse.consume(record);

            }
        }
    }
    private static Properties properties(String groupId) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //definir um id para cada um que roda
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, groupId + " - " + UUID.randomUUID().toString());
        //maximo de mensagem que será commitado - é criado um auto commit
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
//        properties.setProperty(ConsumerConfig.,)
//        properties.setProperty(ConsumerConfig.,)
        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
