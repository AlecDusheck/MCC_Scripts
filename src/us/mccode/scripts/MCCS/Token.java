package us.mccode.scripts.MCCS;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class Token {
    public Token(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }

    public final String text;
    public final TokenType type;
}
