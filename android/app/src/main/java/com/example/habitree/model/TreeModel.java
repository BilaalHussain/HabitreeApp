package com.example.habitree.model;

import java.io.Serializable;
import java.net.URI;

public class TreeModel implements Serializable {
    public String title;
    public URI uri;

    public TreeModel(String title, URI uri) {
        this.title = title;
        this.uri = uri;
    }
}
