package com.intprices.adapter


interface OnPageChange {
    fun incrementPage(): Unit
    fun isLastPage(): Boolean
}