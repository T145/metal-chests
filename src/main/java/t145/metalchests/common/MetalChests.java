package t145.metalchests.common;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MetalChests.ID)
public class MetalChests {

	public static final String ID = "metalchests";
	public static final Logger LOG = getLog();

	private static final Logger getLog() {
		Logger log = Logger.getLogger(ID);

		try {
			FileHandler fh = new FileHandler(String.format("%s.log", ID));
			fh.setEncoding("UTF-8");
			log.addHandler(fh);
		} catch (SecurityException | IOException err) {
			LogManager.getLogger(ID).catching(err);
		}

		log.setLevel(Level.ALL);

		return log;
	}

	public MetalChests() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		bus.addListener(this::metalchests$setup);
		bus.addListener(this::metalchests$clientSetup);
		bus.addListener(this::metalchests$serverSetup);
		bus.addListener(this::metalchests$enqueueInterModComms);
		bus.addListener(this::metalchests$processInterModComms);
		bus.addListener(this::metalchests$fingerprintViolation);
		bus.addListener(this::metalchests$fingerprintViolation);
		bus.addListener(this::metalchests$loadComplete);
	}

	private void metalchests$setup(final FMLCommonSetupEvent event) {
		LOG.info("Hello from setup!");
	}

	private void metalchests$clientSetup(final FMLClientSetupEvent event) {
		LOG.info("Hello from client!");
	}

	private void metalchests$serverSetup(final FMLDedicatedServerSetupEvent event) {
		LOG.info("Hello from dedicated server!");
	}

	private void metalchests$fingerprintViolation(final FMLFingerprintViolationEvent event) {
		LOG.warning("This code may not be that of the original author! BEWARE!");
	}

	private void metalchests$loadComplete(final FMLLoadCompleteEvent event) {
		LOG.info("Mod construction was successful!");
	}

	private void metalchests$enqueueInterModComms(final InterModEnqueueEvent event) {
		LOG.info("Hello from comm queue!");
	}

	private void metalchests$processInterModComms(final InterModProcessEvent event) {
		LOG.info("Hello from comm proc!");
	}
}
