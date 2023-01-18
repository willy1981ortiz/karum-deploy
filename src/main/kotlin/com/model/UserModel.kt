package com.model

import com.Tables.USER
import com.tables.pojos.User

object UserModel : Model() {

    const val NEW_USER = 0.0
    const val INE_OR_PASS_UPLOADED = 1.0
    const val DOCUMENT_COMPLETE = 2.0
    const val SUMMARY_1_COMPLETE = 3.0
    const val TC41_API_COMPLETE = 4.0
    const val AUTH_CODE_TC42_ACCEPTED = 5.0
    const val SUMMARY_2_COMPLETE = 6.0
    const val MOBILE_DATA_COMPLETE = 7.0
    const val TC43_API_COMPLETE = 8.0
    const val TC44_API_COMPLETE = 9.0
    const val SHIPPING_API_COMPLETE = 10.0

    fun login(mobileNumber: String, authToken: String): Int {
        return db.insertInto(USER)
            .set(USER.MOBILE_NUMBER, mobileNumber)
            .set(USER.USER_AUTH, authToken)
            .onDuplicateKeyUpdate()
            .set(USER.USER_AUTH, authToken)
            .execute()
    }

    fun getUser(mobile: String): User? {
        return db.selectFrom(USER).where(USER.MOBILE_NUMBER.eq(mobile)).fetchOne()?.into(User::class.java)
    }

    fun getUserByAuthToken(token: String): User? {
        return db.selectFrom(USER).where(USER.USER_AUTH.eq(token)).fetchOne()?.into(User::class.java)
    }

    fun updateUserStatus(statusCode: Double, userId: Int): Int {
        return db.update(USER)
            .set(USER.STATUS, statusCode)
            .where(USER.USER_ID.eq(userId))
            .execute()
    }

    fun updateMobileHandOffFlag(flag: Boolean, userId: Int): Int {
        return db.update(USER)
            .set(USER.USER_ID, userId).apply {
                if (flag) {
                    set(USER.IS_HANDD_OFF, 1)
                } else {
                    set(USER.IS_HANDD_OFF, 0)
                }
            }
            .where(USER.USER_ID.eq(userId))
            .execute()
    }

    fun updateUserCurp(curp: String? = null, ine: String?, userId: Int): Boolean {
        return db.update(USER)
            .set(USER.USER_ID, userId)
            .apply {
                if (curp != null) {
                    set(USER.CURP, curp)
                }
                if (ine != null) set(USER.INE, ine)
            }
            .where(USER.USER_ID.eq(userId))
            .execute() > 0
    }

    fun updateConfirmationCode(code: String, userId: Int): Int {
        return db.update(USER)
            .set(USER.CONFIRMATION_CODE, code)
            .where(USER.USER_ID.eq(userId))
            .execute()
    }

    fun updateProduct(productId: String, price: String, userId: Int?): Boolean {
        return db.update(USER)
            .set(USER.PRODUCT_ID, productId)
            .set(USER.PRICE, price)
            .where(USER.USER_ID.eq(userId))
            .execute() > 0
    }

    // 1 For Decline
    /*fun updateUserToDecline(userId: Int): Boolean {
        return db.update(USER)
            .set(USER.IS_DECLINED, 1)
            .where(USER.USER_ID.eq(userId))
            .execute() > 0
    }*/
}