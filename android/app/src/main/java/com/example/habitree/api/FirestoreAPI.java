package com.example.habitree.api;

import android.content.Context;
import android.util.Log;

import com.example.habitree.model.HabitModel;
import com.example.habitree.model.PersonModel;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreAPI {
    String TAG = "FirestoreAPI";
    FirebaseFirestore db;


    public FirestoreAPI(Context context) {
        db = FirebaseFirestore.getInstance();
    }

    public void followUser(String followeeUUID, String userUUID) {

        DocumentReference docRef = db.collection("users").document(followeeUUID);
        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (!document.exists()) {
                Log.e(TAG, "followUser could not find the user to follow at path" + docRef.toString());
                return;
            }

            db.collection("users").document(userUUID)
                    .update("followees", FieldValue.arrayUnion(followeeUUID));
        });
    }

    public List<PersonModel> getFolloweeScores(String userUUID) {
        DocumentReference docRef = db.collection("users").document(userUUID);
        List<PersonModel> followees = new ArrayList<>();
        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (!document.exists()) {
                Log.d(TAG, "getFolloweeScores called, but user doesnt exist! " + userUUID);
                return;
            }
            List<String> followeeUUIDs = (List<String>) document.get("followees");
            for (String followeeUUID : followeeUUIDs) {
                db.collection("users").document(followeeUUID)
                        .get().addOnCompleteListener(followeeTask -> {
                    DocumentSnapshot innerDoc = task.getResult();
                    if (innerDoc.exists()) {
                        List<Float> scores = (List<Float>) innerDoc.get("scores");
                        String name = (String) innerDoc.get("name");
                        followees.add(new PersonModel(scores, name));
                    }
                });
            }
        });
        return followees;
    }

    public void saveScore(String uuid, ImmutableMap<HabitModel.Category, Float> scores) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", uuid);
        user.put("scores", Arrays.asList(
                scores.getOrDefault(HabitModel.Category.WORK, 0f),
                scores.getOrDefault(HabitModel.Category.ACADEMIC, 0f),
                scores.getOrDefault(HabitModel.Category.FITNESS, 0f),
                scores.getOrDefault(HabitModel.Category.CREATIVE, 0f),
                scores.getOrDefault(HabitModel.Category.SELF_HELP, 0f)
        ));

        db.collection("users").document(uuid)
                .set(user, SetOptions.merge())
                .addOnFailureListener(e -> Log.e(TAG, "saveScore failed " + e.getMessage()))
                .addOnSuccessListener(unused -> Log.d(TAG, "saveScore succeeded for user " + uuid));
    }

}
