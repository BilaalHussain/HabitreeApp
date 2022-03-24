package com.example.habitree.ui.tree;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habitree.R;
import com.example.habitree.model.TreeModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TreeFragment extends Fragment {

    private static final String ARG_TREE1 = "tree";

    private TreeModel tree;

    public TreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tree Tree to render.
     * @return A new instance of fragment TreeFragment.
     */
    public static TreeFragment newInstance(TreeModel tree) {
        TreeFragment fragment = new TreeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TREE1, tree);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tree = (TreeModel) getArguments().getSerializable(ARG_TREE1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tree, container, false);

        final TextView title = root.findViewById(R.id.tree_title);
        final ImageView image = root.findViewById(R.id.tree_image);
        final FloatingActionButton share = root.findViewById(R.id.share_button);

        title.setText(tree.title);
        Picasso.get().load(tree.uri.toString())
                .placeholder(R.drawable.loading)
                .into(image);

        share.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_tree) + tree.uri);
            startActivity(Intent.createChooser(shareIntent, "Share your tree using"));
        });

        return root;
    }
}