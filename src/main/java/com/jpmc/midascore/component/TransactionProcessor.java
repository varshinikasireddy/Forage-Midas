package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessor.class);

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IncentiveApiClient incentiveApiClient;

    public TransactionProcessor(UserRepository userRepository, TransactionRepository transactionRepository, IncentiveApiClient incentiveApiClient) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveApiClient = incentiveApiClient;
    }

    @Transactional
    public void processTransaction(Transaction transaction) {
        // Validate sender exists
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        if (sender == null) {
            logger.warn("Transaction rejected: Invalid sender ID {}", transaction.getSenderId());
            return;
        }

        // Validate recipient exists
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        if (recipient == null) {
            logger.warn("Transaction rejected: Invalid recipient ID {}", transaction.getRecipientId());
            return;
        }

        // Validate sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Transaction rejected: Insufficient balance for sender {}. Balance: {}, Amount: {}",
                    sender.getName(), sender.getBalance(), transaction.getAmount());
            return;
        }

        // Get incentive from external API
        Incentive incentive = incentiveApiClient.getIncentive(transaction);
        float incentiveAmount = incentive.getAmount();

        // Process the transaction
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

        // Save updated balances
        userRepository.save(sender);
        userRepository.save(recipient);

        // Record the transaction with incentive
        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
        transactionRepository.save(record);

        logger.info("Transaction processed successfully: {} -> {} amount: {} incentive: {}",
                sender.getName(), recipient.getName(), transaction.getAmount(), incentiveAmount);
    }
}
