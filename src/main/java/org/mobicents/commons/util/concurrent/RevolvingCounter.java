package org.mobicents.commons.util.concurrent;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.mobicents.commons.annotations.ThreadSafe;

@ThreadSafe public final class RevolvingCounter {
  private final long initialValue;
  private final long limit;
  private AtomicLong count;
  private final Lock lock;
  
  public RevolvingCounter(final long limit) {
    this(0, limit);
  }
  
  public RevolvingCounter(final long initialValue, final long limit) {
    super();
    this.initialValue = initialValue;
    this.limit = limit;
    this.count = new AtomicLong();
    this.count.set(initialValue);
    this.lock = new ReentrantLock();
  }
  
  public long getAndIncrement() {
    long result = count.getAndIncrement();
    if(result >= limit) {
      while(!lock.tryLock()) { /* Spin */ }
      try {
        if(count.get() >= limit){
          result = initialValue;
          count.set(initialValue + 1);
        } else {
          result = getAndIncrement();
        }
      }
      finally { lock.unlock(); }
    }
    return result;
  }
  
  public long getCount() {
    return count.get();
  }
}
