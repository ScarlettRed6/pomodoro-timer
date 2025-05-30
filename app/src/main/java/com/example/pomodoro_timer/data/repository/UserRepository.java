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

    /**
     * Insert a new user BOTH locally (Room) and remotely (Firestore).
     *
     * @param user     A UserModel with email/password; id will be auto-generated by Room.
     * @param callback Notified once Firestore write is done (or fails).
     */
    public void insertUser(UserModel user, RepositoryCallback callback) {
        executor.execute(() -> {
            try {
                // 2a) Insert into Room; get the generated ID
                long generatedId = userDao.insert(user);
                user.setId((int) generatedId);

                String uid = user.getFirebaseUid();
                if (uid ==  null || uid.isEmpty()) {
                    throw new IllegalStateException("Firebase UID is null cannot push to Firestore");
                }

                // 2b) Now push the same user into Firestore, using the Room‐generated ID as documentId
                usersCollection.document(uid)
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
                // In case Room insert itself fails
                Log.e(TAG, "Room insert FAILED", e);
                callback.onFailure(e);
            }
        });
    }

    /**
     * Update a user BOTH locally (Room) and remotely (Firestore).
     *
     * @param user     UserModel that already has a valid `id`.
     * @param callback Notified once Firestore write is done (or fails).
     */
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

    /**
     * Delete a user BOTH locally and from Firestore.
     *
     * @param user     UserModel to delete.
     * @param callback Notified once Firestore deletion is done (or fails).
     */
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

    /**
     * Fetch a user LOCALLY by email. (We assume local DB is “source of truth” for login.)
     *
     * NOTE: If you wanted to fetch from Firestore, you could add a Firestore query here.
     */
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
}
