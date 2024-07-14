package com.example.project_x.data.model

data class PostResponse(
  val __v: Int?, // 0
  val _id: String?, // 669376bd59680a5550409cd5
  val content: String?, // The NullPointerException indicates that there's a null value being accessed in your LazyColumn. The likely cause is the posts.data being null or empty at the time of rendering the PostItem composables. Here's how to fix the code to handle null and empty lists more gracefully:
  val createdAt: String?, // 2024-07-14T06:57:01.707Z
  val createdBy: CreatedBy?,
  val imageUrl: String?, // http://res.cloudinary.com/ansalpandey/image/upload/v1720940222/awynqmu7rv5ivrlmpbk4.png
  val updatedAt: String?, // 2024-07-14T06:57:01.707Z
  val videoUrl: String?
)