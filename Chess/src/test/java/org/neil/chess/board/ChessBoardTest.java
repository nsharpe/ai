package org.neil.chess.board;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.neil.board.Coordinates;
import org.neil.chess.PlayerType;
import org.neil.chess.pieces.Piece;
import org.neil.chess.pieces.PieceType;

import java.util.Collection;

public class ChessBoardTest {

    @Test
    public void testInit(){
        ChessBoard chessBoard = new ChessBoard();

        Assertions.assertEquals(32,chessBoard.getPieces().size());

        // correct number per player
        Assertions.assertEquals(16,chessBoard.getPieces()
                .stream()
                .filter(x -> x.playerType == PlayerType.WHITE).count());

        //correct number of pawns
        Assertions.assertEquals(16,chessBoard.getPieces()
                .stream()
                .filter(x -> x.pieceType == PieceType.PAWN).count());
    }

    @Test
    public void testClear(){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.clearBoard();

        Assertions.assertEquals(0,chessBoard.getPieces().size());
    }

    @Test
    public void testResetBoard(){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.clearBoard();
        chessBoard.reinit();

        Assertions.assertEquals(32,chessBoard.getPieces().size());
    }

    @Test
    public void testIsEmpty(){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.clearBoard();

        Assertions.assertTrue(chessBoard.isEmpty(Coordinates.of(7,7)));

        chessBoard.addPiece(whitePiece(7,7));

        Assertions.assertFalse(chessBoard.isEmpty(Coordinates.of(7,7)));
    }

    @Test
    public void testSimpleBishopMovement(){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.clearBoard();

        Collection<Coordinates> available = chessBoard.availableBishopLikeMoves(Coordinates.of(0,0),PlayerType.WHITE);

        Assertions.assertEquals(7,available.size());
        Assertions.assertEquals(true,available.contains(Coordinates.of(7,7)));

        available = chessBoard.availableBishopLikeMoves(Coordinates.of(3,3),PlayerType.WHITE);
//        Assertions.assertEquals(13, available.size());

        chessBoard.addPiece(whitePiece(7,7));
        available = chessBoard.availableBishopLikeMoves(Coordinates.of(0,0),PlayerType.WHITE);
        Assertions.assertEquals(6,available.size());
        Assertions.assertFalse(available.contains(Coordinates.of(7,7)));

        available = chessBoard.availableBishopLikeMoves(Coordinates.of(0,0),PlayerType.BLACK);
        Assertions.assertEquals(7,available.size());
        Assertions.assertTrue(available.contains(Coordinates.of(7,7)));
    }

    @Test
    public void testComplexBishopMovement(){
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.clearBoard();

        Collection<Coordinates> available = chessBoard.availableBishopLikeMoves(Coordinates.of(3,3),PlayerType.WHITE);

        Assertions.assertEquals(13, available.size());
    }

    private Piece whitePiece(int x, int y){
        return new Piece(PieceType.ROOK,PlayerType.WHITE,Coordinates.of(x,y));
    }
}
