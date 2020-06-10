package gtclassic.common.event;

import gtclassic.common.GTConfig;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GTEventDecorateBiome {

	@SubscribeEvent
	public void onEvent(DecorateBiomeEvent.Decorate event) {
		if (GTConfig.general.reduceGrassOnWorldGen && event.getType() == DecorateBiomeEvent.Decorate.EventType.GRASS
				&& event.getRand().nextInt(11) != 0) {
			event.setResult(Event.Result.DENY);
		}
	}
}
