package com.example.application.base.ui.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

public final class ViewToolbar extends Composite<Header> {

    // Konstruktor lama (tetap dipertahankan untuk kompatibilitas)
    public ViewToolbar(String viewTitle, Component... components) {
        this(new H1(viewTitle), components); // Delegasi ke konstruktor baru
    }

    // Konstruktor baru (menerima Component sebagai judul)
    public ViewToolbar(Component titleComponent, Component... components) {
        addClassNames(Display.FLEX, FlexDirection.COLUMN, JustifyContent.BETWEEN,
                AlignItems.STRETCH, Gap.MEDIUM, FlexDirection.Breakpoint.Medium.ROW,
                AlignItems.Breakpoint.Medium.CENTER);

        getElement().getStyle()
                .set("padding","5px")
                .set("background-color", "#ffffff");

        var drawerToggle = new DrawerToggle();
        drawerToggle.addClassNames(Margin.NONE);

        // Ganti H1 dengan komponen yang diberikan (bisa Image, Div, dll)
        titleComponent.addClassNames(FontSize.XLARGE, Margin.NONE, FontWeight.LIGHT);

        var toggleAndTitle = new Div(drawerToggle, titleComponent);
        toggleAndTitle.addClassNames(Display.FLEX, AlignItems.CENTER);
        getContent().add(toggleAndTitle);

        if (components.length > 0) {
            var actions = new Div(components);
            actions.addClassNames(Display.FLEX, FlexDirection.COLUMN, JustifyContent.BETWEEN,
                    Flex.GROW, Gap.SMALL, FlexDirection.Breakpoint.Medium.ROW);
            getContent().add(actions);
        }
    }

    // Metode group() tetap sama
    public static Component group(Component... components) {
        var group = new Div(components);
        group.addClassNames(Display.FLEX, FlexDirection.COLUMN, AlignItems.STRETCH,
                Gap.SMALL, FlexDirection.Breakpoint.Medium.ROW,
                AlignItems.Breakpoint.Medium.CENTER);
        return group;
    }
}
