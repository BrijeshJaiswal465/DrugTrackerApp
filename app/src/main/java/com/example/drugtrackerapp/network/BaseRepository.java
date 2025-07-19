package com.example.drugtrackerapp.network;

import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class BaseRepository {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    protected final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    protected void setLoading(boolean isLoading) {
        loading.postValue(isLoading);
    }

    /**
     * Centralized execution of any long-running or async task with automatic loader handling.
     *
     * @param task The main task (e.g. API or Firebase operation)
     * @param onSuccess Callback on success
     * @param onFailure Callback on failure
     */
    protected <T> void launchSafe(RepositoryTask<T> task, RepositoryCallback<T> onSuccess, RepositoryError onFailure) {
        setLoading(true);
        executor.execute(() -> {
            try {
                T result = task.run(); // task (like Firebase call)
                onSuccess.onSuccess(result);
            } catch (Exception e) {
                e.printStackTrace();
                onFailure.onFailure(e);
            } finally {
                setLoading(false);
            }
        });
    }

    // Functional interfaces
    public interface RepositoryTask<T> {
        T run() throws Exception;
    }

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
    }

    public interface RepositoryError {
        void onFailure(Exception e);
    }
}
