package com.example.habitree.listener;

import java.util.UUID;

public class DeleteTagTapped implements Event {
    public UUID id;
    public DeleteTagTapped(UUID id) {
        this.id = id;
    }
}
