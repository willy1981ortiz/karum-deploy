package com.helper

object CurpHelper {
    fun validate(curp: String): Boolean {
        return curp.length == 18  // length 18
                && curp.toCharArray().toList().subList(0, 4).filter { it.isLetter() }.size == 4 // first four are letter
                && curp.toCharArray().toList().subList(13, 16).filter { it.isLetter() }.size == 3 // Characters 14-16 are alphabetic (A-Z, a-z).
                && (curp[10] == 'H' || curp[10] == 'M') // Character 11 is either H or M, representing the male or female gender respectively.
                && curp.last().isDigit() //Character 18 is numeric (0-9).

//        http://support.sas.com/documentation/onlinedoc/qkb/27/QKBCI27/Help/Content/QKBCI_ESMEX/QKBCI_ESMEX_ID_CURP-Validation.html
//        Characters 5-10 are a valid date in YYMMDD format.
//        Characters 12-13 are a valid 2-letter Mexican state code, or "NE" for individuals who are foreign-born.
    }

    fun validateIne(value: String): Boolean {
        return value.length == 18
    }
}