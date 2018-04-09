package shopping_demo.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRetrofitRequest();
            }
        }).start();
    }

    private void sendRetrofitRequest() {
        Retrofit retrofit = new Retrofit.Builder()
            //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        ApiStores apiStores = retrofit.create(ApiStores.class);
        Call<GitLoginData> call = apiStores.getLoginName("simonws");

        try {
            Response<GitLoginData> bodyResponse = call.execute();
            GitLoginData body = bodyResponse.body();//获取返回体的字符串
            Log.d(TAG, "retrofit body=" + body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
