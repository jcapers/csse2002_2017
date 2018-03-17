package planner.gui;

import java.util.*;
import planner.*;

/**
 * The model for the event allocator program.
 */
public class EventAllocatorModel {

    // A list of venues for this municipality
	private List<Venue> venues;
	// Map of Events and assigned Venues
	private Map<Event, Venue> allocations;
	// Current traffic caused by allocation
	private Traffic traffic;

    /**
     * Initialises the model for the event allocator program.
     */
    public EventAllocatorModel() {
    	venues = new ArrayList<>();
    	allocations = new HashMap<>();
    	traffic = new Traffic();
    }

    /**
     * Sets and records venues in municipality in venues list.
     * 
     * @require loadedVenues != null
     * @param loadedVenues
     * 			A list of venues read from file.
     */
    public void setVenues(List<Venue> loadedVenues) {
    	venues.addAll(loadedVenues);
    }
    
    /**
     * Returns list of venues in municipality.
     * 
     * @return venues : a List of venues for this municipality
     */
    public List<Venue> getVenues() {
    	return new ArrayList<>(venues);
    }
    
    /**
     * Returns mapping of allocations assigned by user.
     * 
     * @return allocations : a map of allocations currently assigned.
     */
    public Map<Event, Venue> getAllocations() {
    	return new HashMap<>(allocations);
    }

    /**
     * Remove an allocation per user instruction.
     *
     * @param name
     *          Name of event to be removed.
     * @param size
     *          Size of event to be removed.
     */
    public void removeAllocation(String name, int size) {
        // Remove from allocation if name and size matches an event allocated.
        allocations.keySet().removeIf(e ->
                e.getName().equals(name) && e.getSize() == size);
        updateTraffic();
    }

    /**
     * Returns current allocated traffic.
     *
     * @return Traffic representing current allocation's traffic.
     */
    public Traffic getAllocatedTraffic() {
        return new Traffic(traffic);
    }

    /**
     * Creates an event based on user input.
     *
     * @param name
     *          Name of the event.
     * @param size
     *          Size of the event.
     * @return  Event - An event object set by user.
     */
    public Event createEvent(String name, int size) {
        return new Event(name, size);
    }

    /**
     * Allocates event if and only if event allocation meets requirements:
     *
     * i) Event name is not ""
     * ii) Event size > 0
     * iii) Event not already allocated to a Venue
     * iv) Venue capacity >= Event size
     * v) Traffic is safe
     * 
     * That is, allocation should only occur after the requirements have been
     * confirmed.
     *
     * @param event
     *          The Event for allocation.
     * @param venue
     *          The Venue allocated to Event.
     */
    public void allocateEvent(Event event, Venue venue) {
        allocations.put(event, venue);
        updateTraffic();
    }

    /**
     * Updates traffic after each new allocation or each removal of allocation.
     *
     */
    public void updateTraffic() {
        traffic = new Traffic();
        allocations.forEach((event, venue) -> {
            traffic.addTraffic(venue.getTraffic(event));
        });
    }

    /* Methods to check validity of event and allocation */

    /**
     * Checks that event name is valid. Event name is valid if not null nor
     * an empty string "".
     * 
     * @param name
     * 			The name input by user.
     * @return true if name is valid, else false.
     */
    public boolean validEventName(String name) {
    	if (name == null || name.isEmpty()) {
    		return false;
    	}
    	return true;
    }
    
    /**
     * Checks that event is of a valid size. Event size is valid iff it is
     * greater than zero.
     * 
     * @param size
     * 			The size of the event as input by the user.
     * @return true if event size is valid, else false.
     */
    public boolean validEventSize(int size) {
    	return size > 0;
    }
    
    /**
     * Checks if event is already allocated as per Event.equals()
     * @param event
     * 			Event assigned by user.
     * @return true if duplicate event, else false
     */
    public boolean duplicateEvent(Event event) {
    	return allocations.containsKey(event);
    }

    /**
     * Checks if a venue has already been allocated an event.
     *
     * @param venue
     *          Venue selected by user for allocation.
     * @return true if venue already has allocation, else false
     */
    public boolean duplicateVenue(Venue venue) {
        return allocations.containsValue(venue);
    }

    /**
     * Checks if venue can host the event specified by the user.
     * 
     * @param event
     * 			Event specified by the user.
     * @param venue
     * 			Venue selected by the user.
     * @return true if Venue can host the event, else false.
     */
    public boolean venueCanHost(Event event, Venue venue) {
    	return venue.canHost(event);
    }
    
    /**
     * Checks if traffic caused by new allocation is safe.
     * 
     * @param event
     * 			Event to be allocated.
     * @venue venue
     * 			Venue selected for allocation.
     * @return true if adding new allocation is safe for traffic, else false
     */
    public boolean trafficIsSafe(Event event, Venue venue) {
    	Traffic trafficCheck = new Traffic(traffic);
    	trafficCheck.addTraffic(venue.getTraffic(event));
    	return trafficCheck.isSafe();
    }

}
