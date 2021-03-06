package tars.commons.core;

import com.google.common.eventbus.EventBus;

import tars.commons.events.BaseEvent;

import java.util.logging.Logger;

/**
 * Manages the event dispatching of the app.
 */
public class EventsCenter {
    private static final Logger logger = LogsCenter.getLogger(EventsCenter.class);
    private static final String LOG_EVENT_POSTED =
            "------[Event Posted] %1$s: %2$s";

    private static EventsCenter instance;
    private final EventBus eventBus;

    public static EventsCenter getInstance() {
        if (instance == null) {
            instance = new EventsCenter();
        }
        return instance;
    }

    public static void clearSubscribers() {
        instance = null;
    }

    private EventsCenter() {
        eventBus = new EventBus();
    }

    public EventsCenter registerHandler(Object handler) {
        eventBus.register(handler);
        return this;
    }

    /**
     * Posts an event to the event bus.
     */
    public <E extends BaseEvent> EventsCenter post(E event) {
        logger.info(String.format(LOG_EVENT_POSTED,
                event.getClass().getCanonicalName(), event.toString()));
        eventBus.post(event);
        return this;
    }

    
}
