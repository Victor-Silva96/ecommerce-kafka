package br.com.alura.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
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


    private final ConsumerFunction parse;
    private final KafkaConsumer<String, T> consumer;

    KafkaService(String topic, ConsumerFunction parse, String nameClass, Class<T> type, Map<String,String> overrideProperties) {
        this(parse,nameClass, type, overrideProperties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    KafkaService(Pattern compile, ConsumerFunction parse, String nameClass, Class<T> type, Map<String,String> overrideProperties) {
        this(parse,nameClass, type, overrideProperties);
        consumer.subscribe(compile);

    }

    private KafkaService(ConsumerFunction parse, String nameClass, Class<T> type, Map<String, String> overrideProperties){
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties(nameClass,type,overrideProperties));
    }


    Properties properties(String nameClass, Class<T> type, Map<String, String> overrideProperties) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, nameClass);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, nameClass + "-" + UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(overrideProperties);
        return properties;
    }

    void run() {
        while (true) {
            ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Encontrei "+records.count() + " registros");
                for (ConsumerRecord<String, T> record : records) {
                    parse.consume(record);
                }
            }
        }
    }

    @Override
    public void close() {
        consumer.close();
    }
}
