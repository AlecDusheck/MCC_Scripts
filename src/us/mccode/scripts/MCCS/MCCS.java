package us.mccode.scripts.MCCS;

import us.mccode.scripts.MCCS.Statements.Statement;
import us.mccode.scripts.MCCS.Values.Value;
import us.mccode.scripts.MCCode;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.mccode.scripts.TitleManager;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class MCCS {

    public static void run(Player player, String source){
        MCCS MCCSloc = new MCCS(player);
        MCCSloc.interpret(player, source);
    }

    //lexing

    private static List<Token> tokenize(Player player, String source) {

        TitleManager.sendActionBar(player, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "" + ChatColor.GREEN + "Tokenizing script...");

        List<Token> tokens = new ArrayList<>();

        String token = "";
        TokenizeState state = TokenizeState.DEFAULT;

        // Many tokens are a single character, like operators and ().
        String charTokens = "\n=+-*/<>()";
        TokenType[] tokenTypes = { TokenType.LINE, TokenType.EQUALS,
                TokenType.OPERATOR, TokenType.OPERATOR, TokenType.OPERATOR,
                TokenType.OPERATOR, TokenType.OPERATOR, TokenType.OPERATOR,
                TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN
        };

        // Scan through the code one character at a time, building up the list
        // of tokens.
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (state) {
                case DEFAULT:
                    if (charTokens.indexOf(c) != -1) {
                        tokens.add(new Token(Character.toString(c),
                                tokenTypes[charTokens.indexOf(c)]));
                    } else if (Character.isLetter(c)) {
                        token += c;
                        state = TokenizeState.WORD;
                    } else if (Character.isDigit(c)) {
                        token += c;
                        state = TokenizeState.NUMBER;
                    } else if (c == '"') {
                        state = TokenizeState.STRING;
                    } else if (c == '\'') {
                        state = TokenizeState.COMMENT;
                    }
                    break;

                case WORD:
                    if (Character.isLetterOrDigit(c)) {
                        token += c;
                    } else if (c == ':') {
                        tokens.add(new Token(token, TokenType.LABEL));
                        token = "";
                        state = TokenizeState.DEFAULT;
                    } else {
                        tokens.add(new Token(token, TokenType.WORD));
                        token = "";
                        state = TokenizeState.DEFAULT;
                        i--;
                    }
                    break;

                case NUMBER:
                    if (Character.isDigit(c)) {
                        token += c;
                    } else {
                        tokens.add(new Token(token, TokenType.NUMBER));
                        token = "";
                        state = TokenizeState.DEFAULT;
                        i--;
                    }
                    break;

                case STRING:
                    if (c == '"') {
                        tokens.add(new Token(token, TokenType.STRING));
                        token = "";
                        state = TokenizeState.DEFAULT;
                    } else {
                        token += c;
                    }
                    break;

                case COMMENT:
                    if (c == '\n') {
                        state = TokenizeState.DEFAULT;
                    }
                    break;
            }
        }
        return tokens;
    }

    public MCCS(Player player){
        //variables = new HashMap<String, Value>();
        //labels = new HashMap<String, Integer>();

        setScriptVariables(player, new HashMap<String, Value>());
        setScriptLabels(player, new HashMap<String, Integer>());

        InputStreamReader converter = new InputStreamReader(System.in);
        setLineIn(player, new BufferedReader(converter));
    }

    public static void interpret(Player player, String source) {

        MCCode.currentlyCompiling.add(player);

        String contents = readFile(source);

        // Tokenize.
        List<Token> tokens = tokenize(player, contents);

        // Parse.
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse(player, getScriptLabels(player));

        TitleManager.sendActionBar(player, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "" + ChatColor.GREEN + "Executing script..." );
        //currentStatement = 0;
        setCurrentStatement(player, 0);
        MCCode.startedTimes.put(player, System.currentTimeMillis());

        new BukkitRunnable()
        {
            public void run()
            {
                if(getCurrentStatement(player) < statements.size() && player.isOnline() && MCCode.currentlyCompiling.contains(player)) {
                    int thisStatement = getCurrentStatement(player);
                    setCurrentStatement(player, getCurrentStatement(player) + 1);
                    //currentStatement++;
                    statements.get(thisStatement).execute();
                }else{
                    this.cancel();
                    MCCode.currentlyCompiling.remove(player);
                }
            }
        }.runTaskTimer(MCCode.getPlugin(), 0L, 1L);




        /*
        // Interpret until we're done.
        currentStatement = 0;
        while (currentStatement < statements.size()) {
            int thisStatement = currentStatement;
            currentStatement++;
            statements.get(thisStatement).execute();
        }
        */
    }

    //public static Map<String, Value> variables;
    //public static Map<String, Integer> labels;

    //public static BufferedReader lineIn;

    //public static int currentStatement;

    private static String readFile(String path) {
        try {
            FileInputStream stream = new FileInputStream(path);

            try {
                InputStreamReader input = new InputStreamReader(stream,
                        Charset.defaultCharset());
                Reader reader = new BufferedReader(input);

                StringBuilder builder = new StringBuilder();
                char[] buffer = new char[8192];
                int read;

                while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
                    builder.append(buffer, 0, read);
                }

                // HACK: The parser expects every statement to end in a newline,
                // even the very last one, so we'll just tack one on here in
                // case the file doesn't have one.
                builder.append("\n");

                return builder.toString();
            } finally {
                stream.close();
            }
        } catch (IOException ex) {
            return null;
        }
    }

    public static Map<String, Value> getScriptVariables(Player player){
        return scriptVariables.get(player);
    }

    public static Map<String, Integer> getScriptLabels(Player player){
        return scriptLabels.get(player);
    }

    public static Integer getCurrentStatement(Player player) {
        return scriptCurrentStatements.get(player);
    }

    public static  BufferedReader getLineIn(Player player){
        return scriptLineIn.get(player);
    }

    public static void setScriptVariables(Player player, Map<String, Value> map){
        scriptVariables.put(player, map);
    }

    public static void setScriptLabels(Player player, Map<String, Integer> map){
        scriptLabels.put(player, map);
    }

    public static void setCurrentStatement(Player player, int num){
        scriptCurrentStatements.put(player, num);
    }

    public static void setLineIn(Player player, BufferedReader reader){
        scriptLineIn.put(player, reader);
    }

    public static void putScriptVariables(Player player, String string, Value value){
        Map<String, Value> map = new HashMap<String, Value>();
        map.put(string, value);
        setScriptVariables(player, map);
    }

    public static void putScriptLabels(Player player, String string, int num){
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(string, num);
        setScriptLabels(player, map);
    }


    public static Map<Player, Map> scriptVariables = new HashMap<Player, Map>();
    public static Map<Player, Map> scriptLabels = new HashMap<Player, Map>();

    public static Map<Player, Integer> scriptCurrentStatements = new HashMap<Player, Integer>();

    public static Map<Player, BufferedReader> scriptLineIn = new HashMap<Player, BufferedReader>();






}
