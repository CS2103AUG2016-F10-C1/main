package tars.testutil;

import tars.commons.exceptions.IllegalValueException;
import tars.model.task.*;
import tars.model.tag.Tag;

/**
 *
 */
public class TaskBuilder {

    private TestTask task;

    public TaskBuilder() {
        this.task = new TestTask();
    }

    public TaskBuilder withName(String name) throws IllegalValueException {
        this.task.setName(new Name(name));
        return this;
    }

    public TaskBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            task.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TaskBuilder withDateTime(String dateTime) throws IllegalValueException {
        this.task.setDateTime(new DateTime(dateTime, ""));
        return this;
    }

    public TaskBuilder withPriority(String priority) throws IllegalValueException {
        this.task.setPriority(new Priority(priority));
        return this;
    }

    public TestTask build() {
        return this.task;
    }

}
