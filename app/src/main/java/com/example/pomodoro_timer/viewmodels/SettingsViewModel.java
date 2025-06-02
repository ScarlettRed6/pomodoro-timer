package com.example.pomodoro_timer.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pomodoro_timer.data.repository.UserRepository;
import com.example.pomodoro_timer.model.UserModel;
import com.example.pomodoro_timer.utils.SingleLiveEvent;
import com.example.pomodoro_timer.utils.shared_preferences.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingsViewModel extends AndroidViewModel {

    //Fields
    private final UserRepository userRepo;
    private final ExecutorService executor;
    private final SessionManager sessionManager;

    //Livedata for UI Updates
    private final SingleLiveEvent<Boolean> loginResult = new SingleLiveEvent<>();
    private final MutableLiveData<String> toastLoginResultMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> userIdHolder = new MutableLiveData<>(0);

    //FirebaseAuth Instance
    private final FirebaseAuth firebaseAuth;

    //Login Fields
    private final MutableLiveData<String> loginEmail = new MutableLiveData<>();
    private final MutableLiveData<String> loginPassword = new MutableLiveData<>();

    //Sign up Fields
    private final MutableLiveData<String> signUpEmail = new MutableLiveData<>();
    private final MutableLiveData<String> signUpPassword = new MutableLiveData<>();
    private final MutableLiveData<String> signUpConfirmPassword = new MutableLiveData<>();

    //Themes Fields
    private final MutableLiveData<String> selectedTheme = new MutableLiveData<>("Theme 1");
    private final MutableLiveData<String> selectedAlarm = new MutableLiveData<>("Alarm 1");
    private final MutableLiveData<Boolean> allowNotifications = new MutableLiveData<>(true);
    private final MutableLiveData<Boolean> allowNudge = new MutableLiveData<>(true);

    //Timer Fields (Pomodoro, Short and Long break Timer values)
    private final MutableLiveData<Integer> pomodoroMinutes= new MutableLiveData<>(25);
    private final MutableLiveData<Integer> pomodoroSeconds= new MutableLiveData<>(0);
    private final MutableLiveData<Integer> shortBreakMinutes= new MutableLiveData<>(5);
    private final MutableLiveData<Integer> shortBreakSeconds= new MutableLiveData<>(0);
    private final MutableLiveData<Integer> longBreakMinutes= new MutableLiveData<>(15);
    private final MutableLiveData<Integer> longBreakSeconds= new MutableLiveData<>(0);
    private final MutableLiveData<String> defaultLBIntervalString = new MutableLiveData<>(String.valueOf(4));
    private final MutableLiveData<Boolean> autoStartPomodoro = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> autoStartBreaks = new MutableLiveData<>(false);

    //Getters and Setters
    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }
    public LiveData<String> getToastLoginResultMessage() {
        return toastLoginResultMessage;
    }
    public LiveData<Integer> getUserId() {
        return userIdHolder;
    }
    public void setUserId(Integer userId) {
        this.userIdHolder.setValue(userId);
    }

    //Login getters and setters
    public MutableLiveData<String> getLoginEmail() {
        return loginEmail;
    }
    public MutableLiveData<String> getLoginPassword() {
        return loginPassword;
    }
    public void setLoginEmail(String loginEmail) {
        this.loginEmail.setValue(loginEmail);
    }
    public void setLoginPassword(String loginPassword) {
        this.loginPassword.setValue(loginPassword);
    }

    //Sign up getters and setters
    public MutableLiveData<String> getSignUpEmail() {
        return signUpEmail;
    }
    public MutableLiveData<String> getSignUpPassword() {
        return signUpPassword;
    }
    public MutableLiveData<String> getSignUpConfirmPassword() {
        return signUpConfirmPassword;
    }
    public void setSignUpEmail(String signUpEmail) {
        this.signUpEmail.setValue(signUpEmail);
    }
    public void setSignUpPassword(String signUpPassword) {
        this.signUpPassword.setValue(signUpPassword);
    }
    public void setSignUpConfirmPassword(String signUpConfirmPassword) {
        this.signUpConfirmPassword.setValue(signUpConfirmPassword);
    }

    //Themes Getters and Setters
    public MutableLiveData<String> getSelectedTheme() {
        return selectedTheme;
    }
    public MutableLiveData<String> getSelectedAlarm() {
        return selectedAlarm;
        }
    public MutableLiveData<Boolean> getAllowNotifications() {
        return allowNotifications;
    }
    public MutableLiveData<Boolean> getAllowNudge() {
        return allowNudge;
    }
    public void setSelectedTheme(String selectedTheme) {
        this.selectedTheme.setValue(selectedTheme);
    }
    public void setSelectedAlarm(String selectedAlarm) {
        this.selectedAlarm.setValue(selectedAlarm);
    }
    public void setAllowNotifications(boolean allowNotifications) {
        this.allowNotifications.setValue(allowNotifications);
    }
    public void setAllowNudge(boolean allowNudge) {
        this.allowNudge.setValue(allowNudge);
    }

    //Timer Getters and Setters
    public LiveData<Integer> getPomodoroMinutes() {
        return pomodoroMinutes;
    }
    public LiveData<Integer> getPomodoroSeconds() {
        return pomodoroSeconds;
    }
    public LiveData<Integer> getShortBreakMinutes() {
        return shortBreakMinutes;
    }
    public LiveData<Integer> getShortBreakSeconds() {
        return shortBreakSeconds;
        }
    public LiveData<Integer> getLongBreakMinutes() {
        return longBreakMinutes;
    }
    public LiveData<Integer> getLongBreakSeconds() {
        return longBreakSeconds;
    }
    public void setPomodoroMinutes(int pomodoroMinutes) {
        this.pomodoroMinutes.setValue(pomodoroMinutes);
    }
    public void setPomodoroSeconds(int pomodoroSeconds) {
        this.pomodoroSeconds.setValue(pomodoroSeconds);
    }
    public void setShortBreakMinutes(int shortBreakMinutes) {
        this.shortBreakMinutes.setValue(shortBreakMinutes);
    }
    public void setShortBreakSeconds(int shortBreakSeconds) {
        this.shortBreakSeconds.setValue(shortBreakSeconds);
    }
    public void setLongBreakMinutes(int longBreakMinutes) {
        this.longBreakMinutes.setValue(longBreakMinutes);
    }
    public void setLongBreakSeconds(int longBreakSeconds) {
        this.longBreakSeconds.setValue(longBreakSeconds);
    }
    public MutableLiveData<String> getDefaultLBIntervalString() {
        return defaultLBIntervalString;
    }
    public Integer getDefaultLBIntervalValue() {
        try {
            return Integer.parseInt(defaultLBIntervalString.getValue());
        } catch (NumberFormatException e) {
            return 4; // Or some other default/error value
        }
    }
    public MutableLiveData<Boolean> getAutoStartPomodoro() {
        return autoStartPomodoro;
    }
    public MutableLiveData<Boolean> getAutoStartBreaks() {
        return autoStartBreaks;
    }
    public void setAutoStartPomodoro(boolean autoStartPomodoro) {
        this.autoStartPomodoro.setValue(autoStartPomodoro);
    }
    public void setAutoStartBreaks(boolean autoStartBreaks) {
        this.autoStartBreaks.setValue(autoStartBreaks);
    }

    //Constructor
    public SettingsViewModel(@NonNull Application application){
        super(application);
        sessionManager = new SessionManager(application);
        userRepo = new UserRepository(application);
        firebaseAuth = FirebaseAuth.getInstance();
        executor = Executors.newSingleThreadExecutor();
    }//End of constructor

    //LOGIN VIEWMODEL FUNCTIONS
    public void login() {
        final String email = loginEmail.getValue();
        final String password = loginPassword.getValue();

        if (email == null || password == null) {
            toastLoginResultMessage.setValue("Please fill both email and password.");
            loginResult.setValue(false);
            return;
        }

        // 1) Sign in with FirebaseAuth
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            if (firebaseUser == null) {
                                toastLoginResultMessage.setValue("UNEXPECTED: FIREBASE USER IS NULL");
                                loginResult.setValue(false);
                                return;
                            }
                            final String firebaseUid = firebaseUser.getUid();

                            // 2) Now check local Room DB for this firebaseUid
                            executor.execute(() -> {
                                UserModel localUser = userRepo.getUserByFirebaseUid(firebaseUid);

                                if (localUser != null) {
                                    // --- CASE A: User is found locally ---
                                    if (localUser.getPassword().equals(password)) {
                                        // Mark as logged in locally
                                        localUser.setIsLoggedIn(true);

                                        // Update Room + Firestore via repository
                                        userRepo.updateUser(localUser, new UserRepository.RepositoryCallback() {
                                            @Override
                                            public void onSuccess() {
                                                // Save session and notify UI if sync succeeds
                                                sessionManager.saveLoginSession(localUser.getId(), localUser.getEmail());
                                                userIdHolder.postValue(sessionManager.getUserId());
                                                loginResult.postValue(true);

                                                Log.d("SettingsViewModel", "Login Successful & isLoggedIn synced (local->Firestore)");
                                            }

                                            @Override
                                            public void onFailure(Exception e) {
                                                // Still log in locally if Firestore update fails
                                                Log.e("SettingsViewModel", "Firestore sync FAILED (isLoggedIn) for userId=" + localUser.getId(), e);

                                                sessionManager.saveLoginSession(localUser.getId(), localUser.getEmail());
                                                userIdHolder.postValue(sessionManager.getUserId());
                                                loginResult.postValue(true);

                                                toastLoginResultMessage.postValue(
                                                        "Logged in locally, but could not sync status to server."
                                                );
                                            }
                                        });

                                        Log.d("SettingsViewModel", "Login Successful (local user) ID=" + localUser.getId());
                                    } else {
                                        // Wrong password for the local user
                                        Log.d("SettingsViewModel", "Login Failed (wrong password) for local user");
                                        toastLoginResultMessage.postValue("Login Failed: Wrong password or username");
                                        loginResult.postValue(false);
                                    }

                                } else {
                                    // --- CASE B: User NOT found locally; attempt Firestore fetch ---
                                    Log.d("SettingsViewModel", "Local user not found, fetching from Firestore");

                                    userRepo.fetchUserFromFirestore(firebaseUid, new UserRepository.FirestoreUserCallback() {
                                        @Override
                                        public void onSuccess(UserModel remoteUser) {
                                            // remoteUser now has: id, email, password, isLoggedIn, firebaseUid
                                            if (remoteUser.getPassword().equals(password)) {
                                                // Password matches Firestore copy → insert into local Room
                                                remoteUser.setIsLoggedIn(true);

                                                userRepo.insertUserLocalOnly(remoteUser, new UserRepository.RepositoryCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        // After local insert succeeds, save session
                                                        sessionManager.saveLoginSession(remoteUser.getId(), remoteUser.getEmail());
                                                        userIdHolder.postValue(sessionManager.getUserId());
                                                        loginResult.postValue(true);

                                                        Log.d("SettingsViewModel", "Login successful (remote→local sync). User ID=" + remoteUser.getId());
                                                    }

                                                    @Override
                                                    public void onFailure(Exception e) {
                                                        // Could not insert into Room—treat as failure
                                                        Log.e("SettingsViewModel", "Failed to insert remote user into Room", e);
                                                        toastLoginResultMessage.postValue("Login failed: cannot cache user locally.");
                                                        loginResult.postValue(false);
                                                    }
                                                });
                                            } else {
                                                // Password mismatch with remote Firestore entry
                                                Log.d("SettingsViewModel", "Login Failed (wrong password) for remote user");
                                                toastLoginResultMessage.postValue("Login Failed: Wrong password or username");
                                                loginResult.postValue(false);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            // Failed to fetch from Firestore (user does not exist remotely or network error)
                                            Log.e("SettingsViewModel", "Failed to fetch user from Firestore", e);
                                            toastLoginResultMessage.postValue("User not found");
                                            loginResult.postValue(false);
                                        }
                                    });
                                }
                            });
                        } else {
                            // FirebaseAuth signIn failed (invalid credentials, network error, etc.)
                            Exception e = task.getException();
                            String msg = (e != null) ? e.getMessage() : "Unknown FirebaseAuth error";
                            toastLoginResultMessage.setValue("FirebaseAuth sign‐in failed: " + msg);
                            loginResult.setValue(false);
                            Log.e("SettingsViewModel", "FirebaseAuth signIn failed", e);
                        }
                    }
                });
    }//End of login method

    //SIGN UP VIEWMODEL FUNCTIONS
    public void signUp() {
        final String email = signUpEmail.getValue();
        final String password = signUpPassword.getValue();
        final String confirm = signUpConfirmPassword.getValue();

        // 1) Simple password/confirm check
        if (email == null || password == null || confirm == null) {
            toastLoginResultMessage.setValue("Please fill all fields.");
            loginResult.setValue(false);
            return;
        }
        if (!password.equals(confirm)) {
            toastLoginResultMessage.setValue("Passwords do not match");
            loginResult.setValue(false);
            return;
        }

        // 2) Create user in FirebaseAuth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 2a) Sign‐up succeeded
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            if (firebaseUser == null) {
                                toastLoginResultMessage.setValue("Unexpected error: FirebaseUser is null");
                                loginResult.setValue(false);
                                return;
                            }
                            String firebaseUid = firebaseUser.getUid();
                            Log.d("CHECK FIREBASE UID", "Firebase UID: " + firebaseUid);

                            // 2b) Build a new UserModel, set fields including firebaseUid
                            UserModel newUser = new UserModel(email, password);
                            newUser.setFirebaseUid(firebaseUid);
                            newUser.setIsLoggedIn(true); // Immediately mark as “logged in”
                            Log.d("FIREBASE UID UNDER NEW USER", "Firebase UID Under New User: " + newUser.getFirebaseUid());

                            // 2c) Insert into Room & Firestore
                            userRepo.insertUser(newUser, new UserRepository.RepositoryCallback() {
                                @Override
                                public void onSuccess() {
                                    // 3) On success (both Room + Firestore done):
                                    sessionManager.saveLoginSession(newUser.getId(), newUser.getEmail());
                                    // Log.d("GET SESSIONMANAGER USERID", "SessionManager.getUserID: " + sessionManager.getUserId());
                                    userIdHolder.postValue(sessionManager.getUserId());
                                    loginEmail.postValue(sessionManager.getEmail());
                                    loginResult.postValue(true);
                                    clearSignUpFields();
                                    Log.d("SIGNUP USER CREATED", "signUp(): new user created, UID=" + firebaseUid);
                                    Log.d("GET USER EMAIL", "User Email: " + newUser.getEmail());
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    // Something failed (Room or Firestore). We should probably roll back
                                    // the FirebaseAuth user, but for now, just notify UI.
                                    Log.e("SIGNUP INSERTING ERROR", "signUp(): error inserting user in Room/Firestore", e);
                                    toastLoginResultMessage.postValue("Sign up failed: " + e.getMessage());
                                    loginResult.postValue(false);
                                }
                            });

                        } else {
                            // FirebaseAuth signUp failed (e.g. email already in use, invalid, etc.)
                            Exception e = task.getException();
                            String msg = (e != null) ? e.getMessage() : "Unknown error";
                            toastLoginResultMessage.setValue("FirebaseAuth sign‐up failed: " + msg);
                            loginResult.setValue(false);
                            Log.e("SIGNUP FIREBASE AUTH FAILED", "signUp(): FirebaseAuth failed", e);
                        }
                    }
                });
    }


    public void signOut() {
        // Get the currently‐logged in user ID from SessionManager
        FirebaseUser current = firebaseAuth.getCurrentUser();
        if (current == null) {
            Log.w("NO SIGNED IN", "signOut(): no FirebaseAuth user is signed in");
            // Just clear local Room session if any:
            sessionManager.clearLoginSession();
            userIdHolder.postValue(0);
            return;
        }
        final String firebaseUid = current.getUid();

        // SIGN THIS SHIT OUT OF FIREBASE AUTH
        firebaseAuth.signOut();

        executor.execute(() -> {
            UserModel user = userRepo.getUserByFirebaseUid(firebaseUid);
            if (user == null) {
                Log.d("LOG: CHECK USER NULL ", "USER IS NULL");
                // Log.d("LOG: CHECK CURRENT USER ID", String.valueOf(currentUserId));
            }
            if (user != null) {
                user.setIsLoggedIn(false);

                // Push that change to Room + Firestore
                userRepo.updateUser(user, new UserRepository.RepositoryCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d("LOG: SIGN OUT SUCCESS", "Successfully synced signOut to Firestore for userId=" + getUserId());
                        sessionManager.clearLoginSession();
                        userIdHolder.postValue(0);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("LOG: SIGN OUT FAILURE", "Failed to sync signOut to Firestore for userId=" + getUserId(), e);
                        // Even if Firestore fails we still clear the local session:
                        sessionManager.clearLoginSession();
                        userIdHolder.postValue(0);
                        toastLoginResultMessage.postValue("Signed out locally, but could not update server status.");
                    }
                });
            } else {
                // If user null clear session
                Log.w("LOG: USER NOT FOUND (USER NULL CLEAR SESSION)", "signOut(): user not found in local DB. Clearing session anyway.");
                sessionManager.clearLoginSession();
                userIdHolder.postValue(0);
            }
        });
    }

    private void clearSignUpFields(){
        signUpEmail.postValue("");
        signUpPassword.postValue("");
        signUpConfirmPassword.postValue("");
    }//End of clearSignUpFields method

}
