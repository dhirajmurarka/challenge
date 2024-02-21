package com.dws.challenge.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TransferRequest {
	
  @NotNull
  private String accountFromId;
  
  @NotNull
  private String accountToId;
  
  @NotNull
  private BigDecimal amount;
}
