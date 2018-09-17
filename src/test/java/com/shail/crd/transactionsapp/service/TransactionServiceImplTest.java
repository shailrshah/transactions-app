package com.shail.crd.transactionsapp.service;

import com.shail.crd.transactionsapp.model.HoldingStock;
import com.shail.crd.transactionsapp.model.ModelStock;
import com.shail.crd.transactionsapp.model.Transaction;
import com.shail.crd.transactionsapp.model.TransactionRequest;
import com.shail.crd.transactionsapp.model.TransactionType;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionServiceImplTest {
	private static final HoldingStock hs1 = new HoldingStock("GOOGL", 100);
	private static final HoldingStock hs2 = new HoldingStock("APPL", 50);
	private static final HoldingStock hs3 = new HoldingStock("MSFT", 50);
	private static final ModelStock ms1 = new ModelStock("GOOGL", 60);
	private static final ModelStock ms2 = new ModelStock("APPL", 40);
	private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
	private final TransactionService transactionService;

	public TransactionServiceImplTest() {
		transactionService = new TransactionServiceImpl(validator);
	}

	@Test
	public void transactionsNeededToMakeModel() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		holdingStocks.add(hs1);
		holdingStocks.add(hs2);
		Set<ModelStock> modelStocks = new HashSet<>();
		modelStocks.add(ms1);
		modelStocks.add(ms2);
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, modelStocks);

		Set<Transaction> transactions = transactionService.transactionsNeededToMakeModel(transactionRequest);
		assertEquals(2, transactions.size());
		assertTrue(transactions.stream()
				.anyMatch(t -> t.getName().equals("APPL")
						&& t.getQuantity() == 10
						&& t.getType() == TransactionType.BUY));
		assertTrue(transactions.stream()
				.anyMatch(t -> t.getName().equals("GOOGL")
						&& t.getQuantity() == 10
						&& t.getType() == TransactionType.SELL));
	}

	@Test
	public void transactionsNeededToMakeModelExtraHolding() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		holdingStocks.add(hs1);
		holdingStocks.add(hs2);
		holdingStocks.add(hs3);
		Set<ModelStock> modelStocks = new HashSet<>();
		modelStocks.add(ms1);
		modelStocks.add(ms2);
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, modelStocks);

		Set<Transaction> transactions = transactionService.transactionsNeededToMakeModel(transactionRequest);
		assertEquals(3, transactions.size());
		assertTrue(transactions.stream()
				.anyMatch(t -> t.getName().equals("APPL")
						&& t.getQuantity() == 30
						&& t.getType() == TransactionType.BUY));
		assertTrue(transactions.stream()
				.anyMatch(t -> t.getName().equals("GOOGL")
						&& t.getQuantity() == 20
						&& t.getType() == TransactionType.BUY));
		assertTrue(transactions.stream()
				.anyMatch(t -> t.getName().equals("MSFT")
						&& t.getQuantity() == 50
						&& t.getType() == TransactionType.SELL));

	}


	@Test
	public void transactionsNeededToMakeModelNoModel() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		holdingStocks.add(hs1);
		holdingStocks.add(hs2);
		Set<ModelStock> modelStocks = new HashSet<>();
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, modelStocks);

		Set<Transaction> transactions = transactionService.transactionsNeededToMakeModel(transactionRequest);
		assertEquals(2, transactions.size());

		assertTrue(transactions.stream()
				.anyMatch(t -> t.getName().equals("APPL")
						&& t.getQuantity() == 50
						&& t.getType() == TransactionType.SELL));
		assertTrue(transactions.stream()
				.anyMatch(t -> t.getName().equals("GOOGL")
						&& t.getQuantity() == 100
						&& t.getType() == TransactionType.SELL));
	}

	@Test(expected = IllegalArgumentException.class)
	public void transactionsNeededToMakeModelNoHoldings() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		Set<ModelStock> modelStocks = new HashSet<>();
		modelStocks.add(ms1);
		modelStocks.add(ms2);
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, modelStocks);
		transactionService.transactionsNeededToMakeModel(transactionRequest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transactionsNeededToMakeModelBlankHoldingStockName() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		holdingStocks.add(new HoldingStock("", 50));
		holdingStocks.add(hs2);
		Set<ModelStock> modelStocks = new HashSet<>();
		modelStocks.add(ms1);
		modelStocks.add(ms2);
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, modelStocks);

		transactionService.transactionsNeededToMakeModel(transactionRequest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transactionsNeededToMakeModelBlankModelStockName() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		holdingStocks.add(hs1);
		holdingStocks.add(hs2);
		Set<ModelStock> modelStocks = new HashSet<>();
		modelStocks.add(new ModelStock("", 60));
		modelStocks.add(ms2);
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, modelStocks);

		transactionService.transactionsNeededToMakeModel(transactionRequest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transactionsNeededToMakeModelNegativeQuantity() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		holdingStocks.add(new HoldingStock("GOOGL", -1));
		holdingStocks.add(hs2);
		Set<ModelStock> modelStocks = new HashSet<>();
		modelStocks.add(ms1);
		modelStocks.add(ms2);
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, modelStocks);

		transactionService.transactionsNeededToMakeModel(transactionRequest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transactionsNeededToMakeModelNegativePercentage() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		holdingStocks.add(hs1);
		holdingStocks.add(hs2);
		Set<ModelStock> modelStocks = new HashSet<>();
		modelStocks.add(new ModelStock("GOOGL", -1));
		modelStocks.add(ms2);
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, modelStocks);

		transactionService.transactionsNeededToMakeModel(transactionRequest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transactionsNeededToMakeModelPercentageNot100() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		holdingStocks.add(hs1);
		holdingStocks.add(hs2);
		Set<ModelStock> modelStocks = new HashSet<>();
		modelStocks.add(new ModelStock("GOOGL", 101));
		modelStocks.add(ms2);
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, modelStocks);

		transactionService.transactionsNeededToMakeModel(transactionRequest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transactionsNeededToMakeModelNull() {
		transactionService.transactionsNeededToMakeModel(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transactionsNeededToMakeModelNullHoldingStocks() {
		Set<ModelStock> modelStocks = new HashSet<>();
		modelStocks.add(ms1);
		modelStocks.add(ms2);
		TransactionRequest transactionRequest = new TransactionRequest(null, modelStocks);

		transactionService.transactionsNeededToMakeModel(transactionRequest);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transactionsNeededToMakeModelNullModelStocks() {
		Set<HoldingStock> holdingStocks = new HashSet<>();
		holdingStocks.add(hs1);
		holdingStocks.add(hs2);
		TransactionRequest transactionRequest = new TransactionRequest(holdingStocks, null);

		transactionService.transactionsNeededToMakeModel(transactionRequest);
	}
}