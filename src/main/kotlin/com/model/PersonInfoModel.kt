package com.model

import com.Tables.*
import com.enums.Tc41Alert
import com.tables.pojos.ShippingAddress
import com.tables.pojos.Tc41
import com.tables.pojos.Tc42

object PersonInfoModel : Model() {

    fun getPersonInfoData(userId: Int): Tc41? {
        return db.selectFrom(TC41).where(TC41.USER_ID_REF.eq(userId)).fetchOne()?.into(Tc41::class.java)
    }


    fun isTelephoneRegistered(telephone: String): Boolean {
        return db.fetchCount(db.selectFrom(TC41).where(TC41.TELEPHONE.eq(telephone))) > 0
    }

    fun addPersonInfoData(
        name: String,
        parentSurname: String,
        motherSurname: String,
        year: Int,
        month: Int,
        day: Int,
        curp: String,
        ine: String,
        zipCode: String,
        street: String,
        extNo: String,
        intNo: String?,
        state: String,
        stateSEPOMEXCode: String,
        municipality: String,
        municipalitySEPOMEXCode: String,
        colony: String,
        colonySEPOMEXCode: String,
        city: String?,
        citySEPOMEXCode: String?,
        telephone: String,
        companyName: String,
        companyPhone: String,
        monthlyIncome: String,
        gender: String,
        email: String,
        stateOfBirth: String,
        alert: Tc41Alert,
        userId: Int
    ): Int {
        return db.insertInto(TC41)
            .set(TC41.NAME, name)
            .set(TC41.PARENT_SURNAME, parentSurname)
            .set(TC41.MOTHER_SURNAME, motherSurname)
            .set(TC41.YEAR, year)
            .set(TC41.MONTH, month)
            .set(TC41.DAY, day)
            .set(TC41.CURP_NO, curp)
            .set(TC41.INE, ine)
            .set(TC41.ZIP_CODE, zipCode)
            .set(TC41.STREET, street)
            .set(TC41.EXT_NO, extNo)
            .set(TC41.INT_NO, intNo)
            .set(TC41.STATE, state)
            .set(TC41.STATE_CODE, stateSEPOMEXCode)
            .set(TC41.MUNICIPALITY, municipality)
            .set(TC41.MUNICIPALITY_CODE, municipalitySEPOMEXCode)
            .set(TC41.COLONY, colony)
            .set(TC41.COLONY_CODE, colonySEPOMEXCode)
            .set(TC41.CITY, city)
            .set(TC41.CITY_CODE, citySEPOMEXCode)
            .set(TC41.TELEPHONE, telephone)
            .set(TC41.COMPANY_NAME, companyName)
            .set(TC41.COMPANY_PHONE, companyPhone)
            .set(TC41.MONTHLY_INCOME, monthlyIncome)
            .set(TC41.GENDER, gender)
            .set(TC41.EMAIL, email)
            .set(TC41.STATE_OF_BIRTH, stateOfBirth)
            .set(TC41.USER_ID_REF, userId)
            .set(TC41.ALERT, alert)
            .onDuplicateKeyUpdate()
            .set(TC41.NAME, name)
            .set(TC41.PARENT_SURNAME, parentSurname)
            .set(TC41.MOTHER_SURNAME, motherSurname)
            .set(TC41.YEAR, year)
            .set(TC41.MONTH, month)
            .set(TC41.DAY, day)
            .set(TC41.CURP_NO, curp)
            .set(TC41.INE, ine)
            .set(TC41.ZIP_CODE, zipCode)
            .set(TC41.STREET, street)
            .set(TC41.EXT_NO, extNo)
            .set(TC41.INT_NO, intNo)
            .set(TC41.STATE, state)
            .set(TC41.STATE_CODE, stateSEPOMEXCode)
            .set(TC41.MUNICIPALITY, municipality)
            .set(TC41.MUNICIPALITY_CODE, municipalitySEPOMEXCode)
            .set(TC41.COLONY, colony)
            .set(TC41.COLONY_CODE, colonySEPOMEXCode)
            .set(TC41.CITY, city)
            .set(TC41.CITY_CODE, citySEPOMEXCode)
            .set(TC41.TELEPHONE, telephone)
            .set(TC41.COMPANY_NAME, companyName)
            .set(TC41.COMPANY_PHONE, companyPhone)
            .set(TC41.MONTHLY_INCOME, monthlyIncome)
            .set(TC41.GENDER, gender)
            .set(TC41.EMAIL, email)
            .set(TC41.STATE_OF_BIRTH, stateOfBirth)
            .set(TC41.ALERT, alert)
            .execute()
    }

    fun getSupplementaryData(userId: Int): Tc42? {
        return db.selectFrom(TC42).where(TC42.USER_ID_REF.eq(userId)).fetchOne()?.into(Tc42::class.java)
    }

