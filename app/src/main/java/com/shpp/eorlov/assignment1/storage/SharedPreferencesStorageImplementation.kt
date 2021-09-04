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

package com.shpp.eorlov.assignment1.storage

interface SharedPreferencesStorageImplementation {
    fun save(_key: String, _value: String)
    fun save(_key: String, _value: Int)
    fun save(_key: String, _value: Boolean)
    fun save(_key: String, _value: Long)
    fun save(_key: String, _value: Double)
    fun save(_key: String, _value: Float)
    fun getString(_key: String, default: String = ""): String?
    fun getFloat(_key: String): Float
    fun getBoolean(_key: String): Boolean
    fun getBoolean(_key: String, _def: Boolean): Boolean
    fun getInt(_key: String): Int
    fun getInt(_key: String, default: Int): Int
    fun getLong(_key: String, default: Long): Long
    fun getLong(_key: String): Long
    fun removePrefValue(key: String)
    fun contains(_key: String): Boolean
    fun clearPrefs()
}
