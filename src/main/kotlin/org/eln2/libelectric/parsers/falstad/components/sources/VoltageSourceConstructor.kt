package org.eln2.libelectric.parsers.falstad.components.sources

import org.eln2.libelectric.parsers.falstad.CCData
import org.eln2.libelectric.parsers.falstad.PoleConstructor
import org.eln2.libelectric.sim.electrical.mna.component.Component
import org.eln2.libelectric.sim.electrical.mna.component.VoltageSource

/**
 * Voltage Source Constructor
 *
 * Basic Falstad voltage source. Two pin?
 */
class VoltageSourceConstructor : PoleConstructor() {
    override fun component(ccd: CCData) = VoltageSource()
    override fun configure(ccd: CCData, cmp: Component) {
        (cmp as VoltageSource).potential = ccd.data[2].toDouble()
    }
}
