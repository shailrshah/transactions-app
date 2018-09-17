package com.shail.crd.transactionsapp.service;

import com.shail.crd.transactionsapp.model.HoldingStock;
import com.shail.crd.transactionsapp.model.ModelStock;
import com.shail.crd.transactionsapp.model.Transaction;
import com.shail.crd.transactionsapp.model.TransactionRequest;
import com.shail.crd.transactionsapp.model.TransactionType;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class TransactionServiceImpl implements TransactionService {

	private final Validator validator;

	public TransactionServiceImpl(Validator validator) {
		this.validator = validator;
		log.info("TransactionServiceImpl initialized");
	}

	/**
	 * Get a set of transactions that need to be performed to get the model portfolio
	 * @param transactionRequest an object containing the current and model portfolios
	 * @return a set of transactions that need to be performed to get the model portfolio
	 */
	@Override
	public Set<Transaction> transactionsNeededToMakeModel(TransactionRequest transactionRequest) {
		log.info("Validating transactionRequest");
		validate(transactionRequest);

		log.info("Cleaning transactionRequest");
		TransactionRequest cleanTransactionRequest = clean(transactionRequest);

		log.info("Decomposing transactionRequest for further processing.");
		Set<HoldingStock> holdingStocks = cleanTransactionRequest.getHoldingStocks();
		Set<ModelStock> modelStocks = cleanTransactionRequest.getModelStocks();

		return transactionsNeededToMakeModel(holdingStocks, modelStocks);
	}

	private TransactionRequest clean(TransactionRequest transactionRequest) {
		log.debug("Removing entries of stocks with no quantity");
		Set<HoldingStock> holdingStocks = transactionRequest.getHoldingStocks().stream()
				.filter(hs -> hs.getQuantity()!=0)
				.collect(Collectors.toSet());

		log.debug("Removing entries of stocks with 0%.");
		Set<ModelStock> modelStocks = transactionRequest.getModelStocks().stream()
				.filter(ms -> ms.getPercentage()!=0)
				.collect(Collectors.toSet());

		log.debug("Upper-casing all the stock names for holdings.");
		holdingStocks.forEach(hs -> hs.setName(hs.getName().toUpperCase()));

		log.debug("Upper-casing all the stock names for models.");
		modelStocks.forEach(ms -> ms.setName(ms.getName().toUpperCase()));

		log.debug("transactionRequest has been cleaned.");
		return new TransactionRequest(holdingStocks, modelStocks);
	}

	/**
	 * Get a set of transactions that need to be performed to get the model portfolio
	 * @param holdingStocks the current portfolio
	 * @param modelStocks the desired portfolio
	 * @return a set of transactions that need to be performed to get the model portfolio
	 */
	private Set<Transaction> transactionsNeededToMakeModel(Set<HoldingStock> holdingStocks, Set<ModelStock> modelStocks) {
		Set<Transaction> transactions = new HashSet<>();

		log.debug("Creating a Map from holdingStocks.");
		Map<String, Double> holdingStockMap = holdingStocks.stream()
				.collect(Collectors.toMap(HoldingStock::getName, HoldingStock::getQuantity));
		log.info("holdingStockMap: {}", holdingStockMap);

		log.debug("Creating a set of model stock names from modelStocks");
		Set<String>modelStockNames = modelStocks.stream()
				.map(ModelStock::getName)
				.collect(Collectors.toSet());
		log.info("modelStockNames: {}", modelStockNames);

		double holdingStockSum = holdingStocks.stream()
				.mapToDouble(HoldingStock::getQuantity)
				.sum();
		log.info("The total number of stocks held is {}", holdingStockSum);

		// For each stock in modelStocks, calculate the required stock and current holding of the stock.
		// Add a transaction if there's a difference in the two.
		modelStocks.forEach(ms -> {
			double modelQuantity = ms.getPercentage()/100 * holdingStockSum;
			double holdingQuantity = holdingStockMap.getOrDefault(ms.getName(), 0.0);
			double difference = modelQuantity - holdingQuantity;
			log.info("name = {}, modelQuantity = {}, holdingQuantity = {}, difference = {}",
					ms.getName(), modelQuantity, holdingQuantity, difference);
			if(difference == 0) return;
			TransactionType transactionType = difference > 0 ? TransactionType.BUY : TransactionType.SELL;
			Transaction transaction = new Transaction(ms.getName(), Math.abs(difference), transactionType);
			log.info("New Transaction: {}", transaction);
			transactions.add(transaction);
		});

		// Stocks in holdingStocks but not in modelStocks can be sold off in full
		holdingStocks.forEach(hs -> {
			if(modelStockNames.contains(hs.getName())) return;
			Transaction transaction = new Transaction(hs.getName(), hs.getQuantity(), TransactionType.SELL);
			log.info("New Transaction: {}", transaction);
			transactions.add(transaction);
		});

		log.info("{}", transactions);
		return transactions;
	}

	/**
	 * Validates a TransactionRequest object using Hibernate Validator
	 * @param transactionRequest a TransactionRequest object
	 */
	private void validate(TransactionRequest transactionRequest) {
		Set<ConstraintViolation<TransactionRequest>> constraintViolations = validator.validate(transactionRequest);
		if(!constraintViolations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			constraintViolations.forEach(cv -> sb.append(cv.getPropertyPath())
					.append(": ")
					.append(cv.getMessage())
					.append(". "));
			String errorMessage = sb.toString().trim();
			log.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		double modelStocksPercentageSum = transactionRequest.getModelStocks().stream()
				.mapToDouble(ModelStock::getPercentage)
				.sum();

		if(modelStocksPercentageSum != 100 && modelStocksPercentageSum != 0) {
			String errorMessage = "The percentage of all the model stocks should add up to 0 or 100";
			log.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		log.debug("The TransactionRequest is valid");
	}
}
