package com.shail.crd.transactionsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * A TransactionRequest contains the current and model portfolios
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
	@NotNull(message = "holdingStock should not be null")
	@Valid
	Set<HoldingStock> holdingStock;

	@NotNull(message = "modelStock should not be null")
	@Valid
	Set<ModelStock> modelStock;
}
