package planner.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import planner.*;

import java.io.IOException;
import java.util.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * The controller for the event allocator program.
 */
public class EventAllocatorController {

    // the model of the event allocator
    private EventAllocatorModel model;
    // the view of the event allocator
    private EventAllocatorView view;

    // list of venues loaded from file to send to model
    private List<Venue> loadedVenues;

    /**
     * Initialises the controller for the event allocator program.
     * 
     * @param model
     *            the model of the event allocator
     * @param view
     *            the view of the event allocator
     */
    public EventAllocatorController(EventAllocatorModel model,
            EventAllocatorView view) {
        this.model = model;
        this.view = view;
        loadVenueFile("venues.txt");
        this.model.setVenues(loadedVenues);
        this.view.populateVenueChoices(this.model.getVenues());
        view.addSubmitEventHandler(new EventAllocationHandler());
        view.addRemoveEventHandler(new EventRemovalHandler());

    }
    
    /**
     * Loads venues from supplied venues text file.
     * Provides a list of venues for processing by model.
     * 
     * @require fileName != "" && fileName != null
     * @param fileName
     * 			The name of the text file holding venue information.
     */
    private void loadVenueFile(String fileName) {
        // Load venues from file
    	try {
			loadedVenues = new ArrayList<>(VenueReader.read(fileName));
		} catch (IOException | FormatException e) {
			System.out.println(e.toString());
			Alert alert = new Alert(AlertType.ERROR, e.toString(), 
					ButtonType.CLOSE);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.CLOSE) {
				System.exit(-1);
			}
		}
    }

    /**
     * Event handler for allocating event, upon user submitting chosen
     * allocation.
     *
     */
    private class EventAllocationHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // Get Event Name and Size inputs
            String eventName = view.getEventNameInput();
            int eventSize;
            Venue venue = view.getVenueChoice();
            try {
                eventSize = view.getEventSizeInput();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(AlertType.ERROR,
                        "Event Size must be an integer!", ButtonType.CLOSE);
                alert.showAndWait();
                return;
            }
            // If allocation is valid then we can proceed to allocation
            if (checkAllocation(eventName, eventSize, venue)) {
                Event thisEvent = model.createEvent(eventName, eventSize);
                model.allocateEvent(thisEvent, venue);
                view.populateAllocationsView();
                view.populateTrafficView();
            }
        }
    }

    /**
     * Event handler to handle removal of an allocation upon pressing
     * remove button.
     */
    private class EventRemovalHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                // Get Event Name and Size
                String allocation = view.getSelectedAllocation();
                String lineSplit[] = allocation.split("([()])");
                String sizeSplit[] = lineSplit[1].split(": ");
                String eventName = lineSplit[0].trim();
                int eventSize = Integer.parseInt(sizeSplit[1]);
                // Remove allocation and re-populate views
                model.removeAllocation(eventName, eventSize);
                view.populateAllocationsView();
                view.populateTrafficView();
            }
            catch (NullPointerException e) {
                Alert alert = new Alert(AlertType.ERROR,
                        "Please select an allocation to remove!",
                        ButtonType.CLOSE);
                alert.showAndWait();
            }


        }
    }

    /**
     * Checks if chosen allocation is valid using model validity check methods.
     *
     * Will show alert for source of error.
     *
     * @param eventName
     *          Event name set by user.
     * @param eventSize
     *          Event size set by user.
     * @param venue
     *          Venue selected by user.
     * @return true if allocation is valid, else false.
     */
    private boolean checkAllocation(String eventName, int eventSize,
                                    Venue venue) {
        // Check if event name is valid
        if (!model.validEventName(eventName)) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Event name must not be null or empty!", ButtonType.CLOSE);
            alert.showAndWait();
            return false;
        }
        // Check if event size is valid
        if (!model.validEventSize(eventSize)) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Event size must be greater than zero!", ButtonType.CLOSE);
            alert.showAndWait();
            return false;
        }
        // Check if event already allocated
        Event testEvent = new Event(eventName, eventSize);
        if (model.duplicateEvent(testEvent)) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Same event already allocated!", ButtonType.CLOSE);
            alert.showAndWait();
            return false;
        }
        // Check if venue already allocated
        if (model.duplicateVenue(venue)) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Venue already allocated!", ButtonType.CLOSE);
            alert.showAndWait();
            return false;
        }
        // Check if venue can host event
        if (!model.venueCanHost(testEvent, venue)) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Event exceeds venue capacity", ButtonType.CLOSE);
            alert.showAndWait();
            return false;
        }
        // Check traffic safey
        if (!model.trafficIsSafe(testEvent, venue)) {
            Alert alert = new Alert(AlertType.ERROR,
                    "Traffic from this allocation exceeds capacity!",
                    ButtonType.CLOSE);
            alert.showAndWait();
            return false;
        }
        // Allocation is valid
        return true;
    }

}
