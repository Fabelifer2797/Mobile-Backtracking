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

public class ApplicationView extends View {

    private Cell[][] cells;
    private static final int COLS = 8, ROWS = 8;
    private static final float WALL_THICKNESS = 4;
    private float cellSize, hMargin, vMargin;
    private Paint wallPaint,pathPaint,obstaclePaint,visitedPaint,noPaint;
    Backtracking backtracking = new Backtracking(8, 2, 500); // (size, obstacleIncrease, timePerIteration)


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



    private void createMaze(){

        cells = new Cell[COLS][ROWS];

        for (int x = 0; x < COLS ; x++){

            for (int y = 0; y < ROWS; y++){

                cells[x][y] = new Cell (x,y);
            }
        }



    }


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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        int width = getWidth();
        int height = getHeight();

        if(width/height < COLS/ROWS){
            cellSize = width/(COLS+1);
        }

        else{

            cellSize = height/(ROWS+1);
        }

        hMargin = (width-COLS*cellSize)/2;
        vMargin = (height-ROWS*cellSize)/2;

        canvas.translate(hMargin,vMargin);

        for (int x = 0; x < COLS; x++){

            for(int y = 0; y < ROWS; y++){

                if(cells[x][y].topWall){
                    canvas.drawLine(x*cellSize,y*cellSize,(x+1)*cellSize,y*cellSize,wallPaint);
                }

                if(cells[x][y].leftWall){
                    canvas.drawLine(x*cellSize,y*cellSize,x*cellSize,(y+1)*cellSize,wallPaint);
                }

                if(cells[x][y].bottomWall){
                    canvas.drawLine(x*cellSize,(y+1)*cellSize,(x+1)*cellSize,(y+1)*cellSize,wallPaint);
                }

                if(cells[x][y].rightWall){
                    canvas.drawLine((x+1)*cellSize,y*cellSize,(x+1)*cellSize,(y+1)*cellSize,wallPaint);
                }
            }
        }



        if(backtracking.getSolutionExists()){

            backtracking.createLabyrinth(backtracking.getN(),backtracking.getObstacles() + 2);
            backtracking.solveLabyrinth(backtracking.getLabyrinth());
            actualizarMaze(backtracking.getDisplayMatrix());
            postInvalidateDelayed(TimeUnit.SECONDS.toMillis(2));
            Toast toast1 = Toast.makeText(super.getContext(),"Obstacles = " + backtracking.getObstacles(),Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.BOTTOM,0,0);
            toast1.show();
            drawMaze(canvas);
            backtracking.resetMatrix(backtracking.getSolution());
            resetMaze();
        }

        else{
            actualizarMaze(backtracking.getDisplayMatrix());
            drawMaze(canvas);
            Toast toast2 = Toast.makeText(super.getContext(),"PATH NOT FOUND",Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.BOTTOM,0,0);
            toast2.show();
        }





    }

    private class Cell{

        boolean topWall = true;
        boolean leftWall = true;
        boolean rightWall = true;
        boolean bottomWall = true;
        boolean path;
        boolean obstacle;
        boolean visited;
        boolean noPaint;

        public boolean isNoPaint() {
            return noPaint;
        }

        public void setNoPaint(boolean noPaint) {
            this.noPaint = noPaint;
        }

        int Col;
        int Row;

        public Cell(int col, int row) {
            Col = col;
            Row = row;
            path = false;
            obstacle = false;
            visited = false;
        }

        public boolean isPath() {
            return path;
        }

        public void setPath(boolean path) {
            this.path = path;
        }

        public boolean isObstacle() {
            return obstacle;
        }

        public void setObstacle(boolean obstacle) {
            this.obstacle = obstacle;
        }

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }
    }
}