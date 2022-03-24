package com.example.habitree.listener;

import java.util.UUID;

public class ToggleIsEditing implements Event {
    public UUID tagId;
    public boolean newEditState;
    public ToggleIsEditing(UUID tagId, boolean newEditState) {
        this.tagId = tagId;
        this.newEditState = newEditState;
    }
}
