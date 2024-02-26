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

  Lock s = new ReentrantLock();

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
    //throw error If there is insufficient balance to transfer
    Account fromAccount = getAccount(transferRequest.getAccountFromId());
    if(fromAccount.getBalance().compareTo(transferAmount)<0) {
        throw new NegativeAmmountException(
                "Insufficient Balance in fromAccount " + transferAmount);
    }
    String trans = null;
    do{
       log.info("Trans Status==> {}",trans);
       trans = 	lockTrans(transferRequest,fromAccount,transferAmount);
    } while("in-process".equals(trans));

    return trans;
 }

 private String lockTrans(TransferRequest transferRequest,Account fromAccount, BigDecimal transferAmount) {
    //Transferring Money
    if (s.tryLock()) {
        try {
            log.info("thread begins execution..{}", Thread.currentThread().getName());
            fromAccount = getAccount(transferRequest.getAccountFromId());
            Account toAccount = getAccount(transferRequest.getAccountToId());
            fromAccount.setBalance(fromAccount.getBalance().subtract(transferAmount));
            toAccount.setBalance(toAccount.getBalance().add(transferAmount));

            notificationService.notifyAboutTransfer(toAccount, "transfer amount " + transferAmount);
            log.info("thread exiting..{}", Thread.currentThread().getName());
        }
        finally {
            s.unlock();
            return "success";
        }
    }else
    {
        log.info("another thread is already running so can not run..{}", Thread.currentThread().getName());
        return "in-process";
    }
  }
}