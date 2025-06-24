package co.juanxxo.junta.items;

import co.juanxxo.junta.entity.HerobrineRangedAttackEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HerobrineRangedAttackItem extends Item {
    public HerobrineRangedAttackItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            HerobrineRangedAttackEntity projectile = new HerobrineRangedAttackEntity(world, user, this);

            projectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 5, 1.0f);
            projectile.setPos(user.getX(), user.getEyeY() - 0.1, user.getZ());

            world.spawnEntity(projectile);

            world.playSound(
                    null,
                    user.getBlockPos(),
                    SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
                    SoundCategory.MASTER,
                    2.0f,
                    1.0f
            );
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }


}
