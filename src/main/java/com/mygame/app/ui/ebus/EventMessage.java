package com.mygame.app.ui.ebus;

public class EventMessage {
    private EventType eventType;


    public EventMessage(EventType type) {
        this.eventType = type;
    }

    public EventType getEventType() {
        return eventType;
    }
}
