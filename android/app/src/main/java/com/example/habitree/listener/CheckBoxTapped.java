package com.example.habitree.listener;

import java.util.UUID;

public class CheckBoxTapped implements Event {
    public UUID id;
    public CheckBoxTapped(UUID id) {
        this.id = id;
    }
}