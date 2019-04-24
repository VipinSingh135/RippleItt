package com.rippleitt.controller;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Created by pc on password/29/2018.
 */

public class CustomTextWatcher implements TextWatcher {


    private NumberFormat nf = NumberFormat.getNumberInstance();
    private EditText et;
    private String tmp = "";
    private int moveCaretTo;
    private static final int INTEGER_CONSTRAINT = 40;
    private static final int FRACTION_CONSTRAINT = 2;
    private static final int MAX_LENGTH = INTEGER_CONSTRAINT
            + FRACTION_CONSTRAINT + 1;

    public CustomTextWatcher(EditText et) {
        this.et = et;
        nf.setMaximumIntegerDigits(INTEGER_CONSTRAINT);
        nf.setMaximumFractionDigits(FRACTION_CONSTRAINT);
        nf.setGroupingUsed(false);
    }

    public int countOccurrences(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this); // remove to prevent stackoverflow
        String ss = s.toString();
        int len = ss.length();

        if(ss.contains(".")){
            String[] split_ = ss.split("\\.");
            if(split_.length>1){
                if(split_[1].length()>2){
                    ss= ss.substring(0, ss.indexOf(".")+3);
                }
            }


        }
        et.setText(ss);
        et.setSelection(et.getText().length());
        /*
        int dots = countOccurrences(ss, '.');
        boolean shouldParse = dots <= 1
                && (dots == 0 ? len != (INTEGER_CONSTRAINT + 1)
                : len < (MAX_LENGTH + 1));
        if (shouldParse) {
            if (len > 1 && ss.lastIndexOf(".") != len - 1) {
                try {
                    if (ss.contains(".")) {
                        String[] integerFractionStrings = ss.split("\\.");
                        if (integerFractionStrings.length > 1) {
                            if (integerFractionStrings[1].length() == 1
                                    && integerFractionStrings[1].charAt(0) == '0') {
                                et.setText(ss);
                            } else {
                                Double d = Double.parseDouble(ss);
                                if (d != null) {
                                    et.setText(nf.format(d));
                                }
                            }
                        }
                    } else {
                        Double d = Double.parseDouble(ss);
                        if (d != null) {
                            et.setText(nf.format(d));
                        }
                    }

                } catch (NumberFormatException e) {
                }
            }
        } else {


            et.setText(tmp);
        }

        */
        et.addTextChangedListener(this); // reset listener

        // tried to fix caret positioning after key type:
        /*
        if (et.getText().toString().length() > 0) {
            if (dots == 0 && len >= INTEGER_CONSTRAINT
                    && moveCaretTo > INTEGER_CONSTRAINT) {
                moveCaretTo = INTEGER_CONSTRAINT;
            } else if (dots > 0 && len >= (MAX_LENGTH)
                    && moveCaretTo > (MAX_LENGTH)) {
                moveCaretTo = MAX_LENGTH;
            }
            try {
                et.setSelection(et.getText().toString().length());
                // et.setSelection(moveCaretTo); <- almost had it :))
            } catch (Exception e) {
            }
        }
        */
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        moveCaretTo = et.getSelectionEnd();
        tmp = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int length = et.getText().toString().length();
        if (length > 0) {
            moveCaretTo = start + count - before;
        }
    }
}
