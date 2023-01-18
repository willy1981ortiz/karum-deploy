package com.model

import com.helper.DBHelper

abstract class Model {
    protected val db
        get() = DBHelper.instance.db
}