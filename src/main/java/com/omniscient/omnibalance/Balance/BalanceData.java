package com.omniscient.omnibalance.Balance;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.omniscient.omnibalance.Core.Core;
import com.omniscient.omnibalance.Core.Util.JSON;

import java.math.BigInteger;

public class BalanceData implements JSON {
    private BigInteger balance = BigInteger.ZERO;
    private long lastEarn = 0;

    public BigInteger getBalance() {
        return balance;
    }
    public long getLastEarn() {
        return lastEarn;
    }

    public void setBalance(BigInteger balance) {
        if(balance.compareTo(BigInteger.ZERO) < 0)
            balance = BigInteger.ZERO;
        this.balance = balance;
    }
    public void setLastEarn(long lastEarn) {
        this.lastEarn = lastEarn;
    }
}
