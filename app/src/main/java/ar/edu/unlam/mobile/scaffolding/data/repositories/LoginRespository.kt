package ar.edu.unlam.mobile.scaffolding.data.repositories

import ar.edu.unlam.mobile.scaffolding.data.datasources.network.ApiService
import javax.inject.Inject

class LoginRespository
    @Inject
    constructor(
        private val apiService: ApiService,
    ) {
        // suspend fun login(){
        //   return apiService.login()
        // }
    }
