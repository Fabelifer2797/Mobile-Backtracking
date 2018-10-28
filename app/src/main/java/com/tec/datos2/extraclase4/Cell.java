package com.tec.datos2.extraclase4;

/**
 * Clase que permite la creación de objetos tipo celdas para mostrar en la vista
 * @author Fabricio Elizondo
 */
public class Cell {

    boolean topWall = true;
    boolean leftWall = true;
    boolean rightWall = true;
    boolean bottomWall = true;
    boolean path;
    boolean obstacle;
    boolean visited;
    boolean noPaint;
    int Col;
    int Row;

    public Cell(int col, int row) {
        Col = col;
        Row = row;
        path = false;
        obstacle = false;
        visited = false;
    }


    public boolean isNoPaint() {
        return noPaint;
    }

    public void setNoPaint(boolean noPaint) {
        this.noPaint = noPaint;
    }

    public boolean isTopWall() {
        return topWall;
    }

    public void setTopWall(boolean topWall) {
        this.topWall = topWall;
    }

    public boolean isLeftWall() {
        return leftWall;
    }

    public void setLeftWall(boolean leftWall) {
        this.leftWall = leftWall;
    }

    public boolean isRightWall() {
        return rightWall;
    }

    public void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
    }

    public boolean isBottomWall() {
        return bottomWall;
    }

    public void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
    }

    public int getCol() {
        return Col;
    }

    public void setCol(int col) {
        Col = col;
    }

    public int getRow() {
        return Row;
    }

    public void setRow(int row) {
        Row = row;
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

