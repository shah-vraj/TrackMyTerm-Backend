package com.trackmyterm.response

import com.trackmyterm.response.ResponseBody.ResultType.SUCCESS
import com.trackmyterm.util.Constants.LOGIN_REQUEST_SUCCESS

data class LoginResponse(
    override val resultType: ResponseBody.ResultType,
    override val data: Data,
    override val message: String
) : ResponseBody<LoginResponse.Data> {

    data class Data(
        val token: String
    )

    companion object {
        fun success(data: Data): LoginResponse =
            LoginResponse(SUCCESS, data, LOGIN_REQUEST_SUCCESS)
    }
}
