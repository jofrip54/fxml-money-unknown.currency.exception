package com.jofrip54.view;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

import org.javamoney.moneta.Money;

public class MoneyLabel<T extends MonetaryAmount> extends Label {

	public MoneyLabel() {
		installDefaults();
		installBindings();

		// Method calls below commented out b/c they cause 
		// UnknownCurrencyException to be thrown.
		//		 setInitialCurrency();
		//		 setInitialMonetaryAmount();
	}

	/**
	 * Adds a border to the label. This enables the label to be seen if it
	 * displays no data.
	 */
	protected void installDefaults() {

		final BorderStroke bs = new BorderStroke(
		                                         Color.DIMGRAY,
		                                         BorderStrokeStyle.SOLID,
		                                         CornerRadii.EMPTY,
		                                         BorderWidths.DEFAULT);
		setBorder(new Border(bs));
		setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	}

	/**
	 * Binds the {@code text} property for the label to the
	 * {@code monetaryAmount} property. This requires an {@code ObjectBinding}
	 * b/c the {@code text} and {@code monetaryAmount} properties are of
	 * different types.
	 */
	protected void installBindings() {
		final Callable<String> moneyFunction = () -> {
			final MonetaryAmount ma = getMonetaryAmount();
			if (ma != null) {
				final Locale locale = Locale.forLanguageTag("en_us");
				final MonetaryAmountFormat maf = MonetaryFormats.getAmountFormat(locale);
				return maf.format(ma);
			}
			else
				return null;
		};

		//********************************************************************************
		textProperty().bind(Bindings.createObjectBinding(
		                                                 moneyFunction,
		                                                 monetaryAmountProperty()));
	}

	/**
	 * Sets the initial value for the {@code currency} property. This is called
	 * in the constructor (currently commented out because Scene Builder
	 * falsely reports an {@code UnknownCurrencyException}.
	 */
	protected void setInitialCurrency() {
		// This throws UnknownCurrencyException for Scene Builder
		final CurrencyUnit cu = Monetary.getCurrency("CHF");
		setCurrencyUnit(cu);
	}

	/**
	 * Sets the initial value for the {@code monetaryAmount} property. This is
	 * called in the constructor (currently commented out because Scene Builder
	 * falsely reports an {@code UnknownCurrencyException}.
	 */
	@SuppressWarnings("unchecked")
	protected void setInitialMonetaryAmount() {
		// This throws UnknownCurrencyException for Scene Builder
		final T ma = (T) Money.of(BigDecimal.ZERO, Monetary.getCurrency("CHF"));
		setMonetaryAmount(ma);
	}

	//================================================
	///////////////////////////////////////  PROPERTIES  ///////////////////////////////////////
	//================================================

	public ObjectProperty<CurrencyUnit> currencyUnitProperty() {
		if (currencyUnit == null) {
			// Option #1
			// This throws UnknownCurrencyException for Scene Builder, but
			//  only where property is 'test' accessed by constructor and the property
			//  is initialized with a default value.
			//			currencyUnit = new SimpleObjectProperty<>(
			//			                                          this,
			//			                                          "currencyUnit",
			//			                                          Monetary.getCurrency("CHF"));

			// Option #2
			//TODO Replace with Option #1 after problem solved
			currencyUnit = new SimpleObjectProperty<>(this, "currencyUnit");
		}
		return currencyUnit;
	}
	public CurrencyUnit getCurrencyUnit() {
		return currencyUnitProperty().get();
	}
	public void setCurrencyUnit(CurrencyUnit cu) {
		currencyUnitProperty().set(cu);
	}
	private ObjectProperty<CurrencyUnit> currencyUnit;



	//	@SuppressWarnings("unchecked")
	public ObjectProperty<T> monetaryAmountProperty() {
		if (monetaryAmount == null) {
			// Option #1
			// This throws UnknownCurrencyException for Scene Builder for same
			//  reason as currencyUnit property.
			//			monetaryAmount = new SimpleObjectProperty<>(
			//			                                            this,
			//			                                            "monetaryAmount",
			//			                                            (T) Money.of(BigDecimal.valueOf(100.00), "CHF"));

			// Option #2
			//TODO Replace with Option #1 after problem solved
			monetaryAmount = new SimpleObjectProperty<>(this, "monetaryAmount");
		}
		return monetaryAmount;
	}
	public T getMonetaryAmount() {
		return monetaryAmountProperty().get();
	}
	public void setMonetaryAmount(T a) {
		monetaryAmountProperty().set(a);
	}
	private ObjectProperty<T> monetaryAmount;
}
