package com.example.application.base.ui.view;

import com.example.application.base.ui.component.ViewToolbar;
import com.example.application.controller.AssetController;
import com.example.application.controller.LocationController;
import com.example.application.controller.ProductsController;
import com.example.application.model.Location;
import com.example.application.model.Products;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.DownloadHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@StyleSheet("https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Poppins:wght@400;500;600;700&display=swap")

@Route("ordering")
public final class OrderingView extends Div {

    private Component desktopLayout;
    private Component mobileLayout;

    private ProductsController productsController = new ProductsController();
    private LocationController locationController = new LocationController();
    private AssetController assetController = new AssetController();

    private TextField nameField;
    private ComboBox<String> statusCombo;
    private ComboBox<String> gradeCombo;
    private ComboBox<String> classCombo;
    private ComboBox<Location> locationCombo;
    private DateTimePicker startDateField;
    private DateTimePicker endDateField;
    private TextArea noteField;

    private Location selectedLocation;

    private List<SelectedProduct> selectedProducts = new ArrayList<>();

    // Asset data class
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

    // SelectedProduct data class
    public static class SelectedProduct {
        private String id;
        private String name;
        private String category;
        private String status;
        private int stock;
        private IntegerField qtyField;
        private Checkbox checkbox;

        private Products product;

        public Products getProduct() {
            return product;
        }

        public SelectedProduct(String id, String name, String category, String status, int stock, Checkbox checkbox, IntegerField qtyField) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.status = status;
            this.stock = stock;
            this.checkbox = checkbox;
            this.qtyField = qtyField;

            product = new Products(Integer.valueOf(id), 0, name, category, 0, qtyField.getValue());
        }

        public boolean isSelected() {
            return checkbox.getValue();
        }

        public int getQty() {
            Integer value = qtyField.getValue();
            return value != null ? value : 0;
        }

        public String getName() {
            return name;
        }

