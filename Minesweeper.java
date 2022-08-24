import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Minesweeper extends JFrame {

    private JLabel statusbar;

    public Minesweeper() {
        initUI();
    }

    private void initUI() {

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);

        add(new Board(statusbar));

        setResizable(false);
        pack();

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var layout = new Minesweeper();
            layout.setVisible(true);
        });
    }
}

class Board extends JPanel {

    private final int NUM_OF_IMAGES = 13;
    private final int CELL_SIZE = 30;

    private final int COVER_FOR_CELL = 10;
    private final int MARK_FOR_CELL = 10;
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    private final int DRAW_MINE = 9;
    private final int DRAW_COVER = 10;
    private final int DRAW_MARK = 11;
    private final int DRAW_WRONG_MARK = 12;

    private final int NUM_OF_MINES = 40;
    private final int NUM_OF_ROWS = 16;
    private final int NUM_OF_COLS = 16;

    private final int BOARD_WIDTH = NUM_OF_COLS  * CELL_SIZE + 1;
    private final int BOARD_HEIGHT = NUM_OF_ROWS * CELL_SIZE + 1;

    private int[] gameField;
    private boolean inGame;
    private int minesLeft;
    private Image[] images;

    private int allCells;
    private final JLabel statusbar;

    public Board(JLabel statusbar) {

        this.statusbar = statusbar;
        initBoard();
    }

