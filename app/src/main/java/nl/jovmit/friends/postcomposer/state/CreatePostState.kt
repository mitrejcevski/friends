package nl.jovmit.friends.postcomposer.state

import nl.jovmit.friends.domain.post.Post

sealed class CreatePostState {

  object Loading : CreatePostState()

  data class Created(val post: Post) : CreatePostState()

  object BackendError : CreatePostState()

  object Offline : CreatePostState()
}
