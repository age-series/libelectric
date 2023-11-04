package org.ageseries.libage.data

import org.ageseries.libage.sim.Scale
import org.ageseries.libage.sim.thermal.ThermalUnits

data class QuantityScale<Unit>(val scale: Scale) {
    constructor(factor: Double, base: Double) : this(
        Scale(factor, base)
    )

    val base get() = scale.base

    val factor get() = scale.factor

    /**
     * Amplifies this scale [amplify] times.
     * */
    operator fun times(amplify: Double) = QuantityScale<Unit>(scale.factor / amplify, scale.base)

    /**
     * Reduces this scale [reduce] times.
     * */
    operator fun div(reduce: Double) = QuantityScale<Unit>(scale.factor * reduce, scale.base)

    /**
     * Amplifies this scale 1000 times.
     * */
    operator fun unaryPlus() = this * 1000.0

    /**
     * Reduces this scale 1000 times.
     * */
    operator fun unaryMinus() = this / 1000.0
}

interface Mass
interface MolecularWeight
interface Time
interface Distance
interface Velocity
interface Energy
interface Radioactivity
interface RadiationAbsorbedDose
interface RadiationDoseEquivalent
interface RadiationExposure
interface ArealDensity
interface ReciprocalDistance
interface ReciprocalArealDensity
interface Density
interface Substance
interface Area
interface Volume
interface Temp // Rename once we remove Temperature
interface MolarConcentration
interface SpecificHeatCapacity
interface HeatCapacity
interface ThermalConductivity
interface Pressure

/**
 * Represents a physical quantity, characterised by a [Unit] and a real number [value].
 * */
@JvmInline
value class Quantity<Unit>(val value: Double) : Comparable<Quantity<Unit>> {
    constructor(quantity: Double, s: QuantityScale<Unit>) : this(s.scale.unmap(quantity))

    val isZero get() = value == 0.0

    /**
     * Gets the numerical value of this quantity.
     * */
    operator fun not() = value

    operator fun unaryMinus() = Quantity<Unit>(-value)
    operator fun unaryPlus() = Quantity<Unit>(+value)
    operator fun plus(b: Quantity<Unit>) = Quantity<Unit>(this.value + b.value)
    operator fun minus(b: Quantity<Unit>) = Quantity<Unit>(this.value - b.value)
    operator fun times(scalar: Double) = Quantity<Unit>(this.value * scalar)
    operator fun div(scalar: Double) = Quantity<Unit>(this.value / scalar)
    /**
     * Divides the quantity by another quantity of the same unit. This, in turn, cancels out the quantity, returning the resulting number.
     * */
    operator fun div(b: Quantity<Unit>) = this.value / b.value

    override operator fun compareTo(other: Quantity<Unit>) = value.compareTo(other.value)

    operator fun compareTo(b: Double) = value.compareTo(b)

    /**
     * Maps this quantity to another scale of the same unit.
     * */
    operator fun rangeTo(s: QuantityScale<Unit>) = s.scale.map(value)

    override fun toString() = value.toString()

    fun <U2> reparam(factor: Double = 1.0) = Quantity<U2>(value * factor)
}

fun <U> min(a: Quantity<U>, b: Quantity<U>) = Quantity<U>(kotlin.math.min(!a, !b))
fun <U> max(a: Quantity<U>, b: Quantity<U>) = Quantity<U>(kotlin.math.max(!a, !b))
fun <U> abs(q: Quantity<U>) = Quantity<U>(kotlin.math.abs(!q))

/**
 * Defines the standard scale of the [Unit] (a scale with factor 1).
 * */
fun <Unit> standardScale(factor: Double = 1.0, base: Double = 0.0) = QuantityScale<Unit>(factor, base)

val KILOGRAMS = standardScale<Mass>()
val GRAMS = -KILOGRAMS

val SECOND = standardScale<Time>()
val MILLISECONDS = -SECOND
val MICROSECONDS = -MILLISECONDS
val NANOSECONDS = -MICROSECONDS
val MINUTES = SECOND * 60.0
val HOURS = MINUTES * 60.0
val DAYS = HOURS * 24.0

