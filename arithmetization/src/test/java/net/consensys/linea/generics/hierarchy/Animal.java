package net.consensys.linea.generics.hierarchy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
// This can also be an interface if needed, the notation of generics is the same "extends"
public abstract class Animal {
  final String name;

  public abstract String talk();
}
