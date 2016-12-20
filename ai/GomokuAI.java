/**
 * 
 */
package com.danielkim.gomokuAI.ai;

import java.awt.Point;

import com.danielkim.gomokuAI.model.ChessType;
import com.danielkim.gomokuAI.model.Chessboard;

/**
 * Gomoku AI interface.
 * 
 * @author Daniel Kim
 *
 */
public interface GomokuAI {

    /**
     * 
     * get next move.
     * 
     * @param chessboard
     *            Chessboard.
     * @param chessType
     *            the chess type to move.
     * @return the point to move.
     */
    Point next(Chessboard chessboard, ChessType chessType);

}
