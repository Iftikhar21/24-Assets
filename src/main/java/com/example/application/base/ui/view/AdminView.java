package com.example.application.base.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.security.PermitAll;

import javax.xml.crypto.Data;
import java.awt.*;
import java.time.LocalDateTime;

@StyleSheet("https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Poppins:wght@400;500;600;700&display=swap")
@PermitAll
@Route("admin")


public final class AdminView extends Div {

    public static class Asset {
        private String type;
        private String name;
        private String id;
        private LocalDateTime takeTime;
        private LocalDateTime returnTime;
        private String status;
        private String action;

        public Asset(String type, String name, String id, LocalDateTime takeTime, LocalDateTime returnTime, String status, String action) {
            this.type = type;
            this.name = name;
            this.id = id;
            this.takeTime = takeTime;
            this.returnTime = returnTime;
            this.status = status;
            this.action = action;
        }

        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public LocalDateTime getTakeTime() { return takeTime; }
        public void setTakeTime(LocalDateTime takeTime) { this.takeTime = takeTime; }
        public LocalDateTime getReturnTime() { return returnTime; }
        public void setReturnTime(LocalDateTime returnTime) { this.returnTime = returnTime; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }


    public AdminView(){
        UI.getCurrent().getPage().addStyleSheet("frontend://default/styles.css");

        Div layoutContainer = new Div();
        layoutContainer.getStyle()
                .set("display", "flex")
                .set("height", "100vh");

        Div sidebar = new Div();
        sidebar.getStyle()
                .set("width", "160px")
                .set("height", "100vh")
                .set("background", "#f8f8f8")
                .set("padding", "20px")
                .set("box-shadow", "2px 0 5px rgba(0,0,0,0.1)");

        // Top number (24)
        Image logo24Assets = new Image(DownloadHandler.forClassResource(getClass(),"/images/logo24Assets.png"), "Logo 24 Assets");
        logo24Assets.setWidth("auto");
        logo24Assets.setHeight("32px");
        logo24Assets.getStyle()
                .set("padding", "5px")
                .set("margin-left", "26px");


        // Unsupported Assets section
        Div section = new Div();
        section.getStyle()
                .set("margin-top", "20px")
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("flex-direction", "column")
                .set("margin-bottom", "20px");

        // Add components to section

        Button ManagementAssets = new Button("Management Assets", new Icon(VaadinIcon.PACKAGE));
        ManagementAssets.addClassName("purple-button");

        ManagementAssets.getStyle()
                .set("width", "100%")
                .set("font-size", "12px")
                .set("justify-content", "flex-start")
                .set("padding", "0.75rem 1rem")
                .set("border-radius", "0.25rem")
                .set("border", "none")
                .set("cursor", "pointer")
                .set("font-size", "1rem")
                .set("transition", "all 0.2s ease");

        ManagementAssets.getElement().getStyle().set("transition", "all 0.3s ease");
        ManagementAssets.addThemeVariants(ButtonVariant.LUMO_TERTIARY);


        Button DataHistory = new Button("Data History", new Icon(VaadinIcon.CLOCK));
        DataHistory.addClassName("purple-button");
        DataHistory.getStyle()
                .set("width", "100%")
                .set("font-size", "12px")
                .set("justify-content", "flex-start")
                .set("padding", "0.75rem 1rem")
                .set("border-radius", "0.25rem")
                .set("border", "none")
                .set("cursor", "pointer")
                .set("font-size", "1rem")
                .set("transition", "all 0.2s ease");


        section.add(ManagementAssets, DataHistory);

        // Add all to sidebar
        Button Exit = new Button( "Exit",new Icon(VaadinIcon.EXIT));
        Exit.getStyle()
                .set("width", "100%")
                .set("color", "white")
                .set("font-size", "12px")
                .set("margin-top", "20rem")
                .set("justify-content", "flex-start")
                .set("padding", "0.75rem 1rem")
                .set("border-radius", "0.25rem")
                .set("border", "none")
                .set("cursor", "pointer")
                .set("font-size", "1rem")
                .set("background-color","#7c3aed")
                .set("posision", "absolute")
                .set("transition", "all 0.2s ease");

        sidebar.add(logo24Assets, section, Exit);

        // Add sidebar to main view

        // Kontainer utama di sebelah kanan sidebar
        Div mainContent = new Div();
        mainContent.getStyle()
                .set("flex", "1")
                .set("display", "flex")
                .set("flex-direction", "row")
                .set("height", "100vh")
                .set("padding", "20px");

// Bagian kiri: Daftar Assets
        VerticalLayout assetListLayout = new VerticalLayout();
        assetListLayout.getStyle()
                .set("width", "35%")
                .set("background-color", "#f9fafb")
                .set("border-radius", "12px")
                .set("padding", "20px")
                .set("overflow-y", "auto")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");
        Icon goodsIcon = new Icon(VaadinIcon.PACKAGE);
        goodsIcon.setColor("#7c3aed");
        goodsIcon.getStyle().set("width", "18px").set("height", "18px")
                .set("margin-right", "4px");

        Span goodsTitle = new Span("Assets");
        goodsTitle.getStyle()
                .set("color", "#7c3aed")
                .set("font-weight", "500")
                .set("font-size", "14px")
                .set("margin", "0");

        HorizontalLayout titleLayout = new HorizontalLayout(goodsIcon, goodsTitle);
        titleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        titleLayout.setSpacing(false);
        titleLayout.getStyle()
                .set("gap", "6px") // kasih sedikit jarak manual
                .set("margin", "0"); // hilangkan margin bawaan

// Search TextField dengan ikon search di kiri
        TextField searchAssets = new TextField();
        searchAssets.setPlaceholder("Search goods");
        searchAssets.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchAssets.setWidth("300px");

// Tombol filter (pakai icon slider horizontal)
        Button filterButton = new Button(new Icon(VaadinIcon.SLIDERS));
        filterButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        filterButton.getStyle()
                .set("border", "1px solid #e5e7eb")
                .set("border-radius", "6px")
                .set("background-color", "white");

// Layout search + filter button
        HorizontalLayout searchLayout = new HorizontalLayout(searchAssets, filterButton);
        searchLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        searchLayout.setWidthFull();
        searchLayout.setSpacing(true);

// Gabungkan semuanya ke satu layout
        VerticalLayout headerLayout = new VerticalLayout(titleLayout, searchLayout,filterButton);
        headerLayout.setSpacing(false);
        headerLayout.setPadding(false);
        headerLayout.setWidthFull();


        Div categori = new Div();

        HorizontalLayout filters = new HorizontalLayout();
        filters.setWidthFull();
        filters.setSpacing(true);
        filters.setAlignItems(FlexComponent.Alignment.CENTER);
        filters.getStyle()
                .set("flex-wrap", "nowrap")
                .set("gap", "8px")
                .set("padding-top", "10px");

        Image logoAll = new Image(DownloadHandler.forClassResource(getClass(),"/images/catAll.png"), "Logo All");
        Image logoSoundSys = new Image(DownloadHandler.forClassResource(getClass(),"/images/catSoundSystem.png"), "Logo Sound System");
        Image logoElectronics = new Image(DownloadHandler.forClassResource(getClass(),"/images/catElectronic.png"), "Logo Electronic");
        Image logoProjector = new Image(DownloadHandler.forClassResource(getClass(),"/images/catProjector.png"), "Logo Projector");

        Button soundBtn = createCategoryButton(logoSoundSys, "Audio & Communication", false);
        Button electronicBtn = createCategoryButton(logoElectronics, "Laptop & Computer", false);
        Button projectorBtn = createCategoryButton(logoProjector, "Presentation Equipment", false);
        filters.add(electronicBtn,soundBtn, projectorBtn);
        assetListLayout.add(goodsIcon, goodsTitle, searchAssets);

        Div scrollContainer = new Div();
        scrollContainer.setWidthFull();
        scrollContainer.getStyle()
                .set("overflow-x", "auto") // hanya horizontal scroll
                .set("overflow-y", "hidden") // tidak scroll vertikal
                .set("white-space", "nowrap")
                .set("padding-bottom", "10px");

// Pastikan scrollContainer hanya membungkus filters
        scrollContainer.add(filters);

// Terakhir tambahkan scrollContainer ke layout, setelah komponen lain
        assetListLayout.add(scrollContainer);

// Bagian kanan: Form & Data Table
        VerticalLayout rightPanel = new VerticalLayout();
        rightPanel.getStyle()
                .set("width", "65%")
                .set("padding-left", "20px")
                .set("display", "flex")
                .set("flex-direction", "column");

// Form tambah assets
        Div addAssetForm = new Div();
        addAssetForm.getStyle()
                .set("background-color", "white")
                .set("border-radius", "10px")
                .set("padding", "20px")
                .set("box-shadow", "0 0 10px rgba(0,0,0,0.1)");

        Icon addAssetsIcon= new Icon(VaadinIcon.PACKAGE);
        addAssetsIcon.setColor("#7c3aed");
        addAssetsIcon.getStyle()
                .set("font-size", "12px");
        Span addAssets = new Span("Add Assets");
        addAssets.getStyle()
                .set("color", "#7c3aed")
                .set("font-weight", "400")
                .set("font-size", "16px");

        HorizontalLayout formRow1 = new HorizontalLayout();
        formRow1.setWidthFull();
        TextField productName = new TextField("Product Name");
        searchAssets.setWidthFull();
        searchAssets.setPlaceholder("Search goods");
        productName.setWidthFull();

        formRow1.add(productName);

        HorizontalLayout formRow2 = new HorizontalLayout();
        formRow2.setWidthFull();
        ComboBox<String> categorySelect = new ComboBox<>("Category");
        categorySelect.setItems("Laptop", "Monitor", "UPS", "External HDD");
        categorySelect.setWidthFull();
        IntegerField qtyCombo = new IntegerField("Qty");
        qtyCombo.setStepButtonsVisible(true);
        qtyCombo.setRequiredIndicatorVisible(true);
        qtyCombo.setMin(0);
        qtyCombo.setWidth("100px");
        ComboBox<String> conditionSelect = new ComboBox<>("Condition");
        conditionSelect.setItems("Ready", "Repair", "Damaged");
        conditionSelect.setWidthFull();
        formRow2.add(categorySelect, qtyCombo, conditionSelect);

        HorizontalLayout formRow3 = new HorizontalLayout();
        Button addImage = new Button("Add Image");
        addImage.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button addAssetButton = new Button("Add Assets");
        addAssetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        formRow3.setSpacing(true);
        formRow3.add(addImage, addAssetButton);

        addAssetForm.add(formRow1,formRow2,formRow3);

// Table data assets
        Grid<Asset> assetGrid = new Grid<>(Asset.class, false);

// Product Name (gabungkan name dan id seperti di desain)
        assetGrid.addComponentColumn(asset -> {
            VerticalLayout nameLayout = new VerticalLayout();
            nameLayout.setPadding(false);
            nameLayout.setSpacing(false);

            Span nameProduct = new Span(asset.getName());
            nameProduct.getStyle().set("font-weight", "500").set("font-size", "0.9rem");

            Span productId = new Span("#" + asset.getId());
            productId.getStyle().set("font-size", "0.75rem").set("color", "#9ca3af");

            nameLayout.add(nameProduct, productId);
            return nameLayout;
        }).setHeader("Product name").setAutoWidth(true);

// Category (type)
        assetGrid.addColumn(Asset::getType)
                .setHeader("Category")
                .setAutoWidth(true);

// Qty → pakai kolom Qty sesuai kebutuhan, contoh di sini pakai takeTime sebagai placeholder Qty:
        assetGrid.addColumn(asset -> "N/A")
                .setHeader("Qty")
                .setAutoWidth(true);

// Condition/status badge
        assetGrid.addComponentColumn(asset -> {
            Span badge = new Span(asset.getStatus());
            badge.getElement().getStyle()
                    .set("background-color", asset.getStatus().equalsIgnoreCase("Ready") ? "#10b981" : "#f87171")
                    .set("color", "white")
                    .set("padding", "0.25em 0.75em")
                    .set("border-radius", "0.375rem")
                    .set("font-size", "0.75rem");
            return badge;
        }).setHeader("Condition").setAutoWidth(true);

// Action button
        assetGrid.addComponentColumn(asset -> {
            Button delete = new Button("Delete");
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);
            delete.getStyle()
                    .set("background-color","red")
                    .set("color", "white")
                    .set("padding", "0.25em 0.75em")
                    .set("border-radius", "0.375rem")
                    .set("font-size", "0.75rem");
            Registration registration = delete.addClickListener(e -> Notification.show("Deleted: " + asset.getName()));
            return delete;
        }).setHeader("Action").setAutoWidth(true);

