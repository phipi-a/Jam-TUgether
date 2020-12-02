package de.pcps.jamtugether.api;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.api.errors.Error;
import de.pcps.jamtugether.api.errors.GenericError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseCallback<T> implements Callback<T> {

    @Override
    public void onResponse(@NonNull Call<T> call, Response<T> response) {
        if(response.isSuccessful()) {
            T body = response.body();
            if(body == null) {
                onError(new GenericError());
            } else {
                onSuccess(body);
            }
        } else {
            onError(Error.from(response.code()));
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onError(Error.from(t));
    }

    public abstract void onSuccess(@NonNull T response);

    public abstract void onError(@NonNull Error error);
}
