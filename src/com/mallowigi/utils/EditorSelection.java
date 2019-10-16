package com.mallowigi.utils;

import org.jetbrains.annotations.NotNull;

public final class EditorSelection {
  private final String line;
  private final int offset;
  private final int start;
  private final int end;

  public EditorSelection(@NotNull final String line, final int offset, final int start, final int end) {
    assert start >= 0 && start <= line.length();
    assert end >= start && end <= line.length();

    this.line = line;
    this.offset = offset;
    this.start = start;
    this.end = end;
  }

  @NotNull
  public String getLine() {
    return line;
  }

  public int getOffset() {
    return offset;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}