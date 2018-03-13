package T145.metalchests.items;

import T145.metalchests.items.base.ItemBase;
import T145.metalchests.lib.MetalChestType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChestAbilityUpgrade extends ItemBase {

	public ItemChestAbilityUpgrade() {
		super("ability_upgrade", MetalChestType.AbilityUpgrade.values());
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote || !player.isSneaking()) {
			return EnumActionResult.PASS;
		}

		TileEntity te = world.getTileEntity(pos);

		return EnumActionResult.SUCCESS;
	}
}