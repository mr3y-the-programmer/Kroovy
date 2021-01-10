/*
 *    Copyright [2021] [MR3Y]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mr3y.plugins.kroovy

import java.lang.Exception

/**
 * After Starting the convert/migrate action, we have to return a result to
 * notify the user about the current progress, this result has 3 states:
 *     - Initial: start state of the result
 *     - WorkInProgress: this is the state of Our result as long as there is some work is being done
 *     - Finished: this is the end state of our result and has two states:
 *          - Succeeded: if the conversion process goes as intended
 *          - Failed: if the conversion process failed at any point
 */
sealed class ConvertResult{
    object Initial: ConvertResult()
    data class WorkInProgress(val percentage: Int): ConvertResult()
}

sealed class Finished: ConvertResult() {
    object Succeeded: Finished()
    data class Failed(private val message: String, val exception: Exception? = null): Finished() {
        fun getMessage() = if(message.isNotEmpty()) message else "Unknown failure occurred, try again"
    }
}
