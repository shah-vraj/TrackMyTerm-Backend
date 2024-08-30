package com.trackmyterm.response

import com.trackmyterm.response.ResponseBody.ResultType.SUCCESS
import com.trackmyterm.util.Constants.FORGOT_PASSWORD_REQUEST_SUCCESS

data class ForgotPasswordResponse(
    override val resultType: ResponseBody.ResultType,
    override val data: Boolean,
    override val message: String
) : ResponseBody<Boolean> {

    companion object {
        fun success(): ForgotPasswordResponse =
            ForgotPasswordResponse(SUCCESS, true, FORGOT_PASSWORD_REQUEST_SUCCESS)
    }
}
