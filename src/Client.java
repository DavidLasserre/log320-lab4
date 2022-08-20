/* *****************************************************************************************************
 * @author David L., Philip T., Davina T.
 * Cours : LOG320 - Structures de données et algorithmes
 * Projet : Laboratoire 4 (Ultimate tic-tac-toe)
 *
 * Description du projet :
 * Implémentation de l’algorithme minimax avec élagage alpha-beta.
 * Implémentation d’heuristiques.
 * Implémentation d'optimisations.
 *
 * Dernière modification : 03 août 2022
 * *****************************************************************************************************/

import java.io.*;
import java.net.*;

public class Client {
    static final char NEW_GAME_AS_X = '1';
    static final char NEW_GAME_AS_O = '2';
    static final char LAST_MOVE_PLAYED = '3';
    static final char LAST_MOVE_INVALID = '4';
    static final char GAME_OVER = '5';

    public static void main(String[] args) throws IOException {

        System.out.println("Entrez l'adresse IP : ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String ipAddress = reader.readLine();

        try (Socket socket = new Socket(ipAddress, 8888)) {
            MainBoard board = null;
            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());

            while (true) {
                char command = (char) input.read();

                if (command == NEW_GAME_AS_X) {
                    System.out.println("New game as X!");

                    byte[] buffer = new byte[1024];

                    input.read(buffer, 0, input.available());

                    String[] boardValues = (new String(buffer).trim()).split(" ");

                    board = new MainBoard(1);

                    Move move = board.playBestMove();

                    System.out.println("We played : " + move);

                    output.write(move.toString().getBytes(), 0, move.toString().length());
                    output.flush();
                }

                if (command == NEW_GAME_AS_O) {
                    System.out.println("New game as O! Waiting for X to play.");

                    byte[] buffer = new byte[1024];

                    input.read(buffer, 0, input.available());

                    String[] boardValues = (new String(buffer).trim()).split(" ");

                    board = new MainBoard(2);
                }

                if (command == LAST_MOVE_PLAYED) {
                    byte[] buffer = new byte[16];

                    input.read(buffer, 0, input.available());

                    Move lastMove = new Move((new String(buffer)).trim());

                    assert board != null;
                    board.playMove(lastMove);

                    System.out.println("Opponent played : " + lastMove);
                    Move move = board.playBestMove();
                    System.out.println("We played : " + move);

                    output.write(move.toString().getBytes(), 0, move.toString().length());
                    output.flush();

                }

                if (command == LAST_MOVE_INVALID) {
                    System.out.println("Last move played is invalid, try another move.");

                    assert board != null;
                    board.undoLastMove();

                    Move move = new Move("E5");
                    System.out.println("We played : " + move);

                    output.write(move.toString().getBytes(), 0, move.toString().length());
                    output.flush();
                }

                if (command == GAME_OVER) {
                    byte[] buffer = new byte[16];

                    input.read(buffer, 0, input.available());

                    Move lastMove = new Move((new String(buffer)).trim());

                    assert board != null;
                    board.playMove(lastMove);

                    System.out.println("Game over. Opponent played : " + lastMove);
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
