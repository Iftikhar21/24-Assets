package com.example.application.base.ui.view;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
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
        Button aboutBtn = new Button("About Us");
        aboutBtn.addClassName("navigation-btn");
        Button blogBtn = new Button("Blog");
        blogBtn.addClassName("navigation-btn");

        nav.add(homeBtn, aboutBtn, blogBtn);

        // Tombol Service dan Get Started di kanan
        Button serviceBtn = new Button("Service");
        serviceBtn.addClassName("navigation-btn");

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
}