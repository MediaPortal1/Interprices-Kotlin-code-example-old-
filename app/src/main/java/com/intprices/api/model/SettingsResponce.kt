package com.intprices.api.model


data class SettingsResponce(var categories: List<Category>,
                            var countries: List<Country>,
                            var types: List<Type>,
                            var conditions: List<Condition>,
                            var sorts: List<Sort>)