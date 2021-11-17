package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //producer
        var producer = new KafkaProducer<String, String>(properties());
        for(var i=0 ; i<100 ; i++) {
            var key = UUID.randomUUID().toString();
            var value = key + " 10992310298,3232,44";
            var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", key, value);
            //funciona de forma assyncrono
//        producer.send(record).get();
            //criado um callBack para conseguir esperar o retorno
            Callback callback = (data, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                System.out.println("Sucesso enviado " + data.topic() + ":::partition " + data.partition() + "/ offset" + data.offset() + "/ timestamp " + data.timestamp());
            };
            producer.send(record, callback).get();

            var emailKey = "email";
            var emailValue = "welcome to value email";
            var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", emailKey, emailValue);
            producer.send(emailRecord, callback).get();

        }

    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        //key
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //value
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
}
