package com.example.habitree.ui.follow;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.habitree.R;

public class RerenderLeaderboardCallback {
    Fragment f;
    FragmentManager fm;

    public RerenderLeaderboardCallback(Fragment f, FragmentManager fm) {
        this.f = f;
        this.fm = fm;
    }

    public void execute() {
        FragmentTransaction t = this.fm.beginTransaction().replace(R.id.nav_host_fragment, this.f);
        t.addToBackStack(null);
        t.commit();
    }
}
