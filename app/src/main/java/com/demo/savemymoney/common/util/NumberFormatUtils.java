package com.demo.savemymoney.common.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class NumberFormatUtils {
    public static String formatAsCurrency(Double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-PE"));
        format.setCurrency(Currency.getInstance("PEN"));
        return format.format(amount);
    }
}
