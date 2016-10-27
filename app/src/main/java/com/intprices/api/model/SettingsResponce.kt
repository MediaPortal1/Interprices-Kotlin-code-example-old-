package com.intprices.api.model

/**
 * Created by Alex Poltavets on 24.10.2016.
 */
data class SettingsResponce(var categories: List<Category>,var countries: List<Country>,var types: List<Type>,var conditions: List<Condition>,var sorts: List<Sort>)