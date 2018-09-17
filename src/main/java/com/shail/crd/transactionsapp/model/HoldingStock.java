package com.shail.crd.transactionsapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A HoldingStock encapsulates the name and quantity of a stock that one owns
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoldingStock {

	@NotNull(message = "name should not be blank")
	@Length(min = 1, message = "name's length cannot be less than 1")
	String name;

	@Min(value = 0, message = "quantity has to be non-negative")
	double quantity;
}
