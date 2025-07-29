package com.example.application.base.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class RootView extends Div {
    public RootView() {
        UI.getCurrent().getPage().setLocation("home");
    }
}
