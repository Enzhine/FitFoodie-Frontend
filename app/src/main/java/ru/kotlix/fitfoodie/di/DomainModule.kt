package ru.kotlix.fitfoodie.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kotlix.fitfoodie.domain.dto.UserPreferences
import ru.kotlix.fitfoodie.domain.service.TokenStorage
import ru.kotlix.fitfoodie.domain.service.TokenStorageImpl
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorage
import ru.kotlix.fitfoodie.domain.service.UserCredentialsStorageImpl
import ru.kotlix.fitfoodie.domain.service.UserPreferencesStorage
import ru.kotlix.fitfoodie.domain.service.UserPreferencesStorageImpl
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
    fun provideUserPreferencesStorage(@ApplicationContext ctx: Context): UserPreferencesStorage =
        UserPreferencesStorageImpl(ctx)
}
