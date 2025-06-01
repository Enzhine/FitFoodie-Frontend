package ru.kotlix.fitfoodie.domain

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.datastore by preferencesDataStore(name = "prefs_datastore")