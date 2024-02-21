package com.dws.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.NegativeAmmountException;
import com.dws.challenge.model.TransferRequest;
import com.dws.challenge.repository.AccountsRepository;

import lombok.Getter;

@Service
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

    //Account Retrieval
    String accountFromId = transferRequest.getAccountFromId();
    String accountToId = transferRequest.getAccountToId();
    Account fromAccount = getAccount(accountFromId);
    Account toAccount = getAccount(accountToId);
    String transferDescription = "transfer amount " + transferAmount;

    //throw error If there is insufficient balance to transfer
    if(fromAccount.getBalance().compareTo(transferAmount)<0) {
        throw new NegativeAmmountException(
                "Insufficient Balance in fromAccount " + transferDescription);
    }

    //Transferring Money
    fromAccount.setBalance(fromAccount.getBalance().subtract(transferAmount));
    accountsRepository.save(fromAccount);
    toAccount.setBalance(toAccount.getBalance().add(transferAmount));
    accountsRepository.save(toAccount);
    notificationService.notifyAboutTransfer(toAccount, transferDescription);
    return "success";
  }
   
}
