package tars.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tars.commons.util.FxViewUtil;

/**
 * A ui for the results display that is displayed above the command box of the application.
 */
public class ResultDisplay extends UiPart {
    private static final double BOUNDARY_PARAMETERS_ZERO = 0.0;
    public static final String RESULT_DISPLAY_ID = "resultDisplay";
    private static final String RESULT_DISPLAY_STYLE_SHEET = "result-display";
    private TextArea resultDisplayArea;
    private final StringProperty displayed = new SimpleStringProperty("");

    private static final String FXML = "ResultDisplay.fxml";

    private AnchorPane placeHolder;

    private AnchorPane mainPane;

    public static ResultDisplay load(Stage primaryStage,
            AnchorPane placeHolder) {
        ResultDisplay resultDisplay = UiPartLoader.loadUiPart(primaryStage,
                placeHolder, new ResultDisplay());
        resultDisplay.configure();
        return resultDisplay;
    }

    public void configure() {
        resultDisplayArea = new TextArea();
        resultDisplayArea.setEditable(false);
        resultDisplayArea.setId(RESULT_DISPLAY_ID);
        resultDisplayArea.getStyleClass().removeAll();
        resultDisplayArea.getStyleClass().add(RESULT_DISPLAY_STYLE_SHEET);
        resultDisplayArea.setText("");
        resultDisplayArea.textProperty().bind(displayed);
        FxViewUtil.applyAnchorBoundaryParameters(resultDisplayArea,
                BOUNDARY_PARAMETERS_ZERO, BOUNDARY_PARAMETERS_ZERO,
                BOUNDARY_PARAMETERS_ZERO, BOUNDARY_PARAMETERS_ZERO);
        mainPane.getChildren().add(resultDisplayArea);
        FxViewUtil.applyAnchorBoundaryParameters(mainPane,
                BOUNDARY_PARAMETERS_ZERO, BOUNDARY_PARAMETERS_ZERO,
                BOUNDARY_PARAMETERS_ZERO, BOUNDARY_PARAMETERS_ZERO);
        placeHolder.getChildren().add(mainPane);
    }

    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public void postMessage(String message) {
        displayed.setValue(message);
    }

}
