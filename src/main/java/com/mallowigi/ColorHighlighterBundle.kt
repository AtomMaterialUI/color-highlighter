package com.mallowigi;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public class ColorHighlighterBundle extends DynamicBundle {
  @NonNls
  private static final String BUNDLE = "messages.ColorHighlighterBundle";
  private static final ColorHighlighterBundle INSTANCE = new ColorHighlighterBundle();

  private ColorHighlighterBundle() {
    super(BUNDLE);
  }

  @NotNull
  public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) final String key, @NotNull final Object... params) {
    return INSTANCE.getMessage(key, params);
  }

  @NotNull
  public static Supplier<String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) final String key,
                                                @NotNull final Object... params) {
    return INSTANCE.getLazyMessage(key, params);
  }
}
