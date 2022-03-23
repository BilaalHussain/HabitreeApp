package com.example.habitree.autocomplete;


import java.util.function.Function;



public abstract class AbstractAutocompleteDelegate {

    protected Function<String,Boolean> onCompleteCallback;
    public AbstractAutocompleteDelegate(Function<String, Boolean> onComplete) {
        onCompleteCallback = onComplete;
    }

    public abstract boolean isEnabled();
    public abstract void setEnabled(boolean e);
    public abstract void cleanup();

}
