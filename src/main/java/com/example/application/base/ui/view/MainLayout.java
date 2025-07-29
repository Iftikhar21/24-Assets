package com.example.application.base.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.server.streams.DownloadHandler;

import static com.vaadin.flow.theme.lumo.LumoUtility.*;

@Layout
@StyleSheet("context://styles.css")
@StyleSheet("https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Poppins:wght@400;500;600;700&display=swap")

public final class MainLayout extends AppLayout {

    MainLayout() {
        setPrimarySection(Section.DRAWER);

//        addToDrawer(createHeader(), new Scroller(createSideNav()), createBotttomBtn());
    }

    private Div createHeader() {
        // TODO Replace with real application logo and name
        Image logo24Assets = new Image(DownloadHandler.forClassResource(getClass(),"/images/logo24Assets.png"), "Logo 24 Assets");
        logo24Assets.getStyle()
                .set("padding-top", "50px")
                .set("padding-bottom", "50px")
                .set("height", "48px");

        var header = new Div(logo24Assets);
        header.addClassNames(Display.FLEX, JustifyContent.CENTER, AlignItems.CENTER);
        return header;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addClassNames(Margin.Horizontal.MEDIUM);
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
        } else {
            return new SideNavItem(menuEntry.title(), menuEntry.path());
        }
    }

    private Component createBotttomBtn() {
        HorizontalLayout exit = new HorizontalLayout();
        Icon signOut = new Icon(VaadinIcon.SIGN_OUT);
        Button btnExit = new Button("Exit", signOut);

        btnExit.setWidthFull();
//        btnExit.setWidth("200px");
        exit.addClassNames(Display.FLEX, JustifyContent.CENTER, AlignItems.CENTER);
        exit.getStyle()
                .set("padding", "10px");
        btnExit.getStyle()
                .set("font-family", "'Poppins', sans-serif")
                .set("color", "#ffffff")
                .set("background-color", "#6528F7");

        exit.add(btnExit);

        btnExit.addClickListener(event -> {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Konfirmasi Logout");
            confirmDialog.setText("Apakah Anda yakin ingin keluar?");
            confirmDialog.setCancelable(true);
            confirmDialog.addConfirmListener(e -> {
//                authenticationContext.logout();
            });
            confirmDialog.open();
        });

        return exit;
    }

}
