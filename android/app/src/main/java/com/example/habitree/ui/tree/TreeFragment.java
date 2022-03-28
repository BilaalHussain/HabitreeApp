package com.example.habitree.ui.tree;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habitree.R;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.TreeModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.net.URI;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TreeFragment extends Fragment {

    private static final String ARG_TREE1 = "tree";

    private TreeModel tree;
    PieChart pieChart;
    ImageView image;

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
        if (tree == null) {
            return root;
        }

        final TextView title = root.findViewById(R.id.tree_title);
        image = root.findViewById(R.id.tree_image);
        pieChart = root.findViewById(R.id.piechart);

        // Set the title
        title.setText(tree.title);

        // Set data in pie chart
        updateScorePieChart();

        // Set the tree image
        URI treeUri = tree.score.getTreeUri();
        Picasso.get().load(treeUri.toString())
                .placeholder(R.drawable.loading)
                .into(image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        image.setTranslationY(300f);
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(image, "scaleX", 1.1f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(image, "scaleY", 1.1f);
                        ObjectAnimator translateY = ObjectAnimator.ofFloat(image, "translationY", 0f);
                        scaleDownX.setDuration(1000);
                        scaleDownY.setDuration(1000);
                        translateY.setDuration(1000);

                        AnimatorSet scaleDown = new AnimatorSet();
                        scaleDown.play(scaleDownX).with(scaleDownY).with(translateY);

                        scaleDown.start();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

        final FloatingActionButton share = root.findViewById(R.id.share_button);

        if (tree.sharable) {
            share.setOnClickListener(v -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_tree) + treeUri);
                startActivity(Intent.createChooser(shareIntent, "Share your tree using"));
            });
        } else {
            share.setVisibility(View.INVISIBLE);
        }

        return root;
    }

    private void updateScorePieChart() {
        Map<HabitModel.Category, Float> breakdown = tree.score.percentageBreakdown();
        pieChart.addPieSlice(
                new PieModel(
                        HabitModel.Category.ACADEMIC.toString(),
                        breakdown.getOrDefault(HabitModel.Category.ACADEMIC, 0f),
                        ContextCompat.getColor(getContext(), R.color.academic)));
        pieChart.addPieSlice(
                new PieModel(
                        HabitModel.Category.CREATIVE.toString(),
                        breakdown.getOrDefault(HabitModel.Category.CREATIVE, 0f),
                        ContextCompat.getColor(getContext(), R.color.creative)));
        pieChart.addPieSlice(
                new PieModel(
                        HabitModel.Category.FITNESS.toString(),
                        breakdown.getOrDefault(HabitModel.Category.FITNESS, 0f),
                        ContextCompat.getColor(getContext(), R.color.fitness)));
        pieChart.addPieSlice(
                new PieModel(
                        HabitModel.Category.SELF_HELP.toString(),
                        breakdown.getOrDefault(HabitModel.Category.SELF_HELP, 0f),
                        ContextCompat.getColor(getContext(), R.color.self_help)));
        pieChart.addPieSlice(
                new PieModel(
                        HabitModel.Category.WORK.toString(),
                        breakdown.getOrDefault(HabitModel.Category.WORK, 0f),
                        ContextCompat.getColor(getContext(), R.color.work)));

        Float vals = breakdown.values().stream().reduce(Float::sum).orElse(0f);
        if (vals == 0) {
            pieChart.addPieSlice(
                    new PieModel(
                            HabitModel.Category.WORK.toString(),
                            1f,
                            Color.BLACK));
        }

        pieChart.setUseInnerPadding(false);
        pieChart.startAnimation();
    }
}

