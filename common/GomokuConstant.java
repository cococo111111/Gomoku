/**
 * 
 */
package com.danielkim.gomokuAI.common;

import java.awt.Point;

/**
 * constant.
 * 
 * @author Daniel Kim
 * @date 6/6/14
 */
public class GomokuConstant {

    /**
     * four directions.
     */
    public static final Point[] DIRECTIONS = { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(-1, 1) };

    /**
     * continuous number to win the game.
     */
    public static final int CONTINUOUS_NUMBER = 5;

}
