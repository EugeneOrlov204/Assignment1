/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shpp.eorlov.assignment1.di

import android.content.Context
import com.shpp.eorlov.assignment1.db.ContactsDatabase
import com.shpp.eorlov.assignment1.db.LocalDB
import com.shpp.eorlov.assignment1.storage.SharedPreferencesStorage
import com.shpp.eorlov.assignment1.storage.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class SharedPrefStorage

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ContactsDatabaseStorage

@Module
class StorageModule {

    @SharedPrefStorage
    @Provides
    fun provideStorage(context: Context): Storage {
        return SharedPreferencesStorage(context)
    }

    @ContactsDatabaseStorage
    @Provides
    fun provideDatabase(context: Context): LocalDB {
        return ContactsDatabase()
    }
}