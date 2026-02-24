package com.jpmc.midascore;

import java.util.HashMap;
import java.util.Map;

public class WilburBalanceCalculator {
    
    public static void main(String[] args) {
        // Manual calculation for wilbur's balance
        Map<Long, Float> balances = new HashMap<>();
        balances.put(1L, 1200.23f);
        balances.put(2L, 2215.37f);
        balances.put(3L, 2774.14f);
        balances.put(4L, 12.34f);
        balances.put(5L, 444.55f);
        balances.put(6L, 888.90f);
        balances.put(7L, 777.60f);
        balances.put(8L, 68.70f);
        balances.put(9L, 3476.21f); // wilbur
        balances.put(10L, 2121.54f);
        
        System.out.println("Initial wilbur balance: " + balances.get(9L));
        System.out.println("Note: Incentive amounts must be fetched from API");
        System.out.println("Run TaskFourTests to get actual balance with incentives");
    }
}
