package com.trackmyterm.util

/**
 * Base response body wrapper to define fixed format for response of an API
 * @param resultType Result type indicating success or failure
 * @param data Data that is sent to frontend
 * @param message Additional message/description indicating result for the API
 * @param T Generic type that changes based on the return expectancy
 *
 * Example:
 * ```
 * {
 *     "resultType": "SUCCESS",
 *     "data": {
 *         "id": 6,
 *         "name": "Vraj",
 *         "email": "abc@gmail.com",
 *         "age": 24
 *     },
 *     "message": "User added successfully"
 * }
 * ```
 */
data class ResponseBody<T>(val resultType: ResultType, val data: T, val message: String) {
    enum class ResultType {
        SUCCESS,
        FAILURE
    }
}
