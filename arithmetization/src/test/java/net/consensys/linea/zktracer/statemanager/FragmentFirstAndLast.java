package net.consensys.linea.zktracer.statemanager;

import lombok.Getter;
import lombok.Setter;

/* FragmentFirstAndLast stores the first and last fragments relevant to the state manager in the
 * current transaction segment (they will be either account fragments or storage fragments). */
@Getter
@Setter
public class FragmentFirstAndLast<TraceFragment> {
  TraceFragment first;
  TraceFragment last;
  int firstDom, firstSub;
  int lastDom, lastSub;

  public FragmentFirstAndLast(
      TraceFragment first,
      TraceFragment last,
      int firstDom,
      int firstSub,
      int lastDom,
      int lastSub) {
    this.first = first;
    this.last = last;
    this.firstDom = firstDom;
    this.firstSub = firstSub;
    this.lastDom = lastDom;
    this.lastSub = lastSub;
  }

  public static boolean strictlySmallerStamps(
      int firstDom, int firstSub, int lastDom, int lastSub) {
    return firstDom < lastDom || (firstDom == lastDom && firstSub > lastSub);
  }

  public FragmentFirstAndLast<TraceFragment> copy() {
    return new FragmentFirstAndLast<TraceFragment>(
        this.first, this.last, this.firstDom, this.firstSub, this.lastDom, this.lastSub);
  }
}
