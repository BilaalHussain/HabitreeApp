package com.example.habitree.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreAPI {
    FirebaseFirestore db;
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
}
