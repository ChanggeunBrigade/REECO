package com.example.reeco.syntax;

import android.content.Context;

import com.amrdeveloper.codeview.Code;
import com.amrdeveloper.codeview.CodeView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LanguageManager {

    private final Context context;
    private final CodeView codeView;

    public LanguageManager(Context context, CodeView codeView) {
        this.context = context;
        this.codeView = codeView;
    }

    public void applyTheme(LanguageName language, ThemeName theme) {
        if (theme == ThemeName.WHITE) {
            applyNoctisWhiteTheme(language);
        }
    }

    public String[] getLanguageKeywords(LanguageName language) {
        switch (language) {
            case JAVA:
                return JavaLanguage.getKeywords(context);
            case PYTHON:
                return PythonLanguage.getKeywords(context);
            case GO_LANG:
                return GoLanguage.getKeywords(context);
            default:
                return new String[]{};
        }
    }

    public List<Code> getLanguageCodeList(LanguageName language) {
        switch (language) {
            case JAVA:
                return JavaLanguage.getCodeList(context);
            case PYTHON:
                return PythonLanguage.getCodeList(context);
            case GO_LANG:
                return GoLanguage.getCodeList(context);
            default:
                return new ArrayList<>();
        }
    }

    public Set<Character> getLanguageIndentationStarts(LanguageName language) {
        switch (language) {
            case JAVA:
                return JavaLanguage.getIndentationStarts();
            case PYTHON:
                return PythonLanguage.getIndentationStarts();
            case GO_LANG:
                return GoLanguage.getIndentationStarts();
            default:
                return new HashSet<>();
        }
    }

    public Set<Character> getLanguageIndentationEnds(LanguageName language) {
        switch (language) {
            case JAVA:
                return JavaLanguage.getIndentationEnds();
            case PYTHON:
                return PythonLanguage.getIndentationEnds();
            case GO_LANG:
                return GoLanguage.getIndentationEnds();
            default:
                return new HashSet<>();
        }
    }

    public String getCommentStart(LanguageName language) {
        switch (language) {
            case JAVA:
                return JavaLanguage.getCommentStart();
            case PYTHON:
                return PythonLanguage.getCommentStart();
            case GO_LANG:
                return GoLanguage.getCommentStart();
            default:
                return "";
        }
    }

    public String getCommentEnd(LanguageName language) {
        switch (language) {
            case JAVA:
                return JavaLanguage.getCommentEnd();
            case PYTHON:
                return PythonLanguage.getCommentEnd();
            case GO_LANG:
                return GoLanguage.getCommentEnd();
            default:
                return "";
        }
    }

    private void applyNoctisWhiteTheme(LanguageName language) {
        switch (language) {
            case JAVA:
                JavaLanguage.applyWhiteTheme(context, codeView);
                break;
            case PYTHON:
                PythonLanguage.applyWhiteTheme(context, codeView);
                break;
            case GO_LANG:
                GoLanguage.applyWhiteTheme(context, codeView);
                break;
        }
    }
}
