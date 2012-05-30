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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.mobicents.commons.event.Event;
import org.mobicents.commons.event.EventHandler;

/**
 * @author quintana.thomas@gmail.com (Thomas Quintana)
 */
public abstract class FiniteStateMachine implements EventHandler {
  private final Lock lock;
  private State state;
  private Map<State, Map<State, Transition>> transitions;
  
  public FiniteStateMachine() {
    super();
    this.lock = new ReentrantLock();
  }
  
  private boolean canTransitionTo(final State newState) {
    return transitions.get(state).containsKey(newState);
  }
  
  protected State getState() {
    while(!lock.tryLock()) { /* Spin! */ }
    try { return state; }
    finally { lock.unlock(); }
  }
  
  private Transition getTransitionTo(final State newState) {
    return transitions.get(state).get(newState);
  }
  
  protected void initialize(final State stateOnStart, final Set<Transition> transitions) {
    this.state = stateOnStart;
    this.transitions = toImmutableMap(transitions);
  }
  
  protected void transition(final Event<?> event, final State newState)
      throws TransitionFailedException, TransitionNotFoundException {
    while(!lock.tryLock()) { /* Spin! */ }
    try {
      if(canTransitionTo(newState)) {
        final Transition transition = getTransitionTo(newState);
        final Condition condition = transition.getCondition();
        if(condition != null) {
          if(condition.accept(event, transition)) {
            final Action actionOnExit = state.getActionOnExit();
            if(actionOnExit != null) {
              actionOnExit.execute(event, state);
            }
            state = newState;
            final Action actionOnEnter = newState.getActionOnEnter();
            if(actionOnEnter != null) {
              actionOnEnter.execute(event, state);
            }
          } else {
            throw new TransitionFailedException("The condition guarding the transition did not pass.",
                event, transition);
          }
        }
      } else {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("No transition could be found from ").append(state.getId()).append(" ");
        buffer.append("to ").append(newState.getId());
        throw new TransitionNotFoundException(buffer.toString(), event, newState);
      }
    } finally {
      lock.unlock();
    }
  }
  
  private Map<State, Map<State, Transition>> toImmutableMap(final Set<Transition> transitions) {
    final Map<State, Map<State, Transition>> map = new HashMap<State, Map<State, Transition>>();
    for(final Transition transition : transitions) {
      final State stateOnEnter = transition.getStateOnEnter();
      if(!map.containsKey(stateOnEnter)) {
        map.put(stateOnEnter, new HashMap<State, Transition>());
      }
      final State stateOnExit = transition.getStateOnExit();
      map.get(stateOnEnter).put(stateOnExit, transition);
    }
    return Collections.unmodifiableMap(map);
  }
}
