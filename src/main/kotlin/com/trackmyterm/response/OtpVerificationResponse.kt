package com.trackmyterm.response

import com.trackmyterm.response.ResponseBody.ResultType.SUCCESS
import com.trackmyterm.util.Constants.OTP_VERIFICATION_SUCCESS

data class OtpVerificationResponse(
    override val resultType: ResponseBody.ResultType,
    override val data: Boolean,
    override val message: String
) : ResponseBody<Boolean> {

    companion object {
        fun success(): OtpVerificationResponse =
            OtpVerificationResponse(SUCCESS, true, OTP_VERIFICATION_SUCCESS)
    }
}
