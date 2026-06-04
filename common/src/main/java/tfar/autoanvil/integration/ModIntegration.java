package tfar.autoanvil.integration;

import tfar.autoanvil.platform.Services;

public enum ModIntegration {
  anviltweaks,apotheosis;
  public final boolean loaded = Services.PLATFORM.isModLoaded(name());
}
