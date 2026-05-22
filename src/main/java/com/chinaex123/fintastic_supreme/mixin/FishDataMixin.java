package com.chinaex123.fintastic_supreme.mixin;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.event.FishingEventHandler;
import com.li64.tide.data.fishing.CatchResult;
import com.li64.tide.data.fishing.FishingContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * 鱼类数据 Mixin
 * 作用：在 Tide 模组生成钓鱼结果时，为钓到的鱼添加重量数据组件
 */
@Mixin(targets = "com.li64.tide.data.fishing.FishData")
public class FishDataMixin {

    /**
     * 注入到 FishData.getResult() 方法的返回点
     * 在钓鱼结果生成后，为钓到的鱼添加重量数据
     * @param context 钓鱼上下文对象
     * @param cir 回调信息，用于获取和修改返回值
     */
    @Inject(method = "getResult", at = @At("RETURN"), remap = false)
    private void injectWeight(FishingContext context, CallbackInfoReturnable<CatchResult> cir) {
        try {
            // 获取钓鱼结果对象
            CatchResult result = cir.getReturnValue();
            var catchResultClass = result.getClass();

            ItemStack stack = null;
            for (var field : catchResultClass.getDeclaredFields()) {
                // 如果字段类型是 ItemStack，直接获取
                if (field.getType() == ItemStack.class) {
                    field.setAccessible(true);
                    stack = (ItemStack) field.get(result);
                    break;
                }
                // 如果字段类型是 List，尝试获取列表中的第一个 ItemStack
                else if (field.getType() == List.class) {
                    field.setAccessible(true);
                    List<?> items = (List<?>) field.get(result);
                    if (!items.isEmpty() && items.getFirst() instanceof ItemStack) {
                        stack = (ItemStack) items.getFirst();
                        break;
                    }
                }
            }

            // 如果未找到物品堆，直接返回
            if (stack == null) {
                return;
            }

            FishingEventHandler.ensureFishWeight(stack);
        } catch (Exception e) {
            FintasticSupreme.LOGGER.error("[FishDataMixin] 处理过程中发生错误", e);
        }
    }
}
