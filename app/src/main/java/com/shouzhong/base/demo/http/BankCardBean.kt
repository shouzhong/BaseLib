package com.shouzhong.base.demo.http

data class BankCardBean(
    var validated: Boolean = false,
    var stat: String? = null,
    var cardType: String? = null,
    var bank: String? = null,
    var key: String? = null,
    var message: ArrayList<BankCardItem1Bean>? = null
)

data class BankCardItem1Bean(
    var errorCodes: String? = null,
    var name: String? = null
)