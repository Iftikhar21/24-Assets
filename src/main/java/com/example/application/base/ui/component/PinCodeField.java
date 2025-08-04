package com.example.application.base.ui.component;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.jetbrains.annotations.NotNull;

public class PinCodeField extends CustomField<String> {

    private final TextField[] fields;

    public PinCodeField(int length) {
        if (length <= 0) throw new IllegalArgumentException("Length must be > 0");

        fields = new TextField[length];
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);

        for (int i = 0; i < length; i++) {
            TextField field = getTextField(length, i);

            fields[i] = field;
            layout.add(field);
        }

        add(layout);
    }

    private @NotNull TextField getTextField(int length, int i) {
        TextField field = new TextField();

        field.setClassName("pin-child");

        field.getElement().setAttribute("theme", "align-center");
        field.getStyle().set("box-sizing", "border-box");
        field.getElement().getStyle().set("padding", "0");
        field.getElement().getStyle().set("margin", "0");
        field.getElement().getStyle().set("border", "none");

        field.getElement().executeJs("""
            const input = this.shadowRoot?.querySelector('input');
            const wrapper = this.shadowRoot?.querySelector('[part="input-field"]');
        
            if (input) {
                input.style.width = '100%';
                input.style.height = '100%';
                input.style.padding = '0';
                input.style.margin = '0';
                input.style.textAlign = 'center';
                input.style.boxSizing = 'border-box';
            }
        
            if (wrapper) {
                wrapper.style.padding = '0';
                wrapper.style.margin = '0';
                wrapper.style.height = '100%';
                wrapper.style.width = '100%';
                wrapper.style.display = 'flex';
                wrapper.style.alignItems = 'center';
                wrapper.style.justifyContent = 'center';
            }
        """);

        field.setMaxLength(1);
        field.setValueChangeMode(ValueChangeMode.EAGER);

        field.addValueChangeListener(event -> {
            String value = event.getValue();
            if (!value.isEmpty() && i < length - 1) {
                fields[i + 1].focus();
            } else if (value.isEmpty() && i > 0) {
                fields[i - 1].focus();
            }
            System.out.println((!value.isEmpty() && i < length - 1));
            updateValue();
        });

        return field;
    }

    @Override
    protected String generateModelValue() {
        StringBuilder sb = new StringBuilder();
        for (TextField field : fields) {
            sb.append(field.getValue());
        }
        return sb.toString();
    }

    @Override
    protected void setPresentationValue(String value) {
        if (value == null) return;
        for (int i = 0; i < fields.length; i++) {
            if (i < value.length()) {
                fields[i].setValue(String.valueOf(value.charAt(i)));
            } else {
                fields[i].clear();
            }
        }
    }
}