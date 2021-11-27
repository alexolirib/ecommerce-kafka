package org.estudo.kafka.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    //o T é para permitir deserializar qual é tipo (iremos focar mais no json)
    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFuncion parse;

    public KafkaService(String groupId, String topic, ConsumerFuncion parse, Class<T> type, Map<String, String> properties) {
        this(groupId, parse, type, properties);
        consumer.subscribe(Collections.singleton(topic));
    }

    public KafkaService(String groupId, Pattern topic, ConsumerFuncion parse, Class<T> type, Map<String, String> properties) {
        this(groupId, parse, type, properties);
        consumer.subscribe(topic);
    }

    private KafkaService(String groupId, ConsumerFuncion parse, Class<T> type, Map<String, String> properties) {
        this.consumer = new KafkaConsumer<>(getProperties(type, groupId, properties));
        //informar qual é o tópico que desejo ser o consumidor
        this.parse = parse;

    }

    public void run(){
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if(!records.isEmpty()){
                System.out.println("Nova mensagem:  "+ records.count()+ " registros");
            }
            for (var record: records){
                try {
                    this.parse.consume(record);
                } catch (Exception e) {
                    //TRATAMENTO DE EXCEPTION SERÁ PARA LOGGAR
                    //pegar somente a exception, pois quero ser capaz de pegar qualquer tipo de mensagem
                    e.printStackTrace();
                }

            }
        }
    }
    private Properties getProperties(Class<T> type, String groupId, Map<String, String> overrideProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //definir um id para cada um que roda
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, groupId + " - " + UUID.randomUUID().toString());
        //maximo de mensagem que será commitado - é criado um auto commit
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        //criar uma nova propriedade e inserindo que por padrão será string
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        //Overrides nos properties
        properties.putAll(overrideProperties);
//        properties.setProperty(ConsumerConfig.,)
//        properties.setProperty(ConsumerConfig.,)
        return properties;
    }

    @Override
    public void close() {
        consumer.close();
    }
}
