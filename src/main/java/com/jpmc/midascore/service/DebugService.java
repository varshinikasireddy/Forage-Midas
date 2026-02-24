package com.jpmc.midascore.service;

import com.jpmc.midascore.dto.TransactionDto;
import com.jpmc.midascore.dto.UserBalanceDto;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DebugService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DebugService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public UserBalanceDto getUserBalance(Long userId) {
        UserRecord user = userRepository.findById(userId.longValue());
        if (user == null) {
            return null;
        }
        return new UserBalanceDto(user.getId(), user.getName(), user.getBalance());
    }

    public List<UserBalanceDto> getAllUsers() {
        Iterable<UserRecord> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .map(user -> new UserBalanceDto(user.getId(), user.getName(), user.getBalance()))
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getAllTransactions() {
        Iterable<TransactionRecord> transactions = transactionRepository.findAll();
        return StreamSupport.stream(transactions.spliterator(), false)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getUserTransactions(Long userId) {
        Iterable<TransactionRecord> allTransactions = transactionRepository.findAll();
        return StreamSupport.stream(allTransactions.spliterator(), false)
                .filter(tx -> tx.getSender().getId().equals(userId) || tx.getRecipient().getId().equals(userId))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void resetDatabase() {
        // Clear all transactions
        transactionRepository.deleteAll();
        
        // Clear all users
        userRepository.deleteAll();
        
        // Reload initial user data
        reloadInitialUsers();
    }

    private void reloadInitialUsers() {
        List<UserRecord> initialUsers = new ArrayList<>();
        initialUsers.add(new UserRecord("bernie", 1200.23f));
        initialUsers.add(new UserRecord("grommit", 2215.37f));
        initialUsers.add(new UserRecord("maria", 2774.14f));
        initialUsers.add(new UserRecord("mario", 12.34f));
        initialUsers.add(new UserRecord("waldorf", 444.55f));
        initialUsers.add(new UserRecord("whosit", 888.90f));
        initialUsers.add(new UserRecord("whatsit", 777.60f));
        initialUsers.add(new UserRecord("howsit", 68.70f));
        initialUsers.add(new UserRecord("wilbur", 3476.21f));
        initialUsers.add(new UserRecord("antonio", 2121.54f));
        initialUsers.add(new UserRecord("calypso", 779421.33f));
        
        userRepository.saveAll(initialUsers);
    }

    private TransactionDto mapToDto(TransactionRecord transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getSender().getId(),
                transaction.getSender().getName(),
                transaction.getRecipient().getId(),
                transaction.getRecipient().getName(),
                transaction.getAmount(),
                transaction.getIncentive()
        );
    }
}
