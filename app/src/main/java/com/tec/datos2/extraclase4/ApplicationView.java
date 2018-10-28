package com.tec.datos2.extraclase4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

/**
 * Clase que se encarga de crear la vista gráfica de la aplicación
 * @author Fabricio Elizondo
 */

public class ApplicationView extends View {

    private Cell[][] cells;
    private static final int COLS = 8, ROWS = 8;
    private static final float WALL_THICKNESS = 4;
    private float cellSize, hMargin, vMargin;
    private Paint wallPaint,pathPaint,obstaclePaint,visitedPaint,noPaint;
    Backtracking backtracking = new Backtracking(8, 2, 500); // (size, obstacleIncrease, timePerIteration)


    /**
     * Constructor de la clase que inicializa las variables tipo Paint
     * @param context
     * @param attrs
     */
    public ApplicationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        pathPaint = new Paint();
        pathPaint.setColor(Color.BLUE);

        obstaclePaint = new Paint();
        obstaclePaint.setColor(Color.RED);

        visitedPaint = new Paint();
        visitedPaint.setColor(Color.YELLOW);

        noPaint = new Paint();
        noPaint.setColor(Color.WHITE);


        createMaze();

    }

    /**
     * Función que crea una matriz de 8x8 con objetos tipo celdas
     */

    private void createMaze(){

        cells = new Cell[COLS][ROWS];

        for (int x = 0; x < COLS ; x++){

            for (int y = 0; y < ROWS; y++){

                cells[x][y] = new Cell (x,y);
            }
        }



    }

    /**
     * Función que se encarga de actualizar los flags respectivos para cada objeto de la matriz
     * @param matrix Matriz que representa la solución luego de aplicar el backtracking
     */

    private void actualizarMaze(int[][] matrix ){

        for(int i = 0; i < backtracking.getN(); i++){
            for(int j = 0; j < backtracking.getN(); j++){

                if(matrix[i][j] == 3){

                    cells[i][j].setPath(true);
                }

                else if(matrix[i][j] == 2){

                    cells[i][j].setVisited(true);
                }

                else if(matrix[i][j] == 0){

                    cells[i][j].setObstacle(true);
                }

                else if(matrix[i][j] == 1){
                    cells[i][j].setNoPaint(true);
                }

            }
        }
    }

    /**
     * Función que reinicia los flags de cada obtejo de la matriz
     */

    private void resetMaze(){

        for(int i = 0; i < backtracking.getN(); i++){
            for(int j = 0; j < backtracking.getN(); j++){

                cells[i][j].setObstacle(false);
                cells[i][j].setPath(false);
                cells[i][j].setVisited(false);
                cells[i][j].setNoPaint(false);
            }
        }
    }

    /**
     * Función que dibuja los rectángulos dentro de la matriz, de acuerdo con la solución del backtracking
     * @param canvas
     */
    private void drawMaze(Canvas canvas){
        float margin = cellSize/10;

        for(int i = 0; i < backtracking.getN(); i++){
            for(int j = 0; j < backtracking.getN(); j++){

                Cell cell = cells[i][j];

                if(cell.isPath()){
                    canvas.drawRect(cell.Col*cellSize + margin,cell.Row*cellSize + margin,(cell.Col +1)*cellSize - margin,(cell.Row + 1)*cellSize - margin,pathPaint);
                }

                else if(cell.isObstacle()){
                    canvas.drawRect(cell.Col*cellSize + margin,cell.Row*cellSize + margin,(cell.Col +1)*cellSize - margin,(cell.Row + 1)*cellSize - margin,obstaclePaint);
                }

                else if(cell.isVisited()){

                    canvas.drawRect(cell.Col*cellSize + margin,cell.Row*cellSize + margin,(cell.Col +1)*cellSize - margin,(cell.Row + 1)*cellSize - margin,visitedPaint);
                }

                else if(cell.isNoPaint()){
                    canvas.drawRect(cell.Col*cellSize + margin,cell.Row*cellSize + margin,(cell.Col +1)*cellSize - margin,(cell.Row + 1)*cellSize - margin,noPaint);
                }
            }
        }
    }

    /**
     * Función principal de la vista que se encarga de llamar el bactracking, actualizar los valores y dibujar en la pantalla
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        int width = getWidth();
        int height = getHeight();

        if (width / height < COLS / ROWS) {
            cellSize = width / (COLS + 1);
        } else {

            cellSize = height / (ROWS + 1);
        }

        hMargin = (width - COLS * cellSize) / 2;
        vMargin = (height - ROWS * cellSize) / 2;

        canvas.translate(hMargin, vMargin);

        for (int x = 0; x < COLS; x++) {

            for (int y = 0; y < ROWS; y++) {

                if (cells[x][y].topWall) {
                    canvas.drawLine(x * cellSize, y * cellSize, (x + 1) * cellSize, y * cellSize, wallPaint);
                }

                if (cells[x][y].leftWall) {
                    canvas.drawLine(x * cellSize, y * cellSize, x * cellSize, (y + 1) * cellSize, wallPaint);
                }

                if (cells[x][y].bottomWall) {
                    canvas.drawLine(x * cellSize, (y + 1) * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);
                }

                if (cells[x][y].rightWall) {
                    canvas.drawLine((x + 1) * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, wallPaint);
                }
            }
        }


        if (backtracking.getSolutionExists()) {

            backtracking.createLabyrinth(backtracking.getN(), backtracking.getObstacles() + 2);
            backtracking.solveLabyrinth(backtracking.getLabyrinth());
            actualizarMaze(backtracking.getDisplayMatrix());
            postInvalidateDelayed(TimeUnit.SECONDS.toMillis(2));
            Toast toast1 = Toast.makeText(super.getContext(), "Obstacles = " + backtracking.getObstacles(), Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.BOTTOM, 0, 0);
            toast1.show();
            drawMaze(canvas);
            backtracking.resetMatrix(backtracking.getSolution());
            resetMaze();
        } else {
            actualizarMaze(backtracking.getDisplayMatrix());
            drawMaze(canvas);
            Toast toast2 = Toast.makeText(super.getContext(), "PATH NOT FOUND", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.BOTTOM, 0, 0);
            toast2.show();
        }


    }


}