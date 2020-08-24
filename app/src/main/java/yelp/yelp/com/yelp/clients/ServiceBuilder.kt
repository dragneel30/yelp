import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val mClient = OkHttpClient.Builder().build()

    private val mRetrofit = Retrofit.Builder()
        .baseUrl("https://api.yelp.com/v3/")
        .addConverterFactory(GsonConverterFactory.create())

        .client(mClient)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return mRetrofit.create(service)
    }

}