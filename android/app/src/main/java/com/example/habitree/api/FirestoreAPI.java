package com.example.habitree.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.habitree.model.PersonModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirestoreAPI {
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

    public List<PersonModel> getFolloweeScores(String userUUID) {
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
                                DocumentSnapshot innerDoc = task.getResult();
                                if (innerDoc.exists()) {
                                    List<Float> scores = (List<Float>) innerDoc.get("scores");
                                    String name = (String) innerDoc.get("name");
                                    followees.add(new PersonModel(scores, name, followeeUUID));
                                }
                            }
                        });
                    }
                }
            }
        });
        return followees;
    }
}
