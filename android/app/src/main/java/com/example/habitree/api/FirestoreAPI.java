package com.example.habitree.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.habitree.model.PersonModel;
import com.example.habitree.ui.leaderboard.AddFolloweeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FirestoreAPI {
    String TAG = "FirestoreAPI";
    FirebaseFirestore db;
    List<PersonModel> followees = new ArrayList<PersonModel>();

    public FirestoreAPI(Context context) {
        db = FirebaseFirestore.getInstance();
    }

    public void followUser(String followeeUUID, String userUUID) {
        DocumentReference docRef = db.collection("users").document(followeeUUID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    db.collection("users").document(userUUID)
                            .update("followees", FieldValue.arrayUnion(followeeUUID));
                }
            }
        });
    }

    public List<PersonModel> getFolloweeScores(String userUUID, AddFolloweeCallback addFollowee) {
        DocumentReference docRef = db.collection("users").document(userUUID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<String> followeeUUIDs = (List<String>) document.get("followees");
                    for (String followeeUUID : followeeUUIDs) {
                        db.collection("users").document(followeeUUID)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> followeeTask) {
                                DocumentSnapshot innerDoc = followeeTask.getResult();
                                if (innerDoc.exists()) {
                                    List<Double> scores_double = (List<Double>) innerDoc.get("scores");
                                    List<Float> scores = scores_double.stream().map(i -> new Float(i)).collect(Collectors.toList());
                                    String name = (String) innerDoc.get("name");
                                    addFollowee.execute(new PersonModel(scores, name, followeeUUID));
                                }
                            }
                        });
                    }
                }
            }
        });
        return followees;
    }

    public void saveScore(String uuid, List <Float> scores) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", uuid);
        user.put("scores", scores);

        db.collection("users").document(uuid)
                .set(user, SetOptions.merge())
                .addOnFailureListener(e -> Log.e(TAG, "saveScore failed " + e.getMessage()))
                .addOnSuccessListener(unused -> Log.d(TAG, "saveScore succeeded for user " + uuid));
    }

}
