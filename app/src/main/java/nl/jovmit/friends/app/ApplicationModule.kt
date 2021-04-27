package nl.jovmit.friends.app

import nl.jovmit.friends.domain.user.InMemoryUserCatalog
import nl.jovmit.friends.domain.user.UserCatalog
import nl.jovmit.friends.domain.user.UserRepository
import nl.jovmit.friends.domain.validation.RegexCredentialsValidator
import nl.jovmit.friends.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
  single<UserCatalog> { InMemoryUserCatalog() }
  factory { RegexCredentialsValidator() }
  factory { UserRepository(userCatalog = get()) }

  viewModel {
    SignUpViewModel(
      credentialsValidator = get(),
      userRepository = get(),
      dispatchers = TestDispatchers()
    )
  }
}