        assetGrid.setItems(
                new Asset("Sound System", "Speaker", "A0001", LocalDateTime.now(), LocalDateTime.now().plusDays(1), "Ready", ""),
                new Asset("Electronic", "Monitor", "A0002", LocalDateTime.now(), LocalDateTime.now().plusDays(2), "Ready", ""),
                new Asset("Electronic", "Monitor", "A0003", LocalDateTime.now(), LocalDateTime.now().plusDays(3), "Ready", "")
        );

        assetGrid.getStyle()
                .set("border-radius", "10px")
                .set("box-shadow", "0 0 10px rgba(0,0,0,0.1)");

        rightPanel.add(addAssetForm, assetGrid);


// Gabungkan ke main content
        mainContent.add(assetListLayout, rightPanel);
        layoutContainer.add(sidebar, mainContent);
        add(layoutContainer);


    }
    private Button createCategoryButton(Image icon, String text, boolean active) {
        icon.setWidth("24px");
        icon.setHeight("24px");

        HorizontalLayout content = new HorizontalLayout(icon, new Span(text));
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setSpacing(true);
        content.setPadding(false);

        Button btn = new Button();
        btn.setWidth(null);
        btn.setWidth("auto"); // Let buttons size based on content
        btn.getStyle()
                .set("border-radius", "8px")
                .set("background-color",  "#ede9fe")
                .set("color", "#000")
                .set("box-shadow", "0 1px 2px rgba(0,0,0,0.1)")
                .set("border", "1px solid #e0e0e0")
                .set("padding", "8px 12px")
                .set("cursor", "pointer")
                .set("min-width", "200px");


        HorizontalLayout content2 = new HorizontalLayout();
        content2.setAlignItems(FlexComponent.Alignment.CENTER);
        content2.setSpacing(true);
        content2.setPadding(false);

        Span iconSpan = new Span(icon);
        Span textSpan = new Span(text);
        textSpan.getStyle()
                .set("font-size", "12px")
                .set("white-space", "nowrap"); // Prevent text wrapping

        content2.add(iconSpan, textSpan);
        btn.getElement().appendChild(content2.getElement());

        btn.getStyle()
                .set("border", "1px solid #e9ecef")
                .set("border-radius", "5px")
                .set("padding", "0px 12px")
                .set("font-size", "12px")
                .set("min-height", "32px")
                .set("margin", "0"); // Remove any default margins

        if (active) {
            btn.getStyle()
                    .set("background-color", "#7c3aed")
                    .set("color", "white")
                    .set("border-color", "#7c3aed");
        } else {
            btn.getStyle()
                    .set("background-color", "white")
                    .set("color", "#6c757d");
        }

        return btn;
    }


    public static void showAdminView() {
        UI.getCurrent().navigate(AdminView.class);
    }

}