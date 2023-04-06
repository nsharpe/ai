package org.neil.chess.board;

import org.neil.board.Board;
import org.neil.board.Coordinates;
import org.neil.chess.PlayerType;
import org.neil.chess.pieces.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChessBoard {
    Board<Piece> board = new Board(8, 8);

    public ChessBoard() {
        reinit();
    }

    public void reinit() {
        clearBoard();
        Piece.boardInit()
                .forEach(x -> board.place(x.coordinates, x));
    }

    public void clearBoard() {
        board.clear();
    }

    public Collection<Piece> getPieces() {
        return board.getPieces();
    }

    public void addPiece(Piece p) {
        if (!board.inBounds(p.coordinates) && !board.isEmpty(p.coordinates)) {
            throw new IllegalStateException("Piece is out of bounds or not an empty spot");
        }
        board.place(p.coordinates, p);
    }

    public boolean isEmpty(Coordinates coordinates) {
        return board.isEmpty(coordinates);
    }

    public List<Coordinates> availableRookLikeMoves(final Coordinates coordinates, PlayerType playerType) {
        List<Coordinates> toReturn = new ArrayList<>();
        Coordinates toCheck = coordinates.incrementX();
        while (board.inBounds(toCheck) && board.isEmpty(toCheck)) {
            toReturn.add(toCheck);
            toCheck = toCheck.incrementX();
        }
        if (capturablePiece(toCheck, playerType)) {
            toReturn.add(toCheck);
        }

        toCheck = coordinates.decrementX();
        while (board.inBounds(toCheck) && board.isEmpty(toCheck)) {
            toReturn.add(toCheck);
            toCheck = toCheck.decrementX();
        }
        if (capturablePiece(toCheck, playerType)) {
            toReturn.add(toCheck);
        }

        toCheck = coordinates.incrementY();
        while (board.inBounds(toCheck) && board.isEmpty(toCheck)) {
            toReturn.add(toCheck);
            toCheck = toCheck.incrementY();
        }
        if (capturablePiece(toCheck, playerType)) {
            toReturn.add(toCheck);
        }

        toCheck = coordinates.decrementY();
        while (board.inBounds(toCheck) && board.isEmpty(toCheck)) {
            toReturn.add(toCheck);
            toCheck = toCheck.decrementY();
        }
        if (capturablePiece(toCheck, playerType)) {
            toReturn.add(toCheck);
        }

        return toReturn;
    }

    public List<Coordinates> availableBishopLikeMoves(final Coordinates coordinates, PlayerType playerType) {
        List<Coordinates> toReturn = new ArrayList<>();
        Coordinates toCheck = coordinates.incrementX().incrementY();
        while (board.inBounds(toCheck) && board.isEmpty(toCheck)) {
            toReturn.add(toCheck);
            toCheck = toCheck.incrementX().incrementY();
        }
        if (capturablePiece(toCheck, playerType)) {
            toReturn.add(toCheck);
        }

        toCheck = coordinates.decrementX().incrementY();
        while (board.inBounds(toCheck) && board.isEmpty(toCheck)) {
            toReturn.add(toCheck);
            toCheck = toCheck.decrementX().incrementY();
        }
        if (capturablePiece(toCheck, playerType)) {
            toReturn.add(toCheck);
        }

        toCheck = coordinates.incrementX().decrementY();
        while (board.inBounds(toCheck) && board.isEmpty(toCheck)) {
            toReturn.add(toCheck);
            toCheck = toCheck.incrementX().decrementY();
        }
        if (capturablePiece(toCheck, playerType)) {
            toReturn.add(toCheck);
        }

        toCheck = coordinates.decrementX().decrementY();
        while (board.inBounds(toCheck) && board.isEmpty(toCheck)) {
            toReturn.add(toCheck);
            toCheck = toCheck.decrementX().decrementY();
        }
        if (capturablePiece(toCheck, playerType)) {
            toReturn.add(toCheck);
        }

        return toReturn;
    }

    public List<Coordinates> knightMovement(Coordinates coordinates, PlayerType playerType) {
        return Stream.of(coordinates.incrementX().incrementX().incrementX().incrementY().incrementY(),
                        coordinates.decrementX().decrementX().decrementX().incrementY().incrementY(),
                        coordinates.incrementX().incrementX().incrementX().decrementY().decrementY(),
                        coordinates.decrementX().decrementX().decrementX().decrementY().decrementY(),
                        coordinates.incrementY().incrementY().incrementY().incrementX().incrementX(),
                        coordinates.decrementY().decrementY().decrementY().incrementX().incrementX(),
                        coordinates.incrementY().incrementY().incrementY().decrementX().decrementX(),
                        coordinates.decrementY().decrementY().decrementY().decrementX().decrementX())
                .filter(x -> board.inBounds(x))
                .filter(x -> board.isEmpty(x) || capturablePiece(x, playerType))
                .collect(Collectors.toList());
    }

    /*
    TODO: Add en pass or whatever it is called
     */
    public List<Coordinates> pawnMovement(Coordinates coordinates, PlayerType playerType) {
        Coordinates moveForward = playerType == PlayerType.WHITE ? coordinates.incrementY() : coordinates.decrementY();

        ArrayList toReturn = new ArrayList();

        if(capturablePiece(moveForward.incrementX(), playerType)){
            toReturn.add(moveForward.incrementX());
        }

        if(capturablePiece(moveForward.decrementX(), playerType)){
            toReturn.add(moveForward.decrementX());
        }

        if(board.isEmpty(moveForward) && board.inBounds(moveForward)){
            toReturn.add(moveForward);

            if(playerType == PlayerType.WHITE && coordinates.y == 1){
                moveForward = coordinates.incrementY();
            }
            if(playerType == PlayerType.BLACK && coordinates.y == 6){
                moveForward = coordinates.decrementY();
            }

            if(board.isEmpty(moveForward) && !toReturn.contains(moveForward)) {
                toReturn.add(moveForward);
            }
        }



        return toReturn;
    }

    public List<Coordinates> kingMovements(Coordinates coordinates, PlayerType playerType) {
        return coordinates.adjacent().stream()
                .filter(x -> board.inBounds(coordinates))
                .filter(x -> board.isEmpty(x) || capturablePiece(x, playerType))
                .collect(Collectors.toList());
    }

    public List<Coordinates> queenMovements(Coordinates coordinates, PlayerType playerType) {
        return Stream.concat(availableBishopLikeMoves(coordinates, playerType).stream(),
                        availableRookLikeMoves(coordinates, playerType).stream())
                .collect(Collectors.toList());
    }

    public boolean capturablePiece(Coordinates coordinates, PlayerType attackingType) {
        if (board.inBounds(coordinates) && !board.isEmpty(coordinates)) {
            return board.get(coordinates).playerType != attackingType;
        }
        return false;
    }
}
