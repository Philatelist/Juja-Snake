package com.codenjoy.dojo.snake.client;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.snake.model.Elements;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "a.usoff@gmail.com";

    private Dice dice;
    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

//        Point point = board.getApples().get(0);
//        point.getX();
//        point.getY();

//        int headX = board.getHead().getX();
//        int headY = board.getHead().getY();


        char[][] field = board.getField();

        // found snake
        int snakeHeadX = -1;
        int snakeHeadY = -1;
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field.length; y++) {
                char ch = field[x][y];
                if (ch == Elements.HEAD_DOWN.ch() ||
                    ch == Elements.HEAD_UP.ch() ||
                    ch == Elements.HEAD_LEFT.ch() ||
                    ch == Elements.HEAD_RIGHT.ch())
                {
                    snakeHeadX = x;
                    snakeHeadY = y;
                    break;

                }
            }
            if (snakeHeadX != -1) {
                break;
            }
        }

        // found apple
        int appleX = -1;
        int appleY = -1;
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field.length; y++) {
                char ch = field[x][y];
                if (ch == Elements.GOOD_APPLE.ch()) {
                    appleX = x;
                    appleY = y;
                    break;

                }
            }
            if (appleX != -1) {
                break;
            }
        }

        int dx = snakeHeadX - appleX;
        int dy = snakeHeadY - appleY;

        char rigthDirection = field[snakeHeadX + 1][snakeHeadY];
        char leftDirection = field[snakeHeadX - 1][snakeHeadY];
        char downDirection = field[snakeHeadX][snakeHeadY + 1];
        char upDirection = field[snakeHeadX][snakeHeadY - 1];

        boolean barrierDirectionRigth = toDirection(rigthDirection);
        boolean barrierDirectionLeft = toDirection(leftDirection);
        boolean barrierDirectionDown = toDirection(downDirection);
        boolean barrierDirectionUp = toDirection(upDirection);

        boolean toLeft;
        boolean toRight;
        boolean toUp;
        boolean toDown;


            //если надо вправо
        if (dx < 0) {
            if (barrierDirectionDown && barrierDirectionRigth && barrierDirectionUp) {
                return Direction.LEFT.toString();
            }
            if (dy > 0 && (barrierDirectionRigth && barrierDirectionDown && barrierDirectionLeft
                    || barrierDirectionRigth && barrierDirectionDown
                    || barrierDirectionRigth && !barrierDirectionUp)) {
                return Direction.UP.toString();
            }
            if (dy < 0 && (barrierDirectionLeft && barrierDirectionRigth && barrierDirectionUp
                    || barrierDirectionRigth && barrierDirectionUp
                    || barrierDirectionRigth && !barrierDirectionDown)) {
                return Direction.DOWN.toString();
            }
            return Direction.RIGHT.toString();
        }
            //если надо влево
        if (dx > 0) {
            if (barrierDirectionDown && barrierDirectionLeft && barrierDirectionUp) {
                return Direction.RIGHT.toString();
            }
            if (dy > 0 && (barrierDirectionRigth && barrierDirectionLeft && barrierDirectionDown
                    || barrierDirectionLeft && barrierDirectionDown
                    || barrierDirectionLeft && !barrierDirectionUp)){
                return Direction.UP.toString();
            }
            if (dy < 0 && (barrierDirectionRigth && barrierDirectionLeft && barrierDirectionUp
                    || barrierDirectionLeft && barrierDirectionUp
                    || barrierDirectionLeft && !barrierDirectionDown)) {
                return Direction.DOWN.toString();
            }
            return Direction.LEFT.toString();
        }
            //если надо вниз
        if (dy < 0) {
            if (barrierDirectionDown || barrierDirectionDown && barrierDirectionRigth) {
                return Direction.LEFT.toString();
            }
            if (barrierDirectionDown || barrierDirectionDown && barrierDirectionLeft) {
                return Direction.RIGHT.toString();
            }
            return Direction.DOWN.toString();
        }
            //если надо вверх
        if (dy > 0) {
            if (barrierDirectionUp && barrierDirectionRigth) {
                return Direction.LEFT.toString();
            }
            if (barrierDirectionUp && barrierDirectionLeft) {
                return Direction.RIGHT.toString();
            }
            return Direction.UP.toString();
        }

        return Direction.UP.toString();
    }

    private static boolean toDirection(char direction) {
        if (direction == Elements.BAD_APPLE.ch()
                || direction == Elements.TAIL_END_DOWN.ch()
                || direction == Elements.TAIL_END_LEFT.ch()
                || direction == Elements.TAIL_END_UP.ch()
                || direction == Elements.TAIL_END_RIGHT.ch()
                || direction == Elements.TAIL_HORIZONTAL.ch()
                || direction == Elements.TAIL_VERTICAL.ch()
                || direction == Elements.TAIL_LEFT_DOWN.ch()
                || direction == Elements.TAIL_LEFT_UP.ch()
                || direction == Elements.TAIL_RIGHT_DOWN.ch()
                || direction == Elements.TAIL_RIGHT_UP.ch()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        start(USER_NAME, WebSocketRunner.Host.REMOTE);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new YourSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
