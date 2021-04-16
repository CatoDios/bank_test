package com.example.banktest.di


import com.example.banktest.datasources.UserNetworkDataSource
import com.example.banktest.retrofit.UsersAPI
import com.example.data.RemoteDataSource
import com.example.data.Repository
import com.example.data.UserRepository
import com.example.usecase.GetUsersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@InstallIn(ActivityRetainedComponent::class)
@Module
object AppModule {


    /**
     * Returns the [HttpLoggingInterceptor] instance with logging level set to body
     * @since 1.0.0
     */
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Provides an [OkHttpClient]
     * @param loggingInterceptor [HttpLoggingInterceptor] instance
     * @since 1.0.0
     */
    @Provides
    fun provideOKHttpClient(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient().apply {
        OkHttpClient.Builder().run {
            addInterceptor(loggingInterceptor)
            interceptors().add(loggingInterceptor)
            build()
        }
    }



    /**
     * Returns an instance of the [ArgosAPI] interface for the retrofit class
     * @return [ArgosAPI] impl
     * @since 1.0.0
     */
    @Provides
    fun provideRetrofitInstance(
    ): UsersAPI {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(logging)
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://jsonplaceholder.typicode.com")
                .client(httpClientBuilder.build())
                .build()
        return retrofit.create(UsersAPI::class.java)
    }



    /**
     * Returns a [IRemoteDataSource] impl
     * @param webService [IWebService] instance
     * @since 1.0.0
     */
    @Provides
    fun providesNetworkDataSource(retrofitClient: UsersAPI): RemoteDataSource =
            UserNetworkDataSource(retrofitClient)




    /**
     * Returns a singleton [IRepository] implementation
     * @param remoteDataSource [IRemoteDataSource] implementation
     * @since 1.0.0
     */
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository =
            UserRepository(remoteDataSource)


    /**
     * Returns a [GetPopularPhotosUseCase] instance
     * @param repository [IRepository] impl
     * @since 1.0.0
     */

    @Provides
    fun provideGetUsersUseCase(repository: Repository): GetUsersUseCase =
        GetUsersUseCase(
                    repository
        )


}