package com.mallowigi.search;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public final class SearchResults {
  private final Object label;
  private final Map<Integer, Color> colors = new TreeMap<>();

  SearchResults(@NotNull final Object label) {
    this.label = label;
  }

  @NotNull
  public Object getLabel() {
    return label;
  }

  @NotNull
  public Map<Integer, Color> getColors() {
    return colors;
  }

  void addColor(@NotNull final Color color) {
      colors.put(color.getRGB(), color);
  }
}