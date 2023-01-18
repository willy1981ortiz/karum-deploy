package com.data

class Estado {
    val estadoList = mapOf(
        "AS" to "AGUASCALIENTES",
        "BC" to "BAJA CALIFORNIA",
        "BS" to "BAJA CALIFORNIA SUR",
        "CC" to "CC",
        "CS" to "CHIAPAS",
        "CH" to "CHIHUAHUA",
        "CL" to "COLIMA",
        "DF" to "CIUDAD DE MÉXICO",
        "DG" to "DURANGO",
        "GT" to "GUANAJUATO",
        "GR" to "GUERRERO",
        "HG" to "HIDALGO",
        "JC" to "JC",
        "MC" to "MC",
        "MN" to "MN",
        "MS" to "MS",
        "NT" to "NT",
        "NL" to "NUEVO LEÓN",
        "OC" to "OC",
        "PL" to "PL",
        "QT" to "QT",
        "QR" to "QUINTANA ROO",
        "SP" to "SP",
        "SL" to "SAN LUIS POTOSI",
        "SR" to "SR",
        "TC" to "TC",
        "TS" to "TS",
        "TL" to "TL",
        "VZ" to "VZ",
        "YN" to "YN",
        "ZS" to "ZS",
        "NE" to "NE",
    )

    fun getStateList(): MutableList<EstadoModel> {
        val estadoItemList:MutableList<EstadoModel> = mutableListOf()
        estadoItemList.add(EstadoModel("AGUASCALIENTES", "AS", "AG"))
        estadoItemList.add(EstadoModel("BAJA CALIFORNIA", "BC", "BC"))
        estadoItemList.add(EstadoModel("BAJA CALIFORNIA SUR", "BS", "BS"))
        estadoItemList.add(EstadoModel("CAMPECHE", "CC", "CM"))
        estadoItemList.add(EstadoModel("COAHUILA DE ZARAGOZA", "CL", "CA"))
        estadoItemList.add(EstadoModel("COLIMA", "CM", "CL"))
        estadoItemList.add(EstadoModel("CHIAPAS", "CS", "CS"))
        estadoItemList.add(EstadoModel("CHIHUAHUA", "CH", "CH"))
        estadoItemList.add(EstadoModel("CIUDAD DE MÉXICO", "DF", "DF"))
        estadoItemList.add(EstadoModel("DURANGO", "DG", "DG"))
        estadoItemList.add(EstadoModel("GUANAJUATO", "GT", "GT"))
        estadoItemList.add(EstadoModel("GUERRERO", "GR", "GR"))
        estadoItemList.add(EstadoModel("HIDALGO", "HG", "HG"))
        estadoItemList.add(EstadoModel("JALISCO", "JC", "JA"))
        estadoItemList.add(EstadoModel("MÉXICO", "MC", "MX"))
        estadoItemList.add(EstadoModel("MICHOACAN DE OCAMPO", "MN", "MI"))
        estadoItemList.add(EstadoModel("MORELOS", "MS", "MO"))
        estadoItemList.add(EstadoModel("NAYARIT", "NT", "MA"))
        estadoItemList.add(EstadoModel("NUEVO LEON", "NL", "NL"))
        estadoItemList.add(EstadoModel("OAXACA", "OC", "OX"))
        estadoItemList.add(EstadoModel("PUEBLA", "PL", "PU"))
        estadoItemList.add(EstadoModel("QUERETARO", "QT", "QO"))
        estadoItemList.add(EstadoModel("QUINTANA ROO", "QR", "QR"))
        estadoItemList.add(EstadoModel("SAN LUIS POTOSI", "SP", "SL"))
        estadoItemList.add(EstadoModel("SINALOA", "SL", "SI"))
        estadoItemList.add(EstadoModel("SONORA", "SR", "SO"))
        estadoItemList.add(EstadoModel("TABASCO", "TC", "TA"))
        estadoItemList.add(EstadoModel("TAMAULIPAS", "TS", "TM"))
        estadoItemList.add(EstadoModel("TLAXCALA", "TL", "TX"))
        estadoItemList.add(EstadoModel("VERACRUZ DE IGNACIO DE LA LLAVE", "VZ", "VR"))
        estadoItemList.add(EstadoModel("YUCATAN", "YN", "YC"))
        estadoItemList.add(EstadoModel("ZACATECAS", "ZS", "ZC"))

        return estadoItemList
    }
}

data class EstadoModel(var title:String, var curpCode:String, var codeTC44:String)