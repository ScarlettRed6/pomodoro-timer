package com.example.pomodoro_timer.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pomodoro_timer.data.AppDatabase;
import com.example.pomodoro_timer.data.data_access_objects.UserDao;
import com.example.pomodoro_timer.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class UserRepository {

    private static final String TAG = "UserRepository";
    private final UserDao userDao;
    private final CollectionReference usersCollection;
    private final ExecutorService executor;


    public interface RepositoryCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public UserRepository(@NonNull Application application) {
        // 1) Get Room DAO
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();

        // 2) Get Firestore reference to "users" collection
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        usersCollection = firestore.collection("users");

        // 3) Executor for background Room operations
        executor = Executors.newSingleThreadExecutor();
    }

    // Callback interface specifically for fetching a single UserModel from Firestore
    public interface FirestoreUserCallback {
        void onSuccess(UserModel user);
        void onFailure(Exception e);
    }

    /**
     * Insert a new user BOTH locally (Room) and remotely (Firestore), but first ensure
     * that the new user’s numeric `id` = (highest existing `id` in Firestore + 1).
     *
     * Steps:
     *  A) Query Firestore for the single user document with the highest `id`.
     *  B) If none exist, use maxId = 0. Otherwise, parse that highest `id`.
     *  C) Assign user.setId(maxId + 1), then insert into Room on a background thread.
     *  D) After Room insert, push the same object to Firestore under document(firebaseUid).
     *
     * @param user     A UserModel with email/password and firebaseUid already set.
     * @param callback Notified once Firestore write is done (or fails).
     */
    public void insertUser(UserModel user, RepositoryCallback callback) {
        // STEP A: Look up "highest id" in Firestore
        usersCollection
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    int maxId = 0;

                    // STEP B: If Firestore has at least one document, parse its id field
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        try {
                            // The first document in descending order has the largest `id`.
                            UserModel highestUser =
                                    querySnapshot.getDocuments()
                                            .get(0)
                                            .toObject(UserModel.class);

                            if (highestUser != null) {
                                maxId = highestUser.getId();
                            }
                        } catch (Exception e) {
                            // Failed to parse the highest‐id document; default to 0
                            Log.w(TAG, "Could not parse highest‐id user; defaulting maxId=0", e);
                            maxId = 0;
                        }
                    }

                    // Compute the next ID
                    int nextId = maxId + 1;
                    user.setId(nextId);

                    // STEP C: Insert into Room using manually assigned ID
                    executor.execute(() -> {
                        try {
                            long inserted = userDao.insert(user);
                            if (inserted <= 0) {
                                // If Room returns ≤ 0, insertion may have conflicted.
                                Log.w(TAG, "Room insert returned " + inserted + " (possible conflict). Proceeding to Firestore write anyway.");
                            }

                            // STEP D: Push to Firestore. We use firebaseUid as the Firestore document ID.
                            String uid = user.getFirebaseUid();
                            if (uid == null || uid.isEmpty()) {
                                throw new IllegalStateException("Firebase UID is null; cannot push to Firestore");
                            }

                            usersCollection
                                    .document(uid)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Firestore insert success for userId=" + user.getId());
                                        callback.onSuccess();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Firestore insert FAILED for userId=" + user.getId(), e);
                                        callback.onFailure(e);
                                    });

                        } catch (Exception e) {
                            // Room insert or document‐ID check failed
                            Log.e(TAG, "Room insert FAILED or missing Firebase UID", e);
                            callback.onFailure(e);
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    // Firestore query for highest‐id failed
                    Log.e(TAG, "Failed to fetch highest‐id from Firestore", e);
                    callback.onFailure(e);
                });
    }


//     Update a user BOTH locally (Room) and remotely (Firestore).
//     @param user UserModel that already has a valid `id`.
//     @param callback Notified once Firestore write is done (or fails).

    public void updateUser(final UserModel user, final RepositoryCallback callback) {
        executor.execute(() -> {
            try {
                // Update in Room
                int rowsUpdated = userDao.update(user);
                Log.d(TAG, "Room update updated " + rowsUpdated + " row(s) for userId=" + user.getId());

                // Push updated data under firebaseUid
                String uid = user.getFirebaseUid();
                if (uid == null || uid.isEmpty()) {
                    throw new IllegalStateException("Firebase UID is null cannot update Firestore");
                }

                usersCollection.document(uid)
                        .set(user)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Firestore update success for firebaseUid=" + uid);
                            callback.onSuccess();
                        })
                        .addOnFailureListener(e -> {
                           Log.e(TAG, "Firestore updated failed for firebaseUid=" + uid, e);
                           callback.onFailure(e);
                        });

            } catch (Exception e) {
                Log.e(TAG, "Room update failed or missing Firebase UID", e);
                callback.onFailure(e);
            }
        });
    }


