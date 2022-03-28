package com.example.habitree.model;

import com.google.common.collect.ImmutableMap;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Represents a score for a user for a particular week
public class ScoreModel {
    Map<HabitModel.Category, Float> scores = new HashMap<>();

    public ScoreModel(List<HabitModel> habits, Date startOfWeek) {
        // Uses average score across all category
        Map<HabitModel.Category, Integer> counts = new HashMap<>();
        for (HabitModel habit : habits) {
            scores.put(habit.category, scores.getOrDefault(habit.category, 0f) + habit.getTreeWeeklyScore(startOfWeek));
            counts.put(habit.category, counts.getOrDefault(habit.category, 0) + 1);
        }
        for (HabitModel.Category category : scores.keySet()) {
            scores.put(category, Math.min(scores.get(category) / counts.get(category), 1));
        }
    }

    public URI getTreeUri() {
        return URI.create(MessageFormat.format("https://us-central1-cs446-habitree.cloudfunctions.net/tree/?red={0}&green={1}&blue={2}&orange={3}&purple={4}&seed={5}",
                scores.getOrDefault(HabitModel.Category.ACADEMIC, 0f),
                scores.getOrDefault(HabitModel.Category.CREATIVE, 0f),
                scores.getOrDefault(HabitModel.Category.FITNESS, 0f),
                scores.getOrDefault(HabitModel.Category.SELF_HELP, 0f),
                scores.getOrDefault(HabitModel.Category.WORK, 0f),
                0 // TODO: Use user ID as seed
        ));
    }

    public Map<HabitModel.Category, Float> percentageBreakdown() {
        float sum = 0f;
        for (HabitModel.Category category : scores.keySet()) {
            sum += scores.get(category);
        }
        Map<HabitModel.Category, Float> breakdown = new HashMap<>();
        for (HabitModel.Category category : scores.keySet()) {
            breakdown.put(category, sum == 0 ? 0 : scores.get(category) / sum);
        }
        return breakdown;
    }

    public List<Float> getScore(boolean bonus) {
        // "Curve" score: 10 * sqrt(score as percent)
        if (!bonus) {
            return mapToList(ImmutableMap.copyOf(scores));
        }

        Map<HabitModel.Category, Float> curvedScores = new HashMap<HabitModel.Category, Float>(scores);

        for (HabitModel.Category key : curvedScores.keySet()) {
            float originalScoreAsPercent = curvedScores.getOrDefault(key, 0f) * 100f;

            float curved = (float) (10f * Math.sqrt(originalScoreAsPercent)) / 100f;
            curvedScores.put(key, curved);
        }
        return mapToList(ImmutableMap.copyOf(curvedScores));
    }

    public static List<Float> mapToList(ImmutableMap<HabitModel.Category, Float> scores) {
        return Arrays.asList(
                scores.getOrDefault(HabitModel.Category.ACADEMIC, 0f),
                scores.getOrDefault(HabitModel.Category.CREATIVE, 0f),
                scores.getOrDefault(HabitModel.Category.FITNESS, 0f),
                scores.getOrDefault(HabitModel.Category.SELF_HELP, 0f),
                scores.getOrDefault(HabitModel.Category.WORK, 0f));
    }
}
