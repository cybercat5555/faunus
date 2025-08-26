package cybercat5555.faunus.mixin;

import cybercat5555.faunus.core.EffectStatusRegistry;
import cybercat5555.faunus.core.ItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Mixin(PotionItem.class)
public class DrinkablePotionMixin {

    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void finishUsingMixin(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        // If item is leeching potion, and is a drinkable potion apply clear effects
        if (stack.getItem() == ItemRegistry.BOTTLED_LEECH) {
            clearNegativeEffects(user);
            user.addStatusEffect(new StatusEffectInstance(Registries.STATUS_EFFECT.getEntry(EffectStatusRegistry.LEECHING_EFFECT), 1200));
            stack.decrement(1);

            cir.setReturnValue(stack);
        }
    }

    @Unique
    private void clearNegativeEffects(LivingEntity user) {
        if (user.getWorld().isClient) {
            return;
        }

        Iterator<StatusEffectInstance> iterator = user.getActiveStatusEffects().values().iterator();

        while (iterator.hasNext()) {
            StatusEffectInstance effect = iterator.next();

            if (!effect.getEffectType().value().isBeneficial()) {
                user.removeStatusEffect(effect.getEffectType());
            }

            iterator.remove();
        }
    }
}
