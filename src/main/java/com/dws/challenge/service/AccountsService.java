package com.dws.challenge.service;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.NegativeAmmountException;
import com.dws.challenge.model.TransferRequest;
import com.dws.challenge.repository.AccountsRepository;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  private final NotificationService notificationService;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository, NotificationService notificationService) {
    this.accountsRepository = accountsRepository;
    this.notificationService = notificationService;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  /**
  * Transfer Money
  * @param: TransferRequest
  * @return : String
  */
  public String transferMoney(TransferRequest transferRequest) {

    //throw error If the amount to transfer is less then 0
    BigDecimal transferAmount = transferRequest.getAmount();
    if(transferAmount.compareTo(BigDecimal.ZERO)<0) {
        throw new NegativeAmmountException(
                "Transfering Amount " + transferAmount + " is negative!");
    }
    //->
    Account fromAccount = getAccount(transferRequest.getAccountFromId());
    Account toAccount = getAccount(transferRequest.getAccountToId());
    fromAccount.withdraw(transferAmount);
    toAccount.deposit(transferAmount);
    return "success";
  }
}