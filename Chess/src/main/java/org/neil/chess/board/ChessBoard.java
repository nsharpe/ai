package org.neil.chess.board;

import org.neil.board.Board;
import org.neil.board.Coordinates;
import org.neil.chess.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ChessBoard {
    Board board = new Board(8,8);

    public ChessBoard() {
        reinit();
    }

    public void reinit(){
        clearBoard();
        Piece.boardInit()
                .forEach( x -> board.place(x.coordinates,x));
    }

    public void clearBoard(){
        board.clear();
    }

    public Collection<Piece> getPieces(){
        return board.getPieces();
    }

    public void addPiece(Piece p){
        if(!board.inBounds(p.coordinates) && !board.isEmpty(p.coordinates)){
            throw new IllegalStateException("Piece is out of bounds or not an empty spot");
        }
        board.place(p.coordinates,p);
    }

    public boolean isEmpty(Coordinates coordinates){
        return board.isEmpty(coordinates);
    }

    public List<Coordinates> availableRookLikeMoves(Coordinates coordinates){
        List<Coordinates> toReturn = new ArrayList<>();
        Coordinates toCheck = coordinates.incrementX();
        while(board.inBounds(toCheck) && board.isEmpty(toCheck)){
            toReturn.add(toCheck);
            toCheck = toCheck.incrementX();
        }

        toCheck = coordinates.decrementX();
        while(board.inBounds(toCheck) && board.isEmpty(toCheck)){
            toReturn.add(toCheck);
            toCheck = toCheck.decrementX();
        }

        toCheck = coordinates.incrementY();
        while(board.inBounds(toCheck) && board.isEmpty(toCheck)){
            toReturn.add(toCheck);
            toCheck = toCheck.incrementY();
        }

        toCheck = coordinates.decrementY();
        while(board.inBounds(toCheck) && board.isEmpty(toCheck)){
            toReturn.add(toCheck);
            toCheck = toCheck.decrementY();
        }

        return toReturn;
    }

    public List<Coordinates> availableBishopLikeMoves(Coordinates coordinates){
        List<Coordinates> toReturn = new ArrayList<>();
        Coordinates toCheck = coordinates.incrementX().incrementY();
        while(board.inBounds(toCheck) && board.isEmpty(toCheck)){
            toReturn.add(toCheck);
            toCheck = toCheck.incrementX().incrementY();
        }

        toCheck = coordinates.decrementX().incrementY();
        while(board.inBounds(toCheck) && board.isEmpty(toCheck)){
            toReturn.add(toCheck);
            toCheck = toCheck.decrementX().incrementY();
        }

        toCheck = coordinates.incrementX().decrementY();
        while(board.inBounds(toCheck) && board.isEmpty(toCheck)){
            toReturn.add(toCheck);
            toCheck = toCheck.incrementX().decrementY();
        }

        toCheck = coordinates.decrementX().decrementY();
        while(board.inBounds(toCheck) && board.isEmpty(toCheck)){
            toReturn.add(toCheck);
            toCheck = toCheck.decrementX().decrementY();
        }

        return toReturn;
    }

    public List<Coordinates> kingMovements(Coordinates coordinates){
        return coordinates.adjacent().stream()
                .filter( x -> board.isEmpty(x))
                .filter( x -> board.inBounds(coordinates))
                .collect(Collectors.toList());
    }

    public List<Coordinates> queenMovements(Coordinates coordinates){
        List<Coordinates> toReturn = availableBishopLikeMoves(coordinates);
        toReturn.addAll(availableRookLikeMoves(coordinates));
        return toReturn;
    }

}
