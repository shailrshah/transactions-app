package com.shail.crd.transactionsapp.service;

import com.shail.crd.transactionsapp.model.Transaction;
import com.shail.crd.transactionsapp.model.TransactionRequest;

import java.util.Set;

public interface TransactionService {

	/**
	 * Get a set of transactions that need to be performed to get the model portfolio
	 * @param transactionRequest an object containing the current and model portfolios
	 * @return a set of transactions that need to be performed to get the model portfolio
	 */
	Set<Transaction> transactionsNeededToMakeModel(TransactionRequest transactionRequest);
}
