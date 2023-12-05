package com.example.handheld_terminal_pda.utils

import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or


object BaseUtil {
    /**
     * 字符串 转 十六进制形式的数组
     */
    fun stringToHexByteArray(input: String): ByteArray {
        val hexChars = "0123456789ABCDEF"
        val result = ByteArray(input.length / 2)

        for (i in input.indices step 2) {
            val firstIndex = hexChars.indexOf(input[i].uppercaseChar())
            val secondIndex = hexChars.indexOf(input[i + 1].uppercaseChar())

            val octet = (firstIndex shl 4 or secondIndex).toByte()
            result[i / 2] = octet
        }

        return result
    }

}