val METER = standardScale<Distance>()
val CENTIMETERS = METER / 100.0
val MILLIMETERS = -METER

val JOULE = standardScale<Energy>()
val KILOJOULES = +JOULE
val MEGAJOULES = +KILOJOULES
val GIGAJOULES = +MEGAJOULES
val WATT_SECONDS = QuantityScale<Energy>(JOULE.factor, 0.0)
val WATT_MINUTES = WATT_SECONDS * 60.0
val WATT_HOURS = WATT_MINUTES * 60.0
val KW_HOURS = WATT_HOURS * 1000.0

// Serious precision issues? Hope not! :Fish_Smug:
val ELECTRON_VOLT = JOULE * 1.602176634e-19
val KILO_ELECTRON_VOLT = JOULE * 1.602176634e-16
val MEGA_ELECTRON_VOLT = JOULE * 1.602176634e-13
val GIGA_ELECTRON_VOLT = JOULE * 1.602176634e-10
val TERA_ELECTRON_VOLT = JOULE * 1.602176634e-7

val BECQUEREL = standardScale<Radioactivity>()
val KILOBECQUERELS = +BECQUEREL
val MEGABECQUERELS = +KILOBECQUERELS
val GIGABECQUERELS = +MEGABECQUERELS
val TERABECQUERELS = +GIGABECQUERELS
val CURIE = GIGABECQUERELS * 37.0
val MILLICURIES = MEGABECQUERELS * 37.0
val MICROCURIES = KILOBECQUERELS * 37.0
val NANOCURIES = BECQUEREL * 37.0
val KILOCURIES = +CURIE
val MEGACURIES = +KILOCURIES
val GIGACURIES = +MEGACURIES // Average conversation with Grissess (every disintegration is a cute dragon image)

val GRAY = standardScale<RadiationAbsorbedDose>()
val RAD = GRAY / 100.0

val SIEVERT = standardScale<RadiationDoseEquivalent>()
val MILLISIEVERTS = -SIEVERT
val MICROSIEVERTS = -MILLISIEVERTS
val REM = SIEVERT / 100.0
val MILLIREM = -REM
val MICROREM = -MILLIREM

val COULOMB_PER_KG = standardScale<RadiationExposure>()
val ROENTGEN = COULOMB_PER_KG / 3875.96899225

val RECIP_METER = standardScale<ReciprocalDistance>()
val RECIP_CENTIMETERS = RECIP_METER * 100.0

val KG_PER_M2 = standardScale<ArealDensity>()
val G_PER_CM2 = KG_PER_M2 * 10.0

val KG_PER_M3 = standardScale<Density>()
val G_PER_CM3 = KG_PER_M3 * 1000.0
val G_PER_L = KG_PER_M3

val M2_PER_KG = standardScale<ReciprocalArealDensity>()
val CM2_PER_G = M2_PER_KG / 10.0

val M_PER_S = standardScale<Velocity>()
val KM_PER_S = +M_PER_S

val MOLE = standardScale<Substance>()

val MOLE_PER_M3 = standardScale<MolarConcentration>()

val M2 = standardScale<Area>()

val M3 = standardScale<Volume>()
val LITERS = M3 / 1000.0
val MILLILITERS = -LITERS

val KELVIN = standardScale<Temp>()
val CELSIUS = QuantityScale<Temp>(ThermalUnits.CELSIUS)

val J_PER_KG_K = standardScale<SpecificHeatCapacity>()
val J_PER_G_K = +J_PER_KG_K
val KJ_PER_KG_K = +J_PER_KG_K

val J_PER_K = standardScale<HeatCapacity>()

val W_PER_M_K = standardScale<ThermalConductivity>()
val mW_PER_M_K = -W_PER_M_K

val KG_PER_MOLE = standardScale<MolecularWeight>()
val G_PER_MOLE = -KG_PER_MOLE

val PASCAL = standardScale<Pressure>()
val ATMOSPHERES = PASCAL * 9.86923e-6