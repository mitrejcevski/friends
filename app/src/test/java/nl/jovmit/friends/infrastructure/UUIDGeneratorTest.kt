package nl.jovmit.friends.infrastructure

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.regex.Pattern

class UUIDGeneratorTest {

  private companion object {
    private const val UUID_REGEX =
      """[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}"""
  }

  @Test
  fun generatesCorrectUUID() {
    val pattern = Pattern.compile(UUID_REGEX)

    val uuid = UUIDGenerator().next()

    assertTrue(pattern.matcher(uuid).matches())
  }
}