    private void initBoard() {

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));

        images = new Image[NUM_OF_IMAGES];

        for (int i = 0; i < NUM_OF_IMAGES; i++) {

            var path = "assets/" + i + ".png";
            images[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        newGame();
    }

    private void newGame() {

        int cell;

        var random = new Random();
        inGame = true;
        minesLeft = NUM_OF_MINES;

        allCells = NUM_OF_ROWS * NUM_OF_COLS;
        gameField = new int[allCells];

        for (int i = 0; i < allCells; i++) {

            gameField[i] = COVER_FOR_CELL;
        }

        statusbar.setText(Integer.toString(minesLeft));

        int i = 0;

        while (i < NUM_OF_MINES) {

            int position = (int) (allCells * random.nextDouble());

            if ((position < allCells) && (gameField[position] != COVERED_MINE_CELL)) {

                int current_col = position % NUM_OF_COLS;
                gameField[position] = COVERED_MINE_CELL;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - NUM_OF_COLS;
                    if (cell >= 0) {
                        if (gameField[cell] != COVERED_MINE_CELL) {
                            gameField[cell] += 1;
                        }
                    }
                    cell = position - 1;
                    if (cell >= 0) {
                        if (gameField[cell] != COVERED_MINE_CELL) {
                            gameField[cell] += 1;
                        }
                    }

                    cell = position + NUM_OF_COLS - 1;
                    if (cell < allCells) {
                        if (gameField[cell] != COVERED_MINE_CELL) {
                            gameField[cell] += 1;
                        }
                    }
                }

                cell = position - NUM_OF_COLS;
                if (cell >= 0) {
                    if (gameField[cell] != COVERED_MINE_CELL) {
                        gameField[cell] += 1;
                    }
                }

                cell = position + NUM_OF_COLS;
                if (cell < allCells) {
                    if (gameField[cell] != COVERED_MINE_CELL) {
                        gameField[cell] += 1;
                    }
                }

                if (current_col < (NUM_OF_COLS - 1)) {
                    cell = position - NUM_OF_COLS + 1;
                    if (cell >= 0) {
                        if (gameField[cell] != COVERED_MINE_CELL) {
                            gameField[cell] += 1;
                        }
                    }
                    cell = position + NUM_OF_COLS + 1;
                    if (cell < allCells) {
                        if (gameField[cell] != COVERED_MINE_CELL) {
                            gameField[cell] += 1;
                        }
                    }
                    cell = position + 1;
                    if (cell < allCells) {
                        if (gameField[cell] != COVERED_MINE_CELL) {
                            gameField[cell] += 1;
                        }
                    }
                }
            }
        }
    }

    private void findEmptyCells(int j) {

        int current_col = j % NUM_OF_COLS;
        int cell;

        if (current_col > 0) {
            cell = j - NUM_OF_COLS - 1;
            if (cell >= 0) {
                if (gameField[cell] > MINE_CELL) {
                    gameField[cell] -= COVER_FOR_CELL;
                    if (gameField[cell] == EMPTY_CELL) {
                        findEmptyCells(cell);
                    }
                }
            }

            cell = j - 1;
            if (cell >= 0) {
                if (gameField[cell] > MINE_CELL) {
                    gameField[cell] -= COVER_FOR_CELL;
                    if (gameField[cell] == EMPTY_CELL) {
                        findEmptyCells(cell);
                    }
                }
            }

            cell = j + NUM_OF_COLS- 1;
            if (cell < allCells) {
                if (gameField[cell] > MINE_CELL) {
                    gameField[cell] -= COVER_FOR_CELL;
                    if (gameField[cell] == EMPTY_CELL) {
                        findEmptyCells(cell);
                    }
                }
            }
        }

        cell = j - NUM_OF_COLS;
        if (cell >= 0) {
            if (gameField[cell] > MINE_CELL) {
                gameField[cell] -= COVER_FOR_CELL;
                if (gameField[cell] == EMPTY_CELL) {
                    findEmptyCells(cell);
                }
            }
        }

        cell = j + NUM_OF_COLS;
        if (cell < allCells) {
            if (gameField[cell] > MINE_CELL) {
                gameField[cell] -= COVER_FOR_CELL;
                if (gameField[cell] == EMPTY_CELL) {
                    findEmptyCells(cell);
                }
            }
        }

        if (current_col < (NUM_OF_COLS - 1)) {
            cell = j - NUM_OF_COLS + 1;
            if (cell >= 0) {
                if (gameField[cell] > MINE_CELL) {
                    gameField[cell] -= COVER_FOR_CELL;
                    if (gameField[cell] == EMPTY_CELL) {
                        findEmptyCells(cell);
                    }
                }
            }

            cell = j + NUM_OF_COLS + 1;
            if (cell < allCells) {
                if (gameField[cell] > MINE_CELL) {
                    gameField[cell] -= COVER_FOR_CELL;
                    if (gameField[cell] == EMPTY_CELL) {
                        findEmptyCells(cell);
                    }
                }
            }

            cell = j + 1;
            if (cell < allCells) {
                if (gameField[cell] > MINE_CELL) {
                    gameField[cell] -= COVER_FOR_CELL;
                    if (gameField[cell] == EMPTY_CELL) {
                        findEmptyCells(cell);
                    }
                }
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {

        int reveal = 0;

        for (int i = 0; i < NUM_OF_ROWS; i++) {

            for (int j = 0; j < NUM_OF_COLS; j++) {

                int cell = gameField[(i *NUM_OF_COLS) + j];

                if (inGame && cell == MINE_CELL) {
                    inGame = false;
                }

                if (!inGame) {

                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }

                } else {

                    if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        reveal++;
                    }
                }

                g.drawImage(images[cell], (j * CELL_SIZE), (i * CELL_SIZE), 30, 30, this);
            }
        }

        boolean hasWon = (inGame && reveal == 0);
        if (hasWon) {
            inGame = false;
            statusbar.setText("Congratulations you won! Click on a cell to play again");

        } else if (!inGame) {
            statusbar.setText("Sorry you lost! Click on a cell to play again");
        }
    }

    private class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            boolean doRepaint = false;

            if (!inGame) {
                newGame();
                repaint();
            }

            if ((x < NUM_OF_COLS * CELL_SIZE) && (y < NUM_OF_ROWS * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (gameField[(cRow * NUM_OF_COLS) + cCol] > MINE_CELL) {

                        doRepaint = true;

                        if (gameField[(cRow * NUM_OF_COLS) + cCol] <= COVERED_MINE_CELL) {

                            if (minesLeft > 0) {
                                gameField[(cRow * NUM_OF_COLS) + cCol] += MARK_FOR_CELL;
                                minesLeft--;
                                String msg = Integer.toString(minesLeft);
                                statusbar.setText(msg);
                            } else {
                                statusbar.setText("No marks available");
                            }
                        } else {

                            gameField[(cRow * NUM_OF_COLS) + cCol] -= MARK_FOR_CELL;
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText(msg);
                        }
                    }

                } else {

                    if (gameField[(cRow * NUM_OF_COLS) + cCol] > COVERED_MINE_CELL) 
                        return;
                    
                    if ((gameField[(cRow * NUM_OF_COLS) + cCol] < MARKED_MINE_CELL) && (gameField[(cRow * NUM_OF_COLS) + cCol] > MINE_CELL)) {

                        gameField[(cRow * NUM_OF_COLS) + cCol] -= COVER_FOR_CELL;
                        doRepaint = true;

                        if (gameField[(cRow * NUM_OF_COLS) + cCol] == MINE_CELL) 
                            inGame = false;

                        if (gameField[(cRow * NUM_OF_COLS) + cCol] == EMPTY_CELL) 
                            findEmptyCells((cRow * NUM_OF_COLS) + cCol);   
                    }
                }

                if (doRepaint) {
                    repaint();
                }
            }
        }
    }
}