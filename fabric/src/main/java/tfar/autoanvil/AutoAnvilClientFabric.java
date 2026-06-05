package tfar.autoanvil;

import net.fabricmc.api.ClientModInitializer;
import tfar.autoanvil.client.AutoAnvilClient;

public class AutoAnvilClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoAnvilClient.setup();
    }
}
