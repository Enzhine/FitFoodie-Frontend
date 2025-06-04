package ru.kotlix.fitfoodie.di

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kotlix.fitfoodie.api.DefaultApi
import ru.kotlix.fitfoodie.domain.service.ProductStorage
import ru.kotlix.fitfoodie.domain.service.ProductStorageImpl
import ru.kotlix.fitfoodie.domain.service.ProductsCacheHash
import ru.kotlix.fitfoodie.domain.service.ProductsCacheHashV1
import ru.kotlix.fitfoodie.domain.service.TokenStorage
import ru.kotlix.fitfoodie.domain.service.TokenStorageImpl
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorage
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorageImpl
import ru.kotlix.fitfoodie.domain.service.UserPreferencesStorage
import ru.kotlix.fitfoodie.domain.service.UserPreferencesStorageImpl
import ru.kotlix.fitfoodie.domain.service.UserRelativeTagsProvider
import ru.kotlix.fitfoodie.domain.service.UserRelativeTagsProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext ctx: Context): TokenStorage =
        TokenStorageImpl(ctx)

    @Provides
    @Singleton
    fun provideUserCredentialsStorage(@ApplicationContext ctx: Context): UserCredentialsStorage =
        UserCredentialsStorageImpl(ctx)

    @Provides
    @Singleton
    fun provideUserPreferencesStorage(
        @ApplicationContext ctx: Context,
        userCredentialsStorage: UserCredentialsStorage
    ): UserPreferencesStorage =
        UserPreferencesStorageImpl(ctx, userCredentialsStorage)

    @Provides
    @Singleton
    fun provideProductsCacheHash(): ProductsCacheHash = ProductsCacheHashV1()

    @Provides
    @Singleton
    fun provideProductStorage(
        @ApplicationContext ctx: Context,
        productsCacheHash: ProductsCacheHash,
        userCredentialsStorage: UserCredentialsStorage,
        defaultApi: DefaultApi,
        moshi: Moshi
    ): ProductStorage =
        ProductStorageImpl(ctx, productsCacheHash, userCredentialsStorage, defaultApi, moshi)

    @Provides
    @Singleton
    fun provideUserRelativeTagsProvider(
        userPreferencesStorage: UserPreferencesStorage,
    ): UserRelativeTagsProvider =
        UserRelativeTagsProviderImpl(userPreferencesStorage)
}
