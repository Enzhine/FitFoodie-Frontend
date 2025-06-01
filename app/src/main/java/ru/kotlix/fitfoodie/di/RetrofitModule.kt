package ru.kotlix.fitfoodie.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.openapitools.client.infrastructure.ApiClient
import retrofit2.Retrofit
import ru.kotlix.fitfoodie.R
import javax.inject.Singleton
import ru.kotlix.fitfoodie.api.DefaultApi

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideApiClient(
        @ApplicationContext ctx: Context,
        logger: HttpLoggingInterceptor
    ): ApiClient =
        ApiClient(
            baseUrl = ctx.getString(R.string.baseUrl),
            okHttpClientBuilder = OkHttpClient.Builder().addInterceptor(logger)
        )

    @Provides
    @Singleton
    fun provideAuthApi(apiClient: ApiClient): DefaultApi =
        apiClient.createService(DefaultApi::class.java)

//    @Provides
//    @Singleton
//    fun provideRetrofit(
//        @ApplicationContext ctx: Context,
//        client: OkHttpClient,
//        moshi: Moshi
//    ): Retrofit =
//        Retrofit.Builder()
//            .baseUrl()
//            .client(client)
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .build()

//    @Provides
//    @Singleton
//    fun provideAuthApi(retrofit: Retrofit): DefaultApi =
//        retrofit.create(DefaultApi::class.java)
}
