/**
 * 
 */
package com.danielkim.gomokuAI.fx;

import java.io.IOException;

import com.danielkim.gomokuAI.ai.GomokuAI;
import com.danielkim.gomokuAI.ai.alphabeta.GomokuAlphaBetaPruning;
import com.danielkim.gomokuAI.ai.mcts.GomokuMCTS;
import com.danielkim.gomokuAI.model.Chessboard;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

/**
 * gomoku controller.
 * 
 * @author Daniel Kim
 * @date 5/25/14
 */
public class GomokuController {

    /**
     * black player choice box.
     */
    @FXML
    private ChoiceBox<String> blackPlayerChoiceBox;

    /**
     * white player choice box.
     */
    @FXML
    private ChoiceBox<String> whitePlayerChoiceBox;

    /**
     * announcement text area.
     */
    @FXML
    private TextArea announcementTextArea;

    /**
     * chessboard grid pane.
     */
    @FXML
    private GridPane chessboardGridPane;

    /**
     * GomokuManager.
     */
    private GomokuFXManager gomokuManager;

    /**
     * 
     * initialize.
     * 
     * @throws IOException
     */
    @FXML
    public void initialize() throws IOException {
	this.gomokuManager = new GomokuFXManager();

	// initialize choice box.
	if (this.blackPlayerChoiceBox.getItems().isEmpty()) {
	    this.blackPlayerChoiceBox.setItems(UIConstant.CHOICE_BOX_CHOICES);
	    this.blackPlayerChoiceBox.getSelectionModel().select(0);
	}
	if (this.whitePlayerChoiceBox.getItems().isEmpty()) {
	    this.whitePlayerChoiceBox.setItems(UIConstant.CHOICE_BOX_CHOICES);
	    this.whitePlayerChoiceBox.getSelectionModel().select(1);
	}

	// set strategy according to the choice box.
	this.setPlayerStrategy();

	// add listener when current play changed.
	SimpleObjectProperty<Class<? extends GomokuAI>> currentPlayerProperty = this.gomokuManager
		.getCurrentPlayerProperty();
	this.makeStrategyMove(currentPlayerProperty);
	currentPlayerProperty.addListener(event -> this.makeStrategyMove(currentPlayerProperty));

	// add listener to update announcement when chessboard state is changed.
	this.announcementTextArea.setEditable(false);
	SimpleObjectProperty<ChessboardState> chessboardStateProperty = this.gomokuManager.getChessboardStateProperty();
	this.setAnnouncement(chessboardStateProperty.get());
	chessboardStateProperty.addListener(event -> this.setAnnouncement(chessboardStateProperty.get()));

	// add each cell pane to grid pane.
	this.chessboardGridPane.getChildren().clear();
	for (int row = 0; row < Chessboard.DEFAULT_SIZE; ++row) {
	    for (int column = 0; column < Chessboard.DEFAULT_SIZE; ++column) {
		this.chessboardGridPane.getChildren().add(new GomokuCellPane(row, column, this.gomokuManager));
	    }
	}

    }

    /**
     * 
     * set play's strategy, computer or human role.
     */
    private void setPlayerStrategy() {
	Class<? extends GomokuAI> blackPlayerStrategyClass = UIConstant.COMPUTER_PLAYER.equals(
		this.blackPlayerChoiceBox.getSelectionModel().getSelectedItem()) ? GomokuAlphaBetaPruning.class : null;
	Class<? extends GomokuAI> whitePlayerStrategyClass = UIConstant.COMPUTER_PLAYER.equals(
		this.whitePlayerChoiceBox.getSelectionModel().getSelectedItem()) ? GomokuAlphaBetaPruning.class : null;
	this.gomokuManager.setBlackPlayerStrategyClass(blackPlayerStrategyClass);
	this.gomokuManager.setWhitePlayerStrategyClass(whitePlayerStrategyClass);
	this.gomokuManager.getCurrentPlayerProperty().set(blackPlayerStrategyClass);
    }

    /**
     * 
     * make move if current player is computer.
     * 
     * @param currentPlayerProperty
     *            current player's property.
     */
    private void makeStrategyMove(SimpleObjectProperty<Class<? extends GomokuAI>> currentPlayerProperty) {
	if (null != currentPlayerProperty.get()) {
	    this.gomokuManager.makeStrategyMove();
	}
    }

    /**
     * 
     * set announcement to text area according to chessboard state.
     * 
     * @param chessboardState
     *            chessboard state.
     */
    private void setAnnouncement(ChessboardState chessboardState) {
	String announcementString;
	switch (chessboardState) {
	case GAME_DRAW:
	    announcementString = UIConstant.GAME_DRAW;
	    break;
	case BLACK_WIN:
	    announcementString = UIConstant.BLACK_WIN;
	    break;
	case WHITE_WIN:
	    announcementString = UIConstant.WHITE_WIN;
	    break;
	case GAME_ON:
	default:
	    announcementString = UIConstant.GAME_ON;
	    break;
	}
	this.announcementTextArea.setText(announcementString);
    }

    /**
     * 
     * the method is called when click 'NEW GAME'.
     * 
     * @throws IOException
     */
    @FXML
    public void newGame() throws IOException {
	this.initialize();
    }

}
