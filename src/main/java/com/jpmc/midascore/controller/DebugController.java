package com.jpmc.midascore.controller;

import com.jpmc.midascore.dto.ApiResponse;
import com.jpmc.midascore.dto.TransactionDto;
import com.jpmc.midascore.dto.UserBalanceDto;
import com.jpmc.midascore.service.DebugService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/debug")
@Profile("dev")
public class DebugController {
    private final DebugService debugService;

    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }

    @GetMapping("/users/{id}/balance")
    public ResponseEntity<ApiResponse<UserBalanceDto>> getUserBalance(@PathVariable Long id) {
        UserBalanceDto user = debugService.getUserBalance(id);
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.error("User not found"));
        }
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserBalanceDto>>> getAllUsers() {
        List<UserBalanceDto> users = debugService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getAllTransactions() {
        List<TransactionDto> transactions = debugService.getAllTransactions();
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/users/{id}/transactions")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getUserTransactions(@PathVariable Long id) {
        List<TransactionDto> transactions = debugService.getUserTransactions(id);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<String>> resetDatabase() {
        debugService.resetDatabase();
        return ResponseEntity.ok(ApiResponse.success("Database reset successfully", "All transactions cleared and users reloaded"));
    }
}
