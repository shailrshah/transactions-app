package com.shail.crd.transactionsapp.config;

import com.shail.crd.transactionsapp.service.TransactionService;
import com.shail.crd.transactionsapp.service.TransactionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * This class helps in auto-wiring for dependency injection
 */
@Component
@Slf4j
public class TransactionsConfig {

	/**
	 * Get an instance of a TransactionService
	 * @return an instance of a TransactionService
	 */
	@Bean
	public TransactionService getTransactionService() {
		log.info("getting the instance for TransactionService");
		return new TransactionServiceImpl(getValidator());
	}

	/**
	 * Get an instance of a Hibernate Validator
	 * @return an instance of a Hibernate Validator
	 */
	@Bean
	public Validator getValidator(){
		log.info("getting the instance for Validator");
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
}
