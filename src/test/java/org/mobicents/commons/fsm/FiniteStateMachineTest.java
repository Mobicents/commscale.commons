/*
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.commons.fsm;

import static org.junit.Assert.*;
import org.junit.Test;

import org.mobicents.commons.annotations.ThreadSafe;

/**
 * @author quintana.thomas@gmail.com (Thomas Quintana)
 */
@ThreadSafe public final class FiniteStateMachineTest {
  public FiniteStateMachineTest() {
    super();
  }
  
  @Test public void test() {
    // Initialize
    final LightBulb lightbulb = new IncandescentLightBulb();
    final LightBulbSocket socket = new LightBulbSocket(lightbulb);
    final RemoteSwitch remote = new RemoteSwitch(socket);
    // Can't turn off a socket that is already off.
    remote.turnOff();
    assertTrue(lightbulb.getTimesTurnedOff() == 0);
    // Turn on the socket and verify.
    remote.turnOn();
    assertTrue(lightbulb.getTimesTurnedOn() == 1);
    // Turn off the socket and verify.
    remote.turnOff();
    assertTrue(lightbulb.getTimesTurnedOff() == 1);
    // Try to turn on the socket twice.
    remote.turnOn();
    remote.turnOn();
    // Turn off the socket.
    remote.turnOff();
    // Turn on the socket.
    remote.turnOn();
    // Turn off the socket.
    remote.turnOff();
    // Verify that transitions between states were executed successfully.
    assertTrue(lightbulb.getTimesTurnedOff() == 3);
    assertTrue(lightbulb.getTimesTurnedOn() == 3);
  }
}
