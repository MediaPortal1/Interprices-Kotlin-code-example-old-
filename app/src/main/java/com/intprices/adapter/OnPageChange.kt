package com.intprices.adapter

/**
 * Created by Alex Poltavets on 23.10.2016.
 */
interface OnPageChange {
    fun incrementPage():Unit
    fun isLastPage():Boolean
}