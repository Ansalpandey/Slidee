package com.example.project_x.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.project_x.data.model.Post
import com.example.project_x.data.repository.PostRepository
import javax.inject.Inject

class PostPagingSource @Inject constructor(private val postRepository: PostRepository) :
    PagingSource<Int, Post>() {

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
    return try {
      val currentPage = params.key ?: 1
      val pageSize = params.loadSize
      val response = postRepository.getPosts(currentPage, pageSize)

      LoadResult.Page(
          data = response.body()?.posts ?: emptyList(),
          prevKey = if (currentPage == 1) null else currentPage - 1,
          nextKey = if (response.body()?.posts?.size!! < pageSize) null else currentPage + 1)
    } catch (exception: Exception) {
      LoadResult.Error(exception)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }
}
