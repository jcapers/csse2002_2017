package planner.gui;

import planner.*;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The view for the event allocator program.
 */
public class EventAllocatorView {

    // the model of the event allocator
    private EventAllocatorModel model;
    // Observable Lists for populating views
    ObservableList<Venue> venueOptions = FXCollections.observableArrayList();
    ObservableList<String> eventsAllocated = 
    		FXCollections.observableArrayList();
    ObservableList<Traffic> trafficAllocations =
            FXCollections.observableArrayList();
    // Root node and window dimensions
    private BorderPane rootPane;
    private final int rootMaxWidth = 640;
    private final int rootMaxHeight = 480;
    // Event allocation box
    private GridPane eventAllocationBox;
    private final int eventColumns = 4;
    // Allocations and Traffic views
    private GridPane mainViews;
    private final int mainColumns = 2;
    private final int mainRows = 2;
    // Buttons
    private Button submitEvent;
    private Button deleteEvent;
    // List Views Allocations, Traffic
    private ListView<String> allocationList;
    private ListView<Traffic> trafficList;
    // Fields for Event Allocation Handling
    private TextField eventNameEntry;
    private TextField eventSizeEntry;
    private ChoiceBox<Venue> venueSelectBox;
    // Fonts and colours
    private Font headingFont = Font.font("Helvetica", FontWeight.BOLD, 20);
    private Font labelFont = Font.font("SanSerif", FontWeight.BOLD, 12);

    /**
     * Initialises the view for the event allocator program.
     * 
     * @param model
     *            the model of the event allocator
     */
    public EventAllocatorView(EventAllocatorModel model) {
        this.model = model;
        rootPane = new BorderPane();
        // Build GUI panels
        constructEventAllocation();
        constructMainView();
        setMainGridConstraints();
    }

    /**
     * Returns the scene for the event allocator application.
     * 
     * @return returns the scene for the application
     */
    public Scene getScene() {
        return new Scene(rootPane, rootMaxWidth, rootMaxHeight);
    }

    /**
     * Constructs Event Allocation box at top of window. Gives user
     * the ability to create an event and assign to a pre-loaded list of
     * possible Venues.
     */
    private void constructEventAllocation() {
    	eventAllocationBox = new GridPane();
    	eventAllocationBox.setHgap(20);
    	eventAllocationBox.setVgap(5);
    	eventAllocationBox.setPadding(new Insets(10, 10, 10, 10));
    	eventAllocationBox.setAlignment(Pos.CENTER);
    	eventAllocationBox.setStyle("-fx-background-color: #8BA3C1;");

    	// Title Label
    	Label eventHeading = new Label("Event Allocation");
    	eventHeading.setFont(headingFont);
    	eventAllocationBox.add(eventHeading, 0, 0, eventColumns, 1);

    	// Event Name Input
    	Label eventName = new Label("Event Name: ");
    	eventName.setFont(labelFont);
    	eventNameEntry = new TextField("Enter name");
    	eventNameEntry.setPrefWidth(160);
    	eventAllocationBox.add(eventName, 0, 1);
    	eventAllocationBox.add(eventNameEntry, 0, 2);

    	// Event Capacity Input
    	Label eventSize = new Label("Event Size: ");
    	eventSize.setFont(labelFont);
    	eventSizeEntry = new TextField("0");
    	eventSizeEntry.setPrefWidth(40);
    	eventAllocationBox.add(eventSize, 1, 1);
    	eventAllocationBox.add(eventSizeEntry, 1, 2);

    	// Venue Select Box
    	Label selectVenue = new Label("Venue to Allocate: ");
    	selectVenue.setFont(labelFont);
    	venueSelectBox = new ChoiceBox<>();
    	venueSelectBox.setPrefWidth(160);
    	eventAllocationBox.add(selectVenue, 2, 1);
    	eventAllocationBox.add(venueSelectBox, 2, 2);

    	// Event Submit Button
    	submitEvent = new Button("Submit Event");
    	eventAllocationBox.add(submitEvent, 3, 2);

    	// Add to Root Pane
    	rootPane.setTop(eventAllocationBox);
    }

