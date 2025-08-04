package com.example.application.base.ui.view;

import com.example.application.base.ui.component.PinCodeField;
import com.example.application.controller.AdminController;
import com.example.application.model.Admin;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.streams.DownloadHandler;

@Route("admin/login")
public class LoginView extends VerticalLayout {
    private AdminController adminController = new AdminController();

    public LoginView() {
        Object user = VaadinSession.getCurrent().getAttribute("user");
        if (user != null) {
            UI.getCurrent().getPage().setLocation("admin/assets");
        }

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        PinCodeField pinCodeField = new PinCodeField(4);

        VerticalLayout container = new VerticalLayout();
        container.setWidth("400px");
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setAlignItems(Alignment.CENTER);

        Image imageHeader = new Image(DownloadHandler.forClassResource(getClass(),"/images/adminFill.png"), "header");
        Span textHeader = new Span("Login Admin");
        textHeader.getStyle().set("font-weight", "bold");
        textHeader.getStyle().set("font-size", "1.5em");
        Span desc = new Span("Masukkan PIN keamanan anda untuk masuk ke halaman Dashboard Admin");
        desc.getStyle().set("text-align", "center");

        Button btnLogin = new Button("Submit", e -> {
            Admin admin = adminController.login(pinCodeField.getValue());

            if (admin != null) {
                UI.getCurrent().getPage().setLocation("admin/assets");
                VaadinSession.getCurrent().setAttribute("user", admin);
            } else {
                pinCodeField.setErrorMessage("PIN Tidak Sesuai");
            }
        });
        btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLogin.setWidthFull();

        container.add(imageHeader, textHeader, desc, pinCodeField, btnLogin);
        add(container);
    }
}
