package com.dws.challenge;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dws.challenge.domain.Account;
import com.dws.challenge.model.TransferRequest;
import com.dws.challenge.service.AccountsService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class AccountsServiceMultiTransferTest {

  @Autowired
  AccountsService accountsService;
  
  @Test
  void multiThreadTransferTest() throws InterruptedException {

    Account account1 = new Account("Id-111");
    account1.setBalance(new BigDecimal(1000));
    accountsService.createAccount(account1);
    Account account2 = new Account("Id-222");
    account2.setBalance(new BigDecimal(500));
    accountsService.createAccount(account2);      
    Runnable r = () -> {
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAccountFromId("Id-111");
        transferRequest.setAccountToId("Id-222");
        transferRequest.setAmount(new BigDecimal(10));
        String message = accountsService.transferMoney(transferRequest);
        log.info("Balance Amount Id-111 : {}", accountsService.getAccount("Id-111"));
        log.info("Balance Amount Id-222 : {}", accountsService.getAccount("Id-222"));
    };
    for (int i = 0; i < 3; i++) {
        new Thread(r, "Loop-1-Thread-" + i).start();
    }
    Thread.sleep(3999);
    for (int i = 3; i < 8; i++) {
        new Thread(r, "Loop-2-Thread-" + i).start();
    }
  }
}