// Delete a user BOTH locally and from Firestore.
// @param user     UserModel to delete.
// @param callback Notified once Firestore deletion is done (or fails).
    public void deleteUser(final UserModel user, final RepositoryCallback callback) {
        executor.execute(() -> {
            try {
                // Delete from Room
                int rowsDeleted = userDao.delete(user);
                Log.d(TAG, "Room delete deleted " + rowsDeleted + " row(s) for userId=" + user.getId());

                // Delete from Firestore
                String uid = user.getFirebaseUid();
                if (uid == null || uid.isEmpty()) {
                    throw new IllegalStateException("Firebase UID is null cannot delete from Firestore");
                }

                usersCollection.document(uid)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Firestore delete success for firebaseUid=" + uid);
                            callback.onSuccess();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Firestore delete failed for firebaseUid=" + uid, e);
                            callback.onFailure(e);
                        });

            } catch (Exception e) {
                Log.e(TAG, "Room delete FAILED or missing FIREBASE UID" + user.getId(), e);
                callback.onFailure(e);
            }
        });
    }

//    Fetch a user LOCALLY by email. (We assume local DB is “source of truth” for login.)
//    NOTE: If you wanted to fetch from Firestore, you could add a Firestore query here.

    public UserModel getUserByEmailNow(String email) {
        return userDao.getUserByEmailNow(email);
    }

    // Fetch from local Room the Firebase UID -- Can grab the cached user if you already know the UID
    public UserModel getUserByFirebaseUid(String firebaseUid) {
        return userDao.getUserByFirebaseUid(firebaseUid);
    }

    // This here is so we can fetch fresh beautiful ids from Room
    public UserModel getUserById(int userId) {
        return userDao.getUserById(userId);
    }

//    Fetch exactly one user document from Firestore by its firebaseUid.
//    This does NOT touch Room at all. The callback returns a UserModel
//    (populated from Firestore) or an error.
//    @param firebaseUid  The document ID in Firestore (same as firebaseUid field).
//    @param callback     FirestoreUserCallback to return results.
    public void fetchUserFromFirestore(String firebaseUid, FirestoreUserCallback callback) {
        if (firebaseUid == null || firebaseUid.isEmpty()) {
            callback.onFailure(new IllegalArgumentException("fetchUserFromFirestore: firebaseUid is null or empty"));
            return;
        }

        usersCollection.document(firebaseUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Convert Firestore document into UserModel
                        UserModel remoteUser = documentSnapshot.toObject(UserModel.class);
                        if (remoteUser != null) {
                            // Ensure the model's ID is set correctly (Firestore stored it)
                            // Because in insertUser(...) we stored `id` as Room's primary key
                            callback.onSuccess(remoteUser);
                        } else {
                            callback.onFailure(new IllegalStateException("fetchUserFromFirestore: document exists but cannot parse UserModel"));
                        }
                    } else {
                        callback.onFailure(new IllegalStateException("fetchUserFromFirestore: user does not exist in Firestore"));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user from Firestore for uid=" + firebaseUid, e);
                    callback.onFailure(e);
                });
    }

    //    NEW: Insert a UserModel into Room only (no Firestore write). We trust that `user.id`
    //is already the correct primary key (from remote). This allows us to preserve the
    //same numeric ID in Room as in Firestore.
    // @param user      A UserModel that came from Firestore (has `id`, `email`, `password`, etc).
    // @param callback  Notified once Room insert is done (or fails). Does NOT push to Firestore.
    public void insertUserLocalOnly(UserModel user, RepositoryCallback callback) {
        executor.execute(() -> {
            try {
                // We explicitly set the ID on the UserModel before calling insert.
                // Because @PrimaryKey(autoGenerate = true), Room will accept a non-zero ID
                // and insert it with that exact primary key. If the ID already exists locally,
                // this will conflict (exception) unless the DAO has a conflict strategy.
                long inserted = userDao.insert(user); // This will use the `id` field on user.
                if (inserted <= 0) {
                    Log.w(TAG, "insertUserLocalOnly: insert returned " + inserted + " (might conflict)");
                }
                callback.onSuccess();
            } catch (Exception e) {
                Log.e(TAG, "insertUserLocalOnly: Room insert failed for userId=" + user.getId(), e);
                callback.onFailure(e);
            }
        });
    }
}
