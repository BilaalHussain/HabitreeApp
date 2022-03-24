package com.example.habitree.listener;

import java.util.UUID;

public class UpdateTag implements Event {
    public UUID tagId;
    public String tagName;
    public UpdateTag(UUID tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }
}