        // Tambahkan getter lain kalau perlu
    }


    public OrderingView() {
        addClassName("main-view");
        Image logo24Assets = new Image(DownloadHandler.forClassResource(getClass(),"/images/logo24Assets.png"), "Logo 24 Assets");
        logo24Assets.setWidth("auto");
        logo24Assets.setHeight("28px");
        logo24Assets.getStyle()
                .set("padding", "5px")
                .set("margin-left", "10px");
        ViewToolbar toolbar = new ViewToolbar(logo24Assets); // Gunakan Image sebagai parameter
        add(toolbar);

        setSizeFull();
        getStyle()
                .set("background-color", "#f8f9fa")
                .set("font-family", "'Poppins', sans-serif")
                .set("padding", "0")
                .set("margin", "0");

        buildLayoutBasedOnDevice(layout -> {
            add(toolbar, layout);
        });
    }

    private void buildLayoutBasedOnDevice(Consumer<Component> layoutConsumer) {
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            boolean isMobile = details.getScreenWidth() <= 768;
            Component layout = isMobile ? createMobileLayout() : createDesktopLayout();
            layoutConsumer.accept(layout);
        });
    }

    private Component createDesktopLayout() {
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.getStyle()
                .set("background-color", "#f8f9fa")
                .set("padding", "16px");

        // Left sidebar
        VerticalLayout leftSidebar = createLeftContent();
        leftSidebar.setWidth("50%");
        leftSidebar.setHeightFull();

        // Right content area
        VerticalLayout rightContent = createRightContent();
        rightContent.setWidth("50%");
        rightContent.setFlexGrow(1);
        rightContent.setHeightFull();

        mainLayout.add(leftSidebar, rightContent);
        return mainLayout;
    }

    private VerticalLayout createLeftContent() {
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setPadding(true);
        sidebar.setSpacing(true);
        sidebar.getStyle()
                .set("padding", "16px")
                .set("margin-bottom", "16px");

        // Form fields
        VerticalLayout formSection = createFormSection();

        // History fields
        VerticalLayout historySection = createHistorySection();


        sidebar.add(formSection, historySection);
        return sidebar;
    }

    private VerticalLayout createFormSection() {
        HorizontalLayout orderingHeader = new HorizontalLayout();
        orderingHeader.setWidthFull();
        orderingHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        orderingHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        orderingHeader.getStyle()
                .set("border-radius", "8px")
                .set("padding", "8px 12px")
                .set("margin-bottom", "16px");

        Icon orderIcon = new Icon(VaadinIcon.ALIGN_LEFT);
        orderIcon.setColor("#7c3aed");
        Span orderingText = new Span("Ordering");
        orderingText.getStyle()
                .set("color", "#7c3aed")
                .set("font-weight", "400")
                .set("font-size", "16px");

        orderingHeader.add(orderIcon, orderingText);

        VerticalLayout formSection = new VerticalLayout();
        formSection.setPadding(false);
        formSection.setSpacing(true);
        formSection.setHeight("auto");

        formSection.getStyle()
                .set("background-color", "#ffffff")
                .set("border-radius", "12px")
                .set("padding", "16px")
                .set("margin-bottom", "16px")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        // Name field
        nameField = new TextField("Name");
        nameField.addClassName("custom-textfield");
        nameField.setWidthFull();

        // Row 1: Status, Grade, Class, Location
        FormLayout row1 = new FormLayout();

        statusCombo = new ComboBox<>("Status");
        statusCombo.setItems("Siswa", "Guru", "Lainnya");
        statusCombo.setValue("All");
        statusCombo.setWidth("125px");
        statusCombo.addClassName("custom-textfield");

        gradeCombo = new ComboBox<>("Grade");
        gradeCombo.setItems("A", "B", "C", "D");
        gradeCombo.setValue("Grade");
        gradeCombo.setWidth("125px");
        gradeCombo.addClassName("custom-textfield");

        classCombo = new ComboBox<>("Class");
        classCombo.setItems("Electronics", "Furniture", "Vehicles");
        classCombo.setValue("Class");
        classCombo.setWidth("125px");
        classCombo.addClassName("custom-textfield");

        locationCombo = new ComboBox<>("Location");
        var listLocations = locationController.getListLocations();
        locationCombo.setItems(listLocations);
        locationCombo.setItemLabelGenerator(Location::getLocationName);
        locationCombo.setWidth("125px");
        locationCombo.addClassName("custom-textfield");

        locationCombo.addValueChangeListener(event -> {
            selectedLocation = event.getValue();
        });

        row1.add(statusCombo, gradeCombo, classCombo, locationCombo);

        row1.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("600px", 4)
        );

        // Row 2: Date Time Pickers
        FormLayout row2 = new FormLayout();
        row2.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("600px", 2)
        );

        // Custom date time layout
        VerticalLayout startDateSection = new VerticalLayout();
        startDateSection.setPadding(false);
        startDateSection.setSpacing(false);
        startDateSection.addClassName("custom-textfield");

        startDateField = new DateTimePicker();
        startDateField.addClassName("custom-textfield");
        startDateField.setWidth("250px");
        startDateField.getStyle().set("font-size", "12px");

        HorizontalLayout startDateTime = new HorizontalLayout(startDateField);
        startDateTime.setSpacing(true);
        startDateTime.setAlignItems(FlexComponent.Alignment.CENTER);
        startDateTime.addClassName("custom-textfield");

        Icon angleDoubleRight = new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT);
        angleDoubleRight.setSize("16px");
        angleDoubleRight.getStyle().set("color", "#6c757d");

        startDateTime.add(angleDoubleRight);

        // End date time
        endDateField = new DateTimePicker();
        endDateField.setWidth("250px");
        endDateField.addClassName("custom-textfield");
        endDateField.getStyle().set("font-size", "12px");

        row2.add(startDateField, endDateField);

        // Note field
        noteField = new TextArea("Note");
        noteField.addClassName("custom-textfield");
        noteField.setWidthFull();

        formSection.add(orderingHeader, nameField, row1, row2, noteField);
        return formSection;
    }

    private VerticalLayout createHistorySection() {
        VerticalLayout historySection = new VerticalLayout();
        historySection.setPadding(false);
        historySection.setSpacing(true);
        historySection.setHeight("auto");

        historySection.getStyle()
                .set("background-color", "#ffffff")
                .set("border-radius", "12px")
                .set("padding", "16px")
                .set("margin-bottom", "16px")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        // History header
        HorizontalLayout historyHeader = new HorizontalLayout();
        historyHeader.setWidthFull();
        historyHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        historyHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        historyHeader.getStyle()
                .set("border-radius", "8px")
                .set("padding", "8px 12px")
                .set("margin-bottom", "16px");

        Icon historyIcon = new Icon(VaadinIcon.CLOCK);
        historyIcon.setColor("#7c3aed");
        Span historyText = new Span("History");
        historyText.getStyle()
                .set("color", "#7c3aed")
                .set("font-weight", "600")
                .set("font-size", "14px");

        historyHeader.add(historyIcon, historyText);

        // Search field
        TextField historySearch = new TextField();
        historySearch.setPlaceholder("Search goods");
        historySearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        historySearch.setWidthFull();

        Button sortBtn = new Button("Sort by", new Icon(VaadinIcon.ARROW_LONG_DOWN));
        sortBtn.setHeight("50px");
        sortBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button filterBtn = new Button("Filter", new Icon(VaadinIcon.ALIGN_CENTER));
        filterBtn.setHeight("50px");
        filterBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout bottomRow = new HorizontalLayout(sortBtn, filterBtn);
        bottomRow.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        bottomRow.setSpacing(true);

        VerticalLayout container = new VerticalLayout();
        container.add(historySearch);
        container.add(bottomRow);
        container.setPadding(false);
        container.setSpacing(true);
        container.setWidthFull();

        container.addClassName("search-filter-container");


        Grid<Asset> historyGrid = createHistoryGrid();

        // Tambahkan ke layout utama
        historySection.add(historyHeader, container, historyGrid);
        return historySection;
    }

    private Grid<Asset> createHistoryGrid() {
        Grid<Asset> grid = new Grid<>(Asset.class, false);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeight("300px");
        grid.getStyle().set("font-size", "12px");

        // Add columns with icons
        grid.addComponentColumn(asset -> {
            Icon icon = new Icon(VaadinIcon.PACKAGE);
            icon.setSize("16px");
            return icon;
        }).setHeader("").setWidth("40px").setFlexGrow(0);

        grid.addColumn(Asset::getName).setHeader("Product name").setWidth("120px");
        grid.addColumn(Asset::getId).setHeader("Take").setWidth("80px");
        grid.addColumn(asset -> formatDateTime(asset.getTakeTime())).setHeader("Return").setWidth("100px");
        grid.addColumn(asset -> formatDateTime(asset.getReturnTime())).setHeader("Status").setWidth("100px");

        grid.addComponentColumn(asset -> {
            Span statusBadge = new Span(asset.getStatus());
            if ("Finished".equals(asset.getStatus())) {
                statusBadge.getStyle()
                        .set("background-color", "#fee2e2")
                        .set("color", "#dc2626")
                        .set("padding", "2px 8px")
                        .set("border-radius", "12px")
                        .set("font-size", "10px");
            } else if ("In use".equals(asset.getStatus())) {
                statusBadge.getStyle()
                        .set("background-color", "#dcfce7")
                        .set("color", "#16a34a")
                        .set("padding", "2px 8px")
                        .set("border-radius", "12px")
                        .set("font-size", "10px");
            }
            return statusBadge;
        }).setHeader("Status").setWidth("80px");

        grid.addComponentColumn(asset -> {
            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClassName("icon-button");
            delete.addClickListener(e -> {
                // aksi delete
            });

            Button actionBtn = new Button("Extend");
            actionBtn.addClassName("purple-button");
            actionBtn.getStyle().set("font-size", "10px");

            HorizontalLayout layout = new HorizontalLayout(delete,actionBtn);
            layout.setSpacing(true);
            layout.setPadding(false);
            layout.setMargin(false);
            return layout;

        }).setHeader("Action").setWidth("160px").setFlexGrow(0);


        // Sample data
        List<Asset> assets = Arrays.asList(
            new Asset("📦", "Speaker", "#A0001", LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(1), "Finished", "Continue"),
            new Asset("📦", "Monitor", "#B0001", LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(2), "In use", "Continue"),
            new Asset("📦", "Tab", "#C0001", LocalDateTime.now().minusMinutes(30), LocalDateTime.now().plusHours(3), "In use", "Continue"),
            new Asset("📦", "Projector", "#D0001", LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), "Finished", "Continue"),
            new Asset("📦", "Projector", "#E0001", LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2), "In use", "Continue"),
            new Asset("📦", "Monitor", "#F0001", LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(4), "In use", "Continue")
        );

        grid.setItems(assets);
        return grid;
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.getDayOfMonth() + " April 2025\n" +
                String.format("%02d:%02d %s",
                        dateTime.getHour() > 12 ? dateTime.getHour() - 12 : dateTime.getHour(),
                        dateTime.getMinute(),
                        dateTime.getHour() >= 12 ? "PM" : "AM");
    }

    private VerticalLayout createRightContent() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.getStyle()
                .set("padding", "16px")
                .set("margin-bottom", "16px");

        VerticalLayout goodSection = goodSection();

        content.add(goodSection);
        return content;
    }

    private VerticalLayout goodSection() {
        VerticalLayout container = new VerticalLayout();
        container.getStyle()
                .set("background-color", "#ffffff")
                .set("border-radius", "12px")
                .set("padding", "16px")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");


        HorizontalLayout goodsHeader = createGoodsHeader();

        //  Name Filters
        HorizontalLayout nameFilter = createNameFilters();
        nameFilter.setWidthFull();

        // Category filters
        HorizontalLayout categories = createCategoryFilters();

        // Products grid
        VerticalLayout productsGrid = createProductsGrid();

        // Bottom section with submit
        HorizontalLayout bottomSection = createBottomSection();

        container.add(goodsHeader, nameFilter, categories, productsGrid, bottomSection);
        return container;
    }

    private HorizontalLayout createGoodsHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.getStyle()
                .set("padding", "16px");

        // Goods title with icon
        HorizontalLayout titleSection = new HorizontalLayout();
        titleSection.setAlignItems(FlexComponent.Alignment.CENTER);
        titleSection.setSpacing(true);

        Icon goodsIcon = new Icon(VaadinIcon.PACKAGE);
        goodsIcon.setColor("#7c3aed");
        Span goodsTitle = new Span("Assets");
        goodsTitle.getStyle()
                .set("color", "#7c3aed")
                .set("font-weight", "400")
                .set("font-size", "16px");

        titleSection.add(goodsIcon, goodsTitle);

        header.add(titleSection);
        return header;
    }

    private HorizontalLayout createNameFilters() {
        // Search field - now with proper full width behavior
        HorizontalLayout goodsSearch = new HorizontalLayout();
        goodsSearch.setWidthFull();
        goodsSearch.setPadding(false);
        goodsSearch.setSpacing(false);

        TextField nameSearch = new TextField();
        nameSearch.setWidthFull();
        nameSearch.setPlaceholder("Search goods");
        nameSearch.addClassName("custom-textfield");
        nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        nameSearch.getStyle()
                .set("margin", "0");

        goodsSearch.add(nameSearch);
        goodsSearch.setFlexGrow(1, nameSearch);
        return goodsSearch;
    }

    private HorizontalLayout createCategoryFilters() {
        HorizontalLayout filters = new HorizontalLayout();
        filters.setWidthFull();
        filters.setSpacing(true);
        filters.setAlignItems(FlexComponent.Alignment.CENTER);
        filters.getStyle()
                .set("flex-wrap", "wrap")
                .set("gap", "8px")
                .set("padding-top", "10px");

        Image logoAll = new Image(DownloadHandler.forClassResource(getClass(),"/images/catAll.png"), "Logo All");
        Image logoSoundSys = new Image(DownloadHandler.forClassResource(getClass(),"/images/catSoundSystem.png"), "Logo Sound System");
        Image logoElectronics = new Image(DownloadHandler.forClassResource(getClass(),"/images/catElectronic.png"), "Logo Electronic");
        Image logoRoomKeys = new Image(DownloadHandler.forClassResource(getClass(),"/images/catRoomNames.png"), "Logo Room Keys");
        Image logoProjector = new Image(DownloadHandler.forClassResource(getClass(),"/images/catProjector.png"), "Logo Projector");

        Button allBtn = createCategoryButton(logoAll, "All", true);
        Button soundBtn = createCategoryButton(logoSoundSys, "Sound System", false);
        Button electronicBtn = createCategoryButton(logoElectronics, "Electronic", false);
        Button keysBtn = createCategoryButton(logoRoomKeys, "Room keys", false);
        Button projectorBtn = createCategoryButton(logoProjector, "Projector", false);

        filters.add(allBtn, soundBtn, electronicBtn, keysBtn, projectorBtn);
        return filters;
    }

    private Button createCategoryButton(Image icon, String text, boolean active) {
        Button btn = new Button();
        btn.setWidth("auto"); // Let buttons size based on content
        btn.getStyle()
                .set("cursor", "pointer");

        HorizontalLayout content = new HorizontalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setSpacing(true);
        content.setPadding(false);

        Span iconSpan = new Span(icon);
        Span textSpan = new Span(text);
        textSpan.getStyle()
                .set("font-size", "12px")
                .set("white-space", "nowrap"); // Prevent text wrapping

        content.add(iconSpan, textSpan);
        btn.getElement().appendChild(content.getElement());

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

    private VerticalLayout createProductsGrid() {
        VerticalLayout gridSection = new VerticalLayout();
        gridSection.setPadding(false);
        gridSection.setSpacing(true);
        gridSection.getStyle()
                .set("overflow-y", "auto")  // Enable vertical scrolling
                .set("max-height", "575px") // Set max height for scrollable area
                .set("margin-bottom", "16px");

        List<Products> allProducts = productsController.getListProducts("");

        // Group products by category
        Map<String, List<Products>> productsByCategory = allProducts.stream()
                .collect(Collectors.groupingBy(Products::getCategoryName));

        // Create sections for each category
        for (Map.Entry<String, List<Products>> entry : productsByCategory.entrySet()) {
            String categoryName = entry.getKey();
            List<Products> categoryProducts = entry.getValue();

            // Convert Products to VerticalLayout cards
            List<VerticalLayout> productCards = categoryProducts.stream()
                    .map(product -> createProductCard(
                            product.getProductID().toString(),
                            product.getProductName(),
                            product.getCategoryName(),
                            product.getStock() > 0 ? "Ready" : "Unavailable", // status
                            product.getStock(), // stock
                            selectedProducts))
                    .collect(Collectors.toList());

            // Create category section
            VerticalLayout categorySection = createCategorySection(categoryName, productCards);
            gridSection.add(categorySection);
        }

        return gridSection;
    }

    private VerticalLayout createCategorySection(String categoryName, List<VerticalLayout> products) {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);
        section.getStyle()
                .set("padding", "10px")
                .set("margin-bottom", "16px");

        H4 categoryTitle = new H4(categoryName);
        categoryTitle.getStyle()
                .set("margin", "0 0 12px 0")
                .set("font-size", "14px")
                .set("color", "#374151");

        Div productGrid = new Div();
        productGrid.getStyle()
                .set("display", "flex")
                .set("flex-direction", "row")
                .set("flex-wrap", "wrap")
                .set("gap", "12px")
                .set("align-items", "flex-start");

        for (VerticalLayout product : products) {
            productGrid.add(product);
        }

        section.add(categoryTitle, productGrid);
        return section;
    }

    private VerticalLayout createProductCard(String id, String name, String category, String status, int stock, List<SelectedProduct> selectionTracker) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(true);
        card.setSpacing(false);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setWidth("250px");
        card.setHeight("320px");
        card.getStyle()
                .set("cursor", "pointer")
                .set("border", "2px solid #e9ecef")
                .set("border-radius", "12px")
                .set("background-color", "#ffffff")
                .set("box-shadow", "0 2px 6px rgba(0,0,0,0.04)")
                .set("position", "relative")
                .set("transition", "border-color 0.3s ease");

        // Checkbox pojok kanan bawah
        Checkbox checkbox = new Checkbox();

        // Quantity selector
        HorizontalLayout qtySection = new HorizontalLayout();
        qtySection.setVisible(false);
        qtySection.setAlignItems(FlexComponent.Alignment.CENTER);
        qtySection.setSpacing(true);

        // Highlight card saat dicentang
        checkbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                card.getStyle().set("border", "2px solid #8B5CF6"); // warna ungu
            } else {
                card.getStyle().set("border", "2px solid #e9ecef"); // default
            }
            qtySection.setVisible(event.getValue());
        });
        checkbox.getElement().executeJs(
            "this.addEventListener('click', function(e) { e.stopPropagation(); });"
        );

        Span qtyLabel = new Span("Qty");
        qtyLabel.getStyle().set("font-size", "14px");

        IntegerField qtyCombo = new IntegerField();
        qtyCombo.setStepButtonsVisible(true);
        qtyCombo.setRequiredIndicatorVisible(true);
        qtyCombo.setMin(1);
        qtyCombo.setWidth("100px");
        qtyCombo.getElement().executeJs(
            "this.addEventListener('click', function(e) { e.stopPropagation(); });"
        );

        qtySection.add(qtyLabel, qtyCombo);

        HorizontalLayout bottomSection = new HorizontalLayout();
        bottomSection.setWidthFull();
        bottomSection.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        bottomSection.setAlignItems(FlexComponent.Alignment.CENTER);
        bottomSection.getStyle().set("margin-top", "20px");
        bottomSection.add(qtySection, checkbox);

        // Gambar produk
        Image productImage = new Image(DownloadHandler.forClassResource(getClass(), "/images/speaker.png"), "Product Image");
        productImage.setWidth("120px");
        productImage.getStyle().set("margin-bottom", "12px");

        // Nama produk
        Span nameSpan = new Span(name);
        nameSpan.getElement().setProperty("title", name);
        nameSpan.getStyle()
                .set("font-weight", "600")
                .set("font-size", "16px")
                .set("color", "#111827")
                .set("margin-bottom", "4px")
                .set("max-width", "100%")
                .set("overflow", "hidden")
                .set("white-space", "nowrap")
                .set("text-overflow", "ellipsis");

        // Status badge
        Span statusBadge = new Span(status);
        statusBadge.getStyle()
                .set("background-color", "#E0E7FF")
                .set("color", "#6D28D9")
                .set("padding", "2px 8px")
                .set("font-size", "12px")
                .set("border-radius", "12px")
                .set("margin-left", "8px");

        // Nama produk + status
        HorizontalLayout nameAndStatus = new HorizontalLayout(nameSpan, statusBadge);
        nameAndStatus.setMaxWidth("100%");
        nameAndStatus.setSpacing(true);
        nameAndStatus.setAlignItems(FlexComponent.Alignment.BASELINE);

        // Kategori
        Span categorySpan = new Span(category);
        categorySpan.getStyle()
                .set("font-size", "14px")
                .set("color", "#6B7280")
                .set("margin-bottom", "12px");

        // Badge jumlah stok
        Span stockBadge = new Span(stock + " Products Ready");
        stockBadge.getStyle()
                .set("background-color", "#8B5CF6")
                .set("color", "white")
                .set("padding", "4px 12px")
                .set("border-radius", "16px")
                .set("font-size", "12px")
                .set("font-weight", "500");

        // Kontainer isi
        VerticalLayout content = new VerticalLayout(productImage, nameAndStatus, categorySpan, stockBadge);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setSpacing(false);
        content.setPadding(false);

        card.add(content, bottomSection);
        card.addClickListener(event -> {
            checkbox.setValue(!checkbox.getValue());
        });

        selectionTracker.add(new SelectedProduct(id, name, category, status, stock, checkbox, qtyCombo));

        return card;
    }



    private HorizontalLayout createBottomSection() {
        HorizontalLayout bottom = new HorizontalLayout();
        bottom.setWidthFull();
        bottom.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        bottom.setAlignItems(FlexComponent.Alignment.CENTER);
        bottom.getStyle().set("margin-top", "20px");

        // Submit button
        Button submitBtn = new Button("Submit");
        submitBtn.addClickListener(event -> {
            Products[] productArray = selectedProducts.stream()
                .filter(SelectedProduct::isSelected)
                .map(selected -> {
                    Products p = selected.getProduct();
                    p.setQuantity(selected.getQty());
                    return p;
                })
                .toArray(Products[]::new);

            for (Products selectedProduct : productArray) {
                System.out.println("Produk " + selectedProduct.getProductName() + " dipilih.");
            }
            System.out.println(nameField.getValue());
            com.example.application.model.Asset submitedAsset = new com.example.application.model.Asset(
                "0", startDateField.getValue(), endDateField.getValue(), "",
                "0", noteField.getValue(), selectedLocation, productArray, statusCombo.getValue(), nameField.getValue(), classCombo.getValue()
            );

            assetController.InsertAsset(submitedAsset);
        });
        submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitBtn.getStyle()
                .set("background-color", "#7c3aed")
                .set("border-radius", "8px")
                .set("padding", "10px 24px");

        bottom.add(submitBtn);
        return bottom;
    }

    private Component createMobileLayout() {
        Tabs tabs = new Tabs();
        Tab formTab = new Tab("Form Peminjaman");
        Tab barangTab = new Tab("Ketersediaan Barang");

        tabs.add(formTab, barangTab);

        formTab.getElement().getStyle().set("flex", "1");
        barangTab.getElement().getStyle().set("flex", "1");

        var form = new VerticalLayout();
        form.setHeightFull();
        form.add(createFormSection());
        form.add(goodSection());

        var pemakaian = new VerticalLayout();
        pemakaian.setHeightFull();
        pemakaian.add(createHistorySection());

        Div content = new Div(form);
        content.setWidth("100%");

        tabs.addSelectedChangeListener(event -> {
            content.removeAll();
            switch (event.getSelectedTab().getLabel()) {
                case "Form Peminjaman" -> content.add(form);
                case "Ketersediaan Barang" -> content.add(pemakaian);
            }
        });

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(false);
        mainLayout.add(tabs, content);

        return mainLayout;
    }

    public static void showMainView() {
        UI.getCurrent().navigate(OrderingView.class);
    }
}