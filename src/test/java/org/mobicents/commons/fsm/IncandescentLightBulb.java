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

import org.mobicents.commons.annotations.NotThreadSafe;

/**
 * @author quintana.thomas@gmail.com (Thomas Quintana)
 */
@NotThreadSafe public final class IncandescentLightBulb implements LightBulb {
  private boolean on;
  private int timesTurnedOff;
  private int timesTurnedOn;
  
  public IncandescentLightBulb() {
    super();
    on = false;
    timesTurnedOff = 0;
    timesTurnedOn = 0;
  }
  
  @Override public int getTimesTurnedOff() {
    return timesTurnedOff;
  }
  
  @Override public int getTimesTurnedOn() {
    return timesTurnedOn;
  }

  @Override public boolean isOff() {
    return !on;
  }

  @Override public boolean isOn() {
    return on;
  }

  @Override public void turnOff() {
    on = false;
    ++timesTurnedOff;
  }

  @Override public void turnOn() {
    on = true;
    ++timesTurnedOn;
  }
}
