/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeEqualIgnoringNewLineDifferences.shouldBeEqualIgnoringNewLineDifferences;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.verify;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Strings#assertIsEqualToNormalizingNewlines(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)}</code>
 * .
 *
 * @author Mauricio Aniche
 */
class Strings_assertIsEqualToNormalizingNewlines_Test extends StringsBaseTest {

  @Test
  void should_pass_if_both_strings_are_equals_after_normalizing_newline() {
    strings.assertIsEqualToNormalizingNewlines(someInfo(), "Lord of the Rings\r\nis cool", "Lord of the Rings\nis cool");
    strings.assertIsEqualToNormalizingNewlines(someInfo(), "Lord of the Rings\nis cool", "Lord of the Rings\nis cool");
  }

  @Test
  void should_pass_if_comparing_string_with_only_newlines() {
    strings.assertIsEqualToNormalizingNewlines(someInfo(), "\n", "\r\n");
    strings.assertIsEqualToNormalizingNewlines(someInfo(), "\r\n", "\n");
    strings.assertIsEqualToNormalizingNewlines(someInfo(), "\r\n", "\r\n");
    strings.assertIsEqualToNormalizingNewlines(someInfo(), "\n", "\n");
  }

  @Test
  void should_fail_if_newlines_are_different_in_both_strings() {
    String actual = "Lord of the Rings\r\n\r\nis cool";
    String expected = "Lord of the Rings\nis cool";

    Throwable error = catchThrowable(() -> strings.assertIsEqualToNormalizingNewlines(someInfo(), actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(someInfo(), shouldBeEqualIgnoringNewLineDifferences(actual, expected),
                             "Lord of the Rings\n\nis cool", expected);
  }

  @Test
  void should_pass_if_actual_and_expected_are_both_null() {
    String actual = null;
    String expected = null;
    assertThat(actual).isEqualToNormalizingNewlines(expected);
  }

  @Test
  void should_fail_if_actual_is_null_and_expected_is_not() {
    String actual = null;
    String expected = "";

    Throwable error = catchThrowable(() -> strings.assertIsEqualToNormalizingNewlines(someInfo(), actual, expected));
    assertThat(error).isInstanceOf(AssertionError.class);

    verify(failures).failure(someInfo(), shouldNotBeNull("actual"));
  }

  @Test
  void should_fail_if_actual_is_not_null_but_expected_is_null() {
    String actual = "";
    String expected = null;

    Throwable error = catchThrowable(() -> strings.assertIsEqualToNormalizingNewlines(someInfo(), actual, expected));
    assertThat(error).isInstanceOf(AssertionError.class);

    verify(failures).failure(someInfo(), shouldBeEqualIgnoringNewLineDifferences(actual, "null"));
  }
}