    fun addSupplementaryData(
        profession: String,
        homePhone: String,
        supYear: Int,
        supMonth: Int,
        zipCode: String,
        street: String,
        extNo: String,
        intNo: String?,
        state: String,
        stateSEPOMEXCode: String,
        municipality: String,
        municipalitySEPOMEXCode: String,
        colony: String,
        colonySEPOMEXCode: String,
        city: String?,
        citySEPOMEXCode: String?,
        labYear: Int,
        labMonth: Int,
        giro: String,
        occupation: String,
        familyName1: String,
        familyPhone1: String,
        familyRelation1: String,
        familyName2: String,
        familyPhone2: String,
        familyRelation2: String,
        personalReferenceName: String,
        personalReferencePhone: String,
        userId: Int
    ): Int {
        return db.insertInto(TC42)
            .set(TC42.PROFESSION, profession)
            .set(TC42.HOME_PHONE, homePhone)
            .set(TC42.SUP_YEAR, supYear)
            .set(TC42.SUP_MONTH, supMonth)
            .set(TC42.ZIP_CODE, zipCode)
            .set(TC42.STREET, street)
            .set(TC42.EXT_NO, extNo)
            .set(TC42.INT_NO, intNo)
            .set(TC42.STATE, state)
            .set(TC42.STATE_CODE, stateSEPOMEXCode)
            .set(TC42.MUNICIPALITY, municipality)
            .set(TC42.MUNICIPALITY_CODE, municipalitySEPOMEXCode)
            .set(TC42.COLONY, colony)
            .set(TC42.COLONY_CODE, colonySEPOMEXCode)
            .set(TC42.CITY, city)
            .set(TC42.CITY_CODE, citySEPOMEXCode)
            .set(TC42.LAB_YEAR, labYear)
            .set(TC42.LAB_MONTH, labMonth)
            .set(TC42.GIRO, giro)
            .set(TC42.OCCUPATION, occupation)
            .set(TC42.FAMILY_REFERENCE_NAME1, familyName1)
            .set(TC42.FAMILY_REFERENCE_PHONE1, familyPhone1)
            .set(TC42.FAMILY_RELATION1, familyRelation1)
            .set(TC42.FAMILY_REFERENCE_NAME2, familyName2)
            .set(TC42.FAMILY_REFERENCE_PHONE2, familyPhone2)
            .set(TC42.FAMILY_RELATION2, familyRelation2)
            .set(TC42.PERSONAL_REFERENCE_NAME, personalReferenceName)
            .set(TC42.PERSONAL_REFERENCE_PHONE, personalReferencePhone)
            .set(TC42.USER_ID_REF, userId)
            .execute()
    }

    fun getShippingAddress(userId: Int): ShippingAddress? {
        return db.selectFrom(SHIPPING_ADDRESS)
            .where(SHIPPING_ADDRESS.USER_ID.eq(userId))
            .fetchOne()?.into(ShippingAddress::class.java)
    }

    fun addShippingAddress(
        street: String,
        extNo: String,
        intNo: String?,
        zipCode: String,
        state: String,
        stateSEPOMEXCode: String,
        municipality: String,
        municipalitySEPOMEXCode: String,
        colony: String,
        colonySEPOMEXCode: String,
        city: String,
        citySEPOMEXCode: String,
        userId: Int
    ): Int {
        return db.insertInto(SHIPPING_ADDRESS)
            .set(SHIPPING_ADDRESS.STREET, street)
            .set(SHIPPING_ADDRESS.EXT_NO, extNo)
            .set(SHIPPING_ADDRESS.INT_NO, intNo)
            .set(SHIPPING_ADDRESS.ZIP_CODE, zipCode)
            .set(SHIPPING_ADDRESS.STATE, state)
            .set(SHIPPING_ADDRESS.STATE_CODE, stateSEPOMEXCode)
            .set(SHIPPING_ADDRESS.MUNICIPALITY, municipality)
            .set(SHIPPING_ADDRESS.MUNICIPALITY_CODE, municipalitySEPOMEXCode)
            .set(SHIPPING_ADDRESS.COLONY, colony)
            .set(SHIPPING_ADDRESS.COLONY_CODE, colonySEPOMEXCode)
            .set(SHIPPING_ADDRESS.CITY, city)
            .set(SHIPPING_ADDRESS.CITY_CODE, citySEPOMEXCode)
            .set(SHIPPING_ADDRESS.USER_ID, userId)
            .execute()
    }

    fun updateCurpNo(curp: String, userId: Int): Boolean {
        return db.update(TC41)
            .set(TC41.CURP_NO, curp)
            .where(TC41.USER_ID_REF.eq(userId))
            .execute() > 0
    }

    fun updateApplicationId(applicationId: String, userId: Int): Int {
        return db.update(TC41)
            .set(TC41.APPLICATIONID, applicationId)
            .where(TC41.USER_ID_REF.eq(userId))
            .execute()
    }

    fun addINEData(
        parentSurname: String,
        motherSurname: String,
        name: String,
        gender: String,
        stateOfBirth: String,
        year: Int,
        month: Int,
        day: Int,
        curp: String,
        summaryINE: String,
        userId: Int
    ): Int {
        return db.insertInto(TC41)
            .set(TC41.PARENT_SURNAME, parentSurname)
            .set(TC41.MOTHER_SURNAME, motherSurname)
            .set(TC41.NAME, name)
            .set(TC41.GENDER, gender)
            .set(TC41.STATE_OF_BIRTH, stateOfBirth)
            .set(TC41.YEAR, year)
            .set(TC41.MONTH, month)
            .set(TC41.DAY, day)
            .set(TC41.CURP_NO, curp)
            .set(TC41.INE, summaryINE)
            .set(TC41.USER_ID_REF, userId)
            .onDuplicateKeyUpdate()
            .set(TC41.PARENT_SURNAME, parentSurname)
            .set(TC41.MOTHER_SURNAME, motherSurname)
            .set(TC41.NAME, name)
            .set(TC41.GENDER, gender)
            .set(TC41.STATE_OF_BIRTH, stateOfBirth)
            .set(TC41.YEAR, year)
            .set(TC41.MONTH, month)
            .set(TC41.DAY, day)
            .set(TC41.CURP_NO, curp)
            .set(TC41.INE, summaryINE)
            .execute()
    }
}