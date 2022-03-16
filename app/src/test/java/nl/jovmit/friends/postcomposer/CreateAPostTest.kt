package nl.jovmit.friends.postcomposer

import nl.jovmit.friends.InstantTaskExecutorExtension
import nl.jovmit.friends.app.TestDispatchers
import nl.jovmit.friends.domain.post.InMemoryPostCatalog
import nl.jovmit.friends.domain.post.Post
import nl.jovmit.friends.domain.post.PostRepository
import nl.jovmit.friends.domain.user.InMemoryUserDataStore
import nl.jovmit.friends.infrastructure.ControllableClock
import nl.jovmit.friends.infrastructure.ControllableIdGenerator
import nl.jovmit.friends.postcomposer.state.CreateNewPostScreenState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
class CreateAPostTest {

  @Test
  fun aPostIsCreated() {
    val postText = "First Post"
    val post = Post("postId", "userId", postText, 1L)
    val userData = InMemoryUserDataStore("userId")
    val clock = ControllableClock(1L)
    val idGenerator = ControllableIdGenerator("postId")
    val viewModel = CreatePostViewModel(
      PostRepository(
        userData, InMemoryPostCatalog(
          idGenerator = idGenerator,
          clock = clock
        )
      ),
      TestDispatchers()
    )

    viewModel.createPost(postText)

    assertEquals(CreateNewPostScreenState(createdPostId = post.id), viewModel.screenState.value)
  }

  @Test
  fun anotherPostCreated() {
    val postText = "Second Post"
    val anotherPost = Post("postId2", "userId", postText, 2L)
    val userData = InMemoryUserDataStore("userId")
    val clock = ControllableClock(2L)
    val idGenerator = ControllableIdGenerator("postId2")
    val viewModel = CreatePostViewModel(
      PostRepository(
        userData, InMemoryPostCatalog(
          idGenerator = idGenerator,
          clock = clock
        )
      ),
      TestDispatchers()
    )

    viewModel.createPost(postText)

    assertEquals(
      CreateNewPostScreenState(createdPostId = anotherPost.id),
      viewModel.screenState.value
    )
  }
}