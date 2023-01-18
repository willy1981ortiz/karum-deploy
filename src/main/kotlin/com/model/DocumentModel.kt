package com.model

import com.Tables.DOCUMENT
import com.tables.pojos.Document
import java.time.LocalDate

object DocumentModel : Model() {

    fun getDocumentByUserId(userId: Int): Document? {
        return db.selectFrom(DOCUMENT).where(DOCUMENT.USER_ID.eq(userId)).fetchOne()?.into(Document::class.java)
    }

    fun uploadINEDocument(front: ByteArray, back: ByteArray, userId: Int): Int {
        return db.insertInto(DOCUMENT)
            .set(DOCUMENT.INE_FRONT, front)
            .set(DOCUMENT.INE_BACK, back)
            .set(DOCUMENT.INE_TIMESTAMP, LocalDate.now())
            .set(DOCUMENT.USER_ID, userId)
            .onDuplicateKeyUpdate()
            .set(DOCUMENT.INE_FRONT, front)
            .set(DOCUMENT.INE_BACK, back)
            .set(DOCUMENT.INE_TIMESTAMP, LocalDate.now())
            .execute()
    }

    fun uploadPassportDocument(passportImg: ByteArray, userId: Int): Int {
        return db.insertInto(DOCUMENT)
            .set(DOCUMENT.PASSPORT, passportImg)
            .set(DOCUMENT.PASSPOT_TIMESTAMP, LocalDate.now())
            .set(DOCUMENT.USER_ID, userId)
            .onDuplicateKeyUpdate()
            .set(DOCUMENT.PASSPORT, passportImg)
            .set(DOCUMENT.PASSPOT_TIMESTAMP, LocalDate.now())
            .execute()
    }

    fun uploadAddressDocument(docImg: ByteArray?, userId: Int): Int {
        return db.insertInto(DOCUMENT)
            .set(DOCUMENT.PROOF_OF_ADDRESS, docImg)
            .set(DOCUMENT.POA_TIMESTAMP, LocalDate.now())
            .set(DOCUMENT.USER_ID, userId)
            .onDuplicateKeyUpdate()
            .set(DOCUMENT.PROOF_OF_ADDRESS, docImg)
            .set(DOCUMENT.POA_TIMESTAMP, LocalDate.now())
            .execute()
    }

    fun uploadIncomeDocument(docImg: ByteArray, userId: Int): Int {
        return db.insertInto(DOCUMENT)
            .set(DOCUMENT.PROOF_OF_INCOME, docImg)
            .set(DOCUMENT.POI_TIMESTAMP, LocalDate.now())
            .set(DOCUMENT.USER_ID, userId)
            .onDuplicateKeyUpdate()
            .set(DOCUMENT.PROOF_OF_INCOME, docImg)
            .set(DOCUMENT.POI_TIMESTAMP, LocalDate.now())
            .execute()
    }
}