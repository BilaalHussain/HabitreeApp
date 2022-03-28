package com.example.habitree;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.habitree.api.HabitApi;
import com.example.habitree.helpers.DateHelpers;
import com.example.habitree.model.HabitModel;
import com.example.habitree.model.ScoreModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Tree widget to display a user's tree on their home screen.
 */
public class TreeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tree_widget);

        List<HabitModel> habits = (new HabitApi(context)).getAllHabits();
        ScoreModel score = new ScoreModel(
                habits,
                DateHelpers.startOfCurrentWeek(),
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        Picasso.get()
                .load(score.getTreeUri().toString())
                .transform(new RoundedCornersTransformation(50, 0))
                .into(views, R.id.tree_widget_image_view, new int[] {appWidgetId});

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}