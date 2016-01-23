package com.winbit.windoctor.web.rest.dto;

import com.winbit.windoctor.domain.Event;
import com.winbit.windoctor.domain.Fund;
import com.winbit.windoctor.domain.Supply_type;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Created by achraftalbi on 12/5/15.
 */
public class EventDTO {
    private List<Event> eventList;
    private List<String> startDateList;
    private List<String> endDateList;
    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<String> getEndDateList() {
        return endDateList;
    }

    public void setEndDateList(List<String> endDateList) {
        this.endDateList = endDateList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<String> getStartDateList() {
        return startDateList;
    }

    public void setStartDateList(List<String> startDateList) {
        this.startDateList = startDateList;
    }
}
