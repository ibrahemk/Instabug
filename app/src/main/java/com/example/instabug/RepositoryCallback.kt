package com.example.instabug

interface RepositoryCallback<T> {
    fun onComplete(result: Result<T>?)
}
