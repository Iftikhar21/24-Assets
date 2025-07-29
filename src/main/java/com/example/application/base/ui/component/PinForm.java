package com.example.application.base.ui.component;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class PinForm extends HorizontalLayout {

    private final TextField[] digits = new TextField[4];

    public PinForm() {
        setSpacing(true);

        for (int i = 0; i < 4; i++) {
            digits[i] = new TextField();
            digits[i].setMaxLength(1);
            digits[i].setWidth("40px");
            digits[i].setPattern("\\d");
            digits[i].getStyle().set("text-align", "center");
            int currentIndex = i;

            // Otomatis fokus ke kolom berikutnya setelah isi
            digits[i].addValueChangeListener(e -> {
                if (!e.getValue().isEmpty() && currentIndex < digits.length - 1) {
                    digits[currentIndex + 1].focus();
                }
            });

            add(digits[i]);
        }
    }

    // Method bantu buat ambil PIN sebagai string
    public String getPin() {
        StringBuilder pin = new StringBuilder();
        for (TextField digit : digits) {
            pin.append(digit.getValue());
        }
        return pin.toString();
    }

    // Method bantu untuk reset input
    public void clear() {
        for (TextField digit : digits) {
            digit.clear();
        }
        digits[0].focus();
    }
}
