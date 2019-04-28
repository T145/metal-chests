package T145.metalchests.core;

import T145.metalchests.api.BlocksMC;
import T145.metalchests.api.immutable.ChestType;
import T145.metalchests.api.immutable.ModSupport.Quark;
import T145.metalchests.api.immutable.RegistryMC;
import T145.metalchests.entities.EntityMinecartMetalChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = RegistryMC.MOD_ID)
class ProxyQuark {

	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event) {
		if (Loader.isModLoaded(Quark.MOD_ID)) {
			Entity target = event.getTarget();

			if (target instanceof EntityMinecartEmpty && target.getPassengers().isEmpty()) {
				EntityPlayer player = event.getEntityPlayer();
				EnumHand hand = EnumHand.MAIN_HAND;
				ItemStack stack = player.getHeldItemMainhand();
				Item chestItem = Item.getItemFromBlock(BlocksMC.METAL_CHEST);
				boolean hasChest = stack.getItem().equals(chestItem);

				if (stack.isEmpty() || !hasChest) {
					stack = player.getHeldItemOffhand();
					hand = EnumHand.OFF_HAND;
				}

				if (!stack.isEmpty() && hasChest) {
					player.swingArm(hand);

					World world = event.getWorld();

					if (!world.isRemote) {
						EntityMinecartMetalChest cart = new EntityMinecartMetalChest(world, target.posX, target.posY, target.posZ);

						cart.setChestType(ChestType.byMetadata(stack.getItemDamage()));

						if (cart != null) {
							target.setDead();
							event.getWorld().spawnEntity(cart);
							event.setCanceled(true);

							if (!player.capabilities.isCreativeMode) {
								stack.shrink(1);

								if (stack.getCount() <= 0) {
									player.setHeldItem(hand, ItemStack.EMPTY);
								}
							}
						}
					}
				}
			}
		}
	}
}
