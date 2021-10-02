package nl.jovmit.friends.postcomposer

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.postcomposer.state.CreatePostState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAPostTest {

  @Test
  fun aPostIsCreated() {
    val postText = "First Post"
    val post = Post("postId", "userId", postText, 1L)
    val viewModel = CreatePostViewModel()

    viewModel.createPost(postText)

    assertEquals(CreatePostState.Created(post), viewModel.postState.value)
  }

  @Test
  fun anotherPostCreated() {
    val postText = "Second Post"
    val anotherPost = Post("postId2", "userId", postText, 2L)
    val viewModel = CreatePostViewModel()

    viewModel.createPost(postText)

    assertEquals(CreatePostState.Created(anotherPost), viewModel.postState.value)
  }
}