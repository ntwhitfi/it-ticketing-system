package com.ntwhitfi.it.ticketing.system.common.repository

import com.ntwhitfi.it.ticketing.system.common.model.RepositoryResponse

interface IRepository<T> {
    fun add(item: T) : RepositoryResponse
    fun update(item: T) : RepositoryResponse
    fun getById(id: String) : RepositoryResponse
    fun remove(id: String) : RepositoryResponse
}