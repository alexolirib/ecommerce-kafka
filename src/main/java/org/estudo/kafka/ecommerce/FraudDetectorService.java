package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class FraudDetectorService {

    public static void main(String[] args) {
        var consumer = new KafkaConsumer<String, String>(properties());
        //informar qual é o tópico que desejo ser o consumidor
        consumer.subscribe(Collections.singleton("ECOMMERCE_NEW_ORDER"));
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if(!records.isEmpty()){
                System.out.println("Nova mensagem:  "+ records.count()+ " registros");
            }
            for (var record: records){
                System.out.println("------------------------------------------");
                System.out.println("Processing new order, checking for fraud");
                System.out.println("------------------------------------------");
                System.out.println("key - "+ record.key());
                System.out.println("value - " + record.value());
                System.out.println("partition - " + record.partition());
                System.out.println("offset - " + record.offset());
                System.out.println("------------------------------------------");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    //ignoring
                    e.printStackTrace();
                }
                //order processing

            }
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //importante informar qual é o grupo que irá consumir essa mensagem
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
        //definir um id para cada um que roda
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, FraudDetectorService.class.getSimpleName()+ " _ " +UUID.randomUUID());
//        properties.setProperty(ConsumerConfig.,)
//        properties.setProperty(ConsumerConfig.,)
//        properties.setProperty(ConsumerConfig.,)
//        properties.setProperty(ConsumerConfig.,)
//        properties.setProperty(ConsumerConfig.,)
        return properties;
    }
}
