package com.shail.crd.transactionsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * A TransactionRequest contains the current and model portfolios
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
	@NotNull(message = "holdingStocks should not be null")
	@Size(min = 1, message = "At least one stock should be held")
	@Valid
	Set<HoldingStock> holdingStocks;

	@NotNull(message = "modelStocks should not be null")
	@Valid
	Set<ModelStock> modelStocks;
}
