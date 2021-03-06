package tars.ui;

import java.util.ArrayList;

import edu.emory.mathcs.backport.java.util.Collections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import tars.commons.util.StringUtil;
import tars.model.task.DateTime;
import tars.model.task.rsv.RsvTask;
import tars.ui.formatter.Formatter;

// @@author A0121533W
/**
 * UI Controller for Reserve Task Card
 */
public class RsvTaskCard extends UiPart {

    private static final int PREF_SIZE_HEIGHT = 75;
    private static final int PREF_SIZE_WIDTH = 200;
    private static final String DATE_TIME_LIST_AREA = "dateTimeListArea";
    private static final String FXML = "RsvTaskListCard.fxml";
    private static final String DATETIMELIST_ID = "dateTimeList";

    private TextArea dateTimeListArea;
    private AnchorPane dateTimeListPane;

    private StringProperty dateTimeListdisplayed =
            new SimpleStringProperty(StringUtil.EMPTY_STRING);

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;

    private RsvTask rsvTask;
    private int displayedIndex;

    public RsvTaskCard() {

    }

    public static RsvTaskCard load(RsvTask rsvTask, int displayedIndex) {
        RsvTaskCard card = new RsvTaskCard();
        card.cardPane = new HBox();
        card.dateTimeListPane = new AnchorPane();

        card.rsvTask = rsvTask;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        name.setText(rsvTask.getName().taskName);
        id.setText(displayedIndex + StringUtil.STRING_FULLSTOP);
        setDateTimeList();
        configure();
    }

    public void configure() {
        dateTimeListArea = new TextArea();
        dateTimeListArea.setEditable(false);
        dateTimeListArea.setId(DATETIMELIST_ID);
        dateTimeListArea.getStyleClass().removeAll();
        dateTimeListArea.getStyleClass().add(DATE_TIME_LIST_AREA);
        dateTimeListArea.setWrapText(true);
        dateTimeListArea.setPrefSize(PREF_SIZE_WIDTH, PREF_SIZE_HEIGHT);
        dateTimeListArea.textProperty().bind(dateTimeListdisplayed);
        dateTimeListArea.autosize();

        dateTimeListPane.getChildren().add(dateTimeListArea);
        cardPane.getChildren().add(dateTimeListPane);
    }

    private void setDateTimeList() {
        ArrayList<DateTime> dateTimeList = rsvTask.getDateTimeList();
        Collections.sort(dateTimeList);
        String toSet = Formatter.formatDateTimeList(dateTimeList);
        dateTimeListdisplayed.setValue(toSet);
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox) node;
        dateTimeListPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
