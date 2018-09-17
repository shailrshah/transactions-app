package com.shail.crd.transactionsapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * A TransactionResponse contains a set of Transactions that need to be performed
 * in order to get from the current portfolio to the model portfolio
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {
	Set<Transaction> transactions;
	String error;
}
