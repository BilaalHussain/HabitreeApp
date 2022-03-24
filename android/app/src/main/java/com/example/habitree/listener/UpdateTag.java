package com.example.habitree.listener;

import java.util.UUID;

public class UpdateTag implements Event {
    public UUID tagId;
    public String tagName;
    public Integer color;
    public UpdateTag(UUID tagId, String tagName, Integer color) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.color = color;
    }
}