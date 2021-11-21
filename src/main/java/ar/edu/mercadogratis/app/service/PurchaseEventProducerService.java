package ar.edu.mercadogratis.app.service;

import ar.edu.mercadogratis.app.model.PurchaseProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class PurchaseEventProducerService {

    private final Producer<String, String> purchaseEventsProducer;
    private final String purchaseEventTopic;

    public void publish(PurchaseProduct purchaseProduct) {
        ProducerRecord<String, String> record = buildRecord(purchaseProduct);
        purchaseEventsProducer.send(record, buildLogCallback());
    }

    private ProducerRecord<String, String> buildRecord(PurchaseProduct purchase) {
        return new ProducerRecord<>(
                purchaseEventTopic, "key-" + purchase.getId(), purchase.toString());
    }

    private Callback buildLogCallback() {
        return (recordMetadata, e) -> {
            if (Objects.nonNull(e)){
                log.error("Error publishing message: " + recordMetadata, e);
            } else {
                log.info("Event published: " + recordMetadata);
            }
        };
    }
}
