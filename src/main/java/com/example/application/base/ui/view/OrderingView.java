package com.example.application.base.ui.view;

import com.example.application.base.ui.component.PinCodeField;
import com.example.application.base.ui.component.ViewToolbar;
import com.example.application.controller.AssetController;
import com.example.application.controller.CategoryController;
import com.example.application.controller.LocationController;
import com.example.application.controller.ProductsController;
import com.example.application.model.Asset;
import com.example.application.model.Category;
import com.example.application.model.Location;
import com.example.application.model.Products;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
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
    private VerticalLayout gridSection;

    private ProductsController productsController = new ProductsController();
    private LocationController locationController = new LocationController();
    private AssetController assetController = new AssetController();
    private CategoryController categoryController = new CategoryController();

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

    private VerticalLayout sidebar;
    private Dialog helpDialog;
    private VerticalLayout contentArea;

    private List<Products> allProducts = new ArrayList<>();

    PinCodeField pinCodeField;

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
    }


    public OrderingView() {
        addClassName("main-view");
        setSizeFull();

        UI.getCurrent().getPage().addStyleSheet("/themes/default/home-view.css");
        getStyle()
                .set("background-color", "#f8f9fa")
                .set("font-family", "'Poppins', sans-serif")
                .set("padding", "0")
                .set("margin", "0");

        getElement().getStyle().set("display", "flex");
        getElement().getStyle().set("flex-direction", "column");
        getElement().getStyle().set("height", "100vh");
        getElement().getStyle().set("overflow", "hidden");

        buildLayoutBasedOnDevice(layout -> {
            add(layout);
        });

        allProducts = productsController.getListProducts("");

    }

    private void buildLayoutBasedOnDevice(Consumer<Component> layoutConsumer) {
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            boolean isMobile = details.getScreenWidth() <= 768;
            Component layout = isMobile ? createMobileLayout() : createDesktopLayout();
            layoutConsumer.accept(layout);
        });
    }

    private Component createDesktopLayout() {
        // Main container
        HorizontalLayout mainContainer = new HorizontalLayout();
        mainContainer.setSizeFull();
        mainContainer.setPadding(false);
        mainContainer.setSpacing(false);
        mainContainer.getStyle().set("overflow", "hidden");

        // Create sidebar
        sidebar = createSidebar(); // Menggunakan variabel instance yang sudah dideklarasikan
        sidebar.getStyle()
                .set("position", "fixed")
                .set("top", "0")
                .set("left", "0")
                .set("bottom", "0")
                .set("z-index", "100");

        // Create content area
        contentArea = new VerticalLayout(); // Menggunakan variabel instance
        contentArea.setSizeFull();
        contentArea.setPadding(false);
        contentArea.setSpacing(false);
        contentArea.getStyle()
                .set("margin-left", "300px") // Sesuaikan dengan lebar sidebar
                .set("overflow", "hidden");

        // Create navbar
        HorizontalLayout navbar = createNavbar();
        navbar.getStyle()
                .set("position", "sticky")
                .set("top", "0")
                .set("z-index", "90");

        // Create scrollable content
        Div scrollableContent = new Div();
        scrollableContent.setSizeFull();
        scrollableContent.getStyle()
                .set("overflow-y", "auto")
                .set("padding-bottom", "20px");

        // Create main content
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(false);
        mainContent.setSpacing(false);

        // Left and right content
        VerticalLayout leftContent = createLeftContent();
        leftContent.setWidth("50%");
        leftContent.setHeightFull();

        VerticalLayout rightContent = createRightContent();
        rightContent.setWidth("50%");
        rightContent.setFlexGrow(1);
        rightContent.setHeightFull();

        mainContent.add(leftContent, rightContent);
        scrollableContent.add(mainContent);

        // Add to content area
        contentArea.add(navbar, scrollableContent);
        contentArea.setFlexGrow(1, scrollableContent);

        mainContainer.add(sidebar, contentArea);
        return mainContainer;
    }

    private HorizontalLayout createNavbar() {
        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull();
        navbar.getStyle()
                .set("background-color", "white")
                .set("padding", "16px")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)")
                .set("align-items", "center");

        // Navbar title section
        HorizontalLayout leftSection = new HorizontalLayout();
        leftSection.setAlignItems(FlexComponent.Alignment.CENTER);
        leftSection.setSpacing(true);

        // Menu toggle button
        Button menuButton = new Button(new Icon(VaadinIcon.MENU));
        menuButton.getStyle().set("color", "#333");
        menuButton.addClickListener(e -> {
            sidebar.setVisible(!sidebar.isVisible());
            if (contentArea != null) {
                contentArea.getStyle().set("margin-left", sidebar.isVisible() ? "300px" : "0");
            }
        });

        // Title with number "24"
        Image logo24Assets = new Image(DownloadHandler.forClassResource(getClass(),"/images/logo24Assets.png"), "Logo 24 Assets");
        logo24Assets.getStyle()
                .set("align-items", "center")
                .set("height", "24px");
        Div title = new Div();
        title.getStyle()
                .set("align-items", "center");
        title.add(logo24Assets);

        leftSection.add(menuButton, title);

        // Spacer
        Div spacer = new Div();
        spacer.setWidthFull();

        HorizontalLayout rightSection = new HorizontalLayout();
        Button serviceButton = new Button(new Icon(VaadinIcon.HEADSET));
        serviceButton.getStyle().set("color", "#333");
        createHelpDialog();
        serviceButton.addClickListener(e -> helpDialog.open());

        rightSection.add(serviceButton);

        navbar.add(leftSection, spacer, rightSection);
        navbar.setFlexGrow(1, spacer);

        return navbar;
    }

    private VerticalLayout createSidebar() {
        sidebar = new VerticalLayout();
        sidebar.setWidth("300px");
        sidebar.setHeight("100vh");
        sidebar.setPadding(false);
        sidebar.setSpacing(false);
        sidebar.getStyle()
                .set("background", "#fff")
                .set("position", "fixed")
                .set("top", "0")
                .set("left", "0")
                .set("bottom", "0")
                .set("z-index", "100")
                .set("box-shadow", "2px 0 4px rgba(0,0,0,0.1)");

        // Logo section
        Image logo24Assets = new Image(DownloadHandler.forClassResource(getClass(),"/images/logo24Assets.png"), "Logo 24 Assets");
        logo24Assets.getStyle()
                .set("padding-top", "50px")
                .set("width", "140px");
        VerticalLayout logoSection = new VerticalLayout();
        logoSection.setPadding(true);
        logoSection.setSpacing(false);
        logoSection.setAlignItems(FlexComponent.Alignment.CENTER);
        logoSection.getStyle()
                .set("padding", "32px 24px");
        logoSection.add(logo24Assets);

        // Spacer to push exit button to bottom
        Div spacer = new Div();
        spacer.getStyle()
                .set("flex-grow", "1");

        // Exit button section
        VerticalLayout exitSection = new VerticalLayout();
        exitSection.setPadding(true);
        exitSection.setSpacing(false);
        exitSection.setAlignItems(FlexComponent.Alignment.CENTER);
        exitSection.getStyle()
                .set("padding", "24px");

        Button exitButton = new Button("Exit");
        exitButton.setIcon(new Icon(VaadinIcon.SIGN_OUT));
        exitButton.setIconAfterText(true);
        exitButton.setWidthFull();
        exitButton.addClassName("get-started-btn");
        exitButton.addClickListener(e -> {
            UI.getCurrent().navigate("home");
        });

        exitSection.add(exitButton);

        sidebar.add(logoSection, spacer, exitSection);
        return sidebar;
    }

    private VerticalLayout createLeftContent() {
        VerticalLayout leftContent = new VerticalLayout();
        leftContent.setPadding(true);
        leftContent.setSpacing(true);
        leftContent.getStyle()
                .set("padding", "16px")
                .set("margin-bottom", "16px");

        // Form fields
        VerticalLayout formSection = createFormSection();

        // History fields
        VerticalLayout historySection = createHistorySection();


        leftContent.add(formSection, historySection);
        return leftContent;
    }

    private VerticalLayout createFormSection() {
        // Header section with icon and title
        HorizontalLayout orderingHeader = new HorizontalLayout();
        orderingHeader.setWidthFull();
        orderingHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        orderingHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        orderingHeader.getStyle()
                .set("padding", "0 0 16px 0")
                .set("margin", "0");

        Icon orderIcon = new Icon(VaadinIcon.ALIGN_LEFT);
        orderIcon.setColor("#7c3aed");
        orderIcon.setSize("18px");

        Span orderingText = new Span("Ordering");
        orderingText.getStyle()
                .set("color", "#7c3aed")
                .set("font-weight", "500")
                .set("font-size", "16px")
                .set("margin-left", "8px");

        orderingHeader.add(orderIcon, orderingText);

        // Main form container
        VerticalLayout formSection = new VerticalLayout();
        formSection.setPadding(false);
        formSection.setSpacing(false);
        formSection.setWidthFull();
        formSection.getStyle()
                .set("background-color", "#ffffff")
                .set("border-radius", "8px")
                .set("padding", "20px")
                .set("box-shadow", "0 1px 3px rgba(0,0,0,0.1)")
                .set("border", "1px solid #e5e7eb");

        // Name field
        nameField = new TextField("Name");
        nameField.addClassName("custom-textfield");
        nameField.setWidthFull();
        nameField.getStyle()
                .set("margin-bottom", "16px");

        // Row with 4 combo boxes
        HorizontalLayout comboRow = new HorizontalLayout();
        comboRow.setWidthFull();
        comboRow.setSpacing(true);
        comboRow.setAlignItems(FlexComponent.Alignment.END);
        comboRow.getStyle().set("margin-bottom", "16px");

        statusCombo = new ComboBox<>();
        statusCombo.addClassName("custom-textfield");
        statusCombo.setPlaceholder("Status");
        statusCombo.setItems("Siswa", "Guru", "Lainnya");
        statusCombo.setWidth("120px");

        gradeCombo = new ComboBox<>();
        gradeCombo.addClassName("custom-textfield");
        gradeCombo.setItems("A", "B", "C", "D");
        gradeCombo.setPlaceholder("Grade");
        gradeCombo.setWidth("120px");

        classCombo = new ComboBox<>();
        classCombo.addClassName("custom-textfield");
        classCombo.setItems("Electronics", "Furniture", "Vehicles");
        classCombo.setPlaceholder("Class");
        classCombo.setWidth("120px");

        locationCombo = new ComboBox<>();
        locationCombo.addClassName("custom-textfield");
        var listLocations = locationController.getListLocations();
        locationCombo.setItems(listLocations);
        locationCombo.setItemLabelGenerator(Location::getLocationName);
        locationCombo.setPlaceholder("Location");
        locationCombo.setWidth("120px");

        locationCombo.addValueChangeListener(event -> {
            selectedLocation = event.getValue();
        });

        comboRow.add(statusCombo, gradeCombo, classCombo, locationCombo);

        // Date time row
        HorizontalLayout dateTimeRow = new HorizontalLayout();
        dateTimeRow.setWidthFull();
        dateTimeRow.setSpacing(true);
        dateTimeRow.setAlignItems(FlexComponent.Alignment.END);
        dateTimeRow.getStyle().set("margin-bottom", "16px");

        // Start date picker
        startDateField = new DateTimePicker();
        startDateField.setDatePlaceholder("dd/mm/yy");
        startDateField.setTimePlaceholder("00:00 AM");
        startDateField.setWidth("250px");
        startDateField.addClassName("custom-textfield");

        // Arrow icon between dates
        Icon arrowIcon = new Icon(VaadinIcon.ARROW_RIGHT);
        arrowIcon.setSize("16px");
        arrowIcon.getStyle()
                .set("color", "#6b7280")
                .set("margin", "0 8px")
                .set("align-self", "center");

        // End date picker
        endDateField = new DateTimePicker();
        endDateField.setDatePlaceholder("dd/mm/yy");
        endDateField.setTimePlaceholder("00:00 AM");
        endDateField.setWidth("250px");
        endDateField.addClassName("custom-textfield");

        dateTimeRow.add(startDateField, arrowIcon, endDateField);

        // Note field
        noteField = new TextArea("Note");
        noteField.addClassName("custom-textfield");
        noteField.setWidthFull();
        noteField.setHeight("80px");
        noteField.getStyle()
                .set("resize", "vertical")
                .set("min-height", "80px");

        // Add all components to form section
        formSection.add(orderingHeader, nameField, comboRow, dateTimeRow, noteField);

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
        historySearch.setPlaceholder("Search history");
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

        grid.addColumn(asset -> asset.getProducts()[0].getProductName()).setHeader("Product name").setWidth("120px");
        grid.addComponentColumn(asset -> {
            VerticalLayout layout = new VerticalLayout();
            layout.setSpacing(false);
            layout.setPadding(false);
            layout.setMargin(false);
            layout.add(new Span(asset.getName()), new Span(asset.getClassName()));
            return layout;
        }).setHeader("Borrower");
        grid.addColumn(Asset::getTakeTime).setHeader("Take").setWidth("80px");
        grid.addColumn(asset -> formatDateTime(asset.getReturnTime())).setHeader("Return").setWidth("100px");

        grid.addComponentColumn(asset -> {
            Span statusBadge = new Span(asset.getStatus());
            if ("dikembalikan".equals(asset.getStatus())) {
                statusBadge.getElement().getThemeList().add("badge contrast");
            } else if ("dipinjam".equals(asset.getStatus())) {
                statusBadge.getElement().getThemeList().add("badge success");
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
        List<Asset> assets = assetController.getListAsset();

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
        nameSearch.setPlaceholder("Search assets");
        nameSearch.addClassName("custom-textfield");
        nameSearch.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        nameSearch.getStyle()
                .set("margin", "0");

        nameSearch.addValueChangeListener(e -> {
//            refreshGridProduct(allProducts);
            List<Products> filtered = allProducts.stream()
                .filter(product -> product.getProductName().toLowerCase().contains(e.getValue().toLowerCase()))
                .toList();

            refreshGridProduct(filtered);
        });

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

        Button allBtn = createCategoryButton("All", false);
        List<Category> listCategory = categoryController.getListCategory();

        filters.add(allBtn);
        allBtn.addClickListener(e -> refreshGridProduct(allProducts));
        listCategory.forEach(category -> {
            Button categoryBtn = createCategoryButton(category.getCategoryName(), false);
            categoryBtn.addClickListener(e -> {
                List<Products> filtered = allProducts.stream()
                    .filter(product -> product.getCategoryName().equals(category.getCategoryName()))
                    .collect(Collectors.toList());

                refreshGridProduct(filtered);
            });
            filters.add(categoryBtn);
        });

        return filters;
    }

    private Button createCategoryButton(String text, boolean active) {
        Button btn = new Button();
        btn.setWidth("auto"); // Let buttons size based on content
        btn.getStyle()
                .set("cursor", "pointer");

        HorizontalLayout content = new HorizontalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setSpacing(true);
        content.setPadding(false);

        Span textSpan = new Span(text);
        textSpan.getStyle()
                .set("font-size", "12px")
                .set("white-space", "nowrap"); // Prevent text wrapping

        content.add(textSpan);
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
        gridSection = new VerticalLayout();
        gridSection.setPadding(false);
        gridSection.setSpacing(true);
        gridSection.getStyle()
                .set("overflow-y", "auto")  // Enable vertical scrolling
                .set("max-height", "575px") // Set max height for scrollable area
                .set("margin-bottom", "16px");

        refreshGridProduct(allProducts);

        return gridSection;
    }

    private void refreshGridProduct(List<Products> listProducts) {
        gridSection.removeAll();

        // Group products by category
        Map<String, List<Products>> productsByCategory = listProducts.stream()
            .collect(Collectors.groupingBy(Products::getCategoryName));

        // Create sections for each category
        for (Map.Entry<String, List<Products>> entry : productsByCategory.entrySet()) {
            String categoryName = entry.getKey();
            List<Products> categoryProducts = entry.getValue();

            // Convert Products to HorizontalLayout cards (changed from VerticalLayout)
            List<HorizontalLayout> productCards = categoryProducts.stream()
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
    }

    private VerticalLayout createCategorySection(String categoryName, List<HorizontalLayout> products) {
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

        // Changed to vertical layout for stacked cards
        VerticalLayout productGrid = new VerticalLayout();
        productGrid.setPadding(false);
        productGrid.setSpacing(true);
        productGrid.getStyle()
                .set("gap", "8px");

        for (HorizontalLayout product : products) {
            productGrid.add(product);
        }

        section.add(categoryTitle, productGrid);
        return section;
    }

    private HorizontalLayout createProductCard(String id, String name, String category, String status, int stock, List<SelectedProduct> selectionTracker) {
        HorizontalLayout card = new HorizontalLayout();
        card.setPadding(true);
        card.setSpacing(true);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setWidthFull();
        card.setHeight("70px");
        card.getStyle()
                .set("cursor", "pointer")
                .set("border", "1px solid #e5e7eb")
                .set("border-radius", "8px")
                .set("background-color", "#ffffff")
                .set("padding", "12px 16px")
                .set("transition", "all 0.2s ease")
                .set("position", "relative");

        // Hover effect
        card.getElement().executeJs(
                "this.addEventListener('mouseenter', () => {" +
                        "  this.style.backgroundColor = '#f9fafb';" +
                        "  this.style.borderColor = '#d1d5db';" +
                        "  this.style.boxShadow = '0px 10px 10px -3px rgba(0,0,0,0.1)';" +
                        "});" +
                        "this.addEventListener('mouseleave', () => {" +
                        "  this.style.backgroundColor = '#ffffff';" +
                        "  this.style.borderColor = '#e5e7eb';" +
                        "  this.style.boxShadow = 'none';" +
                        "});"
        );

        // Product icon (left side)
        Div iconContainer = new Div();
        iconContainer.getStyle()
                .set("width", "40px")
                .set("height", "40px")
                .set("background-color", "#f3f4f6")
                .set("border-radius", "6px")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("flex-shrink", "0");

        // Icon based on category (you can customize this)
        Span icon = new Span();
        icon.getStyle()
                .set("font-size", "20px");

        // Simple icon mapping based on category
        if (category.toLowerCase().contains("audio") || category.toLowerCase().contains("speaker")) {
            icon.setText("🔊");
        } else if (category.toLowerCase().contains("computer") || category.toLowerCase().contains("laptop")) {
            icon.setText("💻");
        } else if (category.toLowerCase().contains("monitor")) {
            icon.setText("🖥️");
        } else {
            icon.setText("📦");
        }

        iconContainer.add(icon);

        // Product info section (middle)
        VerticalLayout infoSection = new VerticalLayout();
        infoSection.setPadding(false);
        infoSection.setSpacing(false);
        infoSection.setWidthFull();
        infoSection.getStyle()
                .set("margin-left", "12px")
                .set("flex-grow", "1");

        // Product name
        Span nameSpan = new Span(name);
        nameSpan.getStyle()
                .set("font-weight", "600")
                .set("font-size", "14px")
                .set("color", "#111827")
                .set("line-height", "1.2")
                .set("margin-bottom", "2px");

        // Category and status
        HorizontalLayout metaInfo = new HorizontalLayout();
        metaInfo.setPadding(false);
        metaInfo.setSpacing(true);
        metaInfo.setAlignItems(FlexComponent.Alignment.CENTER);
        metaInfo.getStyle().set("margin-top", "2px");

        Span categorySpan = new Span(category);
        categorySpan.getStyle()
                .set("font-size", "12px")
                .set("color", "#6b7280");

        // Status badge
        Span statusBadge = new Span(status);
        if (status.equals("Ready")) {
            statusBadge.getStyle()
                    .set("background-color", "#dbeafe")
                    .set("color", "#1e40af");
        } else {
            statusBadge.getStyle()
                    .set("background-color", "#fee2e2")
                    .set("color", "#dc2626");
        }
        statusBadge.getStyle()
                .set("padding", "2px 6px")
                .set("font-size", "10px")
                .set("border-radius", "4px")
                .set("font-weight", "500");

        metaInfo.add(categorySpan, statusBadge);
        infoSection.add(nameSpan, metaInfo);

        // Quantity section (initially hidden)
        HorizontalLayout qtySection = new HorizontalLayout();
        qtySection.setVisible(false);
        qtySection.setAlignItems(FlexComponent.Alignment.CENTER);
        qtySection.setSpacing(true);
        qtySection.getStyle()
                .set("margin-right", "12px");

        Span qtyLabel = new Span("Qty");
        qtyLabel.getStyle()
                .set("font-size", "12px")
                .set("color", "#6b7280");

        IntegerField qtyCombo = new IntegerField();
        qtyCombo.setStepButtonsVisible(true);
        qtyCombo.setRequiredIndicatorVisible(true);
        qtyCombo.setMin(1);
        qtyCombo.setWidth("100px");
        qtyCombo.getElement().executeJs(
                "this.addEventListener('click', function(e) { e.stopPropagation(); });"
        );

        qtySection.add(qtyLabel, qtyCombo);

        // Checkbox (right side)
        Checkbox checkbox = new Checkbox();
        checkbox.getStyle()
                .set("margin-left", "auto")
                .set("flex-shrink", "0");

        // Highlight card when checked
        checkbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                card.getStyle()
                        .set("border-color", "#8b5cf6")
                        .set("background-color", "#faf5ff");
            } else {
                card.getStyle()
                        .set("border-color", "#e5e7eb")
                        .set("background-color", "#ffffff");
            }
            qtySection.setVisible(event.getValue());
        });

        checkbox.getElement().executeJs(
                "this.addEventListener('click', function(e) { e.stopPropagation(); });"
        );

        // Add all components to card
        card.add(iconContainer, infoSection, qtySection, checkbox);

        // Click handler for the card
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

        pinCodeField = new PinCodeField(4);

        // Submit button
        Button btnDialog = new Button("Submit");

        Dialog pinDialog = new Dialog();
        pinDialog.setHeaderTitle("Buat Pin Keamananmu");
        VerticalLayout pinDialogLayout = createPinDialogLayout();

        pinDialog.add(pinDialogLayout);

        Button submitBtnForm = new Button("Submit");
        submitBtnForm.addClickListener(event -> {
            if (pinCodeField.isEmpty()) {
                return;
            }
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
            Asset submitedAsset = new Asset(
                "0", startDateField.getValue(), endDateField.getValue(), "",
                pinCodeField.getValue(), noteField.getValue(), selectedLocation, productArray, statusCombo.getValue(), nameField.getValue(), classCombo.getValue()
            );

            var result = assetController.InsertAsset(submitedAsset);

            if (result) {
                pinDialog.close();
                emptyForm();
            }

        });
        submitBtnForm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitBtnForm.setWidthFull();
        submitBtnForm.getStyle()
            .set("background-color", "#7c3aed")
            .set("border-radius", "8px")
            .set("padding", "10px 24px");
        submitBtnForm.addClickListener(event -> {
        });

        pinDialog.getFooter().add(submitBtnForm);

        btnDialog.addClickListener(event -> {
            if (validateRequiredFields()) {
                pinDialog.open();
            }
        });
        btnDialog.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnDialog.getStyle()
            .set("background-color", "#7c3aed")
            .set("border-radius", "8px")
            .set("padding", "10px 24px");

        bottom.add(btnDialog);
        return bottom;
    }

    private VerticalLayout createPinDialogLayout() {
        VerticalLayout mainLayout = new VerticalLayout();

        mainLayout.add(new Span("Agar proses peminjaman barang lebih aman, silakan buat PIN unik yang hanya kamu yang tahu."));

        HorizontalLayout pinLayout = new HorizontalLayout();
        pinLayout.setWidthFull();
        pinLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        pinLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        pinLayout.add(pinCodeField);
        mainLayout.add(pinLayout);

        return mainLayout;
    }

    private boolean validateRequiredFields() {
        boolean isValid = true;

        if (nameField.isEmpty()) {
            nameField.setInvalid(true);
            nameField.setErrorMessage("Name is required");
            isValid = false;
        }

        if (startDateField.isEmpty()) {
            startDateField.setInvalid(true);
            startDateField.setErrorMessage("Start date is required");
            isValid = false;
        }

        if (endDateField.isEmpty()) {
            endDateField.setInvalid(true);
            endDateField.setErrorMessage("End date is required");
            isValid = false;
        }

        if (selectedLocation == null) {
            // Assuming there's a way to flag the location field as invalid
            // setFieldInvalid(locationField, "Location is required");
            isValid = false;
        }

        if (statusCombo.isEmpty()) {
            statusCombo.setInvalid(true);
            statusCombo.setErrorMessage("Status is required");
            isValid = false;
        }

        if (classCombo.isEmpty()) {
            classCombo.setInvalid(true);
            classCombo.setErrorMessage("Class is required");
            isValid = false;
        }

        return isValid;
    }


    public void emptyForm() {
        nameField.clear();
        startDateField.clear();
        endDateField.clear();
        selectedLocation = null;
        statusCombo.clear();
        classCombo.clear();
        pinCodeField.clear();
        noteField.clear();

        selectedProducts.clear();

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

    private void createHelpDialog() {
        helpDialog = new Dialog();
        helpDialog.setCloseOnEsc(true);
        helpDialog.setCloseOnOutsideClick(true);

        // Close button
        Button closeButton = new Button("x");
        closeButton.addClickListener(e -> helpDialog.close());
        closeButton.getStyle()
                .set("position", "absolute")
                .set("top", "12px")
                .set("right", "12px")
                .set("background", "none")
                .set("font-size", "24px")
                .set("color", "#666")
                .set("cursor", "pointer")
                .set("z-index", "1000");

        // Icon container
        Div iconContainer = new Div();
        iconContainer.getStyle()
                .set("text-align", "center")
                .set("margin-bottom", "20px");

        Span headsetIcon = new Span(new Icon(VaadinIcon.HEADSET));
        headsetIcon.getStyle()
                .set("font-size", "48px")
                .set("display", "block")
                .set("margin-bottom", "16px");

        iconContainer.add(headsetIcon);

        // Header
        H2 header = new H2("Butuh Bantuan? Hubungi Admin Layanan");
        header.getStyle()
                .set("margin", "0 0 16px 0")
                .set("color", "#333")
                .set("font-size", "1.25rem")
                .set("font-weight", "600")
                .set("text-align", "center")
                .set("line-height", "1.4");

        // Content
        Paragraph content = new Paragraph(
                "Jika Anda mengalami kendala atau memiliki pertanyaan " +
                        "seputar peminjaman barang, silakan hubungi admin melalui " +
                        "telepon atau email yang tersedia.");
        content.getStyle()
                .set("margin", "0 0 12px 0")
                .set("color", "#666")
                .set("font-size", "0.9rem")
                .set("line-height", "1.5")
                .set("text-align", "center");

        // Contact info container
        Div contactContainer = new Div();
        contactContainer.getStyle()
                .set("text-align", "center")
                .set("margin-top", "10px");

        // Phone number
        Div phone = new Div(new Text("(+82) 88972349289847"));
        phone.getStyle()
                .set("margin", "8px 0")
                .set("font-weight", "500")
                .set("font-size", "1rem")
                .set("color", "#333");

        // Email
        Div email = new Div(new Text("24assets@gmail.com"));
        email.getStyle()
                .set("margin", "8px 0")
                .set("font-weight", "400")
                .set("font-size", "0.9rem")
                .set("color", "#888");

        contactContainer.add(phone, email);

        // Main container with relative positioning for close button
        Div mainContainer = new Div();
        mainContainer.getStyle()
                .set("position", "relative")
                .set("width", "100%");

        mainContainer.add(closeButton);

        // Layout
        VerticalLayout dialogLayout = new VerticalLayout(
                iconContainer,
                header,
                content,
                contactContainer
        );

        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        dialogLayout.getStyle()
                .set("max-width", "500px")
                .set("width", "100%")
                .set("padding", "22px 24px")
                .set("font-family", "'Poppins', sans-serif")
                .set("background", "white")
                .set("border-radius", "15px");

        mainContainer.add(dialogLayout);
        helpDialog.add(mainContainer);

        // Optional: Add overlay styling
        helpDialog.getElement().getStyle()
                .set("--lumo-surface-color", "white");
    }

    public static void showMainView() {
        UI.getCurrent().navigate(OrderingView.class);
    }
}