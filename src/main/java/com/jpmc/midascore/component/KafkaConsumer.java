package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final List<Transaction> receivedTransactions = new ArrayList<>();

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group-v2")
    public void listen(Transaction transaction) {
        logger.info("Received transaction: {}", transaction);
        receivedTransactions.add(transaction);
        // Set a breakpoint here to inspect the transaction
    }

    public List<Transaction> getReceivedTransactions() {
        return new ArrayList<>(receivedTransactions);
    }
}
