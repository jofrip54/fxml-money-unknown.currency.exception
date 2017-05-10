package com.jofrip54.view;

import java.math.BigDecimal;
import java.util.function.Supplier;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.NumberValue;

import org.javamoney.moneta.Money;

public class UnknownCurrencyExceptionController {

	@FXML
	private Button toggleCurrencyButton;
	@FXML
	private MoneyLabel<MonetaryAmount> moneyLabel;

	@FXML
	private void initialize() {
		installBehavior();
		setInitialData();
	}

	protected void installBehavior() {
		toggleCurrencyButton.setOnAction(e -> {

			// Toggles value for currencyUnit property for label's monetaryAmount property 
			final CurrencyUnit oldCurrencyUnit = moneyLabel.getMonetaryAmount().getCurrency();
			final Supplier<CurrencyUnit> currencyUnit = () -> {
				switch (oldCurrencyUnit.getCurrencyCode()) {
					case "CHF":
						return Monetary.getCurrency("USD");
					case "USD":
						return Monetary.getCurrency("GBP");
					case "GBP":
						return Monetary.getCurrency("CHF");
					default:
						return null; //should never be reached
				}
			};

			//************************************************************************
			final NumberValue value = moneyLabel.getMonetaryAmount().getNumber();
			final CurrencyUnit newCurrencyUnit = currencyUnit.get();
			
			if (newCurrencyUnit != null) // should never be null, but just in case ...
				//Note: Not possible to set currencyUnit value b/c monetaryAmount is immutable.
				moneyLabel.setMonetaryAmount(Money.of(value, newCurrencyUnit));
		});
	}

	private void setInitialData() {
		// This should be set to the monetaryAmount property's value as a default when
		// the property is instantiated, or at the very least in the constructor for the class
		// that 'owns' that property. Currently not possible because Scene Builder is
		// reading an UnknownCurrencyException where one shouldn't exist.
		moneyLabel.setMonetaryAmount(Money.of(BigDecimal.valueOf(100.0), "CHF"));
	}
}
