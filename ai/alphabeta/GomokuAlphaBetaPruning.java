/**
 * 
 */
package com.danielkim.gomokuAI.ai.alphabeta;

import java.awt.Point;
import java.util.List;
import java.util.stream.Collectors;

import com.danielkim.gomokuAI.ai.GomokuAI;
import com.danielkim.gomokuAI.common.GomokuReferee;
import com.danielkim.gomokuAI.common.Pair;
import com.danielkim.gomokuAI.model.ChessType;
import com.danielkim.gomokuAI.model.Chessboard;

/**
 * alpha beta pruning strategy.
 * 
 * @author Daniel Kim
 * @date 5/24/14
 */
public class GomokuAlphaBetaPruning implements GomokuAI {

    /**
     * each level, find best number of point to next level.
     */
    private static final int SEARCH_WIDTH = 25;

    /**
     * the depth of search level.
     */
    private static final int SEARCH_DEPTH = 3;

    /**
     * the score when win.
     */
    private static final double MAX_SCORE = 1e20;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.danielkim.gomokuAI.ai.GomokuAI#next(com.danielkim.gomokuAI
     * .model.Chessboard, com.danielkim.gomokuAI.model.ChessType)
     */
    @Override
    public Point next(Chessboard chessboard, ChessType chessType) {
	List<Pair<Point, Double>> bestPointsList = this.getBestPoints(chessboard, chessType);
	if (bestPointsList.isEmpty()) {
	    return new Point(Chessboard.DEFAULT_SIZE / 2, Chessboard.DEFAULT_SIZE / 2);
	}

	bestPointsList.parallelStream().forEach(pair -> {
	    Chessboard newChessboard = chessboard.clone();
	    double estimate = newChessboard.setChess(pair.getFirst(), chessType) ? MAX_SCORE
		    : this.alphaBeta(0, -MAX_SCORE, MAX_SCORE, newChessboard, GomokuReferee.nextChessType(chessType));
	    pair.setSecond(estimate);
	});
	double bestEstimate = bestPointsList.stream().map(pair -> pair.getSecond()).max((a, b) -> Double.compare(a, b))
		.get();
	List<Pair<Point, Double>> resultPointsList = bestPointsList.stream()
		.filter(pair -> Math.abs(bestEstimate - pair.getSecond()) < 1e-6).collect(Collectors.toList());
	int randomIndex = GomokuReferee.RANDOM.nextInt(resultPointsList.size());
	return resultPointsList.get(randomIndex).getFirst();
    }

    private final List<Pair<Point, Double>> getBestPoints(Chessboard chessboard, ChessType chessType) {
	List<Point> pointList = GomokuAlphaBetaPruningUtils.getEmptyPoints(chessboard);
	return pointList.parallelStream().map(point -> {
	    Chessboard newChessboard = chessboard.clone();
	    double estimate = newChessboard.setChess(point, chessType) ? MAX_SCORE
		    : GomokuAlphaBetaPruningUtils.getGlobalEstimate(newChessboard, chessType);
	    return new Pair<Point, Double>(point, estimate);
	}).sorted((a, b) -> Double.compare(b.getSecond(), a.getSecond())).limit(SEARCH_WIDTH)
		.collect(Collectors.toList());
    }

    private final double alphaBeta(int depth, double alpha, double beta, Chessboard chessboard, ChessType chessType) {
	if (SEARCH_DEPTH == depth) {
	    return GomokuAlphaBetaPruningUtils.getGlobalEstimate(chessboard, chessType);
	} else {
	    ChessType nextChessType = GomokuReferee.nextChessType(chessType);
	    List<Pair<Point, Double>> bestPointsList = this.getBestPoints(chessboard, chessType);
	    for (Pair<Point, Double> bestPoint : bestPointsList) {
		Point point = bestPoint.getFirst();
		if ((depth & 1) == 0) {
		    if (chessboard.setChess(point, chessType)) {
			beta = -MAX_SCORE;
		    } else {
			beta = Math.min(beta, this.alphaBeta(depth + 1, alpha, beta, chessboard, nextChessType));
		    }
		    chessboard.setChess(point, ChessType.EMPTY);
		    if (beta <= alpha) {
			break;
		    }
		} else {
		    if (chessboard.setChess(point, chessType)) {
			alpha = MAX_SCORE;
		    } else {
			alpha = Math.max(alpha, this.alphaBeta(depth + 1, alpha, beta, chessboard, nextChessType));
		    }
		    chessboard.setChess(point, ChessType.EMPTY);
		    if (alpha >= beta) {
			break;
		    }
		}
	    }
	    return ((depth & 1) == 0 ? beta : alpha) * 0.99;
	}
    }
}
