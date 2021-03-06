package tars.model.tag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tars.commons.core.Messages;
import tars.commons.exceptions.DuplicateDataException;
import tars.commons.exceptions.IllegalValueException;
import tars.commons.util.CollectionUtil;

import java.util.*;

/**
 * A list of tags that enforces no nulls and uniqueness between its elements.
 *
 * Supports minimal set of list operations for the app's features.
 *
 * @see Tag#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTagList implements Iterable<Tag> {
    
    private static final int INVALID_INDEX = -1;
    private final ObservableList<Tag> internalList = FXCollections.observableArrayList();

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateTagException extends DuplicateDataException {
        protected DuplicateTagException() {
            super(Messages.MESSAGE_DUPLICATE_TAG);
        }
    }
    
    /**
     * Signals that tag does not exist in the list.
     */
    public static class TagNotFoundException extends Exception {
        protected TagNotFoundException() {
            super(Messages.MESSAGE_TAG_CANNOT_BE_FOUND);
        }
    }

    /**
     * Constructs empty TagList.
     */
    public UniqueTagList() {}

    /**
     * Varargs/array constructor, enforces no nulls or duplicates.
     */
    public UniqueTagList(Tag... tags) throws DuplicateTagException {
        assert !CollectionUtil.isAnyNull((Object[]) tags);
        final List<Tag> initialTags = Arrays.asList(tags);
        if (!CollectionUtil.elementsAreUnique(initialTags)) {
            throw new DuplicateTagException();
        }
        internalList.addAll(initialTags);
    }

    /**
     * java collections constructor, enforces no null or duplicate elements.
     */
    public UniqueTagList(Collection<Tag> tags) throws DuplicateTagException {
        CollectionUtil.assertNoNullElements(tags);
        if (!CollectionUtil.elementsAreUnique(tags)) {
            throw new DuplicateTagException();
        }
        internalList.addAll(tags);
    }

    /**
     * java set constructor, enforces no nulls.
     */
    public UniqueTagList(Set<Tag> tags) {
        CollectionUtil.assertNoNullElements(tags);
        internalList.addAll(tags);
    }

    /**
     * Copy constructor, insulates from changes in source.
     */
    public UniqueTagList(UniqueTagList source) {
        internalList.addAll(source.internalList); // insulate internal list from changes in argument
    }

    /**
     * All tags in this list as a Set. This set is mutable and change-insulated against the internal list.
     */
    public Set<Tag> toSet() {
        return new HashSet<>(internalList);
    }

    /**
     * Replaces the Tags in this list with those in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        this.internalList.clear();
        this.internalList.addAll(replacement.internalList);
    }

    /**
     * Adds every tag from the argument list that does not yet exist in this list.
     */
    public void mergeFrom(UniqueTagList tags) {
        final Set<Tag> alreadyInside = this.toSet();
        for (Tag tag : tags) {
            if (!alreadyInside.contains(tag)) {
                internalList.add(tag);
            }
        }
    }

    /**
     * Returns true if the list contains an equivalent Tag as the given argument.
     */
    public boolean contains(Tag toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Adds a Tag to the list.
     *
     * @throws DuplicateTagException if the Tag to add is a duplicate of an existing Tag in the list.
     */
    public void add(Tag toAdd) throws DuplicateTagException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTagException();
        }
        internalList.add(toAdd);
    }
    
    /**
     * Remove a Tag from the list.
     *
     * @throws TagNotFoundException if no such tag could be found.
     */
    public void remove(Tag toRemove) throws TagNotFoundException {
        assert toRemove != null;
        if (!contains(toRemove)) {
            throw new TagNotFoundException();
        }
        internalList.remove(toRemove);
    }
    
    /**
     * Update the equivalent Tag from the list.
     *
     * @throws TagNotFoundException if no such Tag could be found in the list.
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public void update(ReadOnlyTag toBeUpdated, Tag newTag)
            throws TagNotFoundException, IllegalValueException {
        int selectedIndex = internalList.indexOf(new Tag(toBeUpdated));
        
        if (selectedIndex == INVALID_INDEX) {
            throw new TagNotFoundException();
        }
        
        if (contains(newTag)) {
            throw new DuplicateTagException();
        }
        
        internalList.set(selectedIndex, newTag);
    }

    @Override
    public Iterator<Tag> iterator() {
        return internalList.iterator();
    }

    public ObservableList<Tag> getInternalList() {
        return internalList;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTagList // instanceof handles nulls
                        && this.internalList.containsAll(((UniqueTagList) other).internalList)
                        && ((UniqueTagList) other).internalList.containsAll(this.internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
