package com.example.reeco.syntax;

import android.content.Context;
import android.content.res.Resources;

import com.amrdeveloper.codeview.Code;
import com.amrdeveloper.codeview.CodeView;
import com.amrdeveloper.codeview.Keyword;
import com.example.reeco.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@SuppressWarnings("SameReturnValue")
public class PythonLanguage {

    //Language Keywords
    private static final Pattern PATTERN_KEYWORDS = Pattern.compile("\\b(False|await|else|import|pass|None|break|except|in|raise" +
            "|True|class|finally|is|return|and|continue|for|lambda" +
            "|try|as|def|from|nonlocal|while|assert|del|global|not" +
            "|with|async|elif|if|or|yield)\\b");

    //Brackets and Colons
    private static final Pattern PATTERN_BUILTINS = Pattern.compile("[,:;->{}()]");

    //Data
    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\b(\\d*[.]?\\d+)\\b");
    private static final Pattern PATTERN_CHAR = Pattern.compile("['](.*?)[']");
    private static final Pattern PATTERN_STRING = Pattern.compile("[\"](.*?)[\"]");
    private static final Pattern PATTERN_HEX = Pattern.compile("0x[0-9a-fA-F]+");
    private static final Pattern PATTERN_TODO_COMMENT = Pattern.compile("#TODO[^\n]*");
    private static final Pattern PATTERN_ATTRIBUTE = Pattern.compile("\\.[a-zA-Z0-9_]+");
    private static final Pattern PATTERN_OPERATION = Pattern.compile(":|==|>|<|!=|>=|<=|->|=|%|-|-=|%=|\\+|\\+=|\\^|&|\\|::|\\?|\\*");
    private static final Pattern PATTERN_HASH_COMMENT = Pattern.compile("#(?!TODO )[^\\n]*");

    public static void applyWhiteTheme(Context context, CodeView codeView) {
        codeView.resetSyntaxPatternList();
        codeView.resetHighlighter();

        Resources resources = context.getResources();

        //View Background
        codeView.setBackgroundColor(resources.getColor(R.color.white));

        //Syntax Colors
        codeView.addSyntaxPattern(PATTERN_HEX, resources.getColor(R.color.purple_500));
        codeView.addSyntaxPattern(PATTERN_CHAR, resources.getColor(R.color.green));
        codeView.addSyntaxPattern(PATTERN_STRING, resources.getColor(R.color.green));
        codeView.addSyntaxPattern(PATTERN_NUMBERS, resources.getColor(R.color.purple_500));
        codeView.addSyntaxPattern(PATTERN_KEYWORDS, resources.getColor(R.color.pink));
        codeView.addSyntaxPattern(PATTERN_BUILTINS, resources.getColor(R.color.dark_blue));
        codeView.addSyntaxPattern(PATTERN_HASH_COMMENT, resources.getColor(R.color.gray));
        codeView.addSyntaxPattern(PATTERN_ATTRIBUTE, resources.getColor(R.color.blue));
        codeView.addSyntaxPattern(PATTERN_OPERATION, resources.getColor(R.color.pink));

        //Default Color
        codeView.setTextColor(resources.getColor(R.color.orange));

        codeView.addSyntaxPattern(PATTERN_TODO_COMMENT, resources.getColor(R.color.gold));

        codeView.reHighlightSyntax();
    }

    public static String[] getKeywords(Context context) {
        return context.getResources().getStringArray(R.array.python_keywords);
    }

    public static List<Code> getCodeList(Context context) {
        List<Code> codeList = new ArrayList<>();
        String[] keywords = getKeywords(context);
        for (String keyword : keywords) {
            codeList.add(new Keyword(keyword));
        }
        return codeList;
    }

    public static Set<Character> getIndentationStarts() {
        Set<Character> characterSet = new HashSet<>();
        characterSet.add(':');
        return characterSet;
    }

    public static Set<Character> getIndentationEnds() {
        return new HashSet<>();
    }

    public static String getCommentStart() {
        return "#";
    }

    public static String getCommentEnd() {
        return "";
    }
}
