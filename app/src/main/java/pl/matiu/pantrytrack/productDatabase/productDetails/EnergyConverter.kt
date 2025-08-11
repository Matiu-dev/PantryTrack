package pl.matiu.pantrytrack.productDatabase.productDetails

import androidx.room.TypeConverter

class EnergyConverter {

    @TypeConverter
    fun fromEnergy(energy: Energy): String {
        return energy.value.toString() + " " + energy.unit
    }

    @TypeConverter
    fun toEnergy(energy: String): Energy {
        val e = energy.split(" ")
        return Energy(value = e[0].toInt(), unit = e[1])
    }
}