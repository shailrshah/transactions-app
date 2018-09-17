package com.shail.crd.transactionsapp.controller;

import com.shail.crd.transactionsapp.model.Transaction;
import com.shail.crd.transactionsapp.model.TransactionRequest;
import com.shail.crd.transactionsapp.model.TransactionResponse;
import com.shail.crd.transactionsapp.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Controller for the transactions
 */
@RestController
@Slf4j
public class TransactionController {

	private final TransactionService transactionService;

	/**
	 * Constructor for TransactionController
	 * @param transactionService an instance of TransactionService (auto-wired)
	 */
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
		log.info("TransactionController initialized.");
	}

	@PostMapping("/api/transactions")
	ResponseEntity<TransactionResponse> getTransactions(@RequestBody TransactionRequest transactionRequest) {
		try {
			log.info("Calling transactionService.transactionsNeededToMakeModel()");
			Set<Transaction> transactions = transactionService.transactionsNeededToMakeModel(transactionRequest);
			log.info("Call succeeded.");
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(new TransactionResponse(transactions, null));
		} catch (IllegalArgumentException e) {
			log.error("The request was bad: {}", e);
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(new TransactionResponse(null, e.getMessage()));
		} catch (Exception e) {
			log.error("Something went wrong on the server-side: {}", e);
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new TransactionResponse(null, e.getMessage()));
		}
	}
}
