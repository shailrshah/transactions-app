package com.shail.crd.transactionsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A Transaction represents a buying or selling a certain amount of stock
 */
@Data
@AllArgsConstructor
public class Transaction {
	String name;
	double quantity;
	TransactionType type;
}
