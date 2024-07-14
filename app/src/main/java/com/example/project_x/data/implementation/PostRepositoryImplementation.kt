package com.example.project_x.data.implementation

import com.example.project_x.common.Resource
import com.example.project_x.data.datasource.PostDataSource
import com.example.project_x.data.model.PostResponse
import com.example.project_x.data.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostRepositoryImplementation @Inject constructor(private val postDataSource: PostDataSource) :
    PostRepository {
    override suspend fun getPosts(): Flow<Resource<List<PostResponse>>> {
        return postDataSource.getPosts()
    }
}