    /**
     * Constructs main views for event allocation and traffic caused by events.
     */
    private void constructMainView() {
        mainViews = new GridPane();
        mainViews.setAlignment(Pos.CENTER);

        // Label for each view
        Label allocationView = new Label("Events Allocated");
        allocationView.setFont(headingFont);
        mainViews.add(allocationView, 0, 0);
        Label trafficView = new Label("Traffic");
        trafficView.setFont(headingFont);
        mainViews.add(trafficView, 1, 0);

        // ListViews for Allocations and Traffic
        allocationList = new ListView<>();
        mainViews.add(allocationList, 0, 1);
        trafficList = new ListView<>();
        mainViews.add(trafficList, 1, 1);

        // Delete Event Allocation Button
        deleteEvent = new Button("Remove Selected Event");
        deleteEvent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        mainViews.add(deleteEvent, 0, 2);

        // Add to Root Pane
        rootPane.setCenter(mainViews);
    }

    /**
     * Set constraints of MainView GridPane to fill entire window.
     * Adapted from lectures.
     */
    private void setMainGridConstraints() {
        // Extend columns to window
        ColumnConstraints columnConstraint = new ColumnConstraints();
        columnConstraint.setHgrow(Priority.ALWAYS);
        for (int i = 0; i < mainColumns; i++) {
            mainViews.getColumnConstraints().add(columnConstraint);
        }
        // Extend rows to window
        RowConstraints rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.ALWAYS);
        for (int i = 0; i < mainRows; i++) {
            mainViews.getRowConstraints().add(rowConstraint);
        }
    }

    /**
     * Populates Venue Selection with Venues supplied by model.
     * 
     * @param venues
     * 			List of venues in this municipality.
     */
    public void populateVenueChoices(List<Venue> venues) {
        venueOptions.addAll(venues);
        venueSelectBox.setItems(venueOptions);
    }

    /**
     * Get selected item from allocation list.
     *
     * @return  The string representing event and venue allocated.
     */
    public String getSelectedAllocation() {
        return allocationList.getSelectionModel().getSelectedItem();
    }

    /**
     * Get event name input by user.
     *
     * @return  The string entered by user.
     */
    public String getEventNameInput() {
        return eventNameEntry.getText();
    }

    /**
     * Get event size input by user.
     *
     * @return  The number entered by user.
     */
    public int getEventSizeInput() {
        return Integer.parseInt(eventSizeEntry.getText());
    }

    /**
     * Get venue selected by user.
     *
     * @return  The Venue selected by user.
     */
    public Venue getVenueChoice() {
        return venueSelectBox.getSelectionModel().getSelectedItem();
    }
    
    /**
     * Populates allocation list view with events supplied by model.
     */
    public void populateAllocationsView() {
        // Clean up view
        eventsAllocated.clear();
        allocationList.getItems().clear();
        // Get allocations and assign to view with a string representation
        Map<Event, Venue> allocatedEvents = model.getAllocations();
        allocatedEvents.forEach((event, venue) -> {
            String allocationString = event.getName() + " (Size: "
                    + event.getSize() + ") : " + venue.getName()
                    + " (Capacity: " + venue.getCapacity() + ")";
            eventsAllocated.add(allocationString);
        });
        // Update Allocations List View with sorted list
        Collections.sort(eventsAllocated);
        allocationList.getItems().addAll(eventsAllocated);
    }

    /**
     * Updates traffic information for display on Traffic View from
     * traffic tracked in model.
     */
    public void populateTrafficView() {
        trafficList.getItems().clear();
        Traffic allocatedTraffic = model.getAllocatedTraffic();
        trafficList.getItems().addAll(allocatedTraffic);
    }

    /**
     * Adds handler to Submit Event Button.
     *
     * @param handler
     *          the handler to be added by Controller.
     */
    public void addSubmitEventHandler(EventHandler<ActionEvent> handler) {
        submitEvent.setOnAction(handler);
    }

    /**
     * Adds handler to Remove Selected Event Button.
     *
     * @param handler
     *          the handler to be added by Controller.
     */
    public void addRemoveEventHandler(EventHandler<ActionEvent> handler) {
        deleteEvent.setOnAction(handler);
    }
}