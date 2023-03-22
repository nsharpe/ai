package org.neil.chess.pieces;

import org.neil.board.Coordinates;
import org.neil.chess.PlayerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Piece {
    public final PieceType pieceType;
    public final PlayerType playerType;
    public Coordinates coordinates;

    public Piece(PieceType pieceType, PlayerType playerType) {
        this.pieceType = Objects.requireNonNull(pieceType);
        this.playerType = Objects.requireNonNull(playerType);
    }

    public Piece(PieceType pieceType, PlayerType playerType, Coordinates coordinates) {
        this(pieceType,playerType);
        this.coordinates = Objects.requireNonNull(coordinates);
    }



    public static List<Piece> boardInit(){
        ArrayList toReturn = new ArrayList();
        toReturn.add(white(PieceType.ROOK,0,0));
        toReturn.add(white(PieceType.KNIGHT,0,1));
        toReturn.add(white(PieceType.BISHOP,0,2));
        toReturn.add(white(PieceType.KING,0,3));
        toReturn.add(white(PieceType.QUEEN,0,4));
        toReturn.add(white(PieceType.BISHOP,0,5));
        toReturn.add(white(PieceType.KNIGHT,0,6));
        toReturn.add(white(PieceType.ROOK,0,7));
        for(int i = 0; i < 8; i++){
            toReturn.add(white(PieceType.PAWN,1,i));
        }

        toReturn.add(white(PieceType.ROOK,7,0));
        toReturn.add(white(PieceType.KNIGHT,7,1));
        toReturn.add(white(PieceType.BISHOP,7,2));
        toReturn.add(white(PieceType.KING,7,3));
        toReturn.add(white(PieceType.QUEEN,7,4));
        toReturn.add(white(PieceType.BISHOP,7,5));
        toReturn.add(white(PieceType.KNIGHT,7,6));
        toReturn.add(white(PieceType.ROOK,7,7));
        for(int i = 0; i < 8; i++){
            toReturn.add(white(PieceType.PAWN,6,i));
        }

        return toReturn;
    }

    public static Piece white(PieceType pieceType,int x, int y){
        return new Piece(pieceType,PlayerType.WHITE,Coordinates.of(x,y));
    }

    public static Piece black(PieceType pieceType,int x, int y){
        return new Piece(pieceType,PlayerType.BLACK,Coordinates.of(x,y));
    }
}
