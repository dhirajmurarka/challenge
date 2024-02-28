package com.dws.challenge.domain;

import com.dws.challenge.exception.NegativeAmmountException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Account {

  @NotNull
  @NotEmpty
  private final String accountId;

  @JsonIgnore
  private Lock lock;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal balance;

  public Account(String accountId) {
    this.accountId = accountId;
    this.balance = BigDecimal.ZERO;
  }

  @JsonCreator
  public Account(@JsonProperty("accountId") String accountId,
    @JsonProperty("balance") BigDecimal balance) {
    this.accountId = accountId;
    this.balance = balance;
  }

  /**
  * Withdraw money from the account.
  *
  */
  public void withdraw(BigDecimal amount) {
    if (lock == null) {
	  lock = new ReentrantLock();
    }
    lock.lock();
    try {
    // throw error If there is insufficient balance to transfer
    if (balance.compareTo(amount) < 0) {
      throw new NegativeAmmountException("Insufficient Balance in fromAccount " + amount);
    }
      balance = balance.subtract(amount);
    } finally {
	  lock.unlock();
    }
  }

  /**
   * Deposit money to the account.
   *
  */
  public void deposit(BigDecimal amount) {
    if (lock == null) {
      lock = new ReentrantLock();
    }
    lock.lock();
    try {
      balance = balance.add(amount);
    } finally {
      lock.unlock();
    }
  }
}
