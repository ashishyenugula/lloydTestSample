package com.ashish.lloydtest.datasource.datastore

import com.ashish.lloydtest.business.datasource.datastore.AppDataStore

class AppDataStoreManagerTest: AppDataStore {

    private val datastore: MutableMap<String, String> = mutableMapOf()

    override suspend fun setValue(key: String, value: String) {
        datastore[key] = value
    }

    override suspend fun readValue(key: String): String? {
        return datastore[key]
    }
}