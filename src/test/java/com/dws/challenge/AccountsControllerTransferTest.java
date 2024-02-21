package com.dws.challenge;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.dws.challenge.model.TransferRequest;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.web.AccountsController;

@ExtendWith(MockitoExtension.class)
class AccountsControllerTransferTest {

  private MockMvc mockMvc;
  @InjectMocks
  AccountsController accountsController;  
  @Mock
  private AccountsService accountsService;

  @BeforeEach
  void prepareMockMvc() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(accountsController).build();
  }

  @Test
  void transferMoneyTest() throws Exception {	  
	when(accountsService.transferMoney(any(TransferRequest.class))).thenReturn("success");
    this.mockMvc.perform(post("/v1/accounts/transfer-money").contentType(MediaType.APPLICATION_JSON)
      .content("{\"accountFromId\":\"a1\",\"accountToId\":\"a2\",\"amount\":1}")).andExpect(status().isOk());
    verify(accountsService, times(1)).transferMoney(any(TransferRequest.class));
  }

}