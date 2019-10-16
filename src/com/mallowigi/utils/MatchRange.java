package com.mallowigi.utils;

public final class MatchRange {
  private final int startOffset;
  private final int endOffset;
  private final String match;

  public MatchRange(final int startOffset, final int endOffset, final String url) {
    this.startOffset = startOffset;
    this.endOffset = endOffset;
    match = url;
  }

  public int getStartOffset() {
    return startOffset;
  }

  public int getEndOffset() {
    return endOffset;
  }

  public String getMatch() {
    return match;
  }

  @Override
  public int hashCode() {
    int result;
    result = startOffset;
    result = 29 * result + endOffset;
    result = 29 * result + match.hashCode();
    return result;
  }

  @SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    final MatchRange range = (MatchRange) obj;

    if (endOffset != range.endOffset) {
      return false;
    }
    if (startOffset != range.startOffset) {
      return false;
    }

    return match.equals(range.match);
  }
}