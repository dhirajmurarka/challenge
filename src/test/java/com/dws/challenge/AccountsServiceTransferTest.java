package com.dws.challenge;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dws.challenge.domain.Account;
import com.dws.challenge.model.TransferRequest;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.NotificationService;

@ExtendWith(MockitoExtension.class)
class AccountsServiceTransferTest {

  @InjectMocks
  private AccountsService accountsService;
  @Mock
  private AccountsRepository accountsRepository;  
  @Mock
  private NotificationService notificationService;

  
  @Test
  void transferAccountTest() { 
    Account account1 = new Account("Id-123");
    account1.setBalance(new BigDecimal(1000));
    Account account2 = new Account("Id-124");
    account2.setBalance(new BigDecimal(500));  
    TransferRequest transferRequest = new TransferRequest();
	transferRequest.setAccountFromId("Id-123");
	transferRequest.setAccountToId("Id-124");
	transferRequest.setAmount(new BigDecimal(200));
	when(accountsRepository.getAccount("Id-123")).thenReturn(account1);
	when(accountsRepository.getAccount("Id-124")).thenReturn(account2);
	doNothing().when(notificationService).notifyAboutTransfer(isA(Account.class),isA(String.class));
	String message = accountsService.transferMoney(transferRequest);
	assertNotNull(message);
  }


}