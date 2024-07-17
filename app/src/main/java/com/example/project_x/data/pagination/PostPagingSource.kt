package com.example.project_x.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.project_x.common.Resource
import com.example.project_x.data.model.Post
import com.example.project_x.data.repository.PostRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PostPagingSource @Inject constructor(private val postRepository: PostRepository) :
    PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: 1
        return try {
            when (val response = postRepository.getPosts(page, params.loadSize).first()) {
                is Resource.Success -> {
                    val posts = response.data?.posts ?: emptyList()
                    LoadResult.Page(
                        data = posts,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (posts.isEmpty()) null else page + 1,
                    )
                }

                is Resource.Error -> LoadResult.Error(Exception(response.message))
                else -> LoadResult.Error(Exception("Unknown error"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
