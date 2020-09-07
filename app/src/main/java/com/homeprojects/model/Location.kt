package com.homeprojects.model

data class Location (var name: String) {
    companion object {
        val INTERIOR = Location("Home Interior")
        val EXTERIOR = Location("Home Exterior")
        val YARD = Location("Yard")
        val SHOP = Location("Shop")
    }
}