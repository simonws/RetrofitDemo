package shopping_demo.com.myapplication;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by ws on 18-4-13.
 */

public interface RxJavaApiStores {

    @GET("/users/{user}")
    Observable<GitLoginData> getLoginName(@Path("user") String user);
}
