/**
 * 
 */
package com.danielkim.gomokuAI;

import java.awt.Point;
import java.util.logging.Logger;

import com.danielkim.gomokuAI.ai.GomokuAI;
import com.danielkim.gomokuAI.model.ChessType;
import com.danielkim.gomokuAI.model.Chessboard;

/**
 * entry of AI.
 * 
 * @author Daniel Kim
 * @date 6/7/2014
 */
public class gomokuAI {

    private static final Logger LOGGER = Logger.getLogger(gomokuAI.class.getName());

    /**
     * 
     * get next move.
     * 
     * @param gomokuAIClass
     *            subclass of GomokuAI.class, strategy of AI.
     * @param chessboard
     *            Chessboard.class
     * @param chessType
     *            ChessType.class
     * @return point of next move.
     */
    public static Point next(Class<? extends GomokuAI> gomokuAIClass, Chessboard chessboard, ChessType chessType) {
	try {
	    System.out.println("current player: " + chessType);
	    GomokuAI gomokuAI = gomokuAIClass.newInstance();
	    Point nextPoint = gomokuAI.next(chessboard.clone(), chessType);
	    System.out.println("player set: " + nextPoint);
	    return nextPoint;
	} catch (InstantiationException | IllegalAccessException e) {
	    LOGGER.severe(() -> e.getMessage());
	}
	return null;
    }
}
