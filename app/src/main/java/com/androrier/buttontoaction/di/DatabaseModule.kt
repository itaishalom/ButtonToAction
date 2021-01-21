package com.androrier.buttontoaction.di

import android.content.Context
import androidx.room.Room
import com.androrier.buttontoaction.db.ActionRepository
import com.androrier.buttontoaction.db.MyActionDao
import com.androrier.buttontoaction.db.AppDatabase
import com.androrier.buttontoaction.interfaces.Repository
import com.androrier.buttontoaction.managers.AppSharedPrefsManeger
import com.androrier.buttontoaction.managers.ResourceManager
import com.androrier.buttontoaction.network.ApiHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideActionDao(appDatabase: AppDatabase): MyActionDao {
        return appDatabase.myActionDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "ActionsDB"
        ).build()
    }


    @Singleton
    @Provides
    fun provideActionRepository(
        actionDao: MyActionDao,
        apiHelper: ApiHelper,
        resourceManager: ResourceManager,
        appSharedPrefsManager: AppSharedPrefsManeger
    ) = ActionRepository(actionDao,apiHelper, resourceManager,appSharedPrefsManager) as Repository


}