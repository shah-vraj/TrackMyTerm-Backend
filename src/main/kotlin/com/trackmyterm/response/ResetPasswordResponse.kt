package com.trackmyterm.response

import com.trackmyterm.response.ResponseBody.ResultType.SUCCESS
import com.trackmyterm.util.Constants.RESET_PASSWORD_REQUEST_SUCCESS

data class ResetPasswordResponse(
    override val resultType: ResponseBody.ResultType,
    override val data: Boolean,
    override val message: String
) : ResponseBody<Boolean> {

    companion object {
        fun success(): ResetPasswordResponse =
            ResetPasswordResponse(SUCCESS, true, RESET_PASSWORD_REQUEST_SUCCESS)
    }
}
