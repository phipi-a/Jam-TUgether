package de.pcps.jamtugether.api;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.errors.Error;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseCallback<T> implements Callback<T> {

    @Override
    public void onResponse(@NonNull Call<T> call, Response<T> response) {
        if(response.isSuccessful()) {
            onResponse(response.body());
        } else {
            onError(Error.from(response.code()));
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onError(Error.from(t));
    }

    public abstract void onResponse(T response);

    public abstract void onError(@NonNull Error error);
}
