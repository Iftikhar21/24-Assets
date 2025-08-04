package com.example.application.base.ui.view;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("home")
@AnonymousAllowed
@StyleSheet("https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Poppins:wght@400;500;600;700&display=swap")

//@CssImport(value = "./themes/default/home-view.css", themeFor = "vaadin-vertical-layout")
public class HomeView extends VerticalLayout {

    public HomeView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("home-view");

        // Add custom CSS
        UI.getCurrent().getPage().addStyleSheet("/themes/default/home-view.css");

        // Create header
        add(createHeader());

        // Create hero section
        add(createHeroSection());

        // Create dashboard preview section
        add(createDashboardPreview());

        add(createAboutUsSection());

        add(createBlogSection());
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);
        header.addClassName("header");

        // Logo
        Image logo = new Image(DownloadHandler.forClassResource(getClass(),"/images/logo24Assets.png"), "Logo 24 Assets");
        logo.setWidth("auto");
        logo.setHeight("28px");
        logo.getStyle()
                .set("padding", "5px")
                .set("margin-left", "10px");
        HorizontalLayout logoLayout = new HorizontalLayout(logo);
        logoLayout.addClassName("logo");

        getStyle()
                .set("background-color", "#f8f9fa")
                .set("font-family", "'Poppins', sans-serif")
                .set("padding", "0")
                .set("margin", "0");

        // Navigation
        HorizontalLayout nav = new HorizontalLayout();
        nav.setSpacing(true);
        nav.addClassName("navigation");

        Button homeBtn = new Button("Home");
        homeBtn.addClassName("navigation-btn");
        homeBtn.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("document.getElementById('hero-section').scrollIntoView({behavior: 'smooth'})");
        });

        Button aboutBtn = new Button("About Us");
        aboutBtn.addClassName("navigation-btn");
        aboutBtn.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("document.getElementById('about-us-section').scrollIntoView({behavior: 'smooth'})");
        });

        Button blogBtn = new Button("Blog");
        blogBtn.addClassName("navigation-btn");
        blogBtn.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("document.getElementById('blog-section').scrollIntoView({behavior: 'smooth'})");
        });

        nav.add(homeBtn, aboutBtn, blogBtn);

        // Tombol Service dan Get Started di kanan
        Button serviceBtn = new Button("Service");
        serviceBtn.addClassName("navigation-btn");

        Dialog serviceDialog = createServiceDialog();

        // Tambahkan click listener untuk membuka dialog
        serviceBtn.addClickListener(e -> {
            serviceDialog.open();
        });

        Button getStartedBtn = new Button("Get Started");
        getStartedBtn.addClickListener(e -> {
            getStartedBtn.getUI().ifPresent(ui -> ui.navigate("ordering"));
        });
        getStartedBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getStartedBtn.addClassName("get-started-btn");

        HorizontalLayout rightSection = new HorizontalLayout(serviceBtn, getStartedBtn);
        rightSection.setSpacing(true);
        rightSection.setAlignItems(Alignment.CENTER);

        // Gabungkan semuanya
        header.add(logoLayout, nav, rightSection);

        return header;
    }

    private VerticalLayout createHeroSection() {
        VerticalLayout hero = new VerticalLayout();
        hero.setSizeFull();
        hero.setAlignItems(Alignment.CENTER);
        hero.setJustifyContentMode(JustifyContentMode.CENTER);
        hero.addClassName("hero-section");
        hero.setId("hero-section");

        // Main heading
        Html title = new Html("<h1 class='main-title'>The Easiest Way to Get<br>What You Need – Fast!</h1>");
        add(title);

        // Subtitle
        Html subtitle = new Html("<p class='subtitle'>24 Assets - Discover, Borrow, and Manage School Items withEase.<br> Start Managing Your School Needs – Click Here!</p>");
        subtitle.addClassName("subtitle");

        // CTA Button
        Button getStartedBtn = new Button("Get Started", new Icon(VaadinIcon.ARROW_RIGHT));
        getStartedBtn.addClickListener(e -> {
            getStartedBtn.getUI().ifPresent(ui -> ui.navigate("ordering"));
        });
        getStartedBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        getStartedBtn.addClassName("get-started-btn");

        hero.add(title, subtitle, getStartedBtn);
        return hero;
    }

    private VerticalLayout createDashboardPreview() {
        VerticalLayout preview = new VerticalLayout();
        preview.setSizeFull();
        preview.setAlignItems(Alignment.CENTER);
        preview.addClassName("dashboard-preview");

        Image dashboardImage = new Image(DownloadHandler.forClassResource(getClass(),"/images/orderingpage.png"), "Logo 24 Assets");

        preview.add(dashboardImage);
        return preview;
    }

    private VerticalLayout createAboutUsSection() {
        VerticalLayout aboutUsSection = new VerticalLayout();
        aboutUsSection.setWidthFull();
        aboutUsSection.setPadding(false);
        aboutUsSection.setSpacing(false);
        aboutUsSection.addClassName("about-us-section");
        aboutUsSection.setId("about-us-section");

        // Container utama dengan dua kolom
        HorizontalLayout twoColumnLayout = new HorizontalLayout();
        twoColumnLayout.setWidthFull();
        twoColumnLayout.setPadding(true);
        twoColumnLayout.setSpacing(false);
        twoColumnLayout.addClassName("two-column-layout");

        // Kolom kiri (gambar)
        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.setPadding(false);
        leftColumn.setSpacing(false);
        leftColumn.setAlignItems(Alignment.CENTER);
        leftColumn.setJustifyContentMode(JustifyContentMode.CENTER);
        leftColumn.addClassName("left-column");

        Image aboutUsImage = new Image(DownloadHandler.forClassResource(getClass(),"/images/aboutuspage.png"), "Logo 24 Assets");
        aboutUsImage.addClassName("about-us-image");
        leftColumn.add(aboutUsImage);

        // Kolom kanan (teks)
        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setPadding(true);
        rightColumn.setSpacing(false);
        rightColumn.setAlignItems(Alignment.START);
        rightColumn.setJustifyContentMode(JustifyContentMode.CENTER);
        rightColumn.addClassName("right-column");

        // Judul utama
        H1 mainTitle = new H1("Simplify Borrowing, Maximize Access");
        mainTitle.addClassName("about-us-title");

        // Deskripsi
        Paragraph description1 = new Paragraph("24 Assets is a smart platform that makes borrowing school items fast, easy, and organized. Designed for students and staff, it helps you find, request, and manage school-owned tools in just a few clicks — anytime, anywhere.");
        description1.addClassName("about-us-text");

        Paragraph description2 = new Paragraph("We bring efficiency to your fingertips, so you can focus more on learning, not the logistics.");
        description2.addClassName("about-us-text");

        rightColumn.add(mainTitle, description1, description2);

        // Tambahkan kedua kolom ke layout utama
        twoColumnLayout.add(leftColumn, rightColumn);
        twoColumnLayout.setFlexGrow(0.5, leftColumn); // Lebih kecil untuk gambar
        twoColumnLayout.setFlexGrow(0.5, rightColumn); // Lebih besar untuk teks

        aboutUsSection.add(twoColumnLayout);

        return aboutUsSection;
    }

    private VerticalLayout createBlogSection() {
        // Main container
        VerticalLayout blogSection = new VerticalLayout();
        blogSection.setWidthFull();
        blogSection.addClassName("blog-section");
        blogSection.setId("blog-section");

        // Upper frame with blue background
        VerticalLayout frameUp = new VerticalLayout();
        frameUp.addClassName("blog-frame-up");
        frameUp.setHeight("400px");
        frameUp.setWidth("100%"); // Ubah dari "auto" ke "100%"
        frameUp.getStyle().set("background", "linear-gradient(to right, #1e88e5, #0d47a1)");
        frameUp.setPadding(false); // Ubah ke false
        frameUp.setSpacing(false);
        frameUp.setAlignItems(Alignment.CENTER);
        frameUp.setJustifyContentMode(JustifyContentMode.CENTER);

        // Kontainer untuk konten agar lebih mudah diatur
        VerticalLayout contentContainer = new VerticalLayout();
        contentContainer.setPadding(true);
        contentContainer.setSpacing(false);
        contentContainer.setAlignItems(Alignment.CENTER);
        contentContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        contentContainer.getStyle().set("max-width", "600px");

        // Judul dengan line break
        Div title = new Div();
        title.add(new Span("Maximize What Your School"));
        title.add(new Html("<br>"));
        title.add(new Span("Offers-One Article at a Time"));
        title.addClassName("title-blog");

        // Paragraf
        Paragraph paragraph = new Paragraph("Explore practical guides, smart tips, and inspiring stories to help you make the most of your school's resources.");
        paragraph.getStyle()
                .set("color", "white")
                .set("text-align", "center")
                .set("margin", "1rem 0");

        // Button
        Button getStartedBtn = new Button("Get Started", new Icon(VaadinIcon.ARROW_RIGHT));
        getStartedBtn.addClickListener(e -> {
            getStartedBtn.getUI().ifPresent(ui -> ui.navigate("ordering"));
        });
        getStartedBtn.addClassName("get-started-btn-blog");

        contentContainer.add(title, paragraph, getStartedBtn);
        frameUp.add(contentContainer);

        // Lower section with just image and text
        HorizontalLayout lowerSection = new HorizontalLayout();
        lowerSection.setWidthFull();
        lowerSection.getStyle().set("background", "#fff");
        lowerSection.addClassName("lower-section");
        lowerSection.setAlignItems(Alignment.CENTER);
        lowerSection.setJustifyContentMode(JustifyContentMode.BETWEEN);

        // Image on the left
        Image blogImage = new Image(DownloadHandler.forClassResource(getClass(),"/images/logo24Assets.png"), "Logo 24 Assets"); // Replace with your image path
        blogImage.setHeight("100px");
        blogImage.getStyle().set("margin-left", "2rem");

        // Text on the right
        Html footerText = new Html("<h1>Empowering Schools with<br>Smarter Access</h1>");
        footerText.addClassName("footer-text");

        lowerSection.add(blogImage, footerText);

        blogSection.add(frameUp, lowerSection);
        return blogSection;
    }

    private Dialog createServiceDialog() {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        // Set ukuran dialog
        dialog.setWidth("400px");
        dialog.setHeight("auto");

        // Layout utama
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(Alignment.CENTER);
        dialogLayout.getStyle()
                .set("padding", "30px")
                .set("background", "white")
                .set("border-radius", "12px");

        // Tombol close (X) di pojok kanan atas
        Button closeTopButton = new Button();
        closeTopButton.setIcon(VaadinIcon.CLOSE.create());
        closeTopButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeTopButton.getStyle()
                .set("position", "absolute")
                .set("top", "15px")
                .set("right", "15px")
                .set("color", "#666")
                .set("background", "transparent")
                .set("border", "none")
                .set("cursor", "pointer");
        closeTopButton.addClickListener(e -> dialog.close());

        // Icon headset
        Icon headsetIcon = VaadinIcon.HEADSET.create();
        headsetIcon.setSize("48px");
        headsetIcon.getStyle()
                .set("color", "#333")
                .set("margin-bottom", "20px");

        // Title
        H2 title = new H2("Butuh Bantuan? Hubungi Admin Layanan");
        title.getStyle()
                .set("text-align", "center")
                .set("font-size", "22px")
                .set("font-weight", "600")
                .set("color", "#333")
                .set("margin", "0 0 20px 0")
                .set("line-height", "1.3");

        // Description
        Paragraph description = new Paragraph("Jika Anda mengalami kendala atau memiliki pertanyaan " +
                "seputar peminjaman barang, silakan hubungi admin melalui " +
                "telepon atau email yang tersedia.");
        description.getStyle()
                .set("text-align", "center")
                .set("color", "#666")
                .set("font-size", "14px")
                .set("line-height", "1.5")
                .set("margin", "0 0 30px 0")
                .set("max-width", "320px");

        // Contact info container
        VerticalLayout contactLayout = new VerticalLayout();
        contactLayout.setPadding(false);
        contactLayout.setSpacing(false);
        contactLayout.setAlignItems(Alignment.CENTER);
        contactLayout.getStyle().set("margin-bottom", "30px");

        // Phone number
        Paragraph phone = new Paragraph("(+62) 88972349293847");
        phone.getStyle()
                .set("font-weight", "500")
                .set("text-align", "center")
                .set("color", "#007bff")
                .set("font-size", "16px")
                .set("margin", "0 0 8px 0")
                .set("cursor", "pointer");

        // Email
        Paragraph email = new Paragraph("24assets@gmail.com");
        email.getStyle()
                .set("font-weight", "500")
                .set("text-align", "center")
                .set("color", "#007bff")
                .set("font-size", "16px")
                .set("margin", "0")
                .set("cursor", "pointer");

        contactLayout.add(phone, email);

        // Close button
        Button closeButton = new Button("Tutup", e -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        closeButton.getStyle()
                .set("background", "#007bff")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "12px 30px")
                .set("border-radius", "6px")
                .set("font-weight", "500")
                .set("cursor", "pointer");

        // Tambahkan semua komponen ke layout
        dialogLayout.add(headsetIcon, title, description, contactLayout, closeButton);

        // Container untuk positioning tombol close
        Div container = new Div();
        container.getStyle().set("position", "relative");
        container.add(closeTopButton, dialogLayout);

        dialog.add(container);

        // Styling untuk dialog overlay
        dialog.getElement().getStyle()
                .set("--lumo-border-radius", "12px");

        return dialog;
    }   
}