package com.shail.crd.transactionsapp.controller;

import com.shail.crd.transactionsapp.model.Transaction;
import com.shail.crd.transactionsapp.model.TransactionRequest;
import com.shail.crd.transactionsapp.model.TransactionResponse;
import com.shail.crd.transactionsapp.service.TransactionService;
import org.junit.Test;
import org.mockito.internal.matchers.Any;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionControllerTest {

	@Test
	@SuppressWarnings("unchecked")
	public void getTransactions2XX() {
		TransactionService transactionService = mock(TransactionService.class);
		TransactionRequest transactionRequest = mock(TransactionRequest.class);
		Set<Transaction> transactions = (Set<Transaction>)mock(Set.class);
		when(transactionService.transactionsNeededToMakeModel(any(TransactionRequest.class))).thenReturn(transactions);
		TransactionController transactionController = new TransactionController(transactionService);
		ResponseEntity<TransactionResponse> response = transactionController.getTransactions(transactionRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void getTransactions4XX() {
		TransactionService transactionService = mock(TransactionService.class);
		TransactionRequest transactionRequest = mock(TransactionRequest.class);
		when(transactionService.transactionsNeededToMakeModel(any(TransactionRequest.class)))
				.thenThrow(IllegalArgumentException.class);
		TransactionController transactionController = new TransactionController(transactionService);
		ResponseEntity<TransactionResponse> response = transactionController.getTransactions(transactionRequest);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void getTransactions5XX() {
		TransactionService transactionService = mock(TransactionService.class);
		TransactionRequest transactionRequest = mock(TransactionRequest.class);
		when(transactionService.transactionsNeededToMakeModel(any(TransactionRequest.class)))
				.thenThrow(ArithmeticException.class);
		TransactionController transactionController = new TransactionController(transactionService);
		ResponseEntity<TransactionResponse> response = transactionController.getTransactions(transactionRequest);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
}