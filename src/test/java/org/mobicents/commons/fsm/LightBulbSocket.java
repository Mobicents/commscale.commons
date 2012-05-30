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

import java.util.HashSet;
import java.util.Set;

import org.mobicents.commons.event.Event;

/**
 * @author quintana.thomas@gmail.com (Thomas Quintana)
 */
public final class LightBulbSocket extends FiniteStateMachine {
  private final State on;
  private final State off;
  
  private final Set<Transition> transitions;
  
  public LightBulbSocket(final LightBulb lightbulb) {
    super();
    // Initialize states.
    this.on = new State("On", new TurnOnAction(lightbulb), null);
    this.off = new State("Off", new TurnOffAction(lightbulb), null);
    // Initialize transitions.
    final LightBulbReadyCondition condition = new LightBulbReadyCondition(lightbulb);
    this.transitions = new HashSet<Transition>();
    this.transitions.add(new Transition(off, on, condition));
    this.transitions.add(new Transition(on, off, condition));
    // Initialize the finite state machine.
    initialize(off, transitions);
  }

  @Override public boolean accept(final Event<?> event) {
    final Class<?> klass = event.getClass();
    return klass.equals(OffEvent.class) || klass.equals(OnEvent.class);
  }

  @Override public void handle(final Event<?> event) {
    try {
      final Class<?> klass = event.getClass();
      if(klass.equals(OffEvent.class)) {
        transition(event, off);
      } else if(klass.equals(OnEvent.class)) {
        transition(event, on);
      }
    } catch(TransitionFailedException ignored) { }
    catch(TransitionNotFoundException ignored) { }
